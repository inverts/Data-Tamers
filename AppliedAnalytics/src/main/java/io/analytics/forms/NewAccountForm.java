package io.analytics.forms;


import io.analytics.aspect.FieldMatch;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@FieldMatch.List({
	@FieldMatch(first = "password", second = "confirmPassword", message = "account.invalid.password.nomatch"),
	@FieldMatch(first = "email", second = "confirmEmail", message = "account.invalid.email.nomatch")
})
public class NewAccountForm {
	
	@NotEmpty(message = "account.invalid.firstname.empty")
	private String firstname;
	
	@NotEmpty(message = "account.invalid.lastname.empty")
	private String lastname;
	
	@NotEmpty(message = "account.invalid.email.empty") 
	@Email(message = "account.invalid.email.bad")
	private String email;
	
	@NotEmpty(message = "account.invalid.email.confirmed.empty") 
	@Email(message = "account.invalid.email.bad")
	private String confirmEmail;
	
	@NotEmpty(message = "account.invalid.username.empty")
	private String username;
	
	@NotEmpty(message = "account.invalid.password.empty")
	private String password;
	
	@NotEmpty(message = "account.invalid.password.confirmed.empty")
	private String confirmPassword;

	
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	public String getFirstname() {
		return this.firstname;
	}
	
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	public String getLastname() {
		return this.lastname;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public void setConfirmEmail(String email) {
		this.confirmEmail = email;
	}
	
	public String getConfirmEmail() {
		return this.confirmEmail;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	public String getConfirmPassword() {
		return this.confirmPassword;
	}

}
