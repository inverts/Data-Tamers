package io.analytics.site.controllers;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import io.analytics.repository.ManagementRepository.CredentialException;
import io.analytics.service.ManagementService;
import io.analytics.service.SecurityService;
import io.analytics.site.models.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.api.client.auth.oauth2.Credential;

@Controller
public class WidgetController {
	

	@RequestMapping(value = "/HypotheticalFuture", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView hypotheticalFutureView(Model viewMap,	// note: in order for @RequestParam to work, you do need a default value
												@RequestParam(value = "change", defaultValue = "05") String adjustBy,
												@RequestParam(value = "source", defaultValue = "") String source) {

		
		// The Model will map the data via the @ModelAttribute 
		// annotation located on each getter method.
		HypotheticalFutureModel hypotheticalFuture = new HypotheticalFutureModel(adjustBy, source);
		
		viewMap.addAttribute("hfModel", hypotheticalFuture);
		viewMap.addAttribute("DATA", hypotheticalFuture.getVisualization());
	
		return new ModelAndView("HypotheticalFuture");

	}
	
	@RequestMapping(value = "/settings", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView settingsView(Model viewMap,  HttpServletResponse response, HttpSession session, 
			@RequestParam(value = "account", defaultValue = "none") String accountId,
			@RequestParam(value = "property", defaultValue = "none") String propertyId,
			@RequestParam(value = "profile", defaultValue = "none") String profileId,
			@RequestParam(value = "update", defaultValue = "") String update) {

		SettingsModel settings;
		if (SecurityService.checkAuthorization(session)) {
			settings = SecurityService.getUserSettings();
		} else {
			SecurityService.redirectToLogin(session, response);
			return new ModelAndView("unavailable");
		}

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
		
		viewMap.addAttribute("settings", settings);
	
		return new ModelAndView("settings");

	}

}
