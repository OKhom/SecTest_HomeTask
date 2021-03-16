package ua.kiev.prog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText("For activated your account follow by link " + text);

        emailSender.send(message);
    }

    public void sendHtmlEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        boolean multipart = true;
        MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");

        String htmlMsg = "<img src='https://prog.kiev.ua/images/logo100.png'>"
                + "<h1>For activated your account follow by link " + text + "</h1>";
        message.setContent(htmlMsg, "text/html");

        helper.setTo(to);
        helper.setSubject(subject);

        this.emailSender.send(message);
    }
}
