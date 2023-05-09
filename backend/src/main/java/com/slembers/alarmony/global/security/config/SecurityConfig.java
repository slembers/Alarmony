package com.slembers.alarmony.global.security.config;

import com.slembers.alarmony.global.security.auth.CustomAuthenticationProvider;
import com.slembers.alarmony.global.security.filter.JwtAuthorizationFilter;
import com.slembers.alarmony.global.security.jwt.JwtTokenProvider;
import com.slembers.alarmony.global.security.auth.PrincipalDetailsService;
import com.slembers.alarmony.global.security.filter.CustomAuthenticationFilter;
import com.slembers.alarmony.global.security.handler.CustomLoginFailureHandler;
import com.slembers.alarmony.global.security.handler.CustomLoginSuccessHandler;
import com.slembers.alarmony.global.security.handler.CustomLogoutHandler;
import com.slembers.alarmony.global.security.filter.JwtExceptionFilter;
import com.slembers.alarmony.global.redis.service.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
@Configuration
@Slf4j
public class SecurityConfig  {

    private final PrincipalDetailsService principalDetailsService;

    private final JwtTokenProvider jwtTokenProvider;

    private final CustomLogoutHandler customLogoutHandler;

    private final RedisUtil redisUtil;



    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer()  {
        return (web) -> web.ignoring().antMatchers("/members/sign-up", "/members/verify/**", "/members/login", "/members/refresh", "/members/find-id", "/members/find-pw");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);

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
                .antMatchers("/members/test" )
                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .and()
                .addFilterBefore(customAuthenticationFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthorizationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionFilter(), JwtAuthorizationFilter.class)
                .addFilterBefore(new JwtExceptionFilter(), LogoutFilter.class)
                .logout().logoutUrl("/logout").addLogoutHandler(customLogoutHandler).logoutSuccessHandler(((request, response, authentication) ->
                        SecurityContextHolder.clearContext()
                ));
        return http.build();
    }
    /**
     * 로그인 Filter 생성
     *
     * @return CustomAuthenticationFilter
     * @throws Exception 먼 Exception이지... afterPropertiesSet();일수도
     */
    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter(AuthenticationManager authenticationManager) {


        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager);

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


    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, ex) -> {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write("가입이 완료되지 않았습니다. 이메일함에 인증 메일을 통해 가입을 완료해주세요");
        };
    }
}
