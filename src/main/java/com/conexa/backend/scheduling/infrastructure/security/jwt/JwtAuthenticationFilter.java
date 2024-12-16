package com.conexa.backend.scheduling.infrastructure.security.jwt;

import com.conexa.backend.scheduling.infrastructure.security.services.DoctorDetailsService;
import com.conexa.backend.scheduling.infrastructure.security.services.TokenBlacklistService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final DoctorDetailsService doctorDetailsService;
    private final TokenBlacklistService tokenBlacklistService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        log.info("Incoming Request: URI = {}, Method = {}", requestURI, request.getMethod());

        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            log.info("Handling preflight OPTIONS request: {}", requestURI);
            chain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Authorization header missing or invalid for request: {}", requestURI);
            chain.doFilter(request, response);
            return;
        }

        String jwtToken = authHeader.substring(7);

        try {
            if (tokenBlacklistService.isTokenBlacklisted(jwtToken)) {
                log.warn("Blacklisted token used for request: {}", requestURI);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is blacklisted");
                return;
            }

            String email = jwtUtil.extractEmail(jwtToken);
            log.info("Extracted email from JWT: {}", email);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                var userDetails = doctorDetailsService.loadUserByUsername(email);

                if (jwtUtil.validateToken(jwtToken)) {
                    log.info("JWT validated successfully for user: {}", email);

                    var authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    log.warn("Invalid JWT token for request: {}", requestURI);
                }
            }
        } catch (ExpiredJwtException ex) {
            log.warn("JWT expired for request: {}. Exception: {}", requestURI, ex.getMessage());
            throw ex;
        }

        chain.doFilter(request, response);
    }
}
