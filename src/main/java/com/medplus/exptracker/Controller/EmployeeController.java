package com.medplus.exptracker.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/employee")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Validated
public class EmployeeController {
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Map<String,String>> deleteExpense(@PathVariable int expenseId){		
		
		var res = new HashMap<String,String>();
		res.put("message", "Succesfully deleted expense");
		return ResponseEntity.ok(res);
	}
}
