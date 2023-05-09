package com.slembers.alarmony.global.security.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


//Spring Security에서 AccessDeniedHandler 인터페이스를 구현한 클래스입니다.
//인터페이스는 인증된 사용자가 충분한 권한이 없는 보호된 리소스에 액세스하려고 할 때 발생하는 'AccessDeniedException'을 처리하는 역할을 합니다.
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

    }
}
