package com.medplus.exptracker.DaoImpl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.medplus.exptracker.Dao.EmployeeDAO;
import com.medplus.exptracker.Model.Expense;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EmployeeDAOImpl implements EmployeeDAO{
	private final JdbcTemplate jdbcTemplate;
	
	@Override
    public List<Expense> findByEmployeeId(Integer employeeId) {
        String findemp = "SELECT * FROM expenses WHERE employee_id = ? ORDER BY date DESC";
        return jdbcTemplate.query(findemp, new BeanPropertyRowMapper<>(Expense.class), employeeId);
    }
	
	@Override
    public void save(Expense expense) {
        String save = "INSERT INTO expenses (employee_id, category_id, amount, description, date, status, manager_id, remarks, receipt_url) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(save,
                expense.getEmployeeId(),
                expense.getCategoryId(),
                expense.getAmount(),
                expense.getDescription(),
                expense.getDate(),
                expense.getStatus() != null ? expense.getStatus() : "PENDING",
                expense.getManagerId(),
                expense.getRemarks(),
                expense.getReceiptUrl());
    }
	
	@Override
    public int update(Expense expense) {
        String update = "UPDATE expenses SET category_id=?, amount=?, description=?, date=?, remarks=?, receipt_url=? " +
                "WHERE id=? AND employee_id=? AND status='PENDING'";
        return jdbcTemplate.update(update,
                expense.getCategoryId(),
                expense.getAmount(),
                expense.getDescription(),
                expense.getDate(),
                expense.getRemarks(),
                expense.getReceiptUrl(),
                expense.getId(),
                expense.getEmployeeId());
    }
	
	@Override
    public int delete(Integer expenseId) {
        String delete = "DELETE FROM expenses WHERE id = ? AND status = 'PENDING'";
        return jdbcTemplate.update(delete, expenseId);
    }
	
	@Override
	public BigDecimal getTotalExpenseByCategoryByEmployee(int employeeId, int categoryId,int month, int year) {
		String expforcatbymon = "SELECT SUM(amount) FROM expenses " +
                "WHERE employee_id = ? AND category_id = ? " +
                "AND MONTH(date) = ? AND YEAR(date) = ? AND status != 'REJECTED' AND status != 'PENDING'";
		
		BigDecimal result =  jdbcTemplate.queryForObject(expforcatbymon, BigDecimal.class, employeeId, categoryId, month, year);
		if(result == null) {
			result = new BigDecimal("0");
        }
		return result;

	}
}
