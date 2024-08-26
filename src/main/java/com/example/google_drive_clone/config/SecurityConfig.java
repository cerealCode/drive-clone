package com.example.google_drive_clone.config;

import com.example.google_drive_clone.service.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.debug("Starting configuration of the security filter chain.");

        try {
            http
                // Disable CSRF for simplicity in this example, be cautious in production environments
                .csrf(csrf -> csrf.disable())
                
                // Configure authorization rules
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/", "/login", "/api/register", "/css/**", "/js/**", "/images/**").permitAll();
                    auth.anyRequest().authenticated();
                })
                
                // Configure form login
                .formLogin(form -> {
                    form.loginPage("/login")
                        .loginProcessingUrl("/perform_login")
                        .defaultSuccessUrl("/welcome", true)
                        .failureUrl("/login?error=true")
                        .permitAll();
                    logger.debug("Form login configured: loginPage='/login', loginProcessingUrl='/perform_login', defaultSuccessUrl='/welcome'");
                })
                
                // Configure logout
                .logout(logout -> {
                    logout.logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .deleteCookies("JSESSIONID")
                        .permitAll();
                    logger.debug("Logout configured: logoutUrl='/logout', logoutSuccessUrl='/login?logout=true'");
                })
                
                // Session management configuration
                .sessionManagement(session -> {
                    session.maximumSessions(1)
                           .maxSessionsPreventsLogin(false);
                    logger.debug("Session management configured: maximumSessions=1, maxSessionsPreventsLogin=false");
                });

            logger.info("Security configuration successfully applied.");
            return http.build();
        } catch (Exception e) {
            logger.error("Error in configuring security settings: ", e);
            throw e;
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.debug("Configuring BCryptPasswordEncoder.");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        try {
            DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
            authProvider.setUserDetailsService(userDetailsService);
            authProvider.setPasswordEncoder(passwordEncoder());
            logger.info("Authentication provider configured successfully.");
            return authProvider;
        } catch (Exception e) {
            logger.error("Error in configuring authentication provider: ", e);
            throw e;
        }
    }
}
