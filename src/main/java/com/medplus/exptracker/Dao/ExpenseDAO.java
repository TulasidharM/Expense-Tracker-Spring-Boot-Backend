package com.medplus.exptracker.Dao;

import java.math.BigDecimal;
import java.util.List;

import com.medplus.exptracker.Model.Category;
import com.medplus.exptracker.Model.Expense;

public interface ExpenseDAO {
	List<Expense> findByEmployeeId(Integer employeeId);
	void save(Expense expense);
	int update(Expense expense);
	int delete(Integer expenseId);
	Expense findById(Integer id);
	int updateStatus(Integer id, String status, String remarks, Integer managerId);
	List<Expense> findAll(String status);
	List<Category> findAllCategories();
	String getUserRoleById(Integer userId);
	Integer getManagerIdByEmployeeId(Integer employeeId);
	List<Expense> findByStatus(String status);
	List<Expense> findByDateRange(Integer employeeId, String startDate, String endDate);
	Double sumApprovedExpensesByManagerId(Integer managerId);
	List<Expense> findExpensesByManagerId(Integer managerId);
	
	
	
	BigDecimal getTotalExpenseByCategoryByEmployee(int employeeId,int CategoryId,int month, int year);
}