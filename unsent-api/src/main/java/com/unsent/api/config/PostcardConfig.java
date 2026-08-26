package com.unsent.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "app.postcard")
public class PostcardConfig {

    /** Envelope sender. Must be an address on a domain verified with the SMTP provider. */
    private String fromAddress;

    /** Display name the recipient sees in their inbox. */
    private String fromName = "Unsent";

    /** Postcards one signed-in sender may send per hour. */
    private int maxPerHour = 10;

    /**
     * Put the signed-in user's own address in the From header instead of
     * {@link #fromAddress}. Only enable where the SMTP provider accepts it: Gmail
     * rewrites From back to the authenticated account, and providers such as Resend
     * reject any From outside a domain you have verified. When off, the writer is
     * still identified by the display name and Reply-To.
     */
    private boolean sendAsUser = false;
}
