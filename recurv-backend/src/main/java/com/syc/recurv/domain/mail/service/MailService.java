package com.syc.recurv.domain.mail.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;

    public void sendConsultingMail(String name, String email, String company, String message){
        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setTo("yechan0422@gmail.com");
        mail.setSubject("[ERP 제작 상담 신청]" + name + "님의 요청입니다.");
        mail.setText(       "이름: " + name + "\n" +
                "이메일: " + email + "\n" +
                "회사명: " + company + "\n" +
                "문의 내용:\n" + message
        );
        javaMailSender.send(mail);
    }
}
