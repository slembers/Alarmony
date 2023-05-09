package com.slembers.alarmony.global.security.jwt.dto;


import lombok.Getter;

@Getter
public class TokenDto {

    //grantType은 JWT 대한 인증 타입으로, 여기서는 Bearer를 사용한다. 이후 HTTP 헤더에 prefix로 붙여주는 타입이기도 하다.
    private final String accessToken;
    private final String refreshToken;
    public TokenDto(String accessToken, String refreshToken) {

        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}