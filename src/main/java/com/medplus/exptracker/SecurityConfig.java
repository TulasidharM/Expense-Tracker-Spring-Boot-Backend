package com.medplus.exptracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
				.requestMatchers("/login").authenticated()
				.requestMatchers("/getuser").authenticated()
				.requestMatchers("/api/expenses/addexpense").hasRole("EMPLOYEE")
				.requestMatchers("/api/expenses/categories").permitAll()
				.requestMatchers("/api/expenses/employee/**").permitAll()
				.requestMatchers("/getReports").hasRole("ADMIN")
				.requestMatchers("/getClaims").hasRole("EMPLOYEE")
				.requestMatchers("/getEmpClaims/**").hasRole("MANAGER")
				//TODO need to change these from permitall to hasrole manager
				.requestMatchers("/api/manager/expenses/*/approve").permitAll()
				.requestMatchers("/api/manager/expenses/*/reject").permitAll()
				.requestMatchers("/api/manager/expenses/*").permitAll();
		})
		.userDetailsService(userService)
		.httpBasic(Customizer.withDefaults());

	
		return http.build();
	}
	
}