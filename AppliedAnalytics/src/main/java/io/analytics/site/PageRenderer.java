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
             
        if (model.containsKey("HeaderFooter") && (Boolean)model.get("HeaderFooter")) {
        	request.setAttribute("page_header", "/WEB-INF/views/includes/header.jsp");
        	request.setAttribute("page_footer", "/WEB-INF/views/includes/footer.jsp");
        }
        
        // set original view being asked for as a request parmeter
        request.setAttribute("page_frame", dispatcherPath.substring(dispatcherPath.lastIndexOf("/") + 1));
        // force everything to be template.jsp
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/pages/page-template.jsp");
        
        rd.include(request, response);
	 }

}
