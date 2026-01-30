package com.hp_be.login.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoogleUserInfo {

    private String id;

    private String email;

    @SerializedName("verified_email")
    private boolean verifiedEmail;

    private String name;

    private String picture;
}
