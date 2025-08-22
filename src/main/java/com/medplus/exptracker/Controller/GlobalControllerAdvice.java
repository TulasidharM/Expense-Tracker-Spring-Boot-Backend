package com.medplus.exptracker.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerAdvice {
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String,String>> handleGlobalException(Exception e){
		var res = new HashMap<String,String >();
		res.put("message", e.getMessage());
		return ResponseEntity.ok(res);
	}
	
}
