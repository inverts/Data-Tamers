package io.analytics.site.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import io.analytics.service.interfaces.ICoreReportingService;
import io.analytics.service.interfaces.IPagePerfomanceService;
import io.analytics.service.interfaces.ISessionService;
import io.analytics.site.models.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.api.client.auth.oauth2.Credential;

@Controller
public class WidgetController {

	private static final Logger logger = LoggerFactory.getLogger(WidgetController.class);
	
	@Autowired private ICoreReportingService CoreReportingService;
	@Autowired private ISessionService SessionService;
	@Autowired private IPagePerfomanceService PagePerformanceService;

	@RequestMapping(value = "/DataForecast", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView DataForecastView(Model viewMap, HttpServletRequest request, HttpServletResponse response, HttpSession session,	
												@RequestParam(value = "change", defaultValue = "none") String changePercentage,
												@RequestParam(value = "dimension", defaultValue = "none") String dimension) 
	{
		Credential credential;
		SettingsModel settings;
		FilterModel filter;
		
		if (SessionService.checkAuthorization(session)) {
			credential = SessionService.getCredentials();
			filter = SessionService.getFilter();
			settings = SessionService.getUserSettings();
		} else {
			SessionService.redirectToLogin(session, request, response);
			return new ModelAndView("unavailable");
		}
		if (settings.getActiveProfile() == null) {

			//TODO: Make an informative view for when widgets don't have an active profile to get data from.
			return new ModelAndView("unavailable");
		}

		DataForecastModel dataForecast = SessionService.getModel(session, "hypotheticalFuture", DataForecastModel.class);

		//If there is no model available, or if the active profile changed, create a new model.
		if ((dataForecast == null) || !(settings.getActiveProfile().equals(dataForecast.getActiveProfile()))) {
			dataForecast = new DataForecastModel(this.SessionService, this.CoreReportingService);
		}

		if (filter != null) {
			dataForecast.setStartDate(filter.getActiveStartDate());
			dataForecast.setEndDate(filter.getActiveEndDate());
		}


		//Execute API commands to change the model
		if (!changePercentage.equals("none"))
			dataForecast.setChangePercentage(changePercentage);
		if (!dimension.equals("none"))
			dataForecast.setDimension(dimension);
		dataForecast.updateData();
		SessionService.saveModel(session, "hypotheticalFuture", dataForecast);
		viewMap.addAttribute("hfModel", dataForecast);
		viewMap.addAttribute("filterModel", filter);
		/*
		HypotheticalFutureModel hypotheticalFuture = new HypotheticalFutureModel(adjustBy, source);
		
		viewMap.addAttribute("hfModel", hypotheticalFuture);
		viewMap.addAttribute("changeOptions", hypotheticalFuture.getChangePercentOptions());
		viewMap.addAttribute("DATA", hypotheticalFuture.getVisualization());
		 */
		return new ModelAndView("DataForecast");

	}
	
	@RequestMapping(value = "/saveDataForecast", method = RequestMethod.POST)
	private void saveDataForecastSettings(@RequestParam("widgetId") int widgetId, 
										  @RequestParam("data") String inputData)
	{
		try {
			
			
		} catch(Exception e) {
			logger.info("Could not save DataForecast data");
			logger.info(e.getMessage());
		}
	}
	

	@RequestMapping(value = "/RevenueSources", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView revenueSourcesView(Model viewMap, HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		Credential credential;
		SettingsModel settings;
		FilterModel filter;
		if (SessionService.checkAuthorization(session)) {
			credential = SessionService.getCredentials();
			filter = SessionService.getFilter();
			settings = SessionService.getUserSettings();
		} else {
			SessionService.redirectToLogin(session, request, response);
			return new ModelAndView("unavailable");
		}

		if (settings.getActiveProfile() == null) {
			//TODO: Make an informative view for when widgets don't have an active profile to get data from.
			return new ModelAndView ("unavailable");
		}

		return new ModelAndView("RevenueSources");
	}
	
	@RequestMapping(value = "/GrowingProblems", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView growingProblemsView(Model viewMap, HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		Credential credential;
		SettingsModel settings;
		FilterModel filter;
		if (SessionService.checkAuthorization(session)) {
			credential = SessionService.getCredentials();
			filter = SessionService.getFilter();
			settings = SessionService.getUserSettings();
		} else {
			SessionService.redirectToLogin(session, request, response);
			return new ModelAndView("unavailable");
		}

		if (settings.getActiveProfile() == null) {
			//TODO: Make an informative view for when widgets don't have an active profile to get data from.
			return new ModelAndView ("unavailable");
		}

		return new ModelAndView("GrowingProblems");
	}
	
	@RequestMapping(value = "/BoostPerformance", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView boostPerformanceView(Model viewMap, HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		Credential credential;
		SettingsModel settings;
		FilterModel filter;
		if (SessionService.checkAuthorization(session)) {
			credential = SessionService.getCredentials();
			filter = SessionService.getFilter();
			settings = SessionService.getUserSettings();
		} else {
			SessionService.redirectToLogin(session, request, response);
			return new ModelAndView("unavailable");
		}

		if (settings.getActiveProfile() == null) {
			//TODO: Make an informative view for when widgets don't have an active profile to get data from.
			return new ModelAndView ("unavailable");
		}

		return new ModelAndView("BoostPerformance");
	}


	@RequestMapping(value = "/WebsitePerformance", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView websitePerformanceView(Model viewMap, HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		Credential credential;
		SettingsModel settings;
		FilterModel filter;
		if (SessionService.checkAuthorization(session)) {
			credential = SessionService.getCredentials();
			filter = SessionService.getFilter();
			settings = SessionService.getUserSettings();
		} else {
			SessionService.redirectToLogin(session, request, response);
			return new ModelAndView("unavailable");
		}

		if (settings.getActiveProfile() == null) {
			//TODO: Make an informative view for when widgets don't have an active profile to get data from.
			return new ModelAndView ("unavailable");
		}


		//
		// Setting up the model.
		//

		WebsitePerformanceModel webPerform = SessionService.getModel(session, "webPerform", WebsitePerformanceModel.class);

		//If there is no model available, or if the active profile changed, create a new model.
		if ((webPerform == null) || !(settings.getActiveProfile().equals(webPerform.getActiveProfile()))) {
			webPerform = new WebsitePerformanceModel(this.SessionService, this.PagePerformanceService);
		}

		if (filter != null) {
			webPerform.setStartDate(filter.getActiveStartDate());
			webPerform.setEndDate(filter.getActiveEndDate());
		}

		/*
		 * Here's where we start making queries.
		 */
		webPerform.updateData();

		/*
		 * Save the updated model to the session and send it to the view.
		 */

		SessionService.saveModel(session, "websitePerformance", webPerform);
		viewMap.addAttribute("wpModel", webPerform);
		//viewMap.addAttribute("filterModel", filter);

		return new ModelAndView("WebsitePerformance");
	}

}
