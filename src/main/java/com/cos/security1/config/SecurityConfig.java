package com.cos.security1.config;

import com.cos.security1.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration // 빈 등록
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록됩니다.
// secured 어노테이션 활성화, preAuthorize, postAuthorize 어노테이션 활성화
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Bean
    public BCryptPasswordEncoder encoderPWD() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated() // 인증만 되면 들어갈 수 있는 주소
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/signin") // /signout 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행
//                .usernameParameter("email") // loadUserByUsername username 파라미터 변경
                .loginProcessingUrl("/signin")
                .defaultSuccessUrl("/")
                .and()
                .oauth2Login()
                .loginPage("/signin")
                // 구글 로그인이 완료된 후, 후처리 필요
                // 1. 코드 받기(인증), 2. 액세스 토큰(권한),
                // 3. 사용자 프로필 정보, 4-1. 그 정보를 토대로 회원가입을 자동 진행
                // 4-2. (이메일, 전화번호, 이름, 아이디) 쇼핑몰 -> (집 주소), 백화점 -> (vip등급, 일반등급)
                // 근데 OAuth 클라이언트 쓰면 코드X, 액세스 토큰 + 사용자 프로필 정보를 한번에 O
                .userInfoEndpoint()
                .userService(principalOauth2UserService);

    }
}
