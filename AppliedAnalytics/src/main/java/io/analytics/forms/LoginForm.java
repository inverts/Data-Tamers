package io.analytics.forms;

import org.hibernate.validator.constraints.NotEmpty;

public class LoginForm {
	
	@NotEmpty
	private String user;
	
	@NotEmpty
	private String pass;
	
	public void setUser(String user) {
		this.user= user;
	}
	
	public String getUser() {
		return this.user;
	}
	
	public void setPass(String pass) {
		this.pass = pass;
	}
	
	public String getPass() {
		return this.pass;
	}

}
