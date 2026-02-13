package com.hp_be.login.controller;

import com.hp_be.login.service.JoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/join")
public class JoinController {

    @Autowired
    private JoinService joinService;


    @GetMapping("/email")
    public boolean checkEmail(@RequestParam String email) {
        return joinService.checkEmail(email);
    }
}
