package com.medplus.exptracker.ServiceImpl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.medplus.exptracker.Dao.EmployeeDAO;
import com.medplus.exptracker.Dao.ExpenseDAO;
import com.medplus.exptracker.Model.Category;
import com.medplus.exptracker.Model.Expense;
import com.medplus.exptracker.Service.EmployeeService;


@Service
public class EmployeeServiceImpl implements EmployeeService {
	
	@Autowired
	EmployeeDAO employeeDAO;
	@Autowired
	ExpenseDAO expenseDAO;
	
	@Override
    public void createExpense(Expense expense) {
        
        if (expense.getStatus() == null || expense.getStatus().isEmpty()) {
            expense.setStatus("PENDING");
        }
       
        if(isLimitExceededByCatByEmp(expense)) {
        	employeeDAO.save(expense);
        }else {
            throw new RuntimeException("Monthly Limit for selected category has been crossed!");
        }
        
    }
	
	@Override
    public void updateExpense(Expense expense) {
    	int rowsAffected;
        if(isLimitExceededByCatByEmp(expense)) {
        	rowsAffected = employeeDAO.update(expense);
        } else {
        	 throw new RuntimeException("Unable to update expense. The limit has crossed for the category.");
        }
        if (rowsAffected == 0) {
            throw new RuntimeException("Unable to update expense. It may not exist or is not in PENDING status.");
        }
    }

    @Override
    public void deleteExpense(Integer expenseId) {
        int rowsAffected = employeeDAO.delete(expenseId);
        if (rowsAffected == 0) {
            throw new RuntimeException("Unable to delete expense. It may not exist or is not in PENDING status.");
        }
    }
    
    @Override
    public List<Expense> getExpensesByEmployeeId(Integer employeeId) {
        return employeeDAO.findByEmployeeId(employeeId);
    }
    
    
    @Override
	public boolean isLimitExceededByCatByEmp(Expense expense) {
		int employeeId = expense.getEmployeeId();
        int categoryId = expense.getCategoryId();
        int year = expense.getDate().getYear();
        int month = expense.getDate().getMonthValue();
        
        
        BigDecimal expensePerCategory = employeeDAO.getTotalExpenseByCategoryByEmployee(employeeId, categoryId, month,year);
        
        expensePerCategory = expensePerCategory.add(expense.getAmount());
        List<Category> categories = expenseDAO.findAllCategories();
        
        //TODO:use sql to find limit for the category id
        //TODO: throw exception and handle it in global handler
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


}
