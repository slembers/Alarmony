package com.slembers.alarmony.global.jwt.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//AuthenticationEntryPoint는 인증되지 않은 사용자의 요청을 캐치하여 사용자를 인증 페이지로 보내거나, 에러 메시지를 반환하는 역할을 합니다.
public class CustomAuthenticationEntryPoint  implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        final Map<String, Object> body = new HashMap<>();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", authException.getMessage());
        body.put("path", request.getServletPath());
        final ObjectMapper mapper = new ObjectMapper();
        // response 객체에 응답 객체를 넣어줌
        mapper.writeValue(response.getOutputStream(), body);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
