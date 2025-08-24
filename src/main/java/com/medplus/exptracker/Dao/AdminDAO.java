package com.medplus.exptracker.Dao;

import java.util.List;

import com.medplus.exptracker.DTO.EmployeeForAdminDTO;
import com.medplus.exptracker.Model.Expense;

public interface AdminDAO {
	List<Expense> fetchExpensesWithFilters(int managerId ,int  employeeId ,int categoryId,int monthValue);
	List<EmployeeForAdminDTO> fetchAllUsers();
}
