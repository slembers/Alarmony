package com.slembers.alarmony.global.security.auth;

import com.slembers.alarmony.global.security.auth.PrincipalDetails;
import com.slembers.alarmony.member.entity.Member;
import com.slembers.alarmony.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    //로그인 할 유저가 존재하는지 확인합니다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("[PrincipalDetailsService 진입] : loadUserByUsername ");
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("[로그인 실패] 가입되지 않은 회원입니다."));

        return new PrincipalDetails(member);
    }
}