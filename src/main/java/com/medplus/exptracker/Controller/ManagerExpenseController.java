package com.medplus.exptracker.Controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import com.medplus.exptracker.entity.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/manager/expenses")
@CrossOrigin(origins = "*", allowedHeaders = "*")
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
    public ResponseEntity<String> approveExpense(@PathVariable Integer id, @RequestBody Expense expense) {
        managerService.approveExpense(id, expense.getRemarks(), expense.getManagerId());
        return ResponseEntity.ok("Expense approved successfully");
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<String> rejectExpense(@PathVariable Integer id, @RequestBody Expense expense) {
        managerService.rejectExpense(id, expense.getRemarks(), expense.getManagerId());
        return ResponseEntity.ok("Expense rejected successfully");
    }

    @GetMapping("/{managerId}/approvedAmounts")
    public ResponseEntity<List<Expense>> getApprovedAmounts(@PathVariable Integer managerId) {
        List<Expense> approvedExpenses = managerService.getExpensesByManagerId(managerId).stream()
                .filter(exp -> "APPROVED".equals(exp.getStatus()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(approvedExpenses);
    }

    @GetMapping("/{managerId}/employeeList")
    public ResponseEntity<Map<String, Object>> getEmployeeList(@PathVariable Integer managerId) {
        List<Expense> expenses = managerService.getExpensesByManagerId(managerId);
        
        List<String> uniqueEmployees = expenses.stream()
                .map(expense -> {
                    var emp = userService.getUserById(expense.getEmployeeId());
                    return emp != null ? emp.getUsername() : "Unknown Employee";
                })
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        
        Map<String, Object> response = new HashMap<>();
        response.put("employees", uniqueEmployees);
        response.put("totalCount", uniqueEmployees.size());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{managerId}/categoryWiseApproved")
    public ResponseEntity<List<Map<String, Object>>> getCategoryWiseApproved(@PathVariable Integer managerId) {
        LocalDate now = LocalDate.now();
        int currentMonth = now.getMonthValue();
        int currentYear = now.getYear();
        
        List<Expense> currentMonthApproved = managerService.getExpensesByManagerId(managerId).stream()
                .filter(exp -> "APPROVED".equals(exp.getStatus()))
                .filter(exp -> {
                    LocalDate expenseDate = exp.getDate();
                    return expenseDate != null && 
                           expenseDate.getMonthValue() == currentMonth && 
                           expenseDate.getYear() == currentYear;
                })
                .collect(Collectors.toList());
        
        List<Category> categories = expenseService.getCategories();
        
        Map<Integer, Double> categoryAmounts = currentMonthApproved.stream()
                .collect(Collectors.groupingBy(
                    Expense::getCategoryId,
                    Collectors.summingDouble(expense -> expense.getAmount().doubleValue())
                ));
        
        List<Map<String, Object>> result = categories.stream()
                .map(category -> {
                    Integer categoryId = category.getId();
                    String categoryName = category.getName();
                    Double monthlyLimit = category.getMonthly_limit().doubleValue();
                    Double approvedAmount = categoryAmounts.getOrDefault(categoryId, 0.0);
                    Double remainingAmount = monthlyLimit - approvedAmount;
                    
                    Map<String, Object> categoryData = new HashMap<>();
                    categoryData.put("categoryName", categoryName);
                    categoryData.put("approvedAmount", approvedAmount);
                    categoryData.put("remainingAmount", remainingAmount);
                    categoryData.put("monthlyLimit", monthlyLimit);
                    
                    return categoryData;
                })
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(result);
    }
    
    

}