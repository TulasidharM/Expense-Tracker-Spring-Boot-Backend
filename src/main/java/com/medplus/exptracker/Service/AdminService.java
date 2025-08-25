package com.medplus.exptracker.Service;

import java.math.BigDecimal;
import java.util.List;

import com.medplus.exptracker.DTO.EmployeeForAdminDTO;
import com.medplus.exptracker.DTO.ExpensePerCategory;
import com.medplus.exptracker.Model.Expense;

public interface AdminService {	
	List<Expense> getExpensesWithFilters(int managerId ,int  employeeId ,int categoryId,int monthValue);
	List<EmployeeForAdminDTO> getAllEmployees();
	BigDecimal getExpenseForCurrentMonth();
	List<ExpensePerCategory> getExpenseForCategories();
}

