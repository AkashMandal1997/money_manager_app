package in.akash.moneymanager.service;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.event.TransportEvent;
import jakarta.mail.event.TransportListener;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    // Prefer the standardized spring.mail.from property, fall back to spring.mail.username if not set
    @Value("${spring.mail.from:${spring.mail.from:}}")
    private String fromEmail;

    public void sendEmail(String to, String subject, String body) {
        try {
            // Diagnostic logging to help debug mail configuration at runtime (no passwords logged)
            log.info("Preparing to send email to={} subject={} fromConfigured={}", to, subject, (fromEmail != null && !fromEmail.isEmpty()));
            if (mailSender instanceof JavaMailSenderImpl) {
                JavaMailSenderImpl impl = (JavaMailSenderImpl) mailSender;
                log.info("Mail sender impl detected: host={} port={} username={} protocol={}", impl.getHost(), impl.getPort(), impl.getUsername(), impl.getProtocol());
                // If fromEmail is not configured, prefer the mail sender username
                if (fromEmail == null || fromEmail.isEmpty()) {
                    fromEmail = impl.getUsername();
                }
            } else {
                log.info("Mail sender implementation: {}", mailSender.getClass().getName());
            }

            // Use MimeMessage so we can support HTML bodies (notifications send HTML)
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            if (fromEmail != null && !fromEmail.isEmpty()) {
                helper.setFrom(fromEmail);
            }
            helper.setTo(to);
            helper.setSubject(subject);
            // Allow HTML content; for plain-text bodies this will still work
            helper.setText(body, true);

            // If we have JavaMailSenderImpl we can attempt a transport-level send with a TransportListener
            if (mailSender instanceof JavaMailSenderImpl) {
                JavaMailSenderImpl impl = (JavaMailSenderImpl) mailSender;
                try {
                    Session session = impl.getSession();
                    Transport transport = session.getTransport(impl.getProtocol() != null ? impl.getProtocol() : "smtp");

                    transport.addTransportListener(new TransportListener() {
                        @Override
                        public void messageDelivered(TransportEvent e) {
                            log.info("SMTP Transport: messageDelivered to={}", (e.getValidSentAddresses() != null ? e.getValidSentAddresses().length : 0));
                            // Try to log provider response if available
                            try {
                                Object maybeSMTP = transport.getClass().getName().contains("SMTP") ? transport : null;
                                if (maybeSMTP != null) {
                                    // Attempt to read any provider-specific response via toString fallback
                                    log.debug("SMTP transport class: {}", transport.getClass().getName());
                                }
                            } catch (Exception ex) {
                                log.debug("Could not extract provider-specific response: {}", ex.getMessage());
                            }
                        }

                        @Override
                        public void messageNotDelivered(TransportEvent e) {
                            log.warn("SMTP Transport: messageNotDelivered. validSent={}, invalid={}, validUnsent={}",
                                    (e.getValidSentAddresses() != null ? e.getValidSentAddresses().length : 0),
                                    (e.getInvalidAddresses() != null ? e.getInvalidAddresses().length : 0),
                                    (e.getValidUnsentAddresses() != null ? e.getValidUnsentAddresses().length : 0));
                        }

                        @Override
                        public void messagePartiallyDelivered(TransportEvent e) {
                            log.warn("SMTP Transport: messagePartiallyDelivered. validSent={}, invalid={}, validUnsent={}",
                                    (e.getValidSentAddresses() != null ? e.getValidSentAddresses().length : 0),
                                    (e.getInvalidAddresses() != null ? e.getInvalidAddresses().length : 0),
                                    (e.getValidUnsentAddresses() != null ? e.getValidUnsentAddresses().length : 0));
                        }
                    });

                    // Connect using configured host/port/credentials
                    if (impl.getHost() != null) {
                        try {
                            if (impl.getUsername() != null && impl.getPassword() != null) {
                                transport.connect(impl.getHost(), impl.getPort(), impl.getUsername(), impl.getPassword());
                            } else if (impl.getUsername() != null) {
                                transport.connect(impl.getHost(), impl.getPort(), impl.getUsername(), null);
                            } else {
                                transport.connect();
                            }
                        } catch (Exception ex) {
                            log.warn("Transport.connect() failed, falling back to mailSender.send(): {}", ex.getMessage());
                            mailSender.send(message);
                            log.info("Sent email to {} with subject={} (fallback)", to, subject);
                            return;
                        }

                        // send and close
                        transport.sendMessage(message, message.getAllRecipients());
                        transport.close();
                        log.info("Transport.sendMessage completed for {}", to);
                    } else {
                        // Host not configured (shouldn't happen) - fallback
                        mailSender.send(message);
                        log.info("Sent email to {} with subject={} (fallback no-host)", to, subject);
                    }

                } catch (Exception ex) {
                    log.error("Transport-level send failed, falling back to JavaMailSender.send(). Error: {}", ex.getMessage());
                    log.debug("Transport exception", ex);
                    mailSender.send(message);
                    log.info("Sent email to {} with subject={} (fallback)", to, subject);
                }
            } else {
                // Non-JavaMailSenderImpl implementations - use standard send
                mailSender.send(message);
                log.info("Sent email to {} with subject={}", to, subject);
            }

        } catch (Exception e) {
            log.error("Failed to send email to {} subject={}. Error: {}", to, subject, e.getMessage());
            log.debug("Full exception while sending email", e);
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }

    public void sendEmailWithAttachment(String to, String subject, String body, byte[] attachment, String filename) throws MessagingException {
        // Diagnostic logging
        log.info("Preparing to send email-with-attachment to={} filename={} fromConfigured={}", to, filename, (fromEmail != null && !fromEmail.isEmpty()));
        if (mailSender instanceof JavaMailSenderImpl) {
            JavaMailSenderImpl impl = (JavaMailSenderImpl) mailSender;
            log.info("Mail sender impl detected: host={} port={} username={} protocol={}", impl.getHost(), impl.getPort(), impl.getUsername(), impl.getProtocol());
        } else {
            log.info("Mail sender implementation: {}", mailSender.getClass().getName());
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        if (fromEmail != null && !fromEmail.isEmpty()) {
            helper.setFrom(fromEmail);
        }
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);
        helper.addAttachment(filename, new ByteArrayResource(attachment));

        // Reuse the same transport-based approach for attachments when possible
        if (mailSender instanceof JavaMailSenderImpl) {
            JavaMailSenderImpl impl = (JavaMailSenderImpl) mailSender;
            try {
                Session session = impl.getSession();
                Transport transport = session.getTransport(impl.getProtocol() != null ? impl.getProtocol() : "smtp");
                transport.addTransportListener(new TransportListener() {
                    @Override
                    public void messageDelivered(TransportEvent e) {
                        log.info("SMTP Transport (attachment): messageDelivered to={}", (e.getValidSentAddresses() != null ? e.getValidSentAddresses().length : 0));
                    }

                    @Override
                    public void messageNotDelivered(TransportEvent e) {
                        log.warn("SMTP Transport (attachment): messageNotDelivered.");
                    }

                    @Override
                    public void messagePartiallyDelivered(TransportEvent e) {
                        log.warn("SMTP Transport (attachment): messagePartiallyDelivered.");
                    }
                });

                if (impl.getHost() != null) {
                    try {
                        if (impl.getUsername() != null && impl.getPassword() != null) {
                            transport.connect(impl.getHost(), impl.getPort(), impl.getUsername(), impl.getPassword());
                        } else if (impl.getUsername() != null) {
                            transport.connect(impl.getHost(), impl.getPort(), impl.getUsername(), null);
                        } else {
                            transport.connect();
                        }
                    } catch (Exception ex) {
                        log.warn("Transport.connect() failed for attachment send, falling back to mailSender.send(): {}", ex.getMessage());
                        mailSender.send(message);
                        log.info("Sent email (attachment) to {} filename={} (fallback)", to, filename);
                        return;
                    }
                    transport.sendMessage(message, message.getAllRecipients());
                    transport.close();
                    log.info("Transport.sendMessage (attachment) completed for {} filename={}", to, filename);
                } else {
                    mailSender.send(message);
                    log.info("Sent email (attachment) to {} filename={} (fallback no-host)", to, filename);
                }

            } catch (Exception ex) {
                log.error("Transport-level send (attachment) failed, falling back to JavaMailSender.send(). Error: {}", ex.getMessage());
                log.debug("Transport exception (attachment)", ex);
                mailSender.send(message);
                log.info("Sent email (attachment) to {} filename={} (fallback)", to, filename);
            }
        } else {
            mailSender.send(message);
            log.info("Sent email with attachment to {} filename={}", to, filename);
        }
    }
}
