package io.analytics.servlets;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeCallbackServlet;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;


public class AuthorizationCallbackServlet extends AbstractAuthorizationCodeCallbackServlet {

	private static final long serialVersionUID = -7856464786872438430L;

	private final String clientID = "409721414292.apps.googleusercontent.com";
	private final String clientSecret = "ZJkn2D7ciulmkd0oyJ6jX-DU";
@Override
  protected void onSuccess(HttpServletRequest req, HttpServletResponse resp, Credential credential)
      throws ServletException, IOException {
	  HttpSession session = req.getSession();
	  session.setAttribute("credentials", credential);
	  String contextPath = session.getServletContext().getContextPath();
	  resp.sendRedirect(contextPath + "/success");
	  
	  final String accessToken = credential.getAccessToken();
	  GoogleCredential cred = new GoogleCredential.Builder()
	  .setTransport(new NetHttpTransport()).setJsonFactory( new JacksonFactory())
	  .setClientSecrets(clientID, clientSecret).setRequestInitializer((new HttpRequestInitializer(){
	                  @Override
	                  public void initialize(HttpRequest request)
	                          throws IOException {
	                      request.getHeaders().put("Authorization", "Bearer " + accessToken);
	                  }
	              })).build();
	  cred.getServiceAccountId();
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
	String contextPath = req.getSession().getServletContext().getContextPath();
    url.setRawPath(contextPath + "/oauth2callback");
    return url.build();
  }

  @Override
  protected AuthorizationCodeFlow initializeFlow() throws IOException {
	  ArrayList<String> scopes = new ArrayList<String>();
	  scopes.add("openid");
	  scopes.add("email");
	  scopes.add("https://www.googleapis.com/auth/analytics");
	  
	  // TODO: Change this to use the GoogleClientSecrets object and load the ID and client secret from JSON elsewhere.
	  GoogleAuthorizationCodeFlow.Builder builder = new GoogleAuthorizationCodeFlow.Builder(new NetHttpTransport(), new JacksonFactory(), 
    		clientID, clientSecret, scopes);
	  
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

