package com.azael.taskapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.azael.taskapp.persistence.repositories.UserRepository;

@Configuration
public class ApplicationConfiguration {
    private final UserRepository userRepository;

    public ApplicationConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    UserDetailsService userDetailsService() {
        // return email -> userRepository.findByEmail(email)
        // .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return username -> userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // @Bean
    // AuthenticationProvider authenticationProvider(
    // @Lazy UserDetailsService userDetailsService,
    // BCryptPasswordEncoder passwordEncoder) {
    // DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    // provider.setUserDetailsService(userDetailsService);
    // provider.setPasswordEncoder(passwordEncoder);
    // return provider;
    // }
}