package com.medplus.exptracker.Controller;

import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medplus.exptracker.Model.Category;
import com.medplus.exptracker.Model.Expense;
import com.medplus.exptracker.Service.ExpenseService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "*", allowedHeaders = "*" )
@Validated
public class ExpenseController {
    
    @Autowired
    private ExpenseService expenseService;

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Expense>> getExpensesForEmployee(@PathVariable Integer employeeId) {
        List<Expense> expenses = expenseService.getExpensesByEmployeeId(employeeId);
        return ResponseEntity.ok(expenses);
    }

//    @GetMapping("/manager/{managerId}")
//    public ResponseEntity<List<Expense>> getTeamExpenses(
//            @PathVariable Integer managerId,
//            @RequestParam(required = false) String status
//    ) {
//        List<Expense> expenses = expenseService.getTeamExpenses(managerId, status);
//        return ResponseEntity.ok(expenses);
//    }

    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpenses(
            @RequestParam(required = false) String status
    ) {
        List<Expense> expenses = expenseService.getAllExpenses(status);
        return ResponseEntity.ok(expenses);
    }
    
    @GetMapping("/manager/{managerId}")
    public ResponseEntity<Expense> getExpenseByManagerId(
            @PathVariable Integer managerId,
            @RequestParam(required = false) String status
    ) {
        Expense expenses = expenseService.getExpenseByManagerId(managerId);
        return ResponseEntity.ok(expenses);
    }
    
    @PostMapping("/addexpense")
    public ResponseEntity<String> createExpense(@Valid @RequestBody Expense expense) {
        expense.setDate(LocalDate.now());
        log.info("Creating expense: " + expense);
        expenseService.createExpense(expense);
        return ResponseEntity.ok("Expense created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateExpense(
            @PathVariable Integer id,
            @Valid @RequestBody Expense expense
    ) {
        expense.setId(id);
        expenseService.updateExpense(expense);
        return ResponseEntity.ok("Expense updated successfully");
    }
    
    @GetMapping(value="/categories" ,produces = "application/json")
    public List<Category> getCategories(){
        List<Category> categories = expenseService.getCategories();
        return categories;
    } 

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpense(
            @PathVariable Integer id,
            @RequestParam Integer employeeId
    ) {
        expenseService.deleteExpense(id, employeeId);
        return ResponseEntity.ok("Expense deleted successfully");
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<String> approveExpense(
            @PathVariable Integer id,
            @RequestBody Map<String, String> req,
            @RequestParam Integer managerId
    ) {
        String remarks = req.get("remarks");
        expenseService.approveExpense(id, remarks, managerId);
        return ResponseEntity.ok("Expense approved successfully");
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<String> rejectExpense(
            @PathVariable Integer id,
            @RequestBody Map<String, String> req,
            @RequestParam Integer managerId
    ) {
        String remarks = req.get("remarks");
        expenseService.rejectExpense(id, remarks, managerId);
        return ResponseEntity.ok("Expense rejected successfully");
    }
}
