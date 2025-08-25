package com.medplus.exptracker.ServiceImpl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.medplus.exptracker.DTO.ExpenseDTO;
import com.medplus.exptracker.DTO.ExpenseForEmployeeDTO;
import com.medplus.exptracker.DTO.ExpensePerCategory;
import com.medplus.exptracker.Dao.EmployeeDAO;
import com.medplus.exptracker.Dao.ExpenseDAO;
import com.medplus.exptracker.Exceptions.DBException;
import com.medplus.exptracker.Exceptions.MonthlyLimitException;
import com.medplus.exptracker.Model.Expense;
import com.medplus.exptracker.Service.EmployeeService;


@Service
public class EmployeeServiceImpl implements EmployeeService {
	
	@Autowired
	EmployeeDAO employeeDAO;
	@Autowired
	ExpenseDAO expenseDAO;
	
	@Override
    public void createExpense(ExpenseDTO expenseDto) throws MonthlyLimitException {
		//FIXME: map DTO to an expense POJO then use that pojo to save;
		
		Expense expense = new Expense();
		BeanUtils.copyProperties(expenseDto, expense);
		
		System.out.println("Copied Expense POJO : " + expense);
//        if (expense.getStatus() == null || expense.getStatus().isEmpty()) {
//            expense.setStatus("PENDING");
//        }
//        
//        if(isLimitExceededByCatByEmp(expense)) {
//        	employeeDAO.save(expense);
//        }else {
//            throw new MonthlyLimitException();
//        }
//        
    }
	
	@Override
    public void updateExpense(Expense expense) throws MonthlyLimitException,DBException{
    	int rowsAffected; 
        if(isLimitExceededByCatByEmp(expense)) {
        	rowsAffected = employeeDAO.update(expense);
        } else {
        	 throw new MonthlyLimitException();
        }
        if (rowsAffected == 0) {
            throw new DBException("Unable to update expense. It may not exist or is not in PENDING status.");
        }
    }

    @Override
    public void deleteExpense(Integer expenseId) throws DBException{
        int rowsAffected = employeeDAO.delete(expenseId);
        if (rowsAffected == 0) {
            throw new DBException("Unable to delete expense. It may not exist or is not in PENDING status.");
        }
    }
    
    @Override
    public List<ExpenseForEmployeeDTO> getExpensesByEmployeeId(Integer employeeId) {
        List<ExpenseForEmployeeDTO> expenses= employeeDAO.findByEmployeeId(employeeId);
        return expenses;
    }
    
    @Transactional
    @Override
	public boolean isLimitExceededByCatByEmp(Expense expense) {
		int employeeId = expense.getEmployeeId();
        int categoryId = expense.getCategoryId();
        int year = expense.getDate().getYear();
        int month = expense.getDate().getMonthValue();
        
        BigDecimal expensePerCategory = employeeDAO.getTotalExpenseByCategoryByEmployee(employeeId, categoryId, month,year);
        expensePerCategory = expensePerCategory.add(expense.getAmount());
        BigDecimal maxExpenseForCategory = expenseDAO.findMaxExpenseForCategory(categoryId);

		if(expensePerCategory.compareTo(maxExpenseForCategory) == -1) {
			return true;
		}
		else {
			return false;
		}
	
	}
    
    @Override
    public List<ExpensePerCategory> totalExpenseForCategories(int empId) {
    	return employeeDAO.getTotalExpenseForAllCategories(empId);
    }
    
    
    

}
