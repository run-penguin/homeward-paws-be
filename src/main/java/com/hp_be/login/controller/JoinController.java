package com.hp_be.login.controller;

import com.hp_be.common.dto.ApiResDTO;
import com.hp_be.login.service.JoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/join")
public class JoinController {

    @Autowired
    private JoinService joinService;


    @GetMapping("/email")
    public ResponseEntity<ApiResDTO> checkEmail(@RequestParam String email) {
        boolean isAvailable = joinService.checkEmail(email);

        if (isAvailable) {
            return ResponseEntity.ok(ApiResDTO.ok("사용 가능한 이메일입니다.", true));
        } else {
            return ResponseEntity.ok(ApiResDTO.ok("이미 사용중인 이메일입니다.", false));
        }
    }

    @PostMapping("/email/send")
    public ResponseEntity<ApiResDTO<Void>> register(@RequestBody Map<String, String> body) {
        joinService.register(body.get("email"));
        return ResponseEntity.ok(ApiResDTO.ok("인증 이메일을 발송했습니다."));
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResDTO<Void>> verify(@RequestParam String token) {
        joinService.verifyEmail(token);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("http://localhost:5173/verified/email"))
                .build();
    }
}
