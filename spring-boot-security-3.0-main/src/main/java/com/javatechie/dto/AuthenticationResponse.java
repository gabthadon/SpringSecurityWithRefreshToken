package com.javatechie.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class AuthenticationResponse {

    private String accessToken;
    private String refreshToken;  // Added field for refresh token
    private String username;



}
