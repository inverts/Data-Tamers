package io.analytics.service.interfaces;

import io.analytics.domain.User;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService  {

	public User loadUserByUsername(String username);
}
