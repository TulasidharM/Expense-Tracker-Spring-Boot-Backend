package com.medplus.exptracker.Controller;


import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class AuthController {
	@PostMapping("/login")
	public ResponseEntity<Map<String, String>> loginUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String roles = auth.getAuthorities().stream()
						        .map(GrantedAuthority::getAuthority)
						        .collect(Collectors.joining(", "));
		var res = new HashMap<String,String>();
		res.put("role",roles);
		return  ResponseEntity.ok(res);
	}
	
	
}