package io.analytics.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.ELRequestMatcher;
import org.springframework.security.web.util.RequestMatcher;

/**
 * This is a small useful entry point class as presented at:
 * http://www.bmchild.com/2013/05/spring-security-return-401-unauthorized.html
 * 
 * It is meant to be used with Spring Security as an entry point that detects AJAX
 * requests and sends a 401 error response if so. A bean should be created for this
 * class in spring-security.xml, and referenced as the entry-point-ref property of 
 * the <http> node.
 * 
 * @author Brett Child (http://www.bmchild.com/)
 *
 */
public class AjaxAwareAuthEntryPoint extends LoginUrlAuthenticationEntryPoint {
	
	private static final RequestMatcher requestMatcher = new ELRequestMatcher(
			"hasHeader('X-Requested-With','XMLHttpRequest')");
	
	@SuppressWarnings("deprecation")
	public AjaxAwareAuthEntryPoint() {
		super();
	}
	
	public AjaxAwareAuthEntryPoint(String loginFormUrl) {
		super(loginFormUrl);
	}
	
	@Override
	public void commence(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException authException) throws IOException, ServletException {
        if(isPreflight(request)){
        	response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else if (isRestRequest(request)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        } else {
            super.commence(request, response, authException);
        }
    }
	
	/**
   * Checks if this is a X-domain pre-flight request.
	 * @param request
	 * @return
	 */
	private boolean isPreflight(HttpServletRequest request) {
		return "OPTIONS".equals(request.getMethod());
	}
 
	/**
	 * Checks if it is a rest request
	 * @param request
	 * @return
	 */
	protected boolean isRestRequest(HttpServletRequest request) {
		return requestMatcher.matches(request);
	}

}
