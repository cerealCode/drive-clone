package com.example.google_drive_clone.config;

import com.example.google_drive_clone.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Configures the security filter chain, defining which endpoints are accessible
     * and under what conditions.
     *
     * @param http the HttpSecurity object to configure
     * @return the configured SecurityFilterChain
     * @throws Exception in case of any configuration error
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disables CSRF protection for simplicity; enable in production.
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/perform_login", "/login", "/welcome", "/dashboard", "/register", "/api/register", "/api/file/list", "/css/**", "/js/**", "/images/**").permitAll() // Allow public access to these endpoints
                .requestMatchers("/api/folders/**").authenticated() // Secure folder-related APIs
                .anyRequest().authenticated() // Secure all other endpoints
            )
            .formLogin(form -> form
                .loginPage("/login") // Custom login page
                .loginProcessingUrl("/perform_login") // URL to submit the login form
                .defaultSuccessUrl("/dashboard", true) // Redirect to dashboard after successful login
                .failureUrl("/login?error=true") // Redirect to login page on failure
                .permitAll() // Allow everyone to access the login page
            )
            .logout(logout -> logout
                .logoutUrl("/logout") // URL to log out
                .logoutSuccessUrl("/login?logout=true") // Redirect to login page after logout
                .deleteCookies("JSESSIONID") // Invalidate session cookie
                .permitAll() // Allow everyone to access the logout functionality
            );

        return http.build(); // Build the security filter chain
    }

    /**
     * Configures the password encoder using BCrypt, a strong hashing algorithm.
     * 
     * @return the configured PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the authentication provider, setting up user details service and
     * password encoder.
     * 
     * @return the configured DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService); // Custom user details service
        authProvider.setPasswordEncoder(passwordEncoder()); // BCrypt password encoder
        return authProvider;
    }
}
