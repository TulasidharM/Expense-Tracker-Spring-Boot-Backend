package com.medplus.exptracker.Dao;

import java.util.Optional;

import com.medplus.exptracker.Model.User;

public interface UserDAO {
	Optional<User> findByUsername(String username);
	public void addUser(User user);
	public Optional<User> getUserById(Integer userId);
}
