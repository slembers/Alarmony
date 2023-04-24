package com.slembers.alarmony.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration

 // Spring Security 사용을 위한 Configuration Class를 작성하기 위해서
 // WebSecurityConfigurerAdapter를 상속하여 클래스를 생성하고
  //@Configuration 애노테이션 대신 @EnableWebSecurity 애노테이션을 추가한다.

public class SecurityConfig   {



    /**
     * PasswordEncoder를 Bean으로 등록
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // rest api만 사용
                .httpBasic().disable()
                .cors().and()
                .csrf().disable() //csrf 방지
                .formLogin().disable()// /기본 로그인페이지 없애기
                // iframe 요소를 통한 전송 허용
                .headers()
                .frameOptions().sameOrigin()
                .and()
                //
                .authorizeRequests()
//                .antMatchers("/h2-console/**").permitAll() // 추가
              /*  .antMatchers("/members/signup", "/members/login").permitAll()
                .antMatchers(HttpMethod.PUT,"/boards/gif/{gifId}").authenticated()
                .antMatchers(HttpMethod.DELETE,"/boards/gif/{boardId}").authenticated()
                .antMatchers(HttpMethod.DELETE,"/boards/{boardId}").authenticated()
                .antMatchers(HttpMethod.POST,"/boards/{boardId}").authenticated()
                .antMatchers(HttpMethod.PUT,"/boards/{boardId}").authenticated()
                .antMatchers(HttpMethod.POST,"/temp").authenticated()
                .antMatchers(HttpMethod.GET,"/temp/all").hasAnyAuthority("ROLE_MANAGER")
                .antMatchers(HttpMethod.PUT,"/temp/{tempId}").hasAnyAuthority("ROLE_MANAGER")
                .antMatchers(HttpMethod.DELETE,"/temp/{tempId}").hasAnyAuthority("ROLE_MANAGER")
                .antMatchers(HttpMethod.POST, "/comments/**").authenticated()
                .antMatchers(HttpMethod.DELETE,"/comments/**").authenticated()*/
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and().build();
    }


}
