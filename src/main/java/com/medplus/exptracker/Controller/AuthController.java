package com.medplus.exptracker.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            
            Map<String, Object> response = new HashMap<>();
            
            if (isValidUser(loginRequest.getUsername(), loginRequest.getPassword())) {
                String role = getUserRole(loginRequest.getUsername());
                response.put("role", role);
                response.put("message", "Login successful");
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "Invalid credentials");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Login failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    private boolean isValidUser(String username, String password) {
        return (username.equals("admin") && password.equals("54321")) ||
               (username.equals("manager") && password.equals("mgr123")) ||
               (username.equals("employee") && password.equals("emp123"));
    }
    
    private String getUserRole(String username) {
        switch (username.toLowerCase()) {
            case "admin": return "admin";
            case "manager": return "manager";
            case "employee": return "employee";
            default: return "guest";
        }
    }
    
    public static class LoginRequest {
        private String username;
        private String password;
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
