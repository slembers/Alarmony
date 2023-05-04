package com.slembers.alarmony.global.jwt.filter;

import com.slembers.alarmony.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        log.info("[JwtAuthorizationFilter(인가)] 진입");
        // 헤더에서 Access 토큰 받아오기
        String accessToken = jwtTokenProvider.resolveToken(request, "Authorization");
        log.info("[JwtAuthorizationFilter (인가)] 헤더에서 받아온 AccessToken :" + accessToken);
        jwtTokenProvider.validateToken(accessToken);
        // 토큰으로부터 유저 정보를 받아
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        // SecurityContext 에 객체 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 다음 Filter 실행
        chain.doFilter(request, response);
    }

}