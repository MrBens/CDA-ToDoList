package com.todolist.todolist.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.todolist.todolist.model.Role.*;
import static com.todolist.todolist.model.Permission.*;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

  private final JwtAuthenticationFilter jwtAuthFilter;
  private final AuthenticationProvider authenticationProvider;
  private final LogoutHandler logoutHandler;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors()
        .and()
        .csrf()
        .disable()
        .authorizeHttpRequests()
        .requestMatchers(
                "/api/v1/auth/**",
                "/api/v1/users/forgot-password",
                "/api/v1/users/reset-password",
                "/v2/api-docs",
                "/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources",
                "/swagger-resources/**",
                "/configuration/ui",
                "/configuration/security",
                "/swagger-ui/**",
                "/webjars/**",
                "/swagger-ui.html"
        )
          .permitAll()
            // USERS
            .requestMatchers(GET,"/api/v1/users").hasRole(ADMIN.name())
            .requestMatchers(GET,"/api/v1/users/{userId}").hasAnyRole(ADMIN.name(), USER.name())
            .requestMatchers(PUT,"/api/v1/users/{userId}").hasAnyRole(ADMIN.name(), USER.name())
            .requestMatchers(DELETE,"/api/v1/users/{userId}").hasAnyRole(ADMIN.name(), USER.name())

            //ORGANIZATIONS
            .requestMatchers(GET,"/api/v1/tasks", "/api/v1/organizations/{organizationId}").hasRole(ADMIN.name())
            .requestMatchers(POST,"/api/v1/tasks").hasAnyRole(ADMIN.name(), USER.name())
            .requestMatchers(PUT,"/api/v1/tasks/{taskId}").hasAnyRole(ADMIN.name(), USER.name())
            .requestMatchers(DELETE,"/api/v1/tasks/{taskId}").hasAnyRole(ADMIN.name(), USER.name())
        .anyRequest()
          .authenticated()
        .and()
          .sessionManagement()
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .logout()
        .logoutUrl("/api/v1/auth/logout")
        .addLogoutHandler(logoutHandler)
        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
    ;

    return http.build();
  }
}
