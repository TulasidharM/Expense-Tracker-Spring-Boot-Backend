package com.medplus.exptracker;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class WebMvcConfig {
//	  @Override
//	  public void addCorsMappings(CorsRegistry registry) {
//	    registry.addMapping("/**")
//	            .allowedOrigins("http://localhost:3000")
//	            .allowedMethods("GET", "POST", "PUT", "DELETE")
//	            .allowedHeaders("Content-Type", "Authorization");
//	  }
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration configuration = new CorsConfiguration();
	    configuration.setAllowedOrigins(Arrays.asList("*"));
	    configuration.setAllowedMethods(Arrays.asList("*"));
	    configuration.setAllowedHeaders(Arrays.asList("*"));
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", configuration);
	    return source;
	}
}


