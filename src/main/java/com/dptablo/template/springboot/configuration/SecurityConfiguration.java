package com.dptablo.template.springboot.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .cors()

                .and()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeHttpRequests()
                        .anyRequest().permitAll();

//                .authorizeHttpRequests()
//                    .requestMatchers(
//                            "/api/v3/swagger",
//                            "/api/v3/docs",
//                            "/api-docs",
//                            "/swagger-ui.html",
//                            "/api-docs").permitAll()
//                    .requestMatchers("/api/**")
//                        .hasRole("USER")
//                    .anyRequest().authenticated();

        return http.build();
    }
}
