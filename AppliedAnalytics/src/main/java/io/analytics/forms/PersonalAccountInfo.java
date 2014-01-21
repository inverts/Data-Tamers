package io.analytics.forms;

import io.analytics.aspect.FieldMatch;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/*@FieldMatch.List({
	@FieldMatch(first = "password", second = "confirmPassword", message = "personal.invalid.password.nomatch"),
	@FieldMatch(first = "email", second = "confirmEmail", message = "personal.invalid.email.nomatch")
})*/
public class PersonalAccountInfo {
	
	@NotEmpty
	private String firstname;
	
	@NotEmpty(message = "last name is empty")
	private String lastname;
	
	@NotEmpty @Email
	private String email;
	
	@NotEmpty @Email
	private String confirmEmail;
	
	@NotEmpty
	private String username;
	
	@NotEmpty
	private String password;
	
	@NotEmpty
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
