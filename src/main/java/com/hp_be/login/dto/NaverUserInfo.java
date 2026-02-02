package com.hp_be.login.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NaverUserInfo {
    private String id;
    private String nickname;
    private String name;
    private String email;
    private String gender;
    private String age;
    private String birthday;
    private String profile_image;
    private String birthyear;
    private String mobile;
}
