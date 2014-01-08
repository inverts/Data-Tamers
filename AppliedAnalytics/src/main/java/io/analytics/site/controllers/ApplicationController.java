package io.analytics.site.controllers;

import io.analytics.aspect.HeaderFooter;
import io.analytics.aspect.SidePanel;
import io.analytics.service.SessionService;
import io.analytics.site.models.FilterModel;
import io.analytics.site.models.SettingsModel;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class ApplicationController {
	
	private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@HeaderFooter(state = "Application")
	@SidePanel(animate = true)
	@RequestMapping(value = "/application", method = RequestMethod.GET)
	public ModelAndView home(Locale locale, Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		boolean success = SessionService.checkAuthorization(session);
		SettingsModel settings = null;
		if (success) {
			//If authorization succeeded, the following must succeed.
			settings = (SettingsModel) session.getAttribute("settings");
			model.addAttribute("settings", settings);
		} else if (SessionService.redirectToLogin(session, response)) {
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
		
		model.addAttribute("filter", filter);

		return new ModelAndView("dashboard");
	}
	
	@HeaderFooter(state = "Application")
	@SidePanel(animate = false)
	@RequestMapping(value = "/application/trends", method = RequestMethod.GET)
	public ModelAndView trends(Model model) {
		return new ModelAndView("trends");
	}

	@HeaderFooter(state = "Application")
	@SidePanel(animate = false)
	@RequestMapping(value = "/application/forecast", method = RequestMethod.GET)
	public ModelAndView forecast(Model model) {
		return new ModelAndView("forecast");
	}
	
	@HeaderFooter(state = "Application")
	@SidePanel(animate = false)
	@RequestMapping(value = "/application/suggestions", method = RequestMethod.GET)
	public ModelAndView suggestions(Model model) {
		return new ModelAndView("suggestions");
	}
	
	
	@RequestMapping(value = "/settings", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView settingsView(Model viewMap,  HttpServletResponse response, HttpSession session, 
			@RequestParam(value = "account", defaultValue = "none") String accountId,
			@RequestParam(value = "property", defaultValue = "none") String propertyId,
			@RequestParam(value = "profile", defaultValue = "none") String profileId,
			@RequestParam(value = "update", defaultValue = "") String update) 
					throws IOException {

		SettingsModel settings;
		PrintWriter out;

		try {
			
			if (!SessionService.checkAuthorization(session))
				throw new Exception("Check Authorization failed!");
			
			settings = SessionService.getUserSettings();
			response.setContentType("text/html");
			out = response.getWriter();
			
			//Only one change should be made/possible at a time.
			if (!accountId.equals("none"))
				settings.setAccountSelection(accountId);
			else if (!propertyId.equals("none"))
				settings.setPropertySelection(propertyId);
			else if (!profileId.equals("none"))
				settings.setProfileSelection(profileId);
			else if (!update.equals("")) 
				if (settings.setActiveProfile())
					viewMap.addAttribute("update", "Success!"); 
				else
					viewMap.addAttribute("update", "Failed to update."); 
			
			//TODO: Change the above to be an update state within the model instead.
			SessionService.saveUserSettings(session, settings);
			viewMap.addAttribute("settings", settings);
		}
		catch(Exception e) {
			logger.info(e.getMessage());
			SessionService.redirectToLogin(session, response);
			return new ModelAndView("unavailable");
		}
		return new ModelAndView("settings");

	}

	@RequestMapping(value = "/filter", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView filterView(Model viewMap,  HttpServletResponse response, HttpSession session, 
			@RequestParam(value = "startDate", defaultValue = "none") String startDate,
			@RequestParam(value = "endDate", defaultValue = "none") String endDate) {

		FilterModel filter;
		if (SessionService.checkAuthorization(session)) {
			filter = SessionService.getFilter();
		} else {
			SessionService.redirectToLogin(session, response);
			return new ModelAndView("unavailable");
		}
		
		//If a request for updating dates was made, update the dates in the FilterModel.
		if (startDate != "none" && endDate != "none") {
			//Make a formatter to translate the date strings in the view to Date objects.
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date start;
			Date end;
			try {
				start = formatter.parse(startDate);
				end = formatter.parse(endDate);
			} catch (ParseException e) {
				e.printStackTrace();
				return new ModelAndView("unavailable"); // Do nothing if you can't parse.
			}
			filter.setActiveStartDate(start);
			filter.setActiveEndDate(end);
		}
		SessionService.saveFilter(session, filter);
		return new ModelAndView("unavailable");
	}
	
}
