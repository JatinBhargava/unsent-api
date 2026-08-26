package com.unsent.api.helper;

import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

@Component
public class PostcardTemplate {

    /**
     * The envelope sender is always our own verified address — a provider will not
     * send as an arbitrary user, and forging it fails SPF/DKIM. The writer's identity
     * rides in the display name instead, with their address as Reply-To.
     */
    public String buildFromName(String fromName, String brand) {
        return (fromName == null || fromName.isBlank())
                ? brand
                : "%s via %s".formatted(fromName, brand);
    }

    public String buildSubject(String fromName) {
        return (fromName == null || fromName.isBlank())
                ? "You have a postcard"
                : "%s sent you a postcard".formatted(fromName);
    }

    /**
     * Plain-text alternative. The message itself is deliberately absent — the point
     * of the envelope is that the card is read after opening it, not in the preview.
     */
    public String buildPlainText(String toName, String fromName, String openUrl) {

        StringBuilder body = new StringBuilder();

        if (toName != null && !toName.isBlank()) {
            body.append(toName).append(",\n\n");
        }

        body.append(fromName == null || fromName.isBlank() ? "Someone" : fromName)
                .append(" sent you a postcard.\n\n")
                .append("Open it here:\n")
                .append(openUrl)
                .append("\n\nSent with Unsent — a quiet place to write.");

        return body.toString();
    }

    /**
     * A sealed envelope with the card peeking out of it, and one button that opens
     * the real thing in the browser. Email clients run no JavaScript and Gmail strips
     * the CSS tricks that fake interaction, so the flip has to live on a web page —
     * this is the part that has to survive being an email.
     */
    public String buildHtml(String toName, String fromName, String openUrl) {

        String safeTo = HtmlUtils.htmlEscape(
                toName == null || toName.isBlank() ? "you" : toName);
        String safeFrom = HtmlUtils.htmlEscape(
                fromName == null || fromName.isBlank() ? "Someone" : fromName);
        String safeUrl = HtmlUtils.htmlEscape(openUrl);

        return """
            <!DOCTYPE html>
            <html lang="en">
              <body style="margin:0;padding:44px 16px;background:#f6f2ee;font-family:Georgia,'Times New Roman',serif;">
                <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" border="0">
                  <tr>
                    <td align="center">
                      <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" border="0" style="max-width:480px;">

                        <tr>
                          <td align="center" style="padding-bottom:30px;font-family:Helvetica,Arial,sans-serif;font-size:10px;font-weight:600;letter-spacing:3px;text-transform:uppercase;color:#c3aca3;">
                            Unsent
                          </td>
                        </tr>

                        <!-- the card, peeking out of the envelope below it -->
                        <tr>
                          <td align="center">
                            <table role="presentation" width="86%%" cellpadding="0" cellspacing="0" border="0"
                                   style="background:#ffffff;border:1px solid #f0e6e1;border-bottom:none;border-radius:14px 14px 0 0;">
                              <tr>
                                <td style="padding:26px 28px 30px;">
                                  <p style="margin:0;font-family:Helvetica,Arial,sans-serif;font-size:9px;font-weight:600;letter-spacing:1.8px;text-transform:uppercase;color:#c9a9a0;">To</p>
                                  <p style="margin:6px 0 18px;font-size:19px;color:#2f2724;">%s</p>
                                  <p style="margin:0;font-family:Helvetica,Arial,sans-serif;font-size:9px;font-weight:600;letter-spacing:1.8px;text-transform:uppercase;color:#c9a9a0;">From</p>
                                  <p style="margin:6px 0 0;font-size:19px;color:#2f2724;">%s</p>
                                </td>
                              </tr>
                            </table>
                          </td>
                        </tr>

                        <!-- the envelope, its flap folded down over the card -->
                        <tr>
                          <td align="center">
                            <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" border="0"
                                   style="background:#f4dcd5;border-radius:8px 8px 16px 16px;">
                              <tr>
                                <td align="center" style="padding:0;line-height:0;font-size:0;">
                                  <!-- The seal straddles the seam where the card goes in. -->
                                  <table role="presentation" cellpadding="0" cellspacing="0" border="0" style="margin:-21px auto 0;">
                                    <tr>
                                      <td align="center" width="42" height="42"
                                          style="width:42px;height:42px;background:#c4576b;border-radius:21px;color:#ffffff;font-family:Helvetica,Arial,sans-serif;font-size:16px;line-height:42px;">
                                        &#9829;
                                      </td>
                                    </tr>
                                  </table>
                                </td>
                              </tr>
                              <tr>
                                <td align="center" style="padding:22px 28px 32px;">
                                  <p style="margin:0 0 22px;font-size:16px;line-height:1.5;color:#7d6259;">
                                    %s sent you a postcard.
                                  </p>
                                  <a href="%s"
                                     style="display:inline-block;padding:15px 34px;background:#2f2724;color:#fffdfa;text-decoration:none;border-radius:999px;font-family:Helvetica,Arial,sans-serif;font-size:13px;font-weight:600;letter-spacing:0.5px;">
                                    Open your postcard
                                  </a>
                                </td>
                              </tr>
                            </table>
                          </td>
                        </tr>

                        <tr>
                          <td align="center" style="padding-top:28px;font-family:Helvetica,Arial,sans-serif;font-size:12px;line-height:1.7;color:#c3aca3;">
                            This postcard lives in that link and nowhere else &mdash;<br />
                            we keep no copy of it.
                          </td>
                        </tr>

                      </table>
                    </td>
                  </tr>
                </table>
              </body>
            </html>
            """.formatted(safeTo, safeFrom, safeFrom, safeUrl);
    }
}
