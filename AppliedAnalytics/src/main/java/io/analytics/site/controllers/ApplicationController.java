package io.analytics.site.controllers;

import io.analytics.aspect.HeaderFooter;
import io.analytics.aspect.SidePanel;
import io.analytics.domain.Account;
import io.analytics.domain.Dashboard;
import io.analytics.domain.User;
import io.analytics.enums.HeaderType;
import io.analytics.service.interfaces.IDashboardService;
import io.analytics.service.interfaces.ISessionService;
import io.analytics.service.interfaces.IUserService;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;


@Controller
@SessionAttributes({"ACCOUNT_ID", "USER_ID", "DASHBOARD_ID"})
public class ApplicationController {
	
	private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);
	
	/* Services */
	@Autowired private ISessionService SessionService;
	@Autowired private IDashboardService DashboardService;
	@Autowired private IUserService UserService;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@HeaderFooter(HeaderType.APPLICATION)
	@SidePanel(animate = true)
	@RequestMapping(value = "/application", method = RequestMethod.GET)
	public ModelAndView getDashboard(Locale locale, Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		
		//TODO: Obtain Account and Dashboard IDs.
		
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
	
	/**
	 * Create a new dashboard
	 * 
	 * @param accountId - current account Id
	 * @param userId - current User Id
	 * @param name - Name given to the newly created dashboard
	 */
	@RequestMapping(value = "/application/addNewDashboard", method = RequestMethod.POST)
	private void addNewDashboard(@ModelAttribute("ACCOUNT_ID") int accountId,
			  					 @ModelAttribute("USER_ID") int userId,
			  					 @RequestParam(value = "name", defaultValue = "") String name) 
	{ 
		try {
			
			User user = this.UserService.getUserById(userId);
			this.DashboardService.addNewDashboard(user, accountId, name);
			
		} catch(Exception e) {
			logger.info("Could not add new dashboard");
			logger.info(e.getMessage());
		}
	}
	
	
	/**
	 * Delete a dashboard. Could delete any dashboard in account.
	 * 
	 * 
	 * @param userId - current User Id
	 * @param dashboardId - Id of dashboard to be deleted.
	 */
	@RequestMapping(value = "/application/deleteDashboard", method = RequestMethod.POST)
	private void deleteDashboard (@ModelAttribute("USER_ID") int userId,
								  @RequestParam("dashboardId") int dashboardId)
	{
		try {
			
			User user = this.UserService.getUserById(userId);
			this.DashboardService.deleteDashboard(user, dashboardId);
			
		} catch (Exception e) {
			logger.info("Could not delete dashboard");
			logger.info(e.getMessage());
		}
	}
	
	
	
	/**
	 * Update Widget Position on the Dashboard.
	 * 
	 * @param accountId - current account Id
	 * @param userId - current User Id
	 * @param dashboardId - current dashboard Id
	 */
	@RequestMapping(value = "/application/layout", method = RequestMethod.POST)
	private void updateWidgetPosition(@ModelAttribute("ACCOUNT_ID") int accountId,
									  @ModelAttribute("USER_ID") int userId,
									  @ModelAttribute("DASHBOARD_ID") int dashboardId) 
	{
		try {
			// TODO: Need some data type representing Widget positions
			User user = this.UserService.getUserById(userId);
			this.DashboardService.updateDashboardWidgetLayout(user, dashboardId);
			
		} catch(Exception e) {
			logger.info("Could not update Widget Position");
			logger.info(e.getMessage());
		}
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
	@RequestMapping(value = "/application/alerts", method = RequestMethod.GET)
	public ModelAndView suggestions(Model model) {
		return new ModelAndView("application/alerts");
	}

	
}
