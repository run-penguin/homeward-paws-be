package com.hp_be.login.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoUserInfo {
    private Long kakaoId;
    private String email;
    private String nickname;
    private String profileImage;
}
