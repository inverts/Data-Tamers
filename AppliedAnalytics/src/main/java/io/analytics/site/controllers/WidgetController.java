package io.analytics.site.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import io.analytics.domain.CoreReportingData;
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
import com.google.api.services.analytics.model.GaData;
import com.google.api.services.analytics.model.GaData.ColumnHeaders;

@Controller
public class WidgetController {
	
	
	@RequestMapping(value = "/HypotheticalFuture", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView hypotheticalFutureView(Model viewMap, HttpServletResponse response, HttpSession session,	
												@RequestParam(value = "change", defaultValue = "none") String changePercentage,
												@RequestParam(value = "dimension", defaultValue = "none") String dimension) {

		
		Credential credential;
		SettingsModel settings;
		FilterModel filter;
		if (SessionService.checkAuthorization(session)) {
			credential = SessionService.getCredentials();
			filter = SessionService.getFilter();
			settings = SessionService.getUserSettings();
		} else {
			SessionService.redirectToLogin(session, response);
			return new ModelAndView("unavailable");
		}
		if (settings.getActiveProfile() == null) {
			
			//TODO: Make an informative view for when widgets don't have an active profile to get data from.
			return new ModelAndView("unavailable");
		}
		
		
		
		HypotheticalFutureModel hypotheticalFuture = SessionService.getModel(session, "hypotheticalFuture", HypotheticalFutureModel.class);
		
		//If there is no model available, or if the active profile changed, create a new model.
		if ((hypotheticalFuture == null) || !(settings.getActiveProfile().equals(hypotheticalFuture.getActiveProfile()))) {
			CoreReportingService reportingService = null;
			try {
				reportingService = new CoreReportingService(credential, settings.getActiveProfile().getId());
			} catch (io.analytics.repository.CoreReportingRepository.CredentialException e) {
				e.printStackTrace();
				SessionService.redirectToLogin(session, response);
				return new ModelAndView("unavailable");
			}
			hypotheticalFuture = new HypotheticalFutureModel(reportingService);
		}
		
		if (filter != null) {
			hypotheticalFuture.setStartDate(filter.getActiveStartDate());
			hypotheticalFuture.setEndDate(filter.getActiveEndDate());
		}
		
		
		//Execute API commands to change the model
		if (!changePercentage.equals("none"))
			hypotheticalFuture.setChangePercentage(changePercentage);
		if (!dimension.equals("none"))
			hypotheticalFuture.setDimension(dimension);
		hypotheticalFuture.updateData();
		SessionService.saveModel(session, "hypotheticalFuture", hypotheticalFuture);
		viewMap.addAttribute("hfModel", hypotheticalFuture);
		/*
		HypotheticalFutureModel hypotheticalFuture = new HypotheticalFutureModel(adjustBy, source);
		
		viewMap.addAttribute("hfModel", hypotheticalFuture);
		viewMap.addAttribute("changeOptions", hypotheticalFuture.getChangePercentOptions());
		viewMap.addAttribute("DATA", hypotheticalFuture.getVisualization());
		 */
		return new ModelAndView("HypotheticalFuture");

	}
	
	@RequestMapping(value = "/RevenueSources", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView revenueSourcesView(Model viewMap, HttpServletResponse response, HttpSession session)
	{
		Credential credential;
		SettingsModel settings;
		FilterModel filter;
		if (SessionService.checkAuthorization(session)) {
			credential = SessionService.getCredentials();
			filter = SessionService.getFilter();
			settings = SessionService.getUserSettings();
		} else {
			SessionService.redirectToLogin(session, response);
			return new ModelAndView("unavailable");
		}
		if (settings.getActiveProfile() == null) {
			
			//TODO: Make an informative view for when widgets don't have an active profile to get data from.
			return new ModelAndView("unavailable");
		}
		
		return new ModelAndView("RevenueSources");
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
