package com.hp_be.login.service;

import com.hp_be.common.dao.User;
import com.hp_be.common.repository.UserQueryRepository;
import com.hp_be.common.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    private final UserQueryRepository userQueryRepository;

    // 이메일 중복 체크
    public boolean checkEmail(String email) {
        return !userQueryRepository.existsByEmail(email);
    }

    // 이메일 발송 전 등록 (토큰 정보 저장을 위함)
    public void register(String email) {

        // 미인증 유저 가져오기
        Optional<User> oUser = userQueryRepository.findUnverifiedByEmail(email);
        String token = UUID.randomUUID().toString();

        User user  = oUser.orElse(null);

        if (user == null) {
            user = User.builder()
                    .email(email)
                    .verificationToken(token)
                    .tokenExpiry(LocalDateTime.now().plusMinutes(10))
                    .build();
        } else {
            user.setToken(token, LocalDateTime.now().plusMinutes(10));
        }

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
        userRepository.save(user);
    }

    // 이메일 인증 완료 확인
    public boolean checkEmailVerified(String email) {
        Optional<User> oUser = userQueryRepository.findVerifiedByEmail(email);
        return oUser.isPresent();
    }
}
