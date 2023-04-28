package com.slembers.alarmony.global.jwt.handler;

import com.slembers.alarmony.global.jwt.JwtTokenProvider;
import com.slembers.alarmony.global.redis.service.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Service
public class CustomLogoutHandler implements LogoutHandler {


    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {


        String acccessToken = jwtTokenProvider.resolveToken(request,"");
        String refreshToken = jwtTokenProvider.resolveToken(request, "Refresh");

        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
            return;
        }


        //블랙리스트

    }

}
