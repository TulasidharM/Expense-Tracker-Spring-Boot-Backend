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

import com.medplus.exptracker.Model.Category;
import com.medplus.exptracker.Model.Expense;
import com.medplus.exptracker.Service.ExpenseService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Validated
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;
    
    
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Expense>> getExpensesForEmployee(@PathVariable Integer employeeId) {
        List<Expense> expenses = expenseService.getExpensesByEmployeeId(employeeId);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/manager/{managerId}")
    public ResponseEntity<List<Expense>> getExpenseByManagerId(@PathVariable Integer managerId) {
        List<Expense> expenses = expenseService.getExpensesByManagerId(managerId);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpenses() {
        List<Expense> expenses = expenseService.getAllExpenses(null);

    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpenses( @RequestParam(required = false) String status) {
        List<Expense> expenses = expenseService.getAllExpenses(status);
        return ResponseEntity.ok(expenses);
    }

    @PostMapping("/addexpense")
    public ResponseEntity<String> createExpense(@Valid @RequestBody Expense expense) {
        expense.setDate(LocalDate.now());
        log.info("Creating expense: " + expense);
        expenseService.createExpense(expense);
        return ResponseEntity.ok("Expense created successfully");
    }

    @PutMapping("/update-expense")
    public ResponseEntity<Map<String,String>> updateExpense(@Valid @RequestBody Expense expense) {
        expenseService.updateExpense(expense);
        
        var res = new HashMap<String,String>();
        res.put("message", "Expense Updated Succesfully!");
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable Integer id, Integer employeeId) {
        expenseService.deleteExpense(id, employeeId);
        return ResponseEntity.ok("Expense deleted successfully");
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<String> approveExpense(@PathVariable Integer id, @RequestBody Map<String, Object> req) {
        String remarks = (String) req.get("remarks");
        Integer managerId = (Integer) req.get("managerId");
        expenseService.approveExpense(id, remarks, managerId);
        return ResponseEntity.ok("Expense approved successfully");
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<String> rejectExpense(@PathVariable Integer id, @RequestBody Map<String, Object> req) {
        String remarks = (String) req.get("remarks");
        Integer managerId = (Integer) req.get("managerId");
        expenseService.rejectExpense(id, remarks, managerId);
        return ResponseEntity.ok("Expense rejected successfully");
    }

    @GetMapping(value = "/categories", produces = "application/json")
    public List<Category> getCategories() {
        return expenseService.getCategories();
    }
}