package io.analytics.site;

import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.view.InternalResourceView;

public class PageRenderer extends InternalResourceView {	
	
	private final String ACCOUNT_FORM = "accountForm";
	private final String LOGIN_FORM = "loginForm";
	private final String DASHBOARD_FORM = "dashboardForm";
	
	@Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String dispatcherPath = prepareForRendering(request, response);

        // TODO Title logic	
        request.setAttribute("TITLE", "Applied Analytics");

        request.setAttribute("SIDEPANEL", model.get("SIDEPANEL"));
        
        // Set the header and footer if applicable
        request.setAttribute("HEADER", model.get("HEADER"));
        request.setAttribute("SETTINGS", model.get("SETTINGS"));
        request.setAttribute("FOOTER", model.get("FOOTER"));
        
        // Gather any forms and form results
        if (model.containsKey(LOGIN_FORM))
        	request.setAttribute(LOGIN_FORM, model.get(LOGIN_FORM));
        
        if (model.containsKey(ACCOUNT_FORM))
        	request.setAttribute(ACCOUNT_FORM, model.get(ACCOUNT_FORM));
        
        if (model.containsKey(DASHBOARD_FORM))
        	request.setAttribute(DASHBOARD_FORM, model.get(DASHBOARD_FORM));
        
        request.setAttribute("model", model);
        
        
        String uri = dispatcherPath.substring(0, dispatcherPath.lastIndexOf("/"));
        String page = uri.substring(uri.lastIndexOf("/") + 1);

    	// set the content
        //TODO: Set body as url beyond /pages/
        request.setAttribute("BODY", dispatcherPath.substring(dispatcherPath.lastIndexOf("/") + 1));

        // route to page-template
        //if(state.equals("application"))
        RequestDispatcher rd = request.getRequestDispatcher(getMasterPage(page));
        
        rd.include(request, response);
        
	 }
	
	private String getMasterPage(String page)
	{
		return (page.equals("application")) ? "/WEB-INF/views/pages/application/application-master.jsp" : 
			   (page.equals("home")) ? "/WEB-INF/views/pages/home/home-master.jsp" :
			   (page.equals("account")) ? "/WEB-INF/views/pages/account/account-master.jsp"
				   					 : "/WEB-INF/views/pages/default-master.jsp";
	}

}
