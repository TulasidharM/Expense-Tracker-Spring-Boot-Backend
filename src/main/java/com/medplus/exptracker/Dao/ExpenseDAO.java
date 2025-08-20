package com.medplus.exptracker.Dao;

import java.util.List;

import com.medplus.exptracker.Model.Expense;

public interface ExpenseDAO {
    List<Expense> findByEmployeeId(Integer employeeId);
    void save(Expense expense);
    int update(Expense expense);
    int delete(Integer id, Integer employeeId);
    List<Expense> findByManagerId(Integer managerId, String status);
    int updateStatus(Integer id, String status, String remarks, Integer managerId);
    List<Expense> findAll(String status);
    Expense findById(Integer id);
}
