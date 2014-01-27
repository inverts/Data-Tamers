package io.analytics.site.controllers;

import io.analytics.service.SessionService;
import io.analytics.service.interfaces.ISessionService;
import io.analytics.site.models.FilterModel;
import io.analytics.site.models.SettingsModel;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SettingsController {

	private static final Logger logger = LoggerFactory.getLogger(SettingsController.class);
	
	@Autowired private ISessionService SessionService;
	
	/*
	 * SettingsView
	 */
	@RequestMapping(value = "/settings", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView settingsView(Model viewMap,  HttpServletRequest request, HttpServletResponse response, HttpSession session, 
			@RequestParam(value = "account", defaultValue = "none") String accountId,
			@RequestParam(value = "property", defaultValue = "none") String propertyId,
			@RequestParam(value = "profile", defaultValue = "none") String profileId,
			@RequestParam(value = "update", defaultValue = "") String update) 
					throws IOException {

		SettingsModel settings;

		try {
			
			if (!SessionService.checkAuthorization(session))
				throw new Exception("Check Authorization failed!");
			
			settings = SessionService.getUserSettings();
			response.setContentType("text/html");
			
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
			SessionService.redirectToLogin(session, request, response);
			return new ModelAndView("unavailable");
		}
		return new ModelAndView("settings");

	}

	/*
	 * FilterView 
	 */
	@RequestMapping(value = "/filter", method = {RequestMethod.POST, RequestMethod.GET})
	public void filterView(Model viewMap, HttpServletRequest request,  HttpServletResponse response, HttpSession session, 
			@RequestParam(value = "startDate") String startDate,
			@RequestParam(value = "endDate") String endDate) {

		FilterModel filter = null;
		if (SessionService.checkAuthorization(session)) {
			filter = SessionService.getFilter();
		} else {
			SessionService.redirectToLogin(session, request, response);
			//return new ModelAndView("unavailable");
		}
		
		//If a request for updating dates was made, update the dates in the FilterModel.
		if (startDate != null && endDate != null) {
			//Make a formatter to translate the date strings in the view to Date objects.
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date start = null;
			Date end = null;
			try {
				start = formatter.parse(startDate);
				end = formatter.parse(endDate);
			} catch (ParseException e) {
				e.printStackTrace();
				//return new ModelAndView("unavailable"); // Do nothing if you can't parse.
			}
			filter.setActiveStartDate(start);
			filter.setActiveEndDate(end);
		}
		SessionService.saveFilter(session, filter);
		//return new ModelAndView("unavailable");
	}
	
}
