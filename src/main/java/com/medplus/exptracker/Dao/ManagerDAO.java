package com.medplus.exptracker.Dao;

import java.util.List;

import com.medplus.exptracker.DTO.ManagerForAdminDTO;
import com.medplus.exptracker.Model.Expense;

public interface ManagerDAO {
	List<Expense> findExpensesByManagerId(Integer managerId);
    
	int updateStatus(Expense expense);
    List<ManagerForAdminDTO> fetchAllManagers();
}
