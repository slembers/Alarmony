package com.slembers.alarmony.global.jwt;

import com.slembers.alarmony.global.execption.CustomException;
import com.slembers.alarmony.global.jwt.auth.PrincipalDetails;
import com.slembers.alarmony.global.jwt.auth.PrincipalDetailsService;
import com.slembers.alarmony.member.entity.AuthorityEnum;
import com.slembers.alarmony.member.exception.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationProvider  implements AuthenticationProvider {

    private final PrincipalDetailsService principalDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;
    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        log.info("[AuthenticationProvider 진입]");
        final UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        // AuthenticaionFilter에서 생성된 토큰으로부터 아이디와 비밀번호를 조회함
        final String username = token.getName();
        final String password = (String) token.getCredentials();
        // UserDetailsService를 통해 DB에서 아이디로 사용자 조회
        final PrincipalDetails memberDetails = (PrincipalDetails) principalDetailsService.loadUserByUsername(username);
        if (!passwordEncoder.matches(password, memberDetails.getPassword())) {
            log.info("비번이 일치하지 않음");
            throw new BadCredentialsException(memberDetails.getUsername() + "비밀번호가 일치하지 않습니다.");

        }
        //권한 체크
        if(memberDetails.getMember().getAuthority().toString().equals(AuthorityEnum.ROLE_NOT_PERMITTED.name())){
            log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            throw new AccessDeniedException("You do not have permission to access this resource.");

        }
        return new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
