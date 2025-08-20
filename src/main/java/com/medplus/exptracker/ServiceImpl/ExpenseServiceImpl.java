package com.medplus.exptracker.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.medplus.exptracker.Dao.ExpenseDAO;
import com.medplus.exptracker.Model.Expense;
import com.medplus.exptracker.Service.ExpenseService;
import com.medplus.exptracker.Model.Category;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseDAO expenseDAO;
@Override
	public List<Category> getCategories() {
		// TODO:Write Get Categories
		return null;
	}
    @Override
    public List<Expense> getExpensesByEmployeeId(Integer employeeId) {
        return expenseDAO.findByEmployeeId(employeeId);
    }
    
    //TODO: Only allow employees to submit expenses
    @Override
    public void createExpense(Expense expense) {
        if (expense.getStatus() == null || expense.getStatus().isEmpty()) {
            expense.setStatus("PENDING");
        }
        expenseDAO.save(expense);
    }

    @Override
    public void updateExpense(Expense expense) {
        int rowsAffected = expenseDAO.update(expense);
        if (rowsAffected == 0) {
            throw new RuntimeException("Unable to update expense. It may not exist or is not in PENDING status.");
        }
    }

    @Override
    public void deleteExpense(Integer id, Integer employeeId) {
        int rowsAffected = expenseDAO.delete(id, employeeId);
        if (rowsAffected == 0) {
            throw new RuntimeException("Unable to delete expense. It may not exist or is not in PENDING status.");
        }
    }

    @Override
    public List<Expense> getTeamExpenses(Integer managerId, String status) {
        return expenseDAO.findByManagerId(managerId, status);
    }

    @Override
    public void approveExpense(Integer id, String remarks, Integer managerId) {
        int rowsAffected = expenseDAO.updateStatus(id, "APPROVED", remarks, managerId);
        if (rowsAffected == 0) {
            throw new RuntimeException("Unable to approve expense. It may not exist or is not in PENDING status.");
        }
    }

    @Override
    public void rejectExpense(Integer id, String remarks, Integer managerId) {
        int rowsAffected = expenseDAO.updateStatus(id, "REJECTED", remarks, managerId);
        if (rowsAffected == 0) {
            throw new RuntimeException("Unable to reject expense. It may not exist or is not in PENDING status.");
        }
    }

    @Override
    public List<Expense> getAllExpenses(String status) {
        return expenseDAO.findAll(status);
    }

    @Override
    public Expense getExpenseById(Integer id) {
        return expenseDAO.findById(id);
    }
}