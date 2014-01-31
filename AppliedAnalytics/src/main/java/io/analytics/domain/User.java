package io.analytics.domain;

import java.util.Calendar;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class User implements UserDetails {

	private Collection<? extends GrantedAuthority> roles;
	private String username;
	private String passwordTemp;
	private String password;
	private String email;
	private String profileImageUrl;
	private Calendar joinDate;
	private String firstName;
	private String lastName;
	private String passwordSalt;
	
	public void setAuthorities(Collection<? extends GrantedAuthority> roles) {
		this.roles = roles;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles;
	}

	public void setPassword(String password) {
		this.passwordTemp = password;
	}
	
	/**
	 * This isn't going to fly. There will be no raw passwords stored anywhere.
	 */
	@Override
	public String getPassword() {
		return this.passwordTemp;
	}
	
	public void changePassword(String newPassword) {
		//Check password for quality guidelines.
		
		//Create a new password salt.
		
		//Hash the password with the salt.
	}
	
	public String getPasswordHash() {
		return this.password;
	}

	public String getPasswordSalt() {
		return passwordSalt;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public Calendar getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Calendar joinDate) {
		this.joinDate = joinDate;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
