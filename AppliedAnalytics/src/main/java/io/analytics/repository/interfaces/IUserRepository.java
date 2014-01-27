package io.analytics.repository.interfaces;

import io.analytics.domain.User;

import java.io.Serializable;

public interface IUserRepository {
	
	public User loadUserByUsername(String username);

}
