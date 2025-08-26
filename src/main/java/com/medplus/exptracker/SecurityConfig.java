package com.medplus.exptracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.medplus.exptracker.Service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private UserService userService;

	 
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http.csrf(csrf->csrf.disable());
		http.authorizeHttpRequests(auth->{
			auth
				.requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
				.requestMatchers("/login").permitAll()
				.requestMatchers("/getuser").authenticated()
				
				.requestMatchers("/api/expenses/categories").permitAll()
				
				.requestMatchers("/api/employee/**").hasRole("EMPLOYEE")
				.requestMatchers("/api/manager/expenses/**").hasRole("MANAGER")
				.requestMatchers("/api/admin/**").hasRole("ADMIN");
		})
		.userDetailsService(userService)
		.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
		.httpBasic(Customizer.withDefaults());
	
		return http.build();
	}
	
}
