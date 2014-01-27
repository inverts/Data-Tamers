package io.analytics.security;

import java.util.Collection;

import io.analytics.domain.User;
import io.analytics.service.interfaces.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class SiteAuthenticationProvider implements AuthenticationProvider {

	@Autowired private IUserService userService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();
		
		//TODO grab the user from our database.
		User user = userService.loadUserByUsername(username);
		
		//if (user == null) throw new BadCredentialsException("User not found");
		
		//if (!password.equals(user.getPassword())) throw new BadCredentialsException("Password does not match");
		
		// Get the user access level
		Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
		
		return new UsernamePasswordAuthenticationToken(username, password, authorities);
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}

}
