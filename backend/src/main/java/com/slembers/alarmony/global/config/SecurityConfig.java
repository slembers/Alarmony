package com.slembers.alarmony.global.config;

import com.slembers.alarmony.global.jwt.CustomAuthenticationProvider;
import com.slembers.alarmony.global.jwt.filter.JwtAuthorizationFilter;
import com.slembers.alarmony.global.jwt.JwtTokenProvider;
import com.slembers.alarmony.global.jwt.auth.PrincipalDetailsService;
import com.slembers.alarmony.global.jwt.filter.CustomAuthenticationFilter;
import com.slembers.alarmony.global.jwt.handler.CustomLoginFailureHandler;
import com.slembers.alarmony.global.jwt.handler.CustomLoginSuccessHandler;
import com.slembers.alarmony.global.jwt.handler.CustomLogoutHandler;
import com.slembers.alarmony.global.jwt.filter.JwtExceptionFilter;
import com.slembers.alarmony.global.redis.service.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
@Configuration
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PrincipalDetailsService principalDetailsService;

    private final JwtTokenProvider jwtTokenProvider;

    private final JwtExceptionFilter jwtExceptionFilter;

    private final CustomLogoutHandler customLogoutHandler;

    private final RedisUtil redisUtil;


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/members/sign-up", "/members/verify/**", "/members/login", "/members/refresh","/members/find-id","/members/find-pw");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .requestMatchers()
                .antMatchers(
                        "/members/**", "/alert/**", "/groups/**", "/alarms/**"
                )
                .and()
                .authorizeRequests()
                .antMatchers("/members/test")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .and()
                .addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthorizationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthorizationFilter.class)
                .addFilterBefore(jwtExceptionFilter, LogoutFilter.class)
                .logout().logoutUrl("/logout").addLogoutHandler(customLogoutHandler).logoutSuccessHandler(((request, response, authentication) ->
                        SecurityContextHolder.clearContext()
                ));
    }

    /**
     * 로그인 Filter 생성
     *
     * @return CustomAuthenticationFilter
     * @throws Exception 먼 Exception이지... afterPropertiesSet();일수도
     */
    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() throws Exception {

        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager());
        //로그인 url 설정
        customAuthenticationFilter.setFilterProcessesUrl("/members/login");
        // 로그인 성공 , 실패 핸들러 등록
        customAuthenticationFilter.setAuthenticationSuccessHandler(customLoginSuccessHandler());
        customAuthenticationFilter.setAuthenticationFailureHandler(customLoginFailureHandler());
        customAuthenticationFilter.afterPropertiesSet();

        return customAuthenticationFilter;
    }


    /**
     * 로그인 성공 핸들러
     */
    @Bean
    public CustomLoginSuccessHandler customLoginSuccessHandler() {
        return new CustomLoginSuccessHandler(jwtTokenProvider, redisUtil);
    }

    /**
     * 로그인 실패 핸들러
     */
    @Bean
    public CustomLoginFailureHandler customLoginFailureHandler() {
        return new CustomLoginFailureHandler();
    }

    /**
     * AuthenticationProvider 생성
     * 역할 : 스프링 시큐리티에서 인증 처리 , uthenticationManager에서 인증 처리를 위해 사용됨
     *
     * @return CustomAuthenticationProvider
     */
    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(principalDetailsService, bCryptPasswordEncoder());
    }

    /**
     * AuthenticationManagerBuilder를 이용하여 AuthenticationProvider를 등록하는 코드입니다
     * AuthenticationManagerBuilder는 AuthenticationProvider를 생성하고 등록하여, 인증(Authentication)에 사용될 수 있는 AuthenticationManager를 만듭니다.
     *
     * @param authenticationManagerBuilder the {@link AuthenticationManagerBuilder} to use
     */
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) {
        authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider());
    }


    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, ex) -> {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write("가입이 완료되지 않았습니다. 이메일함에 인증 메일을 통해 가입을 완료해주세요");
        };
    }
}
