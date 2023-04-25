package com.slembers.alarmony.global.jwt;

import com.slembers.alarmony.global.jwt.dto.TokenDto;
import com.slembers.alarmony.member.entity.Member;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;


//JwtProvider는 사용자 정보를 기반으로 JWT를 생성하고, 이후 요청에서 JWT를 검증하여 인증을 수행합니다.
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private static final long ACCESS_TIME =  60 * 1000L;
    private static final long REFRESH_TIME =  2 * 60 * 1000L;
    public static final String ACCESS_TOKEN = "Access_Token";
    public static final String REFRESH_TOKEN = "Refresh_Token";


    private static final String AUTHORITIES_KEY ="Role";
    private  static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        @Value("${jwt.secret}")
        private  String secretKey;
        private Key key;


        // bean으로 등록 되면서 딱 한번 실행된다.
        @PostConstruct
        public void init() {
            byte[] bytes = Base64.getDecoder().decode(secretKey);
            key = Keys.hmacShaKeyFor(bytes);
        }


    private static Date createExpireDateForOneMonth() {
        // 토큰 만료시간은 30일으로 설정
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 30);
        return c.getTime();
    }

    private  Key createSigningKey() {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
    }


    //access Token
    public  String generateJwtToken(Member member) {
        JwtBuilder builder = Jwts.builder()
                .setSubject(member.getUsername())
                .setHeader(createHeader())
                .setClaims(createClaims(member))
                .setExpiration(createExpireDateForOneMonth())
                .signWith(createSigningKey(),SignatureAlgorithm.HS256);

        return builder.compact();
    }


    private  Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();

        header.put("typ", "JWT");
        header.put("alg", "HS256");
        header.put("regDate", System.currentTimeMillis());

        return header;
    }

    private  Map<String, Object> createClaims(Member member) {
        // 공개 클레임에 사용자의 이름과 이메일을 설정하여 정보를 조회할 수 있다.
        //지금 당장에는 권한만 줘도되지만 나중에 다른 정보를 받는다 하면 ...  그래서 따로 수정안함.
        Map<String, Object> claims = new HashMap<>();

        //claims.put("email", user.getEmail());
        //member.getAuthority()는 Enum타입임.
        claims.put("role", member.getAuthority().toString());

        return claims;
    }

    // header 토큰을 가져오는 기능
    public String getHeaderToken(HttpServletRequest request, String type) {
        return type.equals("Access") ? request.getHeader(ACCESS_TOKEN) :request.getHeader(REFRESH_TOKEN);
    }





    public String createToken(String username, String type) {

        Date date = new Date();

        long time = type.equals("Access") ? ACCESS_TIME : REFRESH_TIME;

        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(date.getTime() + time))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    //AccessToken, RefreshToken 을 생성하는 메서드
    public String createAccessToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        long now = (new Date()).getTime();


        Date accessTokenExpiresIn = new Date(now + 86400000*3); // 유효기간 1일*3

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(accessTokenExpiresIn)
                .compact();
    }


    public String createRefreshToken(String username) {
        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + 86400000*3); // 유효기간 1일*3
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
  /*  // 토큰 검증
    public Boolean tokenValidation(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
           // log.error(ex.getMessage());
            return false;
        }
    }
    // 토큰에서 email 가져오는 기능
    public String getUserNameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    // 어세스 토큰 헤더 설정
    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader( ACCESS_TOKEN, accessToken);
    }

    // 리프레시 토큰 헤더 설정
    public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
        response.setHeader(REFRESH_TOKEN, refreshToken);
    }
*/

}
