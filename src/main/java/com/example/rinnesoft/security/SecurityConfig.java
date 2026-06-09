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
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // DelegatingPasswordEncoder kullanıyoruz, prefix'e göre otomatik algılar.
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth ->
                auth
                    // 1. HATA SAYFALARI
                    .dispatcherTypeMatchers(DispatcherType.ERROR)
                    .permitAll()
                    .requestMatchers("/error")
                    .permitAll()
                    // 2. STATİK DOSYALAR
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
            // ŞİFRENİ BURAYA YAZACAKSIN. Başındaki {sha256} kısmını SİLME.
            // Örnek hash "123456" şifresine aittir. Kendi hash'ini oluşturup değiştir.
            .password("{noop}d7adb9f23716c7860223d4db329884b6f8bee0e73fb1ce25ceacfc3f27fdb2ea")
            .roles("ADMIN")
            .build();
        return new InMemoryUserDetailsManager(admin);
    }
}
