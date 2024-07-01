package com.demo.campingnavi.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private static final String senderEmail = "CampingNavi2@gmail.com";
    private static int number;

    // 랜덤으로 숫자 생성
    public static void createNumber() {
        number = (int)(Math.random() * (90000)) + 100000;
    }

    public MimeMessage createMail(String mail) {
        createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();

        try{
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("이메일 인증");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + number + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body, "utf-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException(e);
        }

        return message;
    }

    public MimeMessage createCustomMail(String mail, String title, String content) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try{
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("[전체 공지] " + title);
            String body = "";
            body += "<p>" + content + "</p>";
            message.setText(body, "utf-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException(e);
        }

        return message;
    }

    public int sendMail(String mail) {
        MimeMessage message = createMail(mail);
        javaMailSender.send(message);

        return number;
    }

    public void sendCustomMail(String mail, String title, String content) {
        MimeMessage message = createCustomMail(mail, title, content);
        javaMailSender.send(message);
    }

}
