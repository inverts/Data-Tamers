package io.analytics.servlets;

import io.analytics.domain.GoogleUserData;

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
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeCallbackServlet;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
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

		final String ACCESS_TOKEN = credential.getAccessToken();
		GoogleUserData userData = null;

		try {
			URL url = new URL("https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + ACCESS_TOKEN);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			Gson gson = new Gson();
			userData = gson.fromJson(reader, GoogleUserData.class);
			reader.close();
		} catch (MalformedURLException e) {
			// TODO: Add error handling
		} catch (IOException e) {
			// TODO: Add error handling
		}
		try {
			Analytics analytics = new Analytics.Builder(TRANSPORT, JSON_FACTORY, credential).setApplicationName(
					"datatamers-appliedanalytics-0.1").build();

			Accounts accounts = analytics.management().accounts().list().execute();
			if (!accounts.getItems().isEmpty()) {
				for (Account acc : accounts.getItems()) {
					System.out.println(acc.getId() + " - " + acc.getName());
					// Maybe we should do this section at a later point when the user chooses which account they want.
					/*
					Webproperties webproperties = analytics.management().webproperties().list(acc.getId()).execute();
					if (webproperties.getItems().isEmpty())
						continue;
					for (Webproperty wp : webproperties.getItems()) {
						if (wp.getProfileCount() < 1)
							continue;
						Profiles profiles = analytics.management().profiles().list(acc.getId(), wp.getId()).execute();
						for (Profile prof : profiles.getItems()) {
							System.out.println(prof.getId() + " - " + prof.getName());
						}
	
					}
					 */
				}
			} else {
				//TODO: Handle users with no analytics accounts
			}

		} catch (GoogleJsonResponseException e) {
			System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
					+ e.getDetails().getMessage());
		} catch (Throwable t) {
			t.printStackTrace();
		}

		HttpSession session = req.getSession();

		session.setAttribute("credentials", credential);
		if (userData != null)
			session.setAttribute("userData", userData);

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
