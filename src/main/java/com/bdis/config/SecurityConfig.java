package com.bdis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler successHandler;

    public SecurityConfig(CustomAuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**", "/h2-console/**")
            )
            .headers(headers -> headers
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
            )
            .authorizeHttpRequests(auth -> auth
                // Static assets
                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.ico").permitAll()
                
                // Public landing, auth and lookup endpoints
                .requestMatchers("/", "/index", "/login", "/register", "/hospital/register", "/track/**").permitAll()
                .requestMatchers("/api/locations/**", "/api/inventory/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                
                // User / Patient / Donor endpoints
                .requestMatchers("/user/**").hasRole("USER")
                
                // Hospital / Blood Bank endpoints
                .requestMatchers("/hospital/**").hasRole("HOSPITAL")
                
                // Admin endpoints
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // Authenticated AJAX actions
                .requestMatchers(HttpMethod.POST, "/api/requests/create").hasRole("USER")
                .requestMatchers(HttpMethod.PUT, "/api/requests/**").hasAnyRole("HOSPITAL", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/inventory/update").hasAnyRole("HOSPITAL", "ADMIN")
                .requestMatchers("/api/analytics/**").hasAnyRole("ADMIN", "HOSPITAL")
                
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(successHandler)
                .failureUrl("/login?error=true")
                .usernameParameter("email")
                .passwordParameter("password")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/error/403")
            );

        return http.build();
    }
}
