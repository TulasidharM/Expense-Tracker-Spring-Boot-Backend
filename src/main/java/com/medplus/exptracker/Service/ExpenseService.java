package com.medplus.exptracker.Service;

import java.util.List;

import com.medplus.exptracker.Model.Expense;

public interface ExpenseService {
    List<Expense> getExpensesByEmployeeId(Integer employeeId);
    void createExpense(Expense expense);
    void updateExpense(Expense expense);
    void deleteExpense(Integer id, Integer employeeId);
    List<Expense> getTeamExpenses(Integer managerId, String status);
    void approveExpense(Integer id, String remarks, Integer managerId);
    void rejectExpense(Integer id, String remarks, Integer managerId);
    List<Expense> getAllExpenses(String status);
    Expense getExpenseById(Integer id);
}
