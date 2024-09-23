package com.javatechie.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class RefreshTokenResponse {

    private String accessToken;
    private String refreshToken;
}
