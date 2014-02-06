package io.analytics.repository.interfaces;

import io.analytics.domain.User;

import java.io.Serializable;

public interface IUserRepository {
	
	public boolean addNewUser(User u);
	
	public User loadUserByUsername(String username);

}
