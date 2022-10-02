package com.github.tamasmajer.unstorage.server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@Order(1)
public class SecurityConfig {

    @Autowired
    private AdminTokenFilter adminTokenFilter;
    @Autowired
    private UserTokenFilter userTokenFilter;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http = http.cors().and().csrf().disable();

        http = http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();

        http = http.exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, exception) -> {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
                        }).and();

        http.addFilterBefore(this.adminTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(this.userTokenFilter, UsernamePasswordAuthenticationFilter.class);

        http.authorizeHttpRequests()
                .antMatchers("/api/auth").hasRole("ADMIN")
                .antMatchers("/api/data").hasRole("USER")
                .anyRequest().authenticated();

        return http.build();
    }
}