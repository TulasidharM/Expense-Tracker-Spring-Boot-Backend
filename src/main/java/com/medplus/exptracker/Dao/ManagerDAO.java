package com.medplus.exptracker.Dao;

import java.util.List;

import com.medplus.exptracker.Model.Expense;
import com.medplus.exptracker.Model.User;

public interface ManagerDAO {
	List<Expense> findExpensesByManagerId(Integer managerId);
    int updateStatus(Integer id, String status, String remarks, Integer managerId);
    List<User> fetchAllManagers();
}
