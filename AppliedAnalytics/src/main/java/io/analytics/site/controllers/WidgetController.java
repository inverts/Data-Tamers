package io.analytics.site.controllers;

import java.util.Locale;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import io.analytics.repository.ManagementRepository.CredentialException;
import io.analytics.service.CoreReportingService;
import io.analytics.service.ManagementService;
import io.analytics.service.SessionService;
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
	
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView home(Locale locale, Model model) {
		
		return new ModelAndView("entry");
	}
	
	@RequestMapping(value = "/HypotheticalFuture", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView hypotheticalFutureView(Model viewMap, HttpServletResponse response, HttpSession session,	
												@RequestParam(value = "change", defaultValue = "none") String changePercentage,
												@RequestParam(value = "dimension", defaultValue = "none") String dimension) {

		
		// The Model will map the data via the @ModelAttribute 
		// annotation located on each getter method.

		Credential credential;
		SettingsModel settings;
		if (SessionService.checkAuthorization(session)) {
			credential = SessionService.getCredentials();
			settings = SessionService.getUserSettings();
		} else {
			SessionService.redirectToLogin(session, response);
			return new ModelAndView("unavailable");
		}
		if (settings.getActiveProfile() == null) {
			
			//TODO: Make an informative view for when widgets don't have an active profile to get data from.
			return new ModelAndView("unavailable");
		}
		
		
		CoreReportingService reportingService = null;
		try {
			reportingService = new CoreReportingService(credential, settings.getActiveProfile().getId());
		} catch (io.analytics.repository.CoreReportingRepository.CredentialException e) {
			e.printStackTrace();
			SessionService.redirectToLogin(session, response);
			return new ModelAndView("unavailable");
		}
		
		HypotheticalFutureModel hypotheticalFuture = SessionService.getModel(session, "hypotheticalFuture", HypotheticalFutureModel.class);
		
		if (hypotheticalFuture == null)
			hypotheticalFuture = new HypotheticalFutureModel(reportingService);
		
		//Execute API commands to change the model
		if (!changePercentage.equals("none"))
			hypotheticalFuture.setChangePercentage(changePercentage);
		if (!dimension.equals("none"))
			hypotheticalFuture.setDimension(dimension);
		
		viewMap.addAttribute("hfModel", hypotheticalFuture);
	
		return new ModelAndView("HypotheticalFuture");

	}
	
	@RequestMapping(value = "/settings", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView settingsView(Model viewMap,  HttpServletResponse response, HttpSession session, 
			@RequestParam(value = "account", defaultValue = "none") String accountId,
			@RequestParam(value = "property", defaultValue = "none") String propertyId,
			@RequestParam(value = "profile", defaultValue = "none") String profileId,
			@RequestParam(value = "update", defaultValue = "") String update) {

		SettingsModel settings;
		if (SessionService.checkAuthorization(session)) {
			settings = SessionService.getUserSettings();
		} else {
			SessionService.redirectToLogin(session, response);
			return new ModelAndView("unavailable");
		}

		/***** GWEN *****
		 
		//This is your Profile object
		settings.getActiveProfile();
		
		//This is your Profile ID or "Table ID"
		String profileId = settings.getActiveProfile().getId();
		
		//This is your Credential object
		credential
		
		*/

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
