package io.analytics.domain;

import io.analytics.security.PasswordUtils;

import java.util.Calendar;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class User implements UserDetails {

	private Collection<? extends GrantedAuthority> roles;
	private String username;
	private String passwordHash;
	private String email;
	private String profileImageUrl;
	private Calendar joinDate;
	private String firstName;
	private String lastName;
	private String passwordSalt;
	private int id;
	
	public User() {
		super();
	}
	public User(int id) {
		super();
		this.id = id;
	}
	
	public void setAuthorities(Collection<? extends GrantedAuthority> roles) {
		this.roles = roles;
	}
	
	public int getUserId() {
		return this.id;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles;
	}
	
	/**
	 * Obtains the password hash of this user.
	 */
	@Override
	public String getPassword() {
		return this.passwordHash;
	}
	
	/**
	 * Generates a password hash for a new password and updates the hash and salt
	 * in the User object.
	 * 
	 * @param newPassword
	 * @return
	 */
	public boolean setPassword(String newPassword) {
		this.passwordHash = newPassword;
		
		if (PasswordUtils.passwordMeetsGuidelines(newPassword)) {
			String salt = PasswordUtils.generateSalt();
			String hash = PasswordUtils.createPasswordHash(newPassword, salt);
			
			if (hash == null) //This can occur if we obtain a DataAccessException.
				return false;
			
			this.passwordHash = hash;
			this.passwordSalt = salt;
			
			return true;
		} 
		
		return false;
		
	}
	
	public User setPasswordHash(String hash) {
		this.passwordHash = hash;
		return this;
	}
	
	public String getPasswordSalt() {
		return passwordSalt;
	}
	
	public User setPasswordSalt(String salt) {
		this.passwordSalt = salt;
		return this;
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
