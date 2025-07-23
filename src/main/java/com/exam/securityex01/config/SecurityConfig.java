package com.exam.securityex01.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록됨

public class SecurityConfig{
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(CsrfConfigurer::disable); // csrf 비활성화
        http.authorizeHttpRequests(authorize ->
            authorize
                .requestMatchers("/user/**").authenticated() // 별도로 인증이 필요한 url 적어두기
                .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER") // 역할 필요하다
                .requestMatchers("/admin/**").hasAnyRole("ADMIN")

                .anyRequest().permitAll()
        );

        return http.build();
    }
}
