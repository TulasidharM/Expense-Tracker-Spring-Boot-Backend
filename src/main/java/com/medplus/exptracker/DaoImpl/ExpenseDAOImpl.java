package com.medplus.exptracker.DaoImpl;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.medplus.exptracker.Dao.ExpenseDAO;
import com.medplus.exptracker.Model.Expense;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ExpenseDAOImpl implements ExpenseDAO {
    
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Expense> findByEmployeeId(Integer employeeId) {
        String sql = "SELECT * FROM expenses WHERE employee_id = ? ORDER BY date DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Expense.class), employeeId);
    }

    @Override
    public void save(Expense expense) {
        String sql = "INSERT INTO expenses (employee_id, category_id, amount, description, date, status, manager_id, remarks, receipt_url) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
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
        String sql = "UPDATE expenses SET category_id=?, amount=?, description=?, date=?, remarks=?, receipt_url=? " +
                "WHERE id=? AND employee_id=? AND status='PENDING'";
        return jdbcTemplate.update(sql,
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
        String sql = "DELETE FROM expenses WHERE id = ? AND employee_id = ? AND status = 'PENDING'";
        return jdbcTemplate.update(sql, id, employeeId);
    }

    @Override
    public List<Expense> findByManagerId(Integer managerId, String status) {
        StringBuilder sql = new StringBuilder("SELECT * FROM expenses WHERE manager_id = ?");
        if (status != null && !status.isBlank()) {
            sql.append(" AND status = ? ORDER BY date DESC");
            return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(Expense.class), managerId, status);
        }
        sql.append(" ORDER BY date DESC");
        return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(Expense.class), managerId);
    }

    @Override
    public int updateStatus(Integer id, String status, String remarks, Integer managerId) {
        String sql = "UPDATE expenses SET status=?, remarks=? WHERE id=? AND manager_id=? AND status='PENDING'";
        return jdbcTemplate.update(sql, status, remarks, id, managerId);
    }

    @Override
    public List<Expense> findAll(String status) {
        StringBuilder sql = new StringBuilder("SELECT * FROM expenses");
        if (status != null && !status.isBlank()) {
            sql.append(" WHERE status = ? ORDER BY date DESC");
            return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(Expense.class), status);
        }
        sql.append(" ORDER BY date DESC");
        return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(Expense.class));
    }

    @Override
    public Expense findById(Integer id) {
        String sql = "SELECT * FROM expenses WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Expense.class), id);
    }
}
