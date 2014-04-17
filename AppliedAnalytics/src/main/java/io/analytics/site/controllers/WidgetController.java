
package io.analytics.site.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import io.analytics.domain.Widget;
import io.analytics.service.interfaces.ICoreReportingService;
import io.analytics.service.interfaces.IKeywordInsightService;
import io.analytics.service.interfaces.IOverviewService;
import io.analytics.service.interfaces.IPagePerfomanceService;
import io.analytics.service.interfaces.ISessionService;
import io.analytics.service.interfaces.IVisitorDeviceService;
import io.analytics.service.interfaces.IWidgetService;
import io.analytics.site.models.*;
import io.analytics.site.models.widgets.BoostPerformanceModel;
import io.analytics.site.models.widgets.DataForecastModel;
import io.analytics.site.models.widgets.KeyContributingFactorsModel;
import io.analytics.site.models.widgets.KeywordInsightModel;
import io.analytics.site.models.widgets.OverviewModel;
import io.analytics.site.models.widgets.TrafficSourceTrendsModel;
import io.analytics.site.models.widgets.VisitorClusteringModel;
import io.analytics.site.models.widgets.WebsitePerformanceModel;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

	private static final Logger logger = LoggerFactory.getLogger(WidgetController.class);

	@Autowired private ICoreReportingService CoreReportingService;
	@Autowired private ISessionService SessionService;
	@Autowired private IPagePerfomanceService PagePerformanceService;
	@Autowired private IKeywordInsightService KeywordInsightService;
	@Autowired private IWidgetService WidgetService;
	@Autowired private IOverviewService OverviewService;
	@Autowired private IVisitorDeviceService VisitorDeviceService;

	@RequestMapping(value = "/DataForecast", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView DataForecastView(Model viewMap, HttpServletRequest request, HttpServletResponse response, HttpSession session,	
			@RequestParam(value = "change", defaultValue = "none") String changePercentage,
			@RequestParam(value = "dimension", defaultValue = "none") String dimension,
			@RequestParam(value = "serialize", defaultValue = "0") boolean serialize) 
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
		//viewMap.addAttribute("hfModel", dataForecast);
		viewMap.addAttribute("widget", dataForecast);
		viewMap.addAttribute("filterModel", filter);

		/* Did we just request data only? */
		if(serialize) {
			viewMap.addAttribute("model", dataForecast);
			return new ModelAndView("serialize");
		}

		return new ModelAndView("widget", "view", "/WEB-INF/views/widgets/data-forecast.jsp");

	}


	@RequestMapping(value = "/KeyContributingFactors", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView keyContributingFactorsView(Model viewMap, HttpServletRequest request, HttpServletResponse response, HttpSession session) {

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
		KeyContributingFactorsModel keyContributingFactors = SessionService.getModel(session, "keyContributingFactors", KeyContributingFactorsModel.class);


		//If there is no model available, or if the active profile changed, create a new model.
		if ((keyContributingFactors == null) || !(settings.getActiveProfile().equals(keyContributingFactors.getActiveProfile()))) {
			keyContributingFactors = new KeyContributingFactorsModel();
		}

		//TODO: We may need to make setStartDate and setEndDate abstract methods in the WidgetModel class.
		/*if (filter != null) {
			growingProblems.setStartDate(filter.getActiveStartDate());
			growingProblems.setEndDate(filter.getActiveEndDate());
		}*/

		viewMap.addAttribute("widget", keyContributingFactors);


		return new ModelAndView("widget", "view", "/WEB-INF/views/widgets/key-contributing-factors.jsp");

	} 



	//	@RequestMapping(value = "/GrowingProblems", method = {RequestMethod.POST, RequestMethod.GET})
	//	public ModelAndView growingProblemsView(Model viewMap, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
	//
	//		Credential credential;
	//		SettingsModel settings;
	//		FilterModel filter;
	//		if (SessionService.checkAuthorization(session)) {
	//			credential = SessionService.getCredentials();
	//			filter = SessionService.getFilter();
	//			settings = SessionService.getUserSettings();
	//		} else {
	//			SessionService.redirectToLogin(session, request, response);
	//			return new ModelAndView("unavailable");
	//		}
	//
	//		if (settings.getActiveProfile() == null) {
	//			//TODO: Make an informative view for when widgets don't have an active profile to get data from.
	//			return new ModelAndView ("unavailable");
	//		}
	//		
	//		GrowingProblemsModel growingProblems = SessionService.getModel(session, "growingProblems", GrowingProblemsModel.class);
	//		
	//
	//		//If there is no model available, or if the active profile changed, create a new model.
	//		if ((growingProblems == null) || !(settings.getActiveProfile().equals(growingProblems.getActiveProfile()))) {
	//			growingProblems = new GrowingProblemsModel(SessionService, CoreReportingService);
	//		}
	//
	//		//TODO: We may need to make setStartDate and setEndDate abstract methods in the WidgetModel class.
	//		/*if (filter != null) {
	//			growingProblems.setStartDate(filter.getActiveStartDate());
	//			growingProblems.setEndDate(filter.getActiveEndDate());
	//		}*/
	//		
	//		viewMap.addAttribute("widget", growingProblems);
	//
	//		return new ModelAndView("widget", "view", "/WEB-INF/views/widgets/growing-problems.jsp");
	//	}

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

		BoostPerformanceModel boostPerformance = SessionService.getModel(session, "boostPerformance", BoostPerformanceModel.class);

		//If there is no model available, or if the active profile changed, create a new model.
		if ((boostPerformance == null) || !(settings.getActiveProfile().equals(boostPerformance.getActiveProfile()))) {
			boostPerformance = new BoostPerformanceModel();
		}

		/*if (filter != null) {
			boostPerformance.setStartDate(filter.getActiveStartDate());
			boostPerformance.setEndDate(filter.getActiveEndDate());
		}*/

		viewMap.addAttribute("widget", boostPerformance);

		return new ModelAndView("widget", "view", "/WEB-INF/views/widgets/boost-performance.jsp");
	}


	@RequestMapping(value = "/WebsitePerformance", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView websitePerformanceView(Model viewMap, HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(value = "serialize", defaultValue = "none") String serialize) {

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
		viewMap.addAttribute("widget", webPerform);
		//viewMap.addAttribute("filterModel", filter);

		if (!serialize.equals("none")) {
			viewMap.addAttribute("model", webPerform);
			return new ModelAndView("serialize");
		}
		return new ModelAndView("widget", "view", "/WEB-INF/views/widgets/website-performance.jsp");
	}

		@RequestMapping(value = "/Overview", method = {RequestMethod.POST, RequestMethod.GET})
		public ModelAndView overviewView(Model viewMap, HttpServletRequest request, HttpServletResponse response, HttpSession session,
				@RequestParam(value = "serialize", defaultValue = "none") String serialize) {

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

			OverviewModel overview = SessionService.getModel(session, "overview", OverviewModel.class);

			//If there is no model available, or if the active profile changed, create a new model.
			if ((overview == null) || !(settings.getActiveProfile().equals(overview.getActiveProfile()))) {
				overview = new OverviewModel(this.SessionService, this.OverviewService, this.VisitorDeviceService);
			}

			if (filter != null) {
				overview.setStartDate(filter.getActiveStartDate());
				overview.setEndDate(filter.getActiveEndDate());
			}

			/*
			 * Here's where we start making queries.
			 */
			overview.updateData();

			/*
			 * Save the updated model to the session and send it to the view.
			 */

			SessionService.saveModel(session, "overview", overview);
			viewMap.addAttribute("oModel", overview);
			viewMap.addAttribute("widget", overview);
			//viewMap.addAttribute("filterModel", filter);

			if (!serialize.equals("none")) {
				viewMap.addAttribute("model", overview);
				return new ModelAndView("serialize");
			}
			return new ModelAndView("widget", "view", "/WEB-INF/views/widgets/overview.jsp");
		}

		@RequestMapping(value = "/KeywordInsight", method = {RequestMethod.POST, RequestMethod.GET})
		public ModelAndView keywordInsightView(Model viewMap, HttpServletRequest request, HttpServletResponse response, HttpSession session,
				@RequestParam(value = "serialize", defaultValue = "0") boolean serialize) {

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

			KeywordInsightModel keyInsight = SessionService.getModel(session, "keyInsight", KeywordInsightModel.class);

			//If there is no model available, or if the active profile changed, create a new model.
			if ((keyInsight == null) || !(settings.getActiveProfile().equals(keyInsight.getActiveProfile()))) {
				keyInsight = new KeywordInsightModel(this.SessionService, this.KeywordInsightService);
			}

			if (filter != null) {
				keyInsight.setStartDate(filter.getActiveStartDate());
				keyInsight.setEndDate(filter.getActiveEndDate());
			}

			// make queries.
			keyInsight.updateData();

			// Save the updated model to the session and send it to the view.
			SessionService.saveModel(session, "keywordInsight", keyInsight);
			viewMap.addAttribute("kiModel", keyInsight);
			viewMap.addAttribute("widget", keyInsight);
			//viewMap.addAttribute("filterModel", filter);

			if (serialize) {
				viewMap.addAttribute("model", keyInsight);
				return new ModelAndView("serialize");
			}

			return new ModelAndView("widget", "view", "/WEB-INF/views/widgets/keyword-insight.jsp");
		}



		// This will probably be changed to be a generic widget save function
		/*@RequestMapping(value = "/saveDataForecast", method = RequestMethod.POST)
		private void saveDataForecastSettings(@RequestParam("widgetId") int widgetId, 
											  @RequestParam("data") String inputData)
		{
			try {


			} catch(Exception e) {
				logger.info("Could not save DataForecast data");
				logger.info(e.getMessage());
			}
		}*/


		@ResponseBody
		@RequestMapping(value = "/removeWidget", method = RequestMethod.POST)
		private String removeWidget(@RequestParam("widgetId") int widgetId)
		{

			try {
				WidgetService.deleteWidget(widgetId);
				return "true";

			} catch(Exception e) {
				logger.info("Could not remove widget");
				logger.info(e.getMessage());
			}

			return null;
		}


		@ResponseBody
		@RequestMapping(value = "/addWidget", method = RequestMethod.POST)
		private String addWidget(@RequestParam("widgetTypeId") int widgetTypeId,
				@RequestParam("dashboardId") int dashboardId,
				@RequestParam(value = "priority", defaultValue = "0") int priority)
		{
			JSONObject result = new JSONObject();

			try {
				Widget w = new Widget(-1, widgetTypeId);
				w.setDashboardId(dashboardId);
				w.setPriority(priority);

				int newId = WidgetService.addNewWidget(w);
				result.put("widgetId", newId);

			} catch(Exception e) {
				logger.info("Could not add new Widget");
				logger.info(e.getMessage());
			}

			return result.toString();
		}


		@ResponseBody
		@RequestMapping(value = "/updateWidgetPosition", method= RequestMethod.POST)
		private String updateWidget(@RequestParam(value = "widgets", defaultValue = "") String widgetsJSON)
		{
			try {

				if (!widgetsJSON.isEmpty()) {
					//TODO: DeStringify widgetsJSON and traverse it, 
					//create a widget object for each instance and call the service.
					JSONArray widgets = new JSONArray(widgetsJSON);

					for(int i = 0; i < widgets.length(); i++) {
						JSONObject o = widgets.getJSONObject(i);
						Widget w = WidgetService.getWidgetById((Integer)o.get("widgetId"));
						w.setPriority((Integer)o.get("pos"));
						WidgetService.updateWidget(w);
					}
				}

			} catch(Exception e) {
				logger.info("Could not update Widget");
				logger.info(e.getMessage());
			}

			return null;
		}


		/**
		 * TODO: This need to be accessed by the id in the database.
		 * 
		 * @param viewMap
		 * @param request
		 * @param response
		 * @param session
		 * @param serialize
		 * @return
		 */
		@RequestMapping(value = "/widgets/traffic-source-trends", method = {RequestMethod.GET})
		public ModelAndView trafficSourceTrends(Model viewMap, HttpServletRequest request, HttpServletResponse response, HttpSession session,
				@RequestParam(value = "serialize", defaultValue = "0") boolean serialize) {

			//TODO: This needs to go into an Aspect.
			if (!SessionService.checkAuthorization(session)) {
				SessionService.redirectToLogin(session, request, response);
				return new ModelAndView("unavailable");
			}

			TrafficSourceTrendsModel model = new TrafficSourceTrendsModel(SessionService, CoreReportingService);
			viewMap.addAttribute("widget", model);
			return new ModelAndView("widget", "view", "/WEB-INF/views/widgets/traffic-source-trends.jsp");
		}

		@RequestMapping(value = "/widgets/traffic-source-trends/data", method = {RequestMethod.GET})
		public ModelAndView trafficSourceTrendsData(Model viewMap, HttpServletRequest request, HttpServletResponse response, HttpSession session) {

			//TODO: This needs to go into an Aspect.
			if (!SessionService.checkAuthorization(session)) {
				SessionService.redirectToLogin(session, request, response);
				return new ModelAndView("unavailable");
			}

			TrafficSourceTrendsModel model = new TrafficSourceTrendsModel(SessionService, CoreReportingService);
			model.getTrafficSourceDataList();
			viewMap.addAttribute("data", model.getJSONSerialization());
			return new ModelAndView("plaintext");
		}

		@RequestMapping(value = "/widgets/visitor-clustering/data", method = {RequestMethod.GET})
		public ModelAndView visitorClusteringData(Model viewMap, HttpServletRequest request, HttpServletResponse response, HttpSession session) {

			if (!SessionService.checkAuthorization(session)) {
				SessionService.redirectToLogin(session, request, response);
				return new ModelAndView("unavailable");
			}

			VisitorClusteringModel model = new VisitorClusteringModel(SessionService, CoreReportingService);
			model.updateData();

			viewMap.addAttribute("data", model.getJSONSerialization());
			return new ModelAndView("plaintext");
		}

	}
