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
    public int updateStatus(Integer id, String status, String remarks, Integer managerId) {
        String updatests = "UPDATE expenses SET status=?, remarks=? WHERE id=? AND manager_id=? AND status='PENDING'";
        return jdbcTemplate.update(updatests, status, remarks, id, managerId);
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


    
	


}