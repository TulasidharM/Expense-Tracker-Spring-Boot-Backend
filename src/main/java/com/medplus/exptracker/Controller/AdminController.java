package com.medplus.exptracker.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medplus.exptracker.Service.ManagerService;
import com.medplus.exptracker.Service.UserService;
import com.medplus.exptracker.entity.User;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class AdminController {

	@Autowired
	UserService userService;
	@Autowired
	ManagerService managerService;
		
	@PostMapping("/add-user")
	public ResponseEntity<Map<String,String>> addNewUser(@RequestBody Map<String, String> json){
		
		System.out.println(json.get("username"));
		User user = new User();
		
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		String encryptedPassword = json.get("password");
		encryptedPassword=passwordEncoder.encode(encryptedPassword);

		
		user.setUsername(json.get("username"));
		user.setPassword(encryptedPassword);
		
		user.setRoleId(Integer.parseInt(json.get("roleId")));
		
		if(user.getRoleId() == 2) {
			user.setManagerId(null);
		} else if(json.get("managerId") == "0") {
			throw new RuntimeException("You need to assign a manager to the employee");
		} else {
			user.setManagerId(Integer.parseInt(json.get("managerId")));			
		}
		
		userService.addUser(user);
		var res = new HashMap<String,String>();
        res.put("message", "User Created Succesfully!");
        return ResponseEntity.ok(res);
	}
	
	@GetMapping("/get-managers")
    public List<User> getAllManagers() {
    	var result = managerService.getAllManager();
        return result;
    }
	
	
}
