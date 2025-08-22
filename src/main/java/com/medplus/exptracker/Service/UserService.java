package com.medplus.exptracker.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.medplus.exptracker.Repository.UserRepository;
import com.medplus.exptracker.entity.User;

@Service
public class UserService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            
        return org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .password(user.getPassword())
            .roles(getRoleByRoleId(user.getRoleId()))
            .build();
    }
    
   
    
    private String getRoleByRoleId(Integer roleId) {
        switch (roleId) {
            case 1: return "ADMIN";
            case 2: return "MANAGER";
            default: return "EMPLOYEE";
        }
    }
    
    public User getUserByUserName(String username) throws UsernameNotFoundException{
    	User user = userRepository.findByUsername(username)
    	            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    	return user;
    }
    
    
    public User getUserById(Integer userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        return userOpt.orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }
    
    public void addUser(User user) {
    	userRepository.save(user);
    }
}
