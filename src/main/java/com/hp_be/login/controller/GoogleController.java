package com.hp_be.login.controller;

import com.hp_be.common.dao.User;
import com.hp_be.login.dto.GoogleUserInfo;
import com.hp_be.login.service.GoogleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth/google")
public class GoogleController {

    @Autowired
    private GoogleService googleService;

    @Value("${google.client-id}")
    private String clientId;

    @Value("${google.redirect-uri}")
    private String redirectUri;


    @PostMapping("/callback")
    public ResponseEntity<?> googleCallback(@RequestBody Map<String, String> request) {
        try {
            String accessToken = request.get("accessToken");

            // 액세스 토큰으로 사용자 정보 받기
            GoogleUserInfo userInfo = googleService.getUserInfo(accessToken);

            // 회원가입 또는 로그인 처리
            User user = googleService.loginOrSignup(userInfo);

            // JWT 토큰 생성
//            String jwtToken = jwtService.createToken(user);
            String jwtToken = "test_jwt";
            return ResponseEntity.ok(Map.of("token", jwtToken));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
