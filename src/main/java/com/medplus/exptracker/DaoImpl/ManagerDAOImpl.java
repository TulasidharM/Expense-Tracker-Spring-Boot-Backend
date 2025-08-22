package com.medplus.exptracker.DaoImpl;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.medplus.exptracker.Dao.ManagerDAO;
import com.medplus.exptracker.Model.Expense;
import com.medplus.exptracker.entity.User;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ManagerDAOImpl implements ManagerDAO{
	
	private final JdbcTemplate jdbcTemplate;
	
	@Override
    public List<Expense> findExpensesByManagerId(Integer managerId) {
        String findByManager = "SELECT * FROM expenses WHERE manager_id = ? ORDER BY date DESC";
        return jdbcTemplate.query(findByManager, new BeanPropertyRowMapper<>(Expense.class), managerId);
    }
	
	@Override
    public int updateStatus(Integer id, String status, String remarks, Integer managerId) {
        String updatests = "UPDATE expenses SET status=?, remarks=? WHERE id=? AND manager_id=? AND status='PENDING'";
        return jdbcTemplate.update(updatests, status, remarks, id, managerId);
    }

	@Override
	public List<User> fetchAllManagers() {
		String fetchAllManagers = "SELECT * FROM users WHERE role_id=?";
		return jdbcTemplate.query(fetchAllManagers,new BeanPropertyRowMapper<>(User.class),2);
	}
}
