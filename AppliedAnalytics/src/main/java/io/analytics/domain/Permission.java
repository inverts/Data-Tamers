package io.analytics.domain;

/**
 * Represents a single Permission entity in our database.
 * A Permission in the database is simply a label that can be assigned to 
 * Users for various Accounts. The database does not dictate what a single Permission
 * allows for, but the application itself should instead define the abilities
 * associated with various Permissions.
 * 
 * @author Dave Wong
 *
 */
public class Permission {
	private int id;
	private String name;
	
	public Permission(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
