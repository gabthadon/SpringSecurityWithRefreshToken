package com.javatechie.controller;


import com.javatechie.dto.AuthenticationRequest;
import com.javatechie.dto.AuthenticationResponse;
import com.javatechie.dto.RefreshTokenRequest;
import com.javatechie.dto.RefreshTokenResponse;
import com.javatechie.entity.UserInfo;
import com.javatechie.service.AuthenticationService;
import com.javatechie.service.RefreshTokenService;
import com.javatechie.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class   AuthenticationController  {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/authenticate")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest request) throws Exception {
        // Authenticate and return both access and refresh tokens
        return authenticationService.login(request);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        String oldRefreshToken = request.getRefreshToken();
        return refreshTokenService.findByToken(oldRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(token -> {
                    // Generate new access token
                    String accessToken = jwtUtil.generateToken(token.getUser().getName());

                    // Generate new refresh token and save it
                    UserInfo user = token.getUser();
                    refreshTokenService.deleteByUser(user); // Optionally delete old token
                    String newRefreshToken = refreshTokenService.createRefreshToken(user).getToken();

                    // Return new access and refresh tokens
                    return ResponseEntity.ok(new RefreshTokenResponse(accessToken, newRefreshToken));
                })
                .orElseThrow(() -> new RuntimeException("Refresh token not found!"));
    }


}
