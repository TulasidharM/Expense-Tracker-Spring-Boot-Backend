package com.medplus.exptracker.Dao;

import java.util.List;

import com.medplus.exptracker.Model.Category;
import com.medplus.exptracker.Model.Expense;

public interface ExpenseDAO {
    
    List<Expense> findByEmployeeId(Integer employeeId);
    void save(Expense expense);
    int update(Expense expense);
    int delete(Integer id, Integer employeeId);
    Expense findById(Integer id);
    List<Expense> findByManagerId(Integer managerId, String status);
    int updateStatus(Integer id, String status, String remarks, Integer managerId);
    List<Expense> findAll(String status);
    List<Category> findAllCategories();
    String getUserRoleById(Integer userId);
    Integer getManagerIdByEmployeeId(Integer employeeId);
    List<Expense> findByStatus(String status);
    List<Expense> findByDateRange(Integer employeeId, String startDate, String endDate);
    boolean existsByIdAndEmployeeId(Integer id, Integer employeeId);
    boolean existsByIdAndManagerId(Integer id, Integer managerId);
    Long countExpensesByEmployeeId(Integer employeeId);
    Long countPendingExpensesByManagerId(Integer managerId);
    Double sumExpenseAmountsByEmployeeId(Integer employeeId);
    Double sumApprovedExpensesByManagerId(Integer managerId);
    Double getTotalExpensesForCategoryAndMonth(Integer employeeId, Integer categoryId, String month, String year);
}
