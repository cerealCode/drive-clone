package com.example.google_drive_clone.config;

import com.example.google_drive_clone.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Indicates that this class is a source of bean definitions for the application context.
@EnableWebSecurity // Enables Spring Security's web security support and provides the Spring MVC integration.
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disables CSRF protection. This might be necessary in some cases, but be cautious as it can open up security vulnerabilities.
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/perform_login", "/login", "/welcome", "/dashboard", "/register", "/api/register", "/api/file/list", "/css/**", "/js/**", "/images/**").permitAll() // Allows public access to these endpoints.
                .requestMatchers("/api/file/**", "/api/folders/**").authenticated() // Requires authentication for these endpoints.
                .anyRequest().authenticated() // Ensures that any other request requires authentication.
            )
            .formLogin(form -> form
                .loginPage("/login") // Specifies the custom login page URL.
                .loginProcessingUrl("/perform_login") // The URL to submit the login form data to be processed.
                .defaultSuccessUrl("/dashboard", true) // Redirects to the dashboard after successful login.
                .failureUrl("/login?error=true") // Redirects to the login page with an error message on failed login.
                .permitAll() // Allows everyone to access the login page.
            )
            .logout(logout -> logout
                .logoutUrl("/logout") // Specifies the logout URL.
                .logoutSuccessUrl("/login?logout=true") // Redirects to the login page after successful logout.
                .deleteCookies("JSESSIONID") // Deletes the session cookie to ensure the session is invalidated.
                .permitAll() // Allows everyone to access the logout functionality.
            );

        return http.build(); // Builds the security filter chain.
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Defines the password encoder bean using BCrypt, which is a strong and secure hashing algorithm.
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService); // Sets the custom user details service to fetch user details during authentication.
        authProvider.setPasswordEncoder(passwordEncoder()); // Sets the password encoder to verify user passwords.
        return authProvider;
    }
}
