package com.example.rinnesoft.config;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth ->
                auth
                    // 1. HATA SAYFALARI (Şifre sorununun %100 çözümü burasıdır)
                    .dispatcherTypeMatchers(DispatcherType.ERROR)
                    .permitAll()
                    .requestMatchers("/error")
                    .permitAll()
                    // 2. STATİK DOSYALAR (Ana sayfa, CSS, resimler vb.)
                    .requestMatchers(
                        "/",
                        "/index.html",
                        "/admin.html",
                        "/style.css",
                        "/script.js",
                        "/favicon.ico"
                    )
                    .permitAll()
                    .requestMatchers("/*.jpg", "/*.png", "/*.pdf")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/photos/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/photos/**")
                    .authenticated()
                    .requestMatchers(HttpMethod.GET, "/api/photos/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/contact")
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            )
            .formLogin(form -> form.disable())
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
            .username("admin")
            .password(
                "$2a$12$/Q5tgpWbJzXF4gAEtPH4AujUUZXl/dPL8.8JlQtEEqXvUK6F.8XGa"
            )
            .roles("ADMIN")
            .build();
        return new InMemoryUserDetailsManager(admin);
    }
}
