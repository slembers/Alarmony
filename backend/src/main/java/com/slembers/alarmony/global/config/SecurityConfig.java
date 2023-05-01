package com.slembers.alarmony.global.config;

import com.slembers.alarmony.global.jwt.CustomAuthenticationProvider;
import com.slembers.alarmony.global.jwt.JwtAuthorizationFilter;
import com.slembers.alarmony.global.jwt.JwtTokenProvider;
import com.slembers.alarmony.global.jwt.auth.PrincipalDetailsService;
import com.slembers.alarmony.global.jwt.filter.CustomAuthenticationFilter;
import com.slembers.alarmony.global.jwt.filter.RefreshTokenFilter;
import com.slembers.alarmony.global.jwt.handler.CustomLoginSuccessHandler;
import com.slembers.alarmony.global.jwt.handler.CustomLogoutHandler;
import com.slembers.alarmony.global.jwt.handler.JwtExceptionFilter;
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
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
@Configuration
@Slf4j

// Spring Security 사용을 위한 Configuration Class를 작성하기 위함
// Spring security 5.5 부터는  SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>로 써야하나봐요.
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PrincipalDetailsService principalDetailsService;

    private final JwtTokenProvider jwtTokenProvider;

    private final JwtExceptionFilter jwtExceptionFilter;

    private final CustomLogoutHandler customLogoutHandler;

    private final RedisUtil redisUtil;

   // private final RefreshTokenFilter refreshTokenFilter;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/members/sign-up", "/members/verify/**", "/members/login","/members/refresh");
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
                        "/members/**"
                )
                .and()
                .addFilterBefore(
                         new RefreshTokenFilter(),
                        BasicAuthenticationFilter.class
                )
                .authorizeRequests()
                .antMatchers( "/members/test")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .and()
                .addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthorizationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
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
        log.info("[CustomAuthenticationFilter Bean 등록]");

        //authenticationManager() 메소드는 WebSecurityConfigurerAdapter 클래스에서 상속받아 구현한 configure(AuthenticationManagerBuilder auth) 메소드 내부에서 AuthenticationManager 인터페이스를 구현한 객체를 생성하고 반환합니다


        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager());
        //customAuthenticationFilter /members/login  요청 경로로 들어온 요청에 대해 실행됩니다.
        //[작성자생각]
        //UsernamePasswordAuthenticationFilter는 Spring Security에서 인증을 수행하는 필터 중 하나이다.
        //이 필터는(UsernamePasswordAuthenticationFilter 는 "/login" URL로 요청이 들어오면 실행되어 인증을 수행한다.
        //그런데 위에 configure에서  .addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class) 을 봐보자
        //먼저 , url은 아래에서  "/members/login"변경했고 이 요청으로 들어오면 필터에서
        //UsernamePasswordAuthenticationFilter 앞에 커스텀한 필터(customAuthenticationFilter)를 등록했기때문에 , 해당 필터(커스텀)가 요청에 대한 인증 작업을 수행할 수 있도록 한것이다.
        //따라서 로그인 요청 (/members/login) 이 들어오면 customAuthenticationFilter가 불리는 것이다.

        customAuthenticationFilter.setFilterProcessesUrl("/members/login");
        // 인증이 성공했을 때 실행될 핸들러를 등록
        customAuthenticationFilter.setAuthenticationSuccessHandler(customLoginSuccessHandler());
        //afterPropertiesSet() 메소드는 빈이 초기화된 후에 필요한 설정들이 제대로 이루어졌는지 체크하고,
        //필요한 설정이 부족하다면 예외를 발생시키기 위해 사용된다.
        customAuthenticationFilter.afterPropertiesSet();

        return customAuthenticationFilter;
    }


    /**
     * 로그인 성공 핸들러
     *
     * @return CustomLoginSuccessHandler
     */
    @Bean
    public CustomLoginSuccessHandler customLoginSuccessHandler() {
        log.info("[CustomLoginSuccessHandler Bean 등록]");
        return new CustomLoginSuccessHandler(jwtTokenProvider, redisUtil);
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
        //AuthenticationProvider를 등록하여, 해당 Provider가 인증 작업을 수행할 수 있도록 합니다


        //[작성자 생각]
        //configure(AuthenticationManagerBuilder auth) 메소드 내부에서 AuthenticationManager 인터페이스를 구현한 객체를 생성하고 반환합니다
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
