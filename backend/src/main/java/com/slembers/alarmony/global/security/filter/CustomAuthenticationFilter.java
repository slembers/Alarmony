package com.slembers.alarmony.global.security.filter;

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

    private AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(@NotNull HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        final UsernamePasswordAuthenticationToken authenticationToken;
        log.info("[AuthenticationFilter 진입]");

        final LoginDto loginDto;
        try {
            loginDto = new ObjectMapper().readValue(request.getInputStream(), LoginDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        setDetails(request, authenticationToken);

        log.info("[CustomAuthenticationFilter] : setDetail수행 ");
        log.info("[CustomAuthenticationFilter] : authenticate(authenticationToken) 실행 ");

        return this.getAuthenticationManager().authenticate(authenticationToken);
    }
}
