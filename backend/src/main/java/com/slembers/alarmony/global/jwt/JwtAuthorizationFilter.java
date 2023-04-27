package com.slembers.alarmony.global.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException,IOException  {
        log.info("JwtAuthorizationFilter 진입");

        // 헤더에서 토큰 받아오기
        String token = jwtTokenProvider.resolveToken(request);

        if (token == null || !jwtTokenProvider.validateToken(token)) {
            // 토큰이 없거나 유효하지 않을 때 401 Unauthorized 응답 반환
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("토큰이 유효하지 않습니다");
            return;
        }

        // 토큰으로부터 유저 정보를 받아
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        // SecurityContext 에 객체 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 다음 Filter 실행
        chain.doFilter(request, response);
    }

}