package com.conexa.backend.scheduling.infrastructure.security.jwt;

import com.conexa.backend.scheduling.infrastructure.security.services.DoctorDetailsService;
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
        } else {
            log.warn("Failed to authenticate user with JWT for request: {}", requestURI);
        }

        chain.doFilter(request, response);
    }
}
