package com.slembers.alarmony.global.security.auth;

import com.slembers.alarmony.member.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class PrincipalDetails implements UserDetails {

    private Member member;

    public PrincipalDetails(Member member){
        this.member = member;
    }

    public Member getMember() {
        return member;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    //SimpleGrantedAuthority 클래스는 GrantedAuthority 인터페이스를 구현한 클래스로, 권한 정보를 나타냅니다.
    //mber.getAuthority()를 통해 얻은 AuthorityEnum 값을 toString() 메소드를 호출하여 문자열로 변환한 후, 이를 SimpleGrantedAuthority 클래스의 생성자에 전달합니다.
    //, Member 엔티티의 authority 필드가 ROLE_NOT_PERMITTED 등의 문자열로 저장되어 있어도, Spring Security에서는 SimpleGrantedAuthority 클래스를 사용하여 권한 정보를 인식할 수 있습니다.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(member.getAuthority().toString()));
    }
}