package com.javatechie.repository;

import com.javatechie.entity.RefreshToken;
import com.javatechie.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);
    int deleteByUser(UserInfo user);
}
