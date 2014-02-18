package io.analytics.repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.stereotype.Repository;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.analytics.Analytics;
import com.google.api.services.analytics.model.*;
import com.google.gson.Gson;

import io.analytics.domain.GoogleUserData;
import io.analytics.repository.interfaces.IManagementRepository;

/**
 * A repository for interacting with the Google Analytics Management API.
 * Here, we can obtain information about the user, their various Google Analytics
 * accounts, web properties, and profiles, as well as segments, goals, and Google's
 * available metrics and dimensions.
 * 
 * @author Dave Wong
 * 
 */
//TODO: Make sure we are respecting the rate limit (10 queries per second)
@Repository
public class ManagementRepository implements IManagementRepository {

	private final String APPLICATION_NAME = "datatamers-appliedanalytics-0.1";
	//private final String ACCESS_TOKEN;
	//private final Credential CREDENTIAL;
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();

	private Analytics.Management MANAGEMENT;

	public static class CredentialException extends Throwable {
		public CredentialException() {
			super();
		}

		public CredentialException(String s) {
			super(s);
		}
	}

	/*public ManagementRepository(Credential credential) throws CredentialException {
		if (credential == null)
			throw new CredentialException("Null credential object passed.");
		this.ACCESS_TOKEN = credential.getAccessToken();
		this.CREDENTIAL = credential;
		// TODO: Can we do this with only the access token?
		this.MANAGEMENT = new Analytics.Builder(HTTP_TRANSPORT, JSON_FACTORY, CREDENTIAL)
				.setApplicationName(APPLICATION_NAME).build().management();

		// Refresh the access token if it is about to expire.
		if (CREDENTIAL.getExpiresInSeconds() < 10) {
			boolean success = false;
			try {
				success = CREDENTIAL.refreshToken();
			} catch (IOException e) {
				throw new CredentialException("4xx error occured while refreshing token.");
			}
			if (!success)
				throw new CredentialException("Token refresh failed. There may not be a refresh token.");
		}
	}*/
	
	private void refreshToken(Credential credential) throws CredentialException  {
		
		if (credential.getExpiresInSeconds() < 10) {
			boolean success = false;
			try {
				success = credential.refreshToken();
			} catch (IOException e) {
				throw new CredentialException("4xx error occured while refreshing token.");
			}
			if (!success)
				throw new CredentialException("Token refresh failed. There may not be a refresh token.");
		}
	}

	public GoogleUserData getGoogleUserData(Credential credential) {
		GoogleUserData data = null;
		try {
			String accessToken = credential.getAccessToken(); // ACCESS TOKEN
			this.refreshToken(credential);
			URL url = new URL("https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + accessToken);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			Gson gson = new Gson();
			data = gson.fromJson(reader, GoogleUserData.class);
			reader.close();
		} catch (MalformedURLException e) {
			// TODO: Add error handling
		} catch (IOException e) {
			// TODO: Add error handling
		} catch (CredentialException e) {
			// TODO: Add error handling
		}

		return data;
	}

	public Accounts getAccounts(Credential credential) {
		Accounts accounts = null;
		try {
			// Just create the Analytics.Builder here.
			this.MANAGEMENT = new Analytics.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
										           .setApplicationName(APPLICATION_NAME).build().management();
			this.refreshToken(credential);
			accounts = this.MANAGEMENT.accounts().list().execute();

		} catch (GoogleJsonResponseException e) {
			handleGoogleJsonResponseException(e);
		} catch (Throwable t) {
			t.printStackTrace();
		}

		return accounts;
	}

	public Webproperties getWebproperties(Account a) {
		return getWebproperties(a.getId());
	}

	public Profiles getProfiles(Account a, Webproperty w) {
		return getProfiles(a.getId(), w.getId());
	}

	public Webproperties getWebproperties(String accountId) {
		Webproperties properties = null;
		try {

			properties = MANAGEMENT.webproperties().list(accountId).execute();

		} catch (GoogleJsonResponseException e) {
			handleGoogleJsonResponseException(e);
		} catch (Throwable t) {
			t.printStackTrace();
		}

		return properties;
	}
	
	public Profiles getProfiles(String accountId, String webpropertyId) {
		Profiles profiles = null;
		try {

			profiles = MANAGEMENT.profiles().list(accountId, webpropertyId).execute();

		} catch (GoogleJsonResponseException e) {
			handleGoogleJsonResponseException(e);
		} catch (Throwable t) {
			t.printStackTrace();
		}

		return profiles;
	}
	
	private void handleGoogleJsonResponseException(GoogleJsonResponseException e) {
		System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
				+ e.getDetails().getMessage());
	}
}
