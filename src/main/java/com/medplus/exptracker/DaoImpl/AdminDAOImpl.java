package com.medplus.exptracker.DaoImpl;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.medplus.exptracker.DTO.EmployeeForAdminDTO;
import com.medplus.exptracker.Dao.AdminDAO;
import com.medplus.exptracker.Model.Expense;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AdminDAOImpl implements AdminDAO {
    
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Expense> fetchExpensesWithFilters(int managerId, int employeeId, int categoryId, int monthValue) {
        String query = """
            SELECT e.*, u.username as employee_name, c.name as category_name 
            FROM expenses e
            JOIN users u ON e.employee_id = u.id
            JOIN categories c ON e.category_id = c.id
            WHERE (? = 0 OR e.manager_id = ?)
            AND (? = 0 OR e.employee_id = ?)
            AND (? = 0 OR e.category_id = ?)
            AND (? = 0 OR MONTH(e.date) = ?)
            ORDER BY e.date DESC
            """;
            
        return jdbcTemplate.query(query, 
            new BeanPropertyRowMapper<>(Expense.class),
            managerId, managerId,
            employeeId, employeeId,
            categoryId, categoryId,
            monthValue, monthValue
        );
    }
    
    @Override
    public List<EmployeeForAdminDTO> fetchAllUsers() {
		String getEmployees= "SELECT users.username , users.id FROM users WHERE role_id  = 3";
		return jdbcTemplate.query(getEmployees,new BeanPropertyRowMapper<>(EmployeeForAdminDTO.class));
    }
}
