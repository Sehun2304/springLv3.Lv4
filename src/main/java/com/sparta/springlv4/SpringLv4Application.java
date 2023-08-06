package com.sparta.springlv4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableJpaAuditing
@EnableWebSecurity
@SpringBootApplication
@EnableGlobalMethodSecurity(securedEnabled = true) // @Secured 애너테이션 활성화
public class SpringLv4Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringLv4Application.class, args);
    }

}
