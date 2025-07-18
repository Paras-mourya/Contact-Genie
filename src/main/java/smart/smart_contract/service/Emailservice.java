package smart.smart_contract.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class Emailservice {

    @Autowired
    private JavaMailSender mailSender;

    public boolean sendEmail(String subject, String message, String to) {
        boolean f = false;
        String from = "parasmourya388@gmail.com";

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(message, true); // true means enable HTML

            mailSender.send(mimeMessage);
            f = true;

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return f;
    }
}
