package com.javatechie.service;

import com.javatechie.entity.RefreshToken;
import com.javatechie.entity.UserInfo;
import com.javatechie.repository.RefreshTokenRepository;
import com.javatechie.repository.UserInfoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


@Service
public class RefreshTokenService {

    @Value("${jwt.refreshExpirationMs}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserInfoRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserInfoRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(UserInfo userInfo) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString()); // Generate unique token
        refreshToken.setUser(userInfo);
        refreshToken.setExpiryDate(Instant.now().plusMillis(60L * 24 * 3600 * 1000)); // Set expiry time to 60 days

        return refreshTokenRepository.save(refreshToken); // Save and return refresh token
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token was expired. Please make a new login request");
        }

        return token;
    }

    @Transactional
    public RefreshToken refreshUserToken(String refreshTokenStr) {
        RefreshToken existingToken = findByToken(refreshTokenStr)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        verifyExpiration(existingToken); // Verify token expiration

        // Invalidate the existing token
        refreshTokenRepository.delete(existingToken);

        // Create and return a new refresh token
        return createRefreshToken(existingToken.getUser());
    }

    @Transactional
    public int deleteByUser(UserInfo user) {
        return refreshTokenRepository.deleteByUser(user);
    }
}
