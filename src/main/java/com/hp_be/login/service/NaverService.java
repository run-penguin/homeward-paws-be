package com.hp_be.login.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hp_be.common.dao.User;
import com.hp_be.common.repository.UserRepository;
import com.hp_be.login.dto.NaverUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NaverService {

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    @Value("${naver.redirect-uri}")
    private String redirectUri;

    @Value("${naver.token-uri}")
    private String tokenUri;

    @Value("${naver.user-info-uri}")
    private String userInfoUri;

    private final RestTemplate restTemplate = new RestTemplate();
    private final Gson gson = new Gson();

    private final UserRepository userRepository;


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
        params.add("state", "naver_login");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(tokenUri, request, String.class);

        JsonObject jsonObject = gson.fromJson(response.getBody(), JsonObject.class);
        return jsonObject.get("access_token").getAsString();
    }

    public NaverUserInfo getUserInfo(String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(userInfoUri, HttpMethod.GET, request, String.class);

        JsonObject jsonObject = gson.fromJson(response.getBody(), JsonObject.class);
        JsonObject jsonResponse = jsonObject.getAsJsonObject("response");

        String id = jsonResponse.get("id").toString();
        return new NaverUserInfo(id, null, null, null, null,null,null,null,null,null);
    }

    public User loginOrSignup(NaverUserInfo naverUserInfo) {
        String provider = "NAVER";
        Optional<User> existingUser = userRepository.findByProviderAndNaverId(provider, naverUserInfo.getId());
        if (existingUser.isPresent()) return existingUser.get();

        // 신규 회원가입
        User newUser = User.builder()
                .naverId(naverUserInfo.getId())
                .provider(provider)
                .build();

        return userRepository.save(newUser);
    }
}
