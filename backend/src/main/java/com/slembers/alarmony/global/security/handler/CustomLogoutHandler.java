package com.slembers.alarmony.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slembers.alarmony.global.security.jwt.JwtTokenProvider;
import com.slembers.alarmony.global.redis.service.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomLogoutHandler implements LogoutHandler {


    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;

    private final String REFRESH_HEADER = "Refresh:";
    private final String BLACKLIST_HEADER = "Black:";

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        log.info("[LogoutHandler] 진입");

        //헤더를 통해서 AccessToken과 Refresh토큰을 가져옵니다.
        String accessToken = jwtTokenProvider.resolveToken(request, "Authorization");
        String refreshToken = jwtTokenProvider.resolveToken(request, "Refresh");

        //AccessToken의 유효성 검증을 합니다.
        jwtTokenProvider.validateToken(accessToken);

        //로그인 되어있는 사용자 정보를 가져온다.
        // Access Token 에서 authentication 을 가져옵니다.
        authentication = jwtTokenProvider.getAuthentication(accessToken);


        String username = authentication.getName();

        String realRefreshToken = redisUtil.getData(REFRESH_HEADER + username);
        log.info("[LogoutHandler] 진입 리프레시 토큰" + realRefreshToken);
        log.info("[LogoutHandler] 진입 리프레시 헤더 토큰" + refreshToken);

        //refreshToken값이 존재하지 않는다면 만료된 것이므로 로그아웃 시킵니다.
        if (Objects.isNull(realRefreshToken)) {

            //엑세스 토큰 블랙 리스트에 저장 ->  남은 유효시간을 구해서 redis에 저장
            redisUtil.setDataExpireWithSecond(BLACKLIST_HEADER + accessToken, "", jwtTokenProvider.getAccessTokenExpirationTime(accessToken));

                setResponseMessage(request, response);

        } else if (realRefreshToken.equals(refreshToken)) {

            //로그아웃시킨다.

            // log.info("[LogoutHandler] 진입 리프레시 토큰"+ realRefreshToken);

            //refresh 토큰 삭제
            redisUtil.deleteData(REFRESH_HEADER + username);
            //엑세스 토큰 블랙 리스트에 저장 ->  남은 유효시간을 구해서 redis에 저장

            log.info("남은 access_token 시간" + jwtTokenProvider.getAccessTokenExpirationTime(accessToken));
            redisUtil.setDataExpireWithSecond(BLACKLIST_HEADER + accessToken, "", jwtTokenProvider.getAccessTokenExpirationTime(accessToken));


                setResponseMessage(request, response);


        } else if (!Objects.isNull(realRefreshToken) && !realRefreshToken.equals(refreshToken)) {
            //로그아웃을 시킬지 말지  -> 보안상 문제가 있는 경우이다.
            //TODO

        }

        //우효성 검사가 통화한다면 로그아웃 처리를 할 수 있다.

        // redis를 통해 유효한 accesstoken 무효화

        //db 에 저장된 refresh token값이 없다면 유효시간이 만료된것이기 때문에 로그아웃 처리를 해버린다.
        //존재한다면 같은지 비교하고 유효한지 체크한다. 안같으면?

        //서로 다른 리프레쉬 값이면 탈취당함 -> 보안 경고

        // 유효성 체크
    }

    private void setResponseMessage(HttpServletRequest request, HttpServletResponse response)  {

        final Map<String, Object> body = new HashMap<>();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        body.put("message", "로그아웃완료");
        body.put("path", request.getServletPath());
        final ObjectMapper mapper = new ObjectMapper();
        // response 객체에 응답 객체를 넣어줌

        try {
            mapper.writeValue(response.getOutputStream(), body);
        }catch (IOException e){
            log.error("[로그아웃] 네트워크에러로 response에 값 담을때 문제 발생 ");
        }
        response.setStatus(HttpServletResponse.SC_OK);

    }

}
