package com.hp_be.login.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hp_be.common.dao.User;
import com.hp_be.common.repository.UserRepository;
import com.hp_be.login.dto.KakaoUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;


@Service
public class KakaoService {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.client-secret}")
    private String clientSecret;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.token-uri}")
    private String tokenUri;

    @Value("${kakao.user-info-uri}")
    private String userInfoUri;

    private final RestTemplate restTemplate = new RestTemplate();
    private final Gson gson = new Gson();

    @Autowired
    private UserRepository userRepository;


    // 1. 액세스 토큰 받기
    public String getAccessToken(String code) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // parameter
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(tokenUri, request, String.class);

        JsonObject jsonObject = gson.fromJson(response.getBody(), JsonObject.class);
        return jsonObject.get("access_token").getAsString();
    }

    // 2. 사용자 정보 받기
    public KakaoUserInfo getUserInfo(String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(userInfoUri, HttpMethod.GET, request, String.class);

        JsonObject jsonObject = gson.fromJson(response.getBody(), JsonObject.class);

        Long kakaoId = jsonObject.get("id").getAsLong();
        return new KakaoUserInfo(kakaoId, null, null, null);
    }

    // 3. 회원가입 또는 로그인
    public User loginOrSignup(KakaoUserInfo kakaoUserInfo) {
        String provider = "KAKAO";
        Optional<User> existingUser = userRepository.findByProviderAndKakaoId(provider, kakaoUserInfo.getId());
        if (existingUser.isPresent()) return existingUser.get();

        // 신규 회원가입
        User newUser = User.builder()
                .kakaoId(kakaoUserInfo.getId())
                .email(kakaoUserInfo.getEmail())
                .provider(provider)
                .build();

        return userRepository.save(newUser);
    }
}
