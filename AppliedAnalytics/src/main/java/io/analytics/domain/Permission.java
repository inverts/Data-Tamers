package io.analytics.domain;

public class Permission {
	private int id;
	private String name;
	
	public Permission(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
