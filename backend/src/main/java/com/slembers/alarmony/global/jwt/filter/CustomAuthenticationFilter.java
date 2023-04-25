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

//@RequiredArgsConstructor
@Slf4j
// 로그인 API를 호출하고, Json으로 사용자의 아이디와 비밀번호를 보내는 상황
//전송이 오면 AuthenticationFilter로 요청이 먼저 오게 되고, 아이디와 비밀번호를 기반으로 UserPasswordAuthenticationToken을 발급해주어야 한다
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    public CustomAuthenticationFilter(final AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }


    // Authentication 객체 만들어서 리턴 => 의존 : AuthenticationManager
    // 인증 요청시에 실행되는 함수 => /login
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {


        final UsernamePasswordAuthenticationToken authenticationToken;
        log.info("[AuthenticationFilter 진입]");

        try {
            final LoginDto loginDto = new ObjectMapper().readValue(request.getInputStream(), LoginDto.class);
            log.info(request.getInputStream().toString());

            log.info(loginDto.getUsername()+" "+ loginDto.getPassword());
            authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        } catch (IOException exception) {
            //아이디와 비밀번호 값이 유효하게 오지 않았을경우 예외를 발생시킨다.
            throw new CustomException(MemberErrorCode.INPUT_NOT_VALID);
        }

        setDetails(request, authenticationToken);
        return this.getAuthenticationManager().authenticate(authenticationToken);
        //return super.attemptAuthentication(request, response);
    }
}
