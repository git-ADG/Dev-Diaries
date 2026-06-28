package com.example.dev_diaries.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http
        .cors(t -> t.disable())
        .csrf(t -> t.disable())
        .authorizeHttpRequests(t -> t.requestMatchers(HttpMethod.GET,"/api/notes/**").permitAll())
        .authorizeHttpRequests(t -> t.requestMatchers(HttpMethod.POST, "/api/notes/**").permitAll());

        return http.build();
    }
}
