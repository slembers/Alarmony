package com.slembers.alarmony.global.security.jwt;

import com.slembers.alarmony.global.redis.service.RedisUtil;
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
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


/**
 * Jwt 생성 및 검증
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    private final RedisUtil redisUtil;

    @Value("${jwt.secret}")
    private String secretKey;
    private Key key;


    @PostConstruct
    public void init() {
        log.info("[JwtTokenProvider 생성 init]");
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    /**
     * 일 수로 만료시간을 생성
     *
     * @param days 일 수
     * @return Date 만료날짜
     */
    private static Date createExpireDateWithDays(int days) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, days);
        return c.getTime();
    }


    /**
     * 밀리초로 만료시간을 생성
     *
     * @param Milliseconds 밀리 초
     * @return Date 만료날짜
     */
    public Date createExpireDateWithMilliSeconds(long Milliseconds) {
        long nowMillis = System.currentTimeMillis();
        long expireMillis = nowMillis + (Milliseconds);
        return new Date(expireMillis);
    }

    /**
     * Access Token 발급
     *
     * @param member 사용자 Entity
     * @return Access Token
     * 유효기간 : 3시간
     */
    public String generateAccessToken(Member member) {
        JwtBuilder builder = Jwts.builder()
                .setClaims(createClaims(member))
                .setSubject(member.getUsername())
                .setHeader(createHeader())
                .setExpiration(createExpireDateWithMilliSeconds(3 * 60 * 60 * 1000))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(key, SignatureAlgorithm.HS256);

        return builder.compact();
    }

    /**
     * Refresh Token 발급
     *
     * @param member 사용자 Entity
     * @return Refresh Token
     * 유효기간 14일 (2주)
     */
    public String generateRefreshToken(Member member) {
        JwtBuilder builder = Jwts.builder()
                .setSubject(member.getUsername())
                .setHeader(createHeader())
                .setClaims(createClaims(member))
                .setExpiration(createExpireDateWithDays(14))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(key, SignatureAlgorithm.HS256);

        return builder.compact();
    }


    private Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();

        header.put("typ", "JWT");
        header.put("alg", "HS256");
        header.put("regDate", System.currentTimeMillis());

        return header;
    }

    private Map<String, Object> createClaims(Member member) {

        Map<String, Object> claims = new HashMap<>();

        claims.put("role", member.getAuthority().toString());

        return claims;
    }


    // Request Header 에서 토큰 정보 추출
    public String resolveToken(HttpServletRequest request, String authHeader) {
        String bearerToken = request.getHeader(authHeader);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            log.info(authHeader + "-자른토큰" + token);
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 정보를 검증하는 메서드
    public void validateToken(String token) {
        if (token == null) {
            log.error("[JwtTokenProvider] Token값이 존재하지 않습니다.");
            throw new JwtException("Token값이 존재하지 않습니다.");
        }

        //블랙리스트에 존재하면 에러 반환

        if (redisUtil.existToken("Black:"+token)) {
            throw new JwtException("블랙리스트에 존재하는 액세스 토큰입니다.");
        }


        log.info("받은 토큰:" + token);
        try {

            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);

        } catch (ExpiredJwtException e) {   // Token이 만료된 경우 Exception이 발생한다.
            log.error("Token 유효시간 만료. 재발급 필요");
            throw new JwtException("Token 유효시간 만료. 재발급 필요");
        } catch (JwtException e) {        // Token이 변조된 경우 Exception이 발생한다.
            log.error("Token 에러");
            log.error(e.getMessage());
            throw new JwtException("Token 에러되었습니다.");
        }

    }

    public void validRefreshToken(String token) {
        if (token == null) {
            log.error("[JwtTokenProvider] Token값이 존재하지 않습니다.");
            throw new JwtException("Token값이 존재하지 않습니다.");
        }

        log.info("받은 Refresh 토큰:" + token);
        try {

            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);

        } catch (ExpiredJwtException e) {   // Token이 만료된 경우 Exception이 발생한다.
            log.error("Token 유효시간 만료. 로그아웃 해야한다.");
            throw new JwtException("Token 유효시간 만료. 로그아웃 해야한다.");
        } catch (JwtException e) {        // Token이 변조된 경우 Exception이 발생한다.
            log.error("Token 에러");
            log.error(e.getMessage());
            throw new JwtException("Token 에러되었습니다.");
        }

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
        log.info("권한" + authorities.toString());
        log.info("이름" + claims.getSubject());
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

    public long getAccessTokenExpirationTime(String token) {
        // 액세스 토큰의 만료 시간을 조회합니다.
        Claims claims = parseClaims(token);
        Date expirationDate = claims.getExpiration();

        log.info("현재 시간" + System.currentTimeMillis());
        log.info("만료시간 시간" + expirationDate);
        // 만료 시간과 현재 시간의 차이를 계산하여, 만료 시간까지의 시간 간격을 반환합니다. 초단위로 만료기간을 넘겨야하기 때문에 초 /1000 으로 해서 밀리세컨드 단위를초로 만듭니다.
        return (expirationDate.getTime() - System.currentTimeMillis()) / 1000;
    }
}
