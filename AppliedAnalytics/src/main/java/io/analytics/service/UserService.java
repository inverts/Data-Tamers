package io.analytics.service;


import io.analytics.domain.User;
import io.analytics.repository.interfaces.IUserRepository;
import io.analytics.service.interfaces.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserService implements IUserService {

	@Autowired private IUserRepository userRepository;
	
	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.loadUserByUsername(username);
		
		return user;
	}
	
	public User getUserById(int id) {
		return null;
	}
	
	/**
	 * Creates a new user.
	 * 
	 * @param username
	 * @param email
	 * @param password
	 * @return
	 */
	public User createUser(String username, String email, String password) {
		return null;
	}

}
