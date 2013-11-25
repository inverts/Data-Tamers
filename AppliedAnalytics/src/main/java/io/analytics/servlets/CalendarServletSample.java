package io.analytics.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeServlet;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;

public class CalendarServletSample extends AbstractAuthorizationCodeServlet {

	  @Override
	  protected void doGet(HttpServletRequest request, HttpServletResponse response)
	      throws IOException {
	    initializeFlow();
	  }

	  @Override
	  protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
	    GenericUrl url = new GenericUrl(req.getRequestURL().toString());
	    url.setRawPath("/site/oauth2callback");
	    return url.build();
	  }

	  @Override
	  protected AuthorizationCodeFlow initializeFlow() throws IOException {
		  ArrayList<String> scopes = new ArrayList<String>();
		  scopes.add("https://www.googleapis.com/auth/analytics");

		  // TODO: Change this to use the GoogleClientSecrets object and load the ID and client secret from JSON elsewhere.
		  GoogleAuthorizationCodeFlow.Builder builder = new GoogleAuthorizationCodeFlow.Builder(new NetHttpTransport(), new JacksonFactory(), 
	    		"409721414292.apps.googleusercontent.com", "ZJkn2D7ciulmkd0oyJ6jX-DU", scopes);
		  
		  builder.setAccessType("offline"); //This is so we can retain the refresh token, and not have to ask the user again.
		  builder.setApprovalPrompt("force");

		  return builder.build();
	  }

	  @Override
	  protected String getUserId(HttpServletRequest req) throws ServletException, IOException {
		  

		  //TODO: Replace with a unique user ID from this person's session
	    return "dave";
	  }
	}
