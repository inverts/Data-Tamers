package io.analytics.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeServlet;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;

public class AuthorizationEntryServlet extends AbstractAuthorizationCodeServlet {

	private static final long serialVersionUID = 3525725378482642400L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		initializeFlow();
	}

	@Override
	protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
		GenericUrl url = new GenericUrl(req.getRequestURL().toString());
		String contextPath = req.getSession().getServletContext().getContextPath();
		req.getSession().setAttribute("destinationURL", req.getParameter("destinationURL"));
		url.setRawPath(contextPath + "/oauth2callback");
		return url.build();
	}

	@Override
	protected AuthorizationCodeFlow initializeFlow() throws IOException {
		ArrayList<String> scopes = new ArrayList<String>();
		scopes.add("openid");
		scopes.add("email");
		scopes.add("https://www.googleapis.com/auth/userinfo.profile");
		scopes.add("https://www.googleapis.com/auth/analytics");

		// TODO: Change this to use the GoogleClientSecrets object and load the
		// ID and client secret from JSON elsewhere.
		GoogleAuthorizationCodeFlow.Builder builder = new GoogleAuthorizationCodeFlow.Builder(new NetHttpTransport(),
				new JacksonFactory(), "409721414292.apps.googleusercontent.com", "ZJkn2D7ciulmkd0oyJ6jX-DU", scopes);

		// Necessary for obtaining refresh token (important).
		builder.setAccessType("offline");
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
