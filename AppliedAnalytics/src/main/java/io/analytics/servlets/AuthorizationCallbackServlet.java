package io.analytics.servlets;

import java.io.IOException;
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
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;


public class AuthorizationCallbackServlet extends AbstractAuthorizationCodeCallbackServlet {

	private static final long serialVersionUID = -7856464786872438430L;

@Override
  protected void onSuccess(HttpServletRequest req, HttpServletResponse resp, Credential credential)
      throws ServletException, IOException {
	  HttpSession session = req.getSession();
	  session.setAttribute("credentials", credential);
	  
	  resp.sendRedirect("/site/success");
	  
  }

  @Override
  protected void onError(
      HttpServletRequest req, HttpServletResponse resp, AuthorizationCodeResponseUrl errorResponse)
      throws ServletException, IOException {
	    resp.getWriter().print("<p>You've declined to authorize this application.</p>");
	    resp.getWriter().print("<p><a href=\"/\">Visit this page</a> to try again.</p>");
	    resp.setStatus(200);
	    resp.addHeader("Content-Type", "text/html");
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
	  scopes.add("https://www.googleapis.com/auth/plus.login");
	  
	  // TODO: Change this to use the GoogleClientSecrets object and load the ID and client secret from JSON elsewhere.
	  GoogleAuthorizationCodeFlow.Builder builder = new GoogleAuthorizationCodeFlow.Builder(new NetHttpTransport(), new JacksonFactory(), 
    		"409721414292.apps.googleusercontent.com", "ZJkn2D7ciulmkd0oyJ6jX-DU", scopes);
	  
	  builder.setAccessType("offline"); //This is so we can retain the refresh token, and not have to ask the user again.
	  builder.setApprovalPrompt("force");
	  return builder.build();
  }

  @Override
  protected String getUserId(HttpServletRequest req) throws ServletException, IOException {
	  //TODO: This needs to be double-checked that it is an acceptable way of getting the user ID.
	  //Find out exactly what getUserId is for, and what kind of unique ID it needs to function properly.
	  HttpSession session = req.getSession();
	  return session.getId();
  }
}

