package io.analytics.service.interfaces;

import io.analytics.site.models.FilterModel;
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
	
	SettingsModel getUserSettings();
	
	FilterModel getFilter();
	
	void saveFilter(HttpSession session, FilterModel filter);
	
	Credential getCredentials();
	
	HashMap<String, Object> getModels(HttpSession session);
	
	<T> T getModel(HttpSession session, String s, Class<T> c);
	
	void saveModel(HttpSession session, String s, Object model);
	
	boolean redirectToLogin(HttpSession session, HttpServletRequest request, HttpServletResponse response);

}
