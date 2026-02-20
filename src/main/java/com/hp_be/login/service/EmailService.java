package com.hp_be.login.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    
    // final을 사용해야하는 이유가 뭘까?
    private final JavaMailSender mailSender;
    
    public void sendVerificationEmail(String to, String token) {
        String link = "http://localhost:8081/auth/verify?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("[Homeward Paws] 이메일 인증");
        message.setText("아래 링크를 클릭하여 이메일 인증을 완료해 주세요.\n\n" + link + "\n\n링크는 10분간 동안 유효합니다.");
        mailSender.send(message);
    }
}
