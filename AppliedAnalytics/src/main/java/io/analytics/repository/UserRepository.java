package io.analytics.repository;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.*;

import io.analytics.domain.User;
import io.analytics.repository.interfaces.IUserRepository;
import io.analytics.security.Role;

@Repository
public class UserRepository implements IUserRepository {

	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		
		//TODO: JDBC into the database to retrieve user by username.
		
		// For now I just created this customer.
		User user = new User();
		user.setPassword("123456");
		user.setUsername("user");
		Set roles = new HashSet<GrantedAuthority>();
		roles.add(new Role("ROLE_USER"));
		user.setAuthorities(roles);
		
		return user;
	}
} 
