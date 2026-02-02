package com.hp_be.login.controller;

import com.hp_be.common.dao.User;
import com.hp_be.login.dto.NaverUserInfo;
import com.hp_be.login.service.NaverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/naver")
public class NaverController {

    @Autowired
    private NaverService naverService;

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.redirect-uri}")
    private String redirectUri;


    @GetMapping("/callback")
    public ResponseEntity<?> kakaoCallback(@RequestParam String code) {
        try {
            // 인가 코드로 액세스 토큰 받기
            String accessToken = naverService.getAccessToken(code);

            // 액세스 토큰으로 사용자 정보 받기
            NaverUserInfo userInfo = naverService.getUserInfo(accessToken);

            // 회원가입 또는 로그인 처리
            User user = naverService.loginOrSignup(userInfo);

            // JWT 토큰 생성
//            String jwtToken = jwtService.createToken(user);
            String jwtToken = "test_jwt";

            // 프론트엔드로 리다이렉트 (토큰 전달)
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("http://localhost:5173/auth/callback?token=" + jwtToken))
                    .build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
