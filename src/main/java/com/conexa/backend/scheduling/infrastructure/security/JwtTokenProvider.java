package com.conexa.backend.scheduling.infrastructure.security;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class JwtTokenProvider {

    private final String secretKey = "your-secret-key";
    private final Set<String> invalidatedTokens = new HashSet<>();

    public String generateToken(Object userDetails) {
        long validityInMs = 3600000;
        return Jwts.builder()
                .setSubject(userDetails.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validityInMs))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            if (invalidatedTokens.contains(token)) {
                return false;
            }
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public void invalidateToken(String token) {
        invalidatedTokens.add(token);
    }
}
