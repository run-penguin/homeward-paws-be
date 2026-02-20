package com.hp_be.common.repository;

import com.hp_be.common.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByProviderAndKakaoId(String provider, Long id);
    Optional<User> findByProviderAndGoogleId(String provider, String id);
    Optional<User> findByProviderAndNaverId(String provider, String id);

    Optional<User> findByProviderIsNullAndEmailVerifiedIsTrueAndEmail(String email);

    Optional<User> findByVerificationToken(String token);
}
