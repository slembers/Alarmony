package com.slembers.alarmony.global.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slembers.alarmony.global.execption.CustomException;
import com.slembers.alarmony.member.dto.LoginDto;
import com.slembers.alarmony.member.entity.Member;
import com.slembers.alarmony.member.exception.MemberErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
// (상황) 로그인 API를 호출하고, Json으로 사용자의 아이디와 비밀번호를 보내는 상황
// 전송이 오면 AuthenticationFilter로 요청이 먼저 오게 되고, 아이디와 비밀번호를 기반으로 UserPasswordAuthenticationToken을 발급해주어야 한다
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    public CustomAuthenticationFilter(final AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }


    // Authentication 객체 만들어서 리턴 => 의존 : AuthenticationManager

    /**
     *  인증 요청(request)을 처리하는 메소드
     * @param request from which to extract parameters and perform the authentication
     * @param response the response, which may be needed if the implementation has to do a
     * redirect as part of a multi-stage authentication process (such as OpenID).
     * @return Authentication
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {


        //Authentication 객체는 스프링 시큐리티에서 인증을 수행하는 과정에서 인증된 사용자의 정보를 담고 있는 객체입니다.
        final UsernamePasswordAuthenticationToken authenticationToken;
        log.info("[AuthenticationFilter 진입]");

        try {
            final LoginDto loginDto = new ObjectMapper().readValue(request.getInputStream(), LoginDto.class);
            log.info(loginDto.getUsername()+" "+ loginDto.getPassword());
            authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        } catch (IOException exception) {
            //아이디와 비밀번호 값이 유효하게 오지 않았을경우 예외를 발생시킨다.
            log.error("[CustomAuthenticationFilter] : 아이디 비번이 유효하게 오지 않음");
            throw new CustomException(MemberErrorCode.INPUT_NOT_VALID);
        }


        //요청(request)과 인증 토큰(authenticationToken)을 사용하여 Authentication 객체의 세부 정보를 설정하는 메소드입니다.
        setDetails(request, authenticationToken);

        log.info("[CustomAuthenticationFilter] : setDetail수행 ");

        //AuthenticationManager를 사용하여 authenticationToken을 인증하는 메소드입니다. AuthenticationManager는 AuthenticationProvider들을 이용하여 인증을 처리하고, 인증이 완료되면 Authentication 객체를 반환합니다.
        // 이렇게 반환된 Authentication 객체는 인증 성공시 successfulAuthentication() 메소드에서 처리됩니다.

        log.info("[CustomAuthenticationFilter] : authenticate(authenticationToken) 실행 ");
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }
}
