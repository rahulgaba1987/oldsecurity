package com.boot.service;

import java.util.Optional;

import com.boot.model.User;

public interface UserService 
{
	public Integer saveUser(User user);
	
	  Optional<User> findByUserName(String username);

}
