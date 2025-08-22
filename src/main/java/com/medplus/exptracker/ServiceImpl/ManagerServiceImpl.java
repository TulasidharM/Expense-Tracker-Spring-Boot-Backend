package com.medplus.exptracker.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.medplus.exptracker.Dao.ManagerDAO;
import com.medplus.exptracker.Model.Expense;
import com.medplus.exptracker.Service.EmployeeService;
import com.medplus.exptracker.Service.ExpenseService;
import com.medplus.exptracker.Service.ManagerService;
import com.medplus.exptracker.entity.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ManagerServiceImpl implements ManagerService{
	
	
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private ExpenseService expenseService;
	
	@Autowired
	private ManagerDAO managerDAO;
	
	@Override
    public List<Expense> getExpensesByManagerId(Integer managerId) {
        return managerDAO.findExpensesByManagerId(managerId);
    }
    

    @Override
    public void approveExpense(Integer id, String remarks, Integer managerId) {
    	int rowsAffected;
    	
        Expense expense = expenseService.getExpenseById(id);
        log.info("Trying to approve this expense: "+expense);
        
        if(employeeService.isLimitExceededByCatByEmp(expense)) {
        	rowsAffected = managerDAO.updateStatus(id, "APPROVED", remarks, managerId);
        } else {
        	throw new RuntimeException("Unable to approve expense. The limit for the category for the employee has exceeded!.");
        }

        if (rowsAffected == 0) {
            throw new RuntimeException("Unable to approve expense. It may not exist or is not in PENDING status.");
        }
    }

    @Override
    public void rejectExpense(Integer id, String remarks, Integer managerId) {
        int rowsAffected = managerDAO.updateStatus(id, "REJECTED", remarks, managerId);
        if (rowsAffected == 0) {
            throw new RuntimeException("Unable to reject expense. It may not exist or is not in PENDING status.");
        }
    }


	@Override
	public List<User> getAllManager() {
		List<User> managers;
		managers = managerDAO.fetchAllManagers();
 		return managers;
	}
}
