package com.hp_be.login.service;

import com.google.gson.Gson;
import com.hp_be.common.dao.User;
import com.hp_be.common.repository.UserRepository;
import com.hp_be.login.dto.GoogleUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class GoogleService {

    @Value("${google.client-id}")
    private String clientId;

    @Value("${google.redirect-uri}")
    private String redirectUri;

    @Value("${google.user-info-uri}")
    private String userInfoUri;

    private final RestTemplate restTemplate = new RestTemplate();
    private final Gson gson = new Gson();

    @Autowired
    private UserRepository userRepository;


    public GoogleUserInfo  getUserInfo(String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // GET 요청으로 변경
        ResponseEntity<String> response = restTemplate.exchange(
                userInfoUri,
                HttpMethod.GET,
                entity,
                String.class
        );

        // 응답을 JsonObject로 변환하여 반환
        return gson.fromJson(response.getBody(), GoogleUserInfo.class);
    }

    public User loginOrSignup(GoogleUserInfo googleUserInfo) {
        String provider = "GOOGLE";
        Optional<User> existingUser = userRepository.findByProviderAndGoogleId(provider, googleUserInfo.getId());
        if (existingUser.isPresent()) return existingUser.get();

        // 신규 회원가입
        User newUser = User.builder()
                .googleId(googleUserInfo.getId())
                .email(googleUserInfo.getEmail())
                .provider(provider)
                .build();

        return userRepository.save(newUser);
    }
}
