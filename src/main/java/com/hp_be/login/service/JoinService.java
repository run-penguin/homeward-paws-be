package com.hp_be.login.service;

import com.hp_be.common.dao.User;
import com.hp_be.common.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class JoinService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;


    // 이메일 중복 체크
    public boolean checkEmail(String email) {
        Optional<User> user = userRepository.findByProviderIsNullAndEmailVerifiedIsTrueAndEmail(email);
        return user.isEmpty();
    }

    // 이메일 인증을 위한 등록
    public void register(String email) {
        String token = UUID.randomUUID().toString();

        User user = User.builder()
                .email(email)
                .verificationToken(token)
                .tokenExpiry(LocalDateTime.now().plusMinutes(10))
                .build();

        userRepository.save(user);
        emailService.sendVerificationEmail(email, token);
    }

    // 토큰 확인하기
    public void verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰입니다."));

        if (user.getTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("만료된 토큰입니다.");
        }

        user.verified();
    }
}
