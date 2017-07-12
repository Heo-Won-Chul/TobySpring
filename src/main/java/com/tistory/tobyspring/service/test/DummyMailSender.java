package com.tistory.tobyspring.service.test;

import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

/**
 * 메일 전송 기능이 없는 빈 클래스 <BR>
 */
@Service
@Profile("test")
public class DummyMailSender implements MailSender {

    @Override
    public void send(SimpleMailMessage simpleMailMessage) throws MailException {
        // ...
        System.out.println("Dummy ===> 전송 완료");
    }

    @Override
    public void send(SimpleMailMessage[] simpleMailMessages) throws MailException {
        // ...
    }
}