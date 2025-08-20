package com.medplus.exptracker.DaoImpl;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.medplus.exptracker.Dao.ExpenseDAO;
import com.medplus.exptracker.Model.Category;
import com.medplus.exptracker.Model.Expense;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ExpenseDAOImpl implements ExpenseDAO {
    
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
                expense.getReceiptUrl()
        );
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
                expense.getEmployeeId()
        );
    }

    
    @Override
    public int delete(Integer id, Integer employeeId) {
        String delete = "DELETE FROM expenses WHERE id = ? AND employee_id = ? AND status = 'PENDING'";
        return jdbcTemplate.update(delete, id, employeeId);
    }

    

    @Override
    public int updateStatus(Integer id, String status, String remarks, Integer managerId) {
        String updatests = "UPDATE expenses SET status=?, remarks=? WHERE id=? AND manager_id=? AND status='PENDING'";
        return jdbcTemplate.update(updatests, status, remarks, id, managerId);
    }
    
    @Override
    public List<Expense> findAll(String status) {
        StringBuilder searchall = new StringBuilder("SELECT * FROM expenses");
        if (status != null && !status.isBlank()) {
            searchall.append(" WHERE status = ? ORDER BY date DESC");
            return jdbcTemplate.query(searchall.toString(), new BeanPropertyRowMapper<>(Expense.class), status);
        }
        searchall.append(" ORDER BY date DESC");
        return jdbcTemplate.query(searchall.toString(), new BeanPropertyRowMapper<>(Expense.class));
    }
    
    @Override
    public Expense findById(Integer id) {
        String findexpid = "SELECT * FROM expenses WHERE id = ?";
        return jdbcTemplate.queryForObject(findexpid, new BeanPropertyRowMapper<>(Expense.class), id);
    }

    @Override
    public List<Category> findAllCategories() {
        String getcate = "SELECT * FROM categories";
        return jdbcTemplate.query(getcate, new BeanPropertyRowMapper<>(Category.class));
    }

   

    @Override
    public Integer getManagerIdByEmployeeId(Integer employeeId) {
        String mngbyemp = "SELECT manager_id FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(mngbyemp, Integer.class, employeeId);
    }

    @Override
    public List<Expense> findByStatus(String status) {
        String bystatus = "SELECT * FROM expenses WHERE status = ? ORDER BY date DESC";
        return jdbcTemplate.query(bystatus, new BeanPropertyRowMapper<>(Expense.class), status);
    }

    @Override
    public List<Expense> findByDateRange(Integer employeeId, String startDate, String endDate) {
        String daterange = "SELECT * FROM expenses WHERE employee_id = ? AND date BETWEEN ? AND ? ORDER BY date DESC";
        return jdbcTemplate.query(daterange, new BeanPropertyRowMapper<>(Expense.class), employeeId, startDate, endDate);
    }

   

    
    @Override
    public Double sumApprovedExpensesByManagerId(Integer managerId) {
        String approvedexp = "SELECT COALESCE(SUM(amount), 0.0) FROM expenses WHERE manager_id = ? AND status = 'APPROVED'";
        return jdbcTemplate.queryForObject(approvedexp, Double.class, managerId);
    }

    @Override
    public Double getTotalExpensesForCategoryAndMonth(Integer employeeId, Integer categoryId, String month, String year) {
        String expforcatbymon = "SELECT COALESCE(SUM(amount), 0.0) FROM expenses " +
                    "WHERE employee_id = ? AND category_id = ? " +
                    "AND MONTH(date) = ? AND YEAR(date) = ? AND status != 'REJECTED'";
        return jdbcTemplate.queryForObject(expforcatbymon, Double.class, employeeId, categoryId, month, year);
    }

	
}
