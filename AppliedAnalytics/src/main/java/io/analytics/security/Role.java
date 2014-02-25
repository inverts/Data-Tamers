package io.analytics.security;

import org.springframework.security.core.GrantedAuthority;

public class Role implements GrantedAuthority{

	private String role;
	
	public Role(String role) {
		this.role = role;
	}
	
	@Override
	public String getAuthority() {
		// Logic to determine what role the user is
		return this.role;
	}

}
