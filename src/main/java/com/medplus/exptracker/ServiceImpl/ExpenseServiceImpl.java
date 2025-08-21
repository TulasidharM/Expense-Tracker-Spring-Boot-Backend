package com.medplus.exptracker.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.medplus.exptracker.Dao.ExpenseDAO;
import com.medplus.exptracker.Model.Category;
import com.medplus.exptracker.Model.Expense;
import com.medplus.exptracker.Service.ExpenseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseDAO expenseDAO;

    @Override
    public List<Category> getCategories() {
        return expenseDAO.findAllCategories();
    }

    @Override
    public List<Expense> getExpensesByEmployeeId(Integer employeeId) {
        return expenseDAO.findByEmployeeId(employeeId);
    }

    @Override
    public List<Expense> getExpensesByManagerId(Integer managerId) {
        return expenseDAO.findExpensesByManagerId(managerId);
    }

    @Override
    public void createExpense(Expense expense) {
        String userRole = expenseDAO.getUserRoleById(expense.getEmployeeId());
        if (!"EMPLOYEE".equals(userRole)) {
            throw new RuntimeException("Only employees are allowed to submit expenses");
        }
        Integer managerId = expenseDAO.getManagerIdByEmployeeId(expense.getEmployeeId());
        if (managerId == null) {
            throw new RuntimeException("Employee must have an assigned manager to submit expenses");
        }
        expense.setManagerId(managerId);
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

    @Override
    public List<Expense> getExpensesByStatus(String status) {
        return expenseDAO.findByStatus(status);
    }

    @Override
    public List<Expense> getExpensesByDateRange(Integer employeeId, String startDate, String endDate) {
        return expenseDAO.findByDateRange(employeeId, startDate, endDate);
    }

    @Override
    public Double getTotalApprovedAmountByManager(Integer managerId) {
        return expenseDAO.sumApprovedExpensesByManagerId(managerId);
    }

}