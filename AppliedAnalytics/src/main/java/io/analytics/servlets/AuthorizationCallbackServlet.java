package io.analytics.servlets;

import io.analytics.domain.GoogleUserData;
import io.analytics.repository.ManagementRepository.CredentialException;
import io.analytics.service.IManagementService;
import io.analytics.service.ManagementService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeCallbackServlet;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.analytics.Analytics;
import com.google.api.services.analytics.model.Account;
import com.google.api.services.analytics.model.Accounts;
import com.google.api.services.analytics.model.Profile;
import com.google.api.services.analytics.model.Profiles;
import com.google.api.services.analytics.model.Webproperties;
import com.google.api.services.analytics.model.Webproperty;
import com.google.gson.Gson;

public class AuthorizationCallbackServlet extends AbstractAuthorizationCodeCallbackServlet {

	private static final long serialVersionUID = -7856464786872438430L;

	private final String CLIENT_ID = "409721414292.apps.googleusercontent.com";
	private final String CLIENT_SECRET = "ZJkn2D7ciulmkd0oyJ6jX-DU";
	private static final HttpTransport TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();

	@Override
	protected void onSuccess(HttpServletRequest req, HttpServletResponse resp, Credential credential)
			throws ServletException, IOException {

		if (credential == null) {
			// TODO: Handle error.
		}

		HttpSession session = req.getSession();

		try {
			IManagementService management = new ManagementService(credential);
			GoogleUserData userData = management.getGoogleUserData();
			Accounts accounts = management.getAccounts();
			if (accounts != null && accounts.getItems() != null) {
				if (!accounts.getItems().isEmpty()) {
					for (Account a : accounts.getItems())
						System.out.println(a.getName());
				}
			}

		} catch (CredentialException e) {
			System.err.println("Invalid credentials.");
		}
		session.setAttribute("credentials", credential);
		String contextPath = session.getServletContext().getContextPath();
		resp.sendRedirect(contextPath + "/application");
	}

	@Override
	protected void onError(HttpServletRequest req, HttpServletResponse resp, AuthorizationCodeResponseUrl errorResponse)
			throws ServletException, IOException {
		// If the authorization failed or was denied, go back to the home page.
		HttpSession session = req.getSession();
		String contextPath = session.getServletContext().getContextPath();
		resp.sendRedirect(contextPath + "/");
	}

	@Override
	protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
		GenericUrl url = new GenericUrl(req.getRequestURL().toString());
		String contextPath = req.getSession().getServletContext().getContextPath();
		url.setRawPath(contextPath + "/oauth2callback");
		return url.build();
	}

	@Override
	protected AuthorizationCodeFlow initializeFlow() throws IOException {
		// TODO: Do we need all of these scopes?
		ArrayList<String> scopes = new ArrayList<String>();
		scopes.add("openid");
		scopes.add("email");
		scopes.add("https://www.googleapis.com/auth/userinfo.profile");
		scopes.add("https://www.googleapis.com/auth/analytics");

		// TODO: Change this to use the GoogleClientSecrets object and load the
		// ID and client secret from JSON elsewhere.
		GoogleAuthorizationCodeFlow.Builder builder = new GoogleAuthorizationCodeFlow.Builder(new NetHttpTransport(),
				new JacksonFactory(), CLIENT_ID, CLIENT_SECRET, scopes);

		builder.setAccessType("offline"); // This is so we can retain the
											// refresh token, and not have to
											// ask the user again.
		builder.setApprovalPrompt("force");
		return builder.build();
	}

	@Override
	protected String getUserId(HttpServletRequest req) throws ServletException, IOException {
		// TODO: This needs to be double-checked that it is an acceptable way of
		// getting the user ID.
		// Find out exactly what getUserId is for, and what kind of unique ID it
		// needs to function properly.
		HttpSession session = req.getSession();
		return session.getId();
	}
}
