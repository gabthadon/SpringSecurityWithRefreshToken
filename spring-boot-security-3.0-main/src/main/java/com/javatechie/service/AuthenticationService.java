package com.javatechie.service;

import com.javatechie.config.UserInfoUserDetails;
import com.javatechie.dto.AuthenticationRequest;
import com.javatechie.dto.AuthenticationResponse;
import com.javatechie.entity.RefreshToken;
import com.javatechie.entity.UserInfo;
import com.javatechie.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    public AuthenticationResponse login(AuthenticationRequest request) throws Exception {
        try {
            // Authenticate the user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new Exception("Incorrect username or password", e);
        }

        // Load the user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        // Generate the JWT token
        String accessToken = jwtUtil.generateToken(userDetails.getUsername());

        // Cast to UserInfo if applicable
        UserInfo userInfo = ((UserInfoUserDetails) userDetails).getUserInfo();

        // Generate the refresh token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userInfo);

        // Return the response with both access and refresh tokens
        return new AuthenticationResponse(accessToken, refreshToken.getToken(), userDetails.getUsername());
    }

}
