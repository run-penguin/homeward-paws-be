package com.hp_be.common.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;

    @Builder.Default
    private boolean emailVerified = false;

    private String verificationToken;
    private LocalDateTime tokenExpiry;

    private String provider;
    private Long kakaoId;
    private String googleId;
    private String naverId;


    public void verified() {
        this.emailVerified = true;
        this.verificationToken = null;
        this.tokenExpiry = null;
    }
}
