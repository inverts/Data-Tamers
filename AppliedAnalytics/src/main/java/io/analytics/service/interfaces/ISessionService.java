package io.analytics.service.interfaces;

import io.analytics.site.models.FilterModel;
import io.analytics.site.models.SessionModel;
import io.analytics.site.models.SettingsModel;

import java.io.Serializable;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.api.client.auth.oauth2.Credential;

public interface ISessionService extends Serializable {
	
	boolean checkAuthorization(HttpSession session);
	
	void saveUserSettings(HttpSession session, SettingsModel settings);
	
	void saveFilter(HttpSession session, FilterModel filter);
	
	public SettingsModel getUserSettings(HttpSession session);
	
	public FilterModel getFilter(HttpSession session);
	
	public Credential getCredentials(HttpSession session);
	
	public SessionModel getSessionModel(HttpSession session);
	
	public HashMap<String, Object> getModels(HttpSession session);
	
	public <T> T getModel(HttpSession session, String s, Class<T> c);
	
	void saveModel(HttpSession session, String s, Object model);

	boolean redirectToLogin(HttpSession session, HttpServletRequest request, HttpServletResponse response);
	
	boolean redirectToRegularLogin(HttpSession session, String queryString, HttpServletResponse response);
	
}
