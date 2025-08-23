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

	//TODO: shorten number of endpoints
	//TODO: login and getuser can be combined
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http.csrf(csrf->csrf.disable());
		http.authorizeHttpRequests(auth->{
			auth
				.requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
				.requestMatchers("/login").authenticated()
				.requestMatchers("/getuser").authenticated()
				
				.requestMatchers("/api/expenses/categories").permitAll()
				
				.requestMatchers("/api/employee/get-expenses/*").hasRole("EMPLOYEE")
				.requestMatchers("/api/employee/addexpense").hasRole("EMPLOYEE")
				.requestMatchers("/api/employee/update-expense").hasRole("EMPLOYEE")
				.requestMatchers("/api/employee/delete-expense/*").hasRole("EMPLOYEE")

				.requestMatchers("/api/manager/expenses/*/approve").hasRole("MANAGER")
				.requestMatchers("/api/manager/expenses/*/reject").hasRole("MANAGER")
				.requestMatchers("/api/manager/expenses/*").hasRole("MANAGER")
				.requestMatchers("/api/manager/expenses/*/approvedAmounts").hasRole("MANAGER")
				.requestMatchers("/api/manager/expenses/*/employeeList").hasRole("MANAGER")
		        .requestMatchers("/api/manager/expenses/*/categoryWiseApproved").hasRole("MANAGER")
				
			
				.requestMatchers("/api/admin/add-user").hasRole("ADMIN")
				.requestMatchers("/api/admin/get-managers").hasRole("ADMIN");
			
		})
		.userDetailsService(userService)
		.httpBasic(Customizer.withDefaults());

	
		return http.build();
	}
	
}