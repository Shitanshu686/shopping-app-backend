package com.shitanshu.shopping.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.shitanshu.shopping.security.JwtAuthenticationFilter;
@Configuration
public class SecurityConfig {

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {

	    CorsConfiguration configuration =
	            new CorsConfiguration();

	    configuration.setAllowedOrigins(
	            Arrays.asList("*")
	    );

	    configuration.setAllowedMethods(
	            Arrays.asList(
	                    "GET",
	                    "POST",
	                    "PUT",
	                    "DELETE",
	                    "OPTIONS"
	            )
	    );

	    configuration.setAllowedHeaders(
	            Arrays.asList("*")
	    );

	    UrlBasedCorsConfigurationSource source =
	            new UrlBasedCorsConfigurationSource();

	    source.registerCorsConfiguration(
	            "/**",
	            configuration
	    );

	    return source;
	}
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth

                        // ======================
                        // PUBLIC APIs
                        // ======================

                        .requestMatchers(
                            "/users/login",
                            "/users/register"
                        ).permitAll()

                        .requestMatchers(
                            org.springframework.http.HttpMethod.GET,
                            "/products",
                            "/products/**"
                        ).permitAll()


                        // ======================
                        // ADMIN ONLY APIs
                        // ======================

                        .requestMatchers(
                            org.springframework.http.HttpMethod.POST,
                            "/products"
                        ).hasRole("ADMIN")
                        
                        .requestMatchers(
                        	    org.springframework.http.HttpMethod.POST,
                        	    "/products/*/specifications"
                        	).hasRole("ADMIN")

                        .requestMatchers(
                            org.springframework.http.HttpMethod.PUT,
                            "/products/**"
                        ).hasRole("ADMIN")
                        .requestMatchers(
                        	    org.springframework.http.HttpMethod.PUT,
                        	    "/orders/*/status"
                        	).hasRole("ADMIN")

                        .requestMatchers(
                            org.springframework.http.HttpMethod.DELETE,
                            "/products/**"
                        ).hasRole("ADMIN")


                        // ======================
                        // EVERYTHING ELSE
                        // ======================

                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                	    jwtAuthenticationFilter,
                	    UsernamePasswordAuthenticationFilter.class
                	);

        return http.build();

    }

}