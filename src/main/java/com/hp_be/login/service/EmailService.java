package com.hp_be.login.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    
    // autowired 말고 final을 사용해야하는 이유가 뭘까?
    private final JavaMailSender mailSender;
    
    public void sendVerificationEmail(String to, String token) {
        String link = "http://localhost:8081/api/join/email/verify?token=" + token;

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("[Homeward Paws] 이메일 인증");
            helper.setText(buildMailHtml(link), true); // true = HTML 사용

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("이메일 발송 실패", e);
        }
    }

    private String buildMailHtml(String link) {
        return """
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                    <h2>이메일 인증</h2>
                    <p>아래 버튼을 클릭하여 이메일 인증을 완료해 주세요.</p>
                    <a href="%s"
                       style="display: inline-block;
                              padding: 12px 24px;
                              background-color: #4CAF50;
                              color: white;
                              text-decoration: none;
                              border-radius: 4px;" target="_blank">
                        이메일 인증하기
                    </a>
                    <p style="color: gray; font-size: 12px;">링크는 10분간 유효합니다.</p>
                </div>
                """.formatted(link);
    }
}
