package com.unsent.api.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.mail.autoconfigure.MailSenderAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * PostcardService injects JavaMailSender directly, so a deployment with no SMTP
 * settings must still start — a missing postcard feature is not a reason for the
 * whole API to fail to boot. This pins the autoconfiguration down to that.
 */
class MailSenderPresenceTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(MailSenderAutoConfiguration.class));

    @Test
    void mailSenderExistsEvenWhenTheHostIsBlank() {
        runner.withPropertyValues("spring.mail.host=")
                .run(context -> assertThat(context).hasSingleBean(JavaMailSender.class));
    }

    @Test
    void mailSenderExistsWhenConfigured() {
        runner.withPropertyValues("spring.mail.host=smtp.example.test")
                .run(context -> assertThat(context).hasSingleBean(JavaMailSender.class));
    }
}
