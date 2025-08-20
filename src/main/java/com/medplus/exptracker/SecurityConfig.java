package com.medplus.exptracker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http.csrf(csrf->csrf.disable());
		
		http.authorizeHttpRequests(auth->{
			auth.requestMatchers("/login").hasAnyRole("ADMIN","EMPLOYEE","MANAGER")
				.requestMatchers("/getReports").hasRole("ADMIN")
				.requestMatchers("/getClaims").hasRole("EMPLOYEE")
				.requestMatchers("/getEmpClaims/**").hasRole("MANAGER");
		})
		.httpBasic(Customizer.withDefaults());
	
		return http.build();
	}
	
	
	@Bean
	public InMemoryUserDetailsManager userDetailsService() {
		UserDetails user = User.withDefaultPasswordEncoder()
								.username("dasu")
								.password("dasu")
								.roles("USER")
								.build();
		
		
		UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("54321")
                .roles("ADMIN")
                .build();
		
		return new InMemoryUserDetailsManager(user,admin);
				
	}
}
