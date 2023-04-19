package com.slembers.alarmony.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration

public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // rest api만 사용
                .httpBasic().disable()
                .cors().and()
                .csrf().disable()
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
