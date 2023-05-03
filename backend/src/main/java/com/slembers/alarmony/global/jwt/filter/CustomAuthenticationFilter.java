package com.slembers.alarmony.global.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slembers.alarmony.member.dto.LoginDto;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public CustomAuthenticationFilter(final AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(@NotNull HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        //Authentication 객체는 스프링 시큐리티에서 인증을 수행하는 과정에서 인증된 사용자의 정보를 담고 있는 객체입니다.
        final UsernamePasswordAuthenticationToken authenticationToken;
        log.info("[AuthenticationFilter 진입]");


        final LoginDto loginDto;
        try {
            loginDto = new ObjectMapper().readValue(request.getInputStream(), LoginDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());


        //요청(request)과 인증 토큰(authenticationToken)을 사용하여 Authentication 객체의 세부 정보를 설정하는 메소드입니다.
        setDetails(request, authenticationToken);

        log.info("[CustomAuthenticationFilter] : setDetail수행 ");

        //AuthenticationManager를 사용하여 authenticationToken을 인증하는 메소드입니다. AuthenticationManager는 AuthenticationProvider들을 이용하여 인증을 처리하고, 인증이 완료되면 Authentication 객체를 반환합니다.
        // 이렇게 반환된 Authentication 객체는 인증 성공시 successfulAuthentication() 메소드에서 처리됩니다.

        log.info("[CustomAuthenticationFilter] : authenticate(authenticationToken) 실행 ");
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }
}
