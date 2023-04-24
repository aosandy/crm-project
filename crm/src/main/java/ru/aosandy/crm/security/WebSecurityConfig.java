package ru.aosandy.crm.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

    private final Guard guard;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .httpBasic()
            .and()
            .authorizeHttpRequests(requests -> requests
                .requestMatchers("/abonent/report/{numberPhone}").access(guard)
                .requestMatchers("/abonent/**").hasAuthority("CLIENT")
                .requestMatchers("/manager/**").hasAuthority("MANAGER")
                .anyRequest().authenticated()
            )
            .cors().disable().csrf().disable();
        return http.build();
    }
}
