package com.unsent.api.service;

import com.unsent.api.config.PostcardConfig;
import com.unsent.api.dto.PostcardRequestDTO;
import com.unsent.api.dto.PostcardResponseDTO;
import com.unsent.api.helper.PostcardLink;
import com.unsent.api.helper.PostcardTemplate;
import com.unsent.helper.BusinessException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PostcardServiceTest {

    private CapturingMailSender mailSender;
    private PostcardConfig config;
    private PostcardService service;

    @BeforeEach
    void setUp() {
        mailSender = new CapturingMailSender();
        config = new PostcardConfig();
        config.setFromAddress("postcards@unsent.test");
        config.setFromName("Unsent");
        config.setMaxPerHour(3);
        service = new PostcardService(
                mailSender,
                config,
                new PostcardTemplate(),
                new PostcardLink("https://unsent.test/"));
    }

    @Test
    void sendsToTheAddressOnTheFront() throws Exception {
        PostcardResponseDTO response = service.send(request("ada@example.test"), "sender@example.test");

        assertEquals(1, mailSender.sent.size());
        MimeMessage sent = mailSender.sent.get(0);
        assertEquals("ada@example.test", sent.getAllRecipients()[0].toString());
        assertEquals("Grace sent you a postcard", sent.getSubject());

        // identity is carried by the display name and Reply-To; the envelope
        // address stays on our verified domain
        assertEquals("Grace via Unsent <postcards@unsent.test>", sent.getFrom()[0].toString());
        assertEquals("sender@example.test", sent.getReplyTo()[0].toString());

        assertEquals("ada@example.test", response.getToEmail());
        assertNotNull(response.getPostcardId());
        assertNotNull(response.getSentAt());
    }

    @Test
    void sendsAsTheUserWhenConfiguredTo() throws Exception {
        config.setSendAsUser(true);

        service.send(request("ada@example.test"), "sender@example.test");

        assertEquals("Grace <sender@example.test>",
                mailSender.sent.get(0).getFrom()[0].toString());
    }

    @Test
    void fallsBackToTheAddressWhenTheUserHasNoName() throws Exception {
        config.setSendAsUser(true);

        service.send(
                PostcardRequestDTO.builder()
                        .toEmail("ada@example.test")
                        .message("Thinking of you.")
                        .build(),
                "sender@example.test");

        assertEquals("sender@example.test",
                mailSender.sent.get(0).getFrom()[0].toString());
    }

    @Test
    void stopsASenderPastTheHourlyLimit() {
        for (int i = 0; i < 3; i++) {
            service.send(request("ada@example.test"), "sender@example.test");
        }

        BusinessException rejected = assertThrows(BusinessException.class,
                () -> service.send(request("ada@example.test"), "sender@example.test"));

        assertEquals("PC001", rejected.getErrorCode());
        assertEquals(3, mailSender.sent.size(), "a rejected postcard must not reach the mail server");
    }

    @Test
    void countsTheLimitPerSenderNotGlobally() {
        for (int i = 0; i < 3; i++) {
            service.send(request("ada@example.test"), "sender@example.test");
        }

        service.send(request("ada@example.test"), "other@example.test");
        assertEquals(4, mailSender.sent.size());
    }

    @Test
    void keepsTheLimitCaseInsensitive() {
        for (int i = 0; i < 3; i++) {
            service.send(request("ada@example.test"), "Sender@Example.test");
        }

        assertThrows(BusinessException.class,
                () -> service.send(request("ada@example.test"), "sender@example.test"));
    }

    @Test
    void reportsADeliveryFailureAsABusinessError() {
        mailSender.failing = true;

        BusinessException failure = assertThrows(BusinessException.class,
                () -> service.send(request("ada@example.test"), "sender@example.test"));

        assertEquals("PC002", failure.getErrorCode());
        assertTrue(failure.getMessage().contains("could not deliver"));
    }

    private PostcardRequestDTO request(String toEmail) {
        return PostcardRequestDTO.builder()
                .toEmail(toEmail)
                .toName("Ada")
                .fromName("Grace")
                .message("Thinking of you.")
                .build();
    }

    private static class CapturingMailSender extends JavaMailSenderImpl {

        final List<MimeMessage> sent = new ArrayList<>();
        boolean failing;

        @Override
        public void send(MimeMessage mimeMessage) throws MailException {
            if (failing) {
                throw new MailSendException("no smtp server here");
            }
            sent.add(mimeMessage);
        }
    }
}
