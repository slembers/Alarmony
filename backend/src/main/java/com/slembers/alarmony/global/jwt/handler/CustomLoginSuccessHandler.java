package com.slembers.alarmony.global.jwt.handler;

import com.slembers.alarmony.global.jwt.AuthConstants;
import com.slembers.alarmony.global.jwt.JwtTokenProvider;
import com.slembers.alarmony.global.jwt.auth.PrincipalDetails;
import com.slembers.alarmony.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Spring Security의 인증 성공 핸들러를 구현한 클래스입니다.
//인증이 성공하면, SavedRequestAwareAuthenticationSuccessHandler 클래스에서 상속 받은 onAuthenticationSuccess() 메소드가 호출되며, 이 메소드에서는 인증된 사용자를 로그인 처리

//이 클래스를 상속받아 LoginSuccessHandler 클래스를 구현함으로써, 로그인 성공 시 추가적인 처리를 수행할 수 있습니다.
@RequiredArgsConstructor
@Slf4j
public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final JwtTokenProvider provider;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {


        log.info("[CustomLoginSuccessHandler 진입]");
        //Spring Security에서 인증된 사용자 정보를 가져와서 Member 객체로 변환하는 코드입니다.
        //authentication.getPrincipal() 메소드는 현재 인증된 사용자 정보를 반환하는 메소드입니다.
         Member member = ((PrincipalDetails) authentication.getPrincipal()).getUser();
         String token = provider.generateJwtToken(member);

        log.info("[CustomLoginSuccessHandler에서 토큰 생성 ]"+ token);

        response.addHeader(AuthConstants.AUTH_HEADER, AuthConstants.TOKEN_TYPE + ":" + AuthConstants.ACCESS_TOKEN + " " + token);
       // response.addHeader(AuthConstants.AUTH_HEADER, AuthConstants.TOKEN_TYPE + ":" + AuthConstants.REFRESH_TOKEN + " " + token);
      //  super.onAuthenticationSuccess(request, response, authentication);
    }
}
