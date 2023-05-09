package com.slembers.alarmony.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slembers.alarmony.global.security.jwt.AuthConstants;
import com.slembers.alarmony.global.security.jwt.JwtTokenProvider;
import com.slembers.alarmony.global.security.auth.PrincipalDetails;
import com.slembers.alarmony.global.redis.service.RedisUtil;
import com.slembers.alarmony.member.dto.response.TokenResponseDto;
import com.slembers.alarmony.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 로그인 성공시 불리는 핸들러입니다.
 * AccessToken 과 RefreshToken 발급합니다.
 */

@RequiredArgsConstructor
@Slf4j
public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final JwtTokenProvider provider;

    private final RedisUtil redisUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        log.info("[LoginSuccessHandler] 로그인 성공 핸들러 진입");

        Member member = ((PrincipalDetails) authentication.getPrincipal()).getMember();

        log.info("[LoginSuccessHandler] 로그인한 유저 ID] " + member.getUsername());

        String accessToken = provider.generateAccessToken(member);
        String refreshToken = provider.generateRefreshToken(member);

        log.info("[LoginSuccessHandler] AccessToken 발급 " + accessToken);
        log.info("[LoginSuccessHandler] RefreshToken 발급" + refreshToken);

        TokenResponseDto tokenResponseDto = new TokenResponseDto("bearer", accessToken, refreshToken);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(tokenResponseDto);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();

        redisUtil.setDataExpireWithDays(AuthConstants.REDIS_REFRESH_TOKEN_HEADER + member.getUsername(), refreshToken, 14);

        log.info("[LoginSuccessHandler] 로그인 성공");
    }

}
