package com.example.contract.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * Simple API‑key filter. The key is supplied via the {@code X-API-KEY} header.
 * If the header matches the configured key, the request is authenticated with a dummy user.
 */
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private final String expectedApiKey;
    private static final String HEADER_NAME = "X-API-KEY";

    public ApiKeyAuthFilter(String expectedApiKey) {
        this.expectedApiKey = expectedApiKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String provided = request.getHeader(HEADER_NAME);
        if (expectedApiKey != null && expectedApiKey.equals(provided)) {
            // Grant a simple authority; in a real system you would map to real roles.
            var auth = new UsernamePasswordAuthenticationToken(
                    "api-key-user",
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        // Continue regardless – Spring Security will reject unauthenticated requests later.
        filterChain.doFilter(request, response);
    }
}
