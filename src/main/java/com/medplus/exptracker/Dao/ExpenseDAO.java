package com.medplus.exptracker.Dao;

import java.util.List;

import com.medplus.exptracker.Model.Category;
import com.medplus.exptracker.Model.Expense;

public interface ExpenseDAO {
	Expense findById(Integer id);
	List<Category> findAllCategories();
}