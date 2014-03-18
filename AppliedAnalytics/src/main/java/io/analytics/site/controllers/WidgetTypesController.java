package io.analytics.site.controllers;

import io.analytics.service.interfaces.IWidgetLibrariesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WidgetTypesController {
	
	@Autowired
	IWidgetLibrariesService WidgetLibrariesService;
	
	
	@RequestMapping(value = "/widget-types", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView getAllWidgetTypes(Model viewMap) {
		//TODO: Implement when needed.
		return new ModelAndView("plaintext");
	}
	@RequestMapping(value = "/widget-types/{widgetTypeId}", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView getWidgetType(Model viewMap, @PathVariable String widgetTypeId) {
		//TODO: Implement when needed.
		return new ModelAndView("plaintext");
	}
	@RequestMapping(value = "/widget-library-types", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView getAllLibraryWidgetTypes(Model viewMap) {
		//TODO: Implement when needed.
		return new ModelAndView("plaintext");
	}
	@RequestMapping(value = "/widget-library-types/{widgetLibraryTypeId}", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView getLibraryWidgetTypes(Model viewMap, @PathVariable String widgetLibraryTypeId) {
		return new ModelAndView("plaintext");
	}
}
