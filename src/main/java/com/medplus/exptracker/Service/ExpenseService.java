package com.medplus.exptracker.Service;

import java.util.List;

import com.medplus.exptracker.Model.Category;
import com.medplus.exptracker.Model.Expense;

public interface ExpenseService {
    List<Expense> getExpensesByEmployeeId(Integer employeeId);
    void createExpense(Expense expense);
    void updateExpense(Expense expense);
    void deleteExpense(Integer id, Integer employeeId);
    Expense getExpenseById(Integer id);
    List<Expense> getExpensesByManagerId(Integer managerId);
    void approveExpense(Integer id, String remarks, Integer managerId);
    void rejectExpense(Integer id, String remarks, Integer managerId);
    List<Expense> getAllExpenses(String status);
    List<Category> getCategories();
    List<Expense> getExpensesByStatus(String status);
    List<Expense> getExpensesByDateRange(Integer employeeId, String startDate, String endDate);
    Double getTotalApprovedAmountByManager(Integer managerId);
}