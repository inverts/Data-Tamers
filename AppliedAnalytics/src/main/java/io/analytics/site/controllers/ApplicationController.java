package io.analytics.site.controllers;

import io.analytics.aspect.HeaderFooter;
import io.analytics.aspect.SidePanel;
import io.analytics.enums.HeaderType;
import io.analytics.service.interfaces.ISessionService;
import io.analytics.site.models.FilterModel;
import io.analytics.site.models.SettingsModel;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class ApplicationController {
	
	private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);
	
	@Autowired private ISessionService SessionService;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@HeaderFooter(HeaderType.APPLICATION)
	@SidePanel(animate = true)
	@RequestMapping(value = "/application", method = RequestMethod.GET)
	public ModelAndView dashboard(Locale locale, Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		boolean success = SessionService.checkAuthorization(session);
		SettingsModel settings = null;
		if (success) {
			//If authorization succeeded, the following must succeed.
			settings = (SettingsModel) session.getAttribute("settings");
			model.addAttribute("settings", settings);
		} else if (SessionService.redirectToLogin(session, request, response)) {
			return null;
		} else {
			return new ModelAndView("unavailable");
		}
		
		FilterModel filter = null;
		try {
			filter = (FilterModel) session.getAttribute("filter");
		} catch (ClassCastException e) {
			logger.info("Corrupted session information. See below for more info.");
			logger.info(e.getMessage());
			return new ModelAndView("unavailable");
		}
		if (filter == null) {
			filter = new FilterModel();
			session.setAttribute("filter", filter);
		}
		
		request.setAttribute("state", "application");
		
		model.addAttribute("filter", filter);

		return new ModelAndView("application/dashboard");
	}
	
	@HeaderFooter(HeaderType.APPLICATION)
	@SidePanel(animate = false)
	@RequestMapping(value = "/application/trends", method = RequestMethod.GET)
	public ModelAndView trends(Model model) {
		return new ModelAndView("application/trends");
	}

	@HeaderFooter(HeaderType.APPLICATION)
	@SidePanel(animate = false)
	@RequestMapping(value = "/application/forecast", method = RequestMethod.GET)
	public ModelAndView forecast(Model model) {
		return new ModelAndView("application/forecast");
	}
	
	@HeaderFooter(HeaderType.APPLICATION)
	@SidePanel(animate = false)
	@RequestMapping(value = "/application/suggestions", method = RequestMethod.GET)
	public ModelAndView suggestions(Model model) {
		return new ModelAndView("application/suggestions");
	}

	
}
