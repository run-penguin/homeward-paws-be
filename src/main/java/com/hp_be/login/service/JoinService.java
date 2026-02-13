package com.hp_be.login.service;

import com.hp_be.common.dao.User;
import com.hp_be.common.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JoinService {

    @Autowired
    private UserRepository userRepository;


    public boolean checkEmail(String email) {
        Optional<User> user = userRepository.findByProviderIsNullAndEmail(email);
        return user.isPresent();
    }
}
