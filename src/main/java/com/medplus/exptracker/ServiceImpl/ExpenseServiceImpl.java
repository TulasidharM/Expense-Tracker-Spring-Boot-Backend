package com.medplus.exptracker.ServiceImpl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.medplus.exptracker.Dao.ExpenseDAO;
import com.medplus.exptracker.Model.Category;
import com.medplus.exptracker.Model.Expense;
import com.medplus.exptracker.Service.ExpenseService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExpenseServiceImpl implements ExpenseService {
	
	@Autowired
    private ExpenseDAO expenseDAO;

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
        
        if (expense.getStatus() == null || expense.getStatus().isEmpty()) {
            expense.setStatus("PENDING");
        }
        
        if(isLimitExceededByCatByEmp(expense)) {
        	expenseDAO.save(expense);
        }else {
            throw new RuntimeException("Monthly Limit for selected category has been crossed!");
        }
        
    }
    
    @Override
	public boolean isLimitExceededByCatByEmp(Expense expense) {
		int employeeId = expense.getEmployeeId();
        int categoryId = expense.getCategoryId();
        int year = expense.getDate().getYear();
        int month = expense.getDate().getMonthValue();
        
        
        BigDecimal expensePerCategory = expenseDAO.getTotalExpenseByCategoryByEmployee(employeeId, categoryId, month,year);
        
        
        expensePerCategory = expensePerCategory.add(expense.getAmount());
        List<Category> categories = expenseDAO.findAllCategories();
        
        for(Category category : categories) {
        	if(categoryId == category.getId()) {
        		if(expensePerCategory.compareTo(category.getMonthly_limit()) == -1) {
        			return true;
        		}
        		else {
        			return false;
        		}
        	}
        }
        return false;
	}

    @Override
    public void updateExpense(Expense expense) {
    	int rowsAffected;
        if(isLimitExceededByCatByEmp(expense)) {
        	rowsAffected = expenseDAO.update(expense);
        } else {
        	 throw new RuntimeException("Unable to update expense. The limit has crossed for the category.");
        }
        if (rowsAffected == 0) {
            throw new RuntimeException("Unable to update expense. It may not exist or is not in PENDING status.");
        }
    }

    @Override
    public void deleteExpense(Integer expenseId) {
        int rowsAffected = expenseDAO.delete(expenseId);
        if (rowsAffected == 0) {
            throw new RuntimeException("Unable to delete expense. It may not exist or is not in PENDING status.");
        }
    }

    @Override
    public void approveExpense(Integer id, String remarks, Integer managerId) {
    	int rowsAffected;
    	
        Expense expense = getExpenseById(id);
        log.info("Trying to approve this expense: "+expense);
        
        if(isLimitExceededByCatByEmp(expense)) {
        	rowsAffected = expenseDAO.updateStatus(id, "APPROVED", remarks, managerId);
        } else {
        	throw new RuntimeException("Unable to approve expense. The limit for the category for the employee has exceeded!.");
        }

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
    public List<Expense> getExpensesByStatus(String status) {
        return expenseDAO.findByStatus(status);
    }


    @Override
    public Double getTotalApprovedAmountByManager(Integer managerId) {
        return expenseDAO.sumApprovedExpensesByManagerId(managerId);
    }

    @Override
    public Expense getExpenseById(Integer id) {
        return expenseDAO.findById(id);
    }

	@Override
	public List<Expense> getExpensesByDateRange(Integer employeeId, String startDate, String endDate) {
		// TODO Auto-generated method stub
		return null;
	}

}