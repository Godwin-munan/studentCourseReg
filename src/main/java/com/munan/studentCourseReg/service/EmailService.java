package com.munan.studentCourseReg.service;

import com.sun.mail.smtp.SMTPTransport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Date;
import java.util.Properties;

import static com.munan.studentCourseReg.constants.EmailConstant.*;
import static javax.mail.Message.RecipientType.CC;
import static javax.mail.Message.RecipientType.TO;

@Service
@AllArgsConstructor
public class EmailService {


//    public void sendNewPasswordEmail(String firstName, String password, String email) throws MessagingException {
//        Message message = createEmail(firstName, password, email);
//        SMTPTransport smtpTransport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
//        smtpTransport.connect(GMAIL_SMTP_SERVER, USERNAME, PASSWORD);
//        smtpTransport.sendMessage(message, message.getAllRecipients());
//        smtpTransport.close();
//    }


    public  Message sendEmailToStudent(String firstName, String email) throws MessagingException {
        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(TO, InternetAddress.parse(email, false));
        message.setRecipients(CC, InternetAddress.parse(CC_EMAIL, false));
        message.setSubject(EMAIL_SUBJECT);
        message.setText("Hello " + firstName + ", \n \n Your registration is: " +"The Support Team");
        message.setSentDate(new Date());
        message.saveChanges();


        return message;
    }

    private static Session getEmailSession() {
        Properties properties = System.getProperties();
        properties.put(SMTP_HOST, GMAIL_SMTP_SERVER);
        properties.put(SMTP_AUTH, true);
        properties.put(SMTP_PORT, DEFAULT_PORT);
        properties.put(SMTP_STARTTLS_ENABLE, true);
        properties.put(SMTP_STARTTLS_REQUIRED, true);
        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME,PASSWORD);
            }
        });
    }
}