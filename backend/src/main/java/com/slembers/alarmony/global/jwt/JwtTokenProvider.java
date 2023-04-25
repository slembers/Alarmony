package com.slembers.alarmony.global.jwt;

import com.slembers.alarmony.global.jwt.dto.TokenDto;
import com.slembers.alarmony.member.entity.Member;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.security.core.userdetails.User;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

//JwtProvider는 사용자 정보를 기반으로 JWT를 생성하고, 이후 요청에서 JWT를 검증하여 인증을 수행합니다.
@Component
@RequiredArgsConstructor
@Slf4j
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

    private static Date createExpireDateForOneDay() {
        // 토큰 만료시간은 30일으로 설정
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }

    private  Key createSigningKey() {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
    }


    //access Token
    public  String generateAccessToken(Member member) {
        JwtBuilder builder = Jwts.builder()
                .setClaims(createClaims(member))
                .setSubject(member.getUsername())
                .setHeader(createHeader())

                .setExpiration(createExpireDateForOneDay())
                .signWith(createSigningKey(),SignatureAlgorithm.HS256);

        return builder.compact();
    }

    //Refresh Token
    public  String generateRefreshToken(Member member) {
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
  */



    // Request Header 에서 토큰 정보 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {


            String token = bearerToken.substring(7);
            log.info("자른토큰"+token);
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        log.info("받은 토큰:"+token);
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch(ExpiredJwtException e) {   // Token이 만료된 경우 Exception이 발생한다.
            log.error("Token 만료");

        }catch(JwtException e) {        // Token이 변조된 경우 Exception이 발생한다.
            log.error("Token Error");
        }
        return false;
    }
    // JWT token 복호화 하여 토큰에 들어있는 정보를 꺼내는 매서드
    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get("role") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("role").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        // UserDetails 객체를 만들어서 Authentication 리턴
        log.info("권한"+authorities.toString());
        log.info("이름"+claims.getSubject());
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);

    }
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
