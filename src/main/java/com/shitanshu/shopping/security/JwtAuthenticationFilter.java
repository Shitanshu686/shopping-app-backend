package com.shitanshu.shopping.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {


        String authHeader =
                request.getHeader("Authorization");


        if (authHeader != null &&
                authHeader.startsWith("Bearer ")) {


            String token =
                    authHeader.substring(7);


            try {

                String email =
                        jwtUtil.extractEmail(token);
                String role =
                        jwtUtil.extractRole(token);


                if (email != null &&
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication() == null) {


                    if (jwtUtil.validateToken(token)) {

                    	UsernamePasswordAuthenticationToken authentication =
                    	        new UsernamePasswordAuthenticationToken(
                    	                email,
                    	                null,
                    	                java.util.List.of(
                    	                        new org.springframework.security.core.authority.SimpleGrantedAuthority(
                    	                                "ROLE_" + role
                    	                        )
                    	                )
                    	        );


                        SecurityContextHolder
                                .getContext()
                                .setAuthentication(authentication);

                    }

                }

            }
            catch (Exception e) {

                System.out.println(
                        "Invalid JWT Token"
                );

            }

        }


        filterChain.doFilter(
                request,
                response
        );

    }

}