package com.medplus.exptracker.Controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medplus.exptracker.Model.Expense;
import com.medplus.exptracker.Service.ExpenseService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/employee")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Validated
public class EmployeeController {
	
	@Autowired
	ExpenseService expenseService;
	
	@PostMapping("/addexpense")
    public ResponseEntity<String> createExpense(@Valid @RequestBody Expense expense) {
        expense.setDate(LocalDate.now());
        log.info("Creating expense: " + expense);
        expenseService.createExpense(expense);
        return ResponseEntity.ok("Expense created successfully");
    }

	
	@GetMapping("/get-expenses/{employeeId}")
    public ResponseEntity<List<Expense>> getExpensesForEmployee(@PathVariable Integer employeeId) {
        List<Expense> expenses = expenseService.getExpensesByEmployeeId(employeeId);
        return ResponseEntity.ok(expenses);
    }
	
	@PutMapping("/update-expense")
    public ResponseEntity<Map<String,String>> updateExpense(@Valid @RequestBody Expense expense) {
        expenseService.updateExpense(expense);
        
        var res = new HashMap<String,String>();
        res.put("message", "Expense Updated Succesfully!");
        return ResponseEntity.ok(res);
    }
	
	@DeleteMapping("/delete-expense/{expenseId}")
	public ResponseEntity<Map<String,String>> deleteExpense(@PathVariable int expenseId){		
		var res = new HashMap<String,String>();
		expenseService.deleteExpense(expenseId);
		
		res.put("message", "Succesfully deleted expense");
		return ResponseEntity.ok(res);
	}
	
	
}
