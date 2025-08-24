package com.medplus.exptracker.Service;

import java.util.List;

import com.medplus.exptracker.DTO.ExpensePerCategory;
import com.medplus.exptracker.Model.Expense;

public interface EmployeeService {
	public void createExpense(Expense expense);
	public void updateExpense(Expense expense);
	public void deleteExpense(Integer expenseId);
	public List<Expense> getExpensesByEmployeeId(Integer employeeId);
	public boolean isLimitExceededByCatByEmp(Expense expense);
	
	List<ExpensePerCategory> totalExpenseForCategories(int empId);
}
