package com.hp_be.login.service;

import com.hp_be.common.dao.User;
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


    // 이메일 중복 체크
    public boolean checkEmail(String email) {
        Optional<User> user = userRepository.findByProviderIsNullAndEmailVerifiedAndEmail(true, email);
        return user.isEmpty();
    }

    // 이메일 인증을 위한 등록
    public void register(String email) {

        // 기존에 완료되지 않은 동일한 주소가 있다면 가져온다.
        Optional<User> oUser = userRepository.findByProviderIsNullAndEmailVerifiedAndEmail(false, email);
        String token = UUID.randomUUID().toString();

        User user = oUser.orElse(null);

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
}
