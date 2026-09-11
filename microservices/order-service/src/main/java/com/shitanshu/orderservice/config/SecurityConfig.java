package com.shitanshu.orderservice.config;
import org.springframework.http.HttpMethod;
import com.shitanshu.orderservice.security.JwtAuthenticationFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

                // Order APIs
            		.requestMatchers(
            			    HttpMethod.GET,
            			    "/orders",
            			    "/orders/**"
            			).authenticated()

            			.requestMatchers(
            			    HttpMethod.POST,
            			    "/orders"
            			).authenticated()

            			.requestMatchers(
            			    HttpMethod.PUT,
            			    "/orders/*/status"
            			).hasRole("ADMIN")

                .anyRequest().authenticated()
            )
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
