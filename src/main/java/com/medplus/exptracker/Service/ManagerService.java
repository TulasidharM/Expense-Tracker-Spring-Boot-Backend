package com.medplus.exptracker.Service;

import java.util.List;

import com.medplus.exptracker.Model.Expense;
import com.medplus.exptracker.entity.User;

public interface ManagerService {
    List<Expense> getExpensesByManagerId(Integer managerId);
    void approveExpense(Integer id, String remarks, Integer managerId);
    void rejectExpense(Integer id, String remarks, Integer managerId);
    List<User> getAllManager();
}
