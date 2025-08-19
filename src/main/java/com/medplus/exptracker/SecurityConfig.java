package com.medplus.exptracker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers("/login").permitAll()
                    .requestMatchers("/getReports").hasRole("ADMIN")
                    .requestMatchers("/getClaims").hasRole("EMPLOYEE")
                    .requestMatchers("/getEmpClaims/**").hasRole("MANAGER")
                    .anyRequest().authenticated();
            })
            .httpBasic(Customizer.withDefaults());
        
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails employee = User.withDefaultPasswordEncoder()
                .username("employee")
                .password("emp123")
                .roles("EMPLOYEE")
                .build();

        UserDetails manager = User.withDefaultPasswordEncoder()
                .username("manager")
                .password("mgr123")
                .roles("MANAGER")
                .build();

        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("54321")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(employee, manager, admin);
    }
}
