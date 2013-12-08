package io.analytics.domain;

import java.io.Serializable;

public class GoogleUserData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8530457342959938233L;
	
	private String id;
	private String email;
	private String verified_email;
	private String name;
	private String given_name;
	private String family_name;
	private String link;
	private String picture;
	private String gender;
	private String locale;

	public String getId() {
		return id;
	}
	public String getEmail() {
		return email;
	}
	public String getVerified_email() {
		return verified_email;
	}
	public String getName() {
		return name;
	}
	public String getGiven_name() {
		return given_name;
	}
	public String getFamily_name() {
		return family_name;
	}
	public String getLink() {
		return link;
	}
	public String getPicture() {
		return picture;
	}
	public String getGender() {
		return gender;
	}
	public String getLocale() {
		return locale;
	}
}
