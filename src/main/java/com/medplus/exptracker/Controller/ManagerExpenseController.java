package com.medplus.exptracker.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medplus.exptracker.Model.Category;
import com.medplus.exptracker.Model.Expense;
import com.medplus.exptracker.Service.ExpenseService;
import com.medplus.exptracker.Service.ManagerService;
import com.medplus.exptracker.Service.UserService;

import lombok.extern.slf4j.Slf4j;



@Slf4j
@RestController
@RequestMapping("/api/manager/expenses")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Validated
public class ManagerExpenseController {

    @Autowired
    private ExpenseService expenseService;
    @Autowired
    private ManagerService managerService;

    @Autowired
    private UserService userService;

    private Map<Integer, String> categoryIdToNameMap;

    @Autowired
    public void initCategoryMap() {
        categoryIdToNameMap = expenseService.getCategories().stream()
            .collect(Collectors.toMap(Category::getId, Category::getName));
    }

    @GetMapping("/{managerId}")
    public ResponseEntity<List<Expense>> getTeamExpenses(@PathVariable Integer managerId) {
        List<Expense> expenses = managerService.getExpensesByManagerId(managerId);

        List<Expense> enrichedExpenses = expenses.stream().map(expense -> {
            var emp = userService.getUserById(expense.getEmployeeId());
            if (emp != null) {
                expense.setEmployeeName(emp.getUsername());
            }
            Integer catId = expense.getCategoryId();
            if (catId != null && categoryIdToNameMap != null) {
                expense.setCategoryName(categoryIdToNameMap.getOrDefault(catId, "Unknown"));
            }
            return expense;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(enrichedExpenses);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Map<String,String>> approveExpense(@PathVariable Integer id, @RequestBody Expense expense) {
    	managerService.approveExpense(id, expense.getRemarks(), expense.getManagerId());
        var res = new HashMap<String,String>();
        res.put("message", "Expense approved succesfully");
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Map<String,String>> rejectExpense(@PathVariable Integer id, @RequestBody Expense expense) {
    	managerService.rejectExpense(id, expense.getRemarks(), expense.getManagerId());
        var res = new HashMap<String,String>();
        res.put("message", "Expense rejected successfully");
        return ResponseEntity.ok(res);
    }
}