package com.sparta.springlv4.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration //해당 클래스가 스프링의 설정 클래스임을 나타냄
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        //BCryptPasswordEncoder()는 spring security에서 제공하는 비밀번호 암호화 클래스
    }
}