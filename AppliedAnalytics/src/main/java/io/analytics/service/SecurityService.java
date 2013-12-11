package io.analytics.service;

import java.io.IOException;

import io.analytics.repository.ManagementRepository.CredentialException;
import io.analytics.site.models.SettingsModel;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.auth.oauth2.Credential;

public class SecurityService {

	private static final Logger logger = LoggerFactory.getLogger(SecurityService.class);
	private static SettingsModel userSettings = null;
	private static Credential credentials = null;
	
	/**
	 * Checks to see if the user's current session is authorized.
	 * Also double-checks to make sure the user's settings are loaded into the session and
	 * loads them in if they are missing.
	 * If there is a problem, it returns false. Otherwise if the user is authorized, returns true.
	 * @param session
	 * @return
	 */
	public static boolean checkAuthorization(HttpSession session) {
		Credential credentials = null;
		SettingsModel settings = null;
		try {
			 credentials = (Credential) session.getAttribute("credentials");
			 settings = (SettingsModel) session.getAttribute("settings");
		} catch (ClassCastException e) {
			logger.info("Corrupted session information. See below for more info.");
			logger.info(e.getMessage());
			return false;
		}
		if (credentials == null) {
			return false;
		} else if (settings == null) {
			ManagementService management;
			try {
				management = new ManagementService(credentials);
			} catch (CredentialException e) {
				logger.info("There was a problem with credentials, but they were not null.");
				logger.info(e.getMessage());
				return false;
			}
			settings = new SettingsModel(management);
			session.setAttribute("settings", settings);
			SecurityService.userSettings = settings;
			SecurityService.credentials = credentials;
		}
		
		return true;
		
	}

	public static SettingsModel getUserSettings() {
		return userSettings;
	}

	public static Credential getCredentials() {
		return credentials;
	}
	
	public static boolean redirectToLogin(HttpSession session, HttpServletResponse response) {
		String contextPath = session.getServletContext().getContextPath();
		try {
			response.sendRedirect(contextPath + "/login");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
