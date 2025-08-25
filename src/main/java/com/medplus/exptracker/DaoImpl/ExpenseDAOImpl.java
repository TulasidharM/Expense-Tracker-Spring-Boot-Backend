package com.medplus.exptracker.DaoImpl;

import java.math.BigDecimal;
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
    public BigDecimal findMaxExpenseForCategory(int categoryId) {
    	String getMaxForCategory = "SELECT monthly_limit FROM categories WHERE id = ?";
    	return jdbcTemplate.queryForObject(getMaxForCategory, BigDecimal.class,categoryId);
    }
}