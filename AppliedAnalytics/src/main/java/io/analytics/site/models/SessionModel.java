package io.analytics.site.models;

import io.analytics.domain.Account;
import io.analytics.domain.GoogleAccount;
import io.analytics.domain.User;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.google.api.client.auth.oauth2.Credential;

/**
 * A Session model represents a single user session with the application.
 * Known session attributes have getters and setters, but the model also allows for unknown attributes to be saved and retrieved.
 * 
 * @author Dave Wong
 *
 */
public class SessionModel {
	
	private HttpSession session;
	private SettingsModel settings;
	private FilterModel filter;
	private Credential credentials;
	private User user;
	private Account account;
	private List<GoogleAccount> availableGoogleAccounts;
	private GoogleAccount activeGoogleAccount;

	private static final String ACCOUNT = "account";
	private static final String USER = "user";
	private static final String CREDENTIALS = "credentials";
	private static final String FILTER = "filter";
	private static final String SETTINGS = "settings";
	
	private HashMap<String, Object> attributes = new HashMap<String, Object>();
	
	public SessionModel(HttpSession session) {
		if (session == null)
			throw new IllegalArgumentException();
		else
			this.session = session;
	}
	
	/**
	 * @return the session
	 */
	public HttpSession getSession() {
		return session;
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(HttpSession session) {
		this.session = session;
	}

	/**
	 * @return the SettingsModel for this session
	 * @throws CorruptedSessionException 
	 */
	public SettingsModel getSettings() throws CorruptedSessionException {
		try {
			return (settings != null) ? settings : (SettingsModel) getAttribute(SETTINGS);
		} 
		catch (ClassCastException e) { throw new CorruptedSessionException(); }
	}

	/**
	 * @param settings the settings to set
	 */
	public void setSettings(SettingsModel settings) {
		this.settings = settings;
		setAttribute(SETTINGS, settings);
	}

	/**
	 * @return the filter
	 * @throws CorruptedSessionException 
	 */
	public FilterModel getFilter() throws CorruptedSessionException {
		try {
			return (filter != null) ? filter : (FilterModel) getAttribute(FILTER);
		} 
		catch (ClassCastException e) { throw new CorruptedSessionException(); }
	}

	/**
	 * @param filter the filter to set
	 */
	public void setFilter(FilterModel filter) {
		this.filter = filter;
		setAttribute(FILTER, filter);
	}

	/**
	 * @return the credentials
	 * @throws CorruptedSessionException 
	 */
	public Credential getCredentials() throws CorruptedSessionException {
		try {
			return (credentials != null) ? credentials : (Credential) getAttribute(CREDENTIALS);
		} 
		catch (ClassCastException e) { throw new CorruptedSessionException(); }
	}

	/**
	 * @param credentials the credentials to set
	 */
	public void setCredentials(Credential credentials) {
		this.credentials = credentials;
		setAttribute(CREDENTIALS, credentials);
	}

	/**
	 * @return the user
	 * @throws CorruptedSessionException 
	 */
	public User getUser() throws CorruptedSessionException {
		try {
			return (user != null) ? user : (User) getAttribute(USER);
		} 
		catch (ClassCastException e) { throw new CorruptedSessionException(); }
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
		setAttribute(USER, user);
	}

	/**
	 * @return the account
	 * @throws CorruptedSessionException 
	 */
	public Account getAccount() throws CorruptedSessionException {
		try {
			return (account != null) ? account : (Account) getAttribute(ACCOUNT);
		} 
		catch (ClassCastException e) { throw new CorruptedSessionException(); }
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(Account account) {
		this.account = account;
		setAttribute(ACCOUNT, account);
	}

	/**
	 * Retrieves the known attributes from this session.
	 * 
	 * @return The known attributes from this session.
	 */
	public HashMap<String, Object> getKnownAttributes() {
		return attributes;
	}
	
	/**
	 * Gets the desired attribute from this session.
	 * 
	 * @param name Name of the attribute
	 * @return The attribute object, or null if not available.
	 */
	public Object getAttribute(String name) {
		return attributes.containsKey(name) ? attributes.get(name) : session.getAttribute(name);
	}
	
	/**
	 * Saves the specified attribute into the session.
	 * 
	 * @param name Name of the attribute.
	 * @param value The value to assign.
	 */
	public void setAttribute(String name, Object value) {
		session.setAttribute(name, value);
		attributes.put(name, value);
	}
	
	/**
	 * @return the availableGoogleAccounts
	 */
	public List<GoogleAccount> getAvailableGoogleAccounts() {
		return availableGoogleAccounts;
	}

	/**
	 * @param availableGoogleAccounts the availableGoogleAccounts to set
	 */
	public void setAvailableGoogleAccounts(List<GoogleAccount> availableGoogleAccounts) {
		this.availableGoogleAccounts = availableGoogleAccounts;
	}

	/**
	 * @return the activeGoogleAccount
	 */
	public GoogleAccount getActiveGoogleAccount() {
		return activeGoogleAccount;
	}

	/**
	 * @param activeGoogleAccount the activeGoogleAccount to set
	 */
	public void setActiveGoogleAccount(GoogleAccount activeGoogleAccount) {
		this.activeGoogleAccount = activeGoogleAccount;
	}

	/**
	 * Throws when a session is detected to be corrupted.
	 * This can happen if the wrong type of object is stored in a known attribute.
	 */
	public class CorruptedSessionException extends Exception {
		
	}
	
}
