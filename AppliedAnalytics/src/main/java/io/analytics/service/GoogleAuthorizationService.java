package io.analytics.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;

/**
 * A service for obtaining Google credentials, which are necessary to make
 * most Analytics calls.
 * 
 * @author Dave Wong
 *
 */
public class GoogleAuthorizationService {

	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	private static final String CLIENT_SECRET_LOCATION = "src/main/resources/client_secrets.json";
	private static GoogleClientSecrets clientSecrets = null;

	// Request a new Access token using the refresh token.
	public Credential getAccountCredentials(String refreshToken) {
		Credential credential = createCredentialWithRefreshToken(HTTP_TRANSPORT, JSON_FACTORY,
				new TokenResponse().setRefreshToken(refreshToken));
		try {
			credential.refreshToken();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return credential;
	}

	private static Credential createCredentialWithRefreshToken(HttpTransport transport, JsonFactory jsonFactory,
			TokenResponse tokenResponse) {

		GoogleClientSecrets clientSecrets = loadClientSecrets();
		return new GoogleCredential.Builder().setTransport(transport).setJsonFactory(jsonFactory)
				.setClientSecrets(clientSecrets).build().setFromTokenResponse(tokenResponse);
	}

	/**
	 * Helper to load client ID/Secret from file.
	 */
	private static GoogleClientSecrets loadClientSecrets() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(CLIENT_SECRET_LOCATION));
			clientSecrets = GoogleClientSecrets.load(new JacksonFactory(), reader);
			return clientSecrets;
		} catch (Exception e) {
			System.err.println("Could not load client_secrets.json");
			e.printStackTrace();
		}
		return clientSecrets;
	}
}
