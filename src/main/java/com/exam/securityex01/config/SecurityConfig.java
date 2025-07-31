package com.exam.securityex01.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록됨

public class SecurityConfig{
    // 이렇게 Bean 메소드에 적게 되면
    // 해당 메소드의 리턴되는 오브젝트를 Ioc로 등록해준다.
    @Bean
    public BCryptPasswordEncoder encodePwd(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(CsrfConfigurer::disable); // csrf 비활성화
        http.authorizeHttpRequests(authorize ->
            authorize
                .requestMatchers("/user/**").authenticated() // 별도로 인증이 필요한 url 적어두기 -> 인증만 되면 들어갈 수 있는 url
                .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER") // 역할 필요하다
                .requestMatchers("/admin/**").hasAnyRole("ADMIN") // 로그인 안하고 접근하면 403 에러 뜬다
                .anyRequest().permitAll()) // 나머지 경로는 모두 허용되어있다.
                .formLogin(form -> form
                    .loginPage("/loginForm") // 커스텀 로그인 페이지 -> 만일 로그인이 안되어 있으면 login 페이지로 바로 가게 만들어줌
                    .loginProcessingUrl("/login") // /login 주소가 호출되면 시큐리티가 낚아채서 대신 로그인을 진행한다.
                    .defaultSuccessUrl("/")
                    .permitAll()
                );

        return http.build();
    }
}
