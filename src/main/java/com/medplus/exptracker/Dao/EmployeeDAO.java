package com.medplus.exptracker.Dao;

import java.math.BigDecimal;
import java.util.List;

import com.medplus.exptracker.Model.Expense;

public interface EmployeeDAO {
	public List<Expense> findByEmployeeId(Integer employeeId);
	 public void save(Expense expense);
	 public int update(Expense expense);
	 public int delete(Integer expenseId);
	 public BigDecimal getTotalExpenseByCategoryByEmployee(int employeeId, int categoryId,int month, int year);
}
