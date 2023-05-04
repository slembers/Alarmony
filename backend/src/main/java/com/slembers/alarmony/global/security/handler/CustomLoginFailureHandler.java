package com.slembers.alarmony.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CustomLoginFailureHandler  implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("[CustomLoginFailureHandler] 진입");

        log.error("[CustomLoginFailureHandler]  발생에러 : " + exception.getMessage());
        if(exception instanceof BadCredentialsException || exception instanceof InternalAuthenticationServiceException){
            log.error("[CustomLoginFailureHandler] 아이디나 비밀번호가 일치하지 않습니다. 다시 확인해주세요. ");
            setErrorResponse(request,response, exception.getMessage(),HttpServletResponse.SC_UNAUTHORIZED,"401");
        } else if (exception instanceof UsernameNotFoundException) {
            log.error("[CustomLoginFailureHandler] 회원이 존재하지 않습니다. ");
            setErrorResponse(request,response,exception.getMessage(),HttpServletResponse.SC_NOT_FOUND,"MEMBER_NOT_FOUND");
        }else if (exception instanceof DisabledException){
            log.error("[CustomLoginFailureHandler] 로그인 할 권한이 없는유저. ");
            setErrorResponse(request,response,exception.getMessage(),HttpServletResponse.SC_FORBIDDEN,"MEMBER_NOT_PERMIT");
        }
    }

    private void setErrorResponse(HttpServletRequest request, HttpServletResponse response, String message , int httpServletResponse, String error) throws IOException {

        final Map<String, Object> body = new HashMap<>();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        body.put("status", httpServletResponse);
        body.put("error",  error);
        body.put("message", message);
        body.put("path", request.getServletPath());
        final ObjectMapper mapper = new ObjectMapper();
        // response 객체에 응답 객체를 넣어줌
        mapper.writeValue(response.getOutputStream(), body);
        response.setStatus(HttpServletResponse.SC_OK);

    }
}
