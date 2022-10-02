package com.github.tamasmajer.unstorage.server.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class AdminTokenFilter extends OncePerRequestFilter {

    @Value("${SECRET_ADMIN_KEY}")
    private String SECRET_ADMIN_KEY;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        boolean isAdmin = ("Bearer " + SECRET_ADMIN_KEY).equals(authHeader);
        if (isAdmin) {
            Set<SimpleGrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    "ADMIN", null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

}
