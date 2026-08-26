package com.unsent.api.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.unsent.api.config.PostcardConfig;
import com.unsent.api.dto.PostcardRequestDTO;
import com.unsent.api.dto.PostcardResponseDTO;
import com.unsent.api.helper.PostcardLink;
import com.unsent.api.helper.PostcardTemplate;
import com.unsent.helper.BusinessException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class PostcardService {

    private static final Logger log = LoggerFactory.getLogger(PostcardService.class);

    private final JavaMailSender mailSender;
    private final PostcardConfig config;
    private final PostcardTemplate template;
    private final PostcardLink link;

    /**
     * A postcard is never persisted, so the only per-sender state is this counter.
     * It resets when the app restarts, which is acceptable: it exists to stop the
     * endpoint being used as a spam relay, not to keep an audit trail.
     */
    private final Cache<String, AtomicInteger> sendsThisHour = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofHours(1))
            .maximumSize(10_000)
            .build();

    public PostcardResponseDTO send(PostcardRequestDTO request, String senderEmail) {

        final String toEmail = request.getToEmail().trim();
        final String toName = trimToNull(request.getToName());
        final String fromName = trimToNull(request.getFromName());
        final String message = request.getMessage().trim();

        enforceRateLimit(senderEmail);

        final String postcardId = UUID.randomUUID().toString();

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            // Multipart is required for the plain-text + HTML alternative pair below.
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage, true, StandardCharsets.UTF_8.name());

            if (config.isSendAsUser()) {
                if (fromName == null) {
                    helper.setFrom(senderEmail);
                } else {
                    helper.setFrom(senderEmail, fromName);
                }
            } else {
                helper.setFrom(
                        config.getFromAddress(),
                        template.buildFromName(fromName, config.getFromName()));
            }

            helper.setTo(toEmail);
            helper.setSubject(template.buildSubject(fromName));

            // Replies belong to the person who wrote the postcard, not to us. This
            // does show the recipient the writer's real address.
            helper.setReplyTo(senderEmail);

            // The message never goes in the email itself — it travels in the link,
            // which is what the recipient opens to see the card flip over.
            String openUrl = link.build(toName, fromName, message);

            helper.setText(
                    template.buildPlainText(toName, fromName, openUrl),
                    template.buildHtml(toName, fromName, openUrl));

            mailSender.send(mimeMessage);

        } catch (MessagingException | UnsupportedEncodingException | MailException e) {
            log.error("Postcard {} failed to send", postcardId, e);
            throw new BusinessException("PC002", "We could not deliver that postcard. Please try again.");
        }

        log.info("Postcard {} sent", postcardId);

        return PostcardResponseDTO.builder()
                .postcardId(postcardId)
                .toEmail(toEmail)
                .sentAt(Instant.now().toString())
                .build();
    }

    private void enforceRateLimit(String senderEmail) {

        AtomicInteger sent = sendsThisHour.get(
                senderEmail.toLowerCase(), key -> new AtomicInteger());

        if (sent.incrementAndGet() > config.getMaxPerHour()) {
            throw new BusinessException(
                    "PC001",
                    "You have sent %d postcards this hour. Try again a little later."
                            .formatted(config.getMaxPerHour()));
        }
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
