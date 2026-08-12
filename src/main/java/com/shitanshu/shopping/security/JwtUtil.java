package com.shitanshu.shopping.security;

import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

import java.util.Date;
@Component
public class JwtUtil {
	private final SecretKey SECRET_KEY =
	        Keys.hmacShaKeyFor(
	                "mysecretkeymysecretkeymysecretkey123456".getBytes());

	private final long EXPIRATION_TIME =
	        1000 * 60 * 60 * 24;
	public String generateToken(String email, String role) {

	    return Jwts.builder()
	            .subject(email)
	            .claim("role", role)
	            .issuedAt(new Date())
	            .expiration(
	                    new Date(
	                            System.currentTimeMillis()
	                            + EXPIRATION_TIME
	                    )
	            )
	            .signWith(SECRET_KEY)
	            .compact();

	}
	public String extractEmail(String token) {

	    return Jwts
	            .parser()
	            .verifyWith(SECRET_KEY)
	            .build()
	            .parseSignedClaims(token)
	            .getPayload()
	            .getSubject();

	}
	public String extractRole(String token) {

	    return Jwts
	            .parser()
	            .verifyWith(SECRET_KEY)
	            .build()
	            .parseSignedClaims(token)
	            .getPayload()
	            .get("role", String.class);

	}
	public boolean validateToken(String token) {

	    try {

	        Jwts
	                .parser()
	                .verifyWith(SECRET_KEY)
	                .build()
	                .parseSignedClaims(token);

	        return true;

	    }
	    catch (Exception e) {

	        return false;

	    }

	}
}