package com.slembers.alarmony.global.jwt.auth;

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
        log.info("PrincipalDetailsService : 진입");

        //MemberRepository로부터 조회한 결과를 Optional로 반환하고 있기 때문에 map 함수를 이용해서 새로운 UserDetails 객체로 생성하여 반환하고 있다
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("[로그인 실패] 해당 회원이 존재하지 않습니다."));

        return new PrincipalDetails(member);
    }
}