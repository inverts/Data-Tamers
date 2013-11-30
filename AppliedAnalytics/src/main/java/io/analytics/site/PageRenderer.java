package io.analytics.site;

import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.InternalResourceView;

public class PageRenderer extends InternalResourceView {
	@Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String dispatcherPath = prepareForRendering(request, response);
        
        // Set the header and footer if applicable
        request.setAttribute("HEADER", model.get("HEADER"));
        request.setAttribute("FOOTER", model.get("FOOTER"));
        
        request.setAttribute("model", model);

    	// set the content
        request.setAttribute("page_frame", dispatcherPath.substring(dispatcherPath.lastIndexOf("/") + 1));
        
        // route to page-template
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/pages/page-template.jsp");
        
        rd.include(request, response);
	 }

}
