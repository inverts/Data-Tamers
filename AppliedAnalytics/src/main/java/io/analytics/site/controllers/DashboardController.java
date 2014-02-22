package io.analytics.site.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import io.analytics.aspect.HeaderFooter;
import io.analytics.aspect.SidePanel;
import io.analytics.domain.Account;
import io.analytics.domain.User;
import io.analytics.enums.HeaderType;
import io.analytics.forms.DashboardForm;
import io.analytics.service.interfaces.IAccountService;
import io.analytics.service.interfaces.IDashboardService;
import io.analytics.service.interfaces.ISessionService;
import io.analytics.service.interfaces.IUserService;
import io.analytics.site.models.FilterModel;
import io.analytics.site.models.SessionModel;
import io.analytics.site.models.SessionModel.CorruptedSessionException;
import io.analytics.site.models.SettingsModel;
import io.analytics.site.models.SidePanelModel;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"ACCOUNT_ID", "USER_ID", "DASHBOARD_ID"})
public class DashboardController {
	
private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);
	
	/* Services */
	@Autowired private IAccountService AccountService;
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
		
		SessionModel sessionModel = new SessionModel(session);
		
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
		
		try {
			if (sessionModel.getUser() == null) {
				User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				sessionModel.setUser(user);
			}
			if (sessionModel.getAccount() == null) {
				List<Account> accounts = AccountService.getAccountsOwnedByUser(sessionModel.getUser().getId());
				if (accounts.isEmpty()) {
					SessionService.redirectToRegularLogin(session, "?errors=1&noaccount=1", response);
					return null;
				}
				//Get the first account owned by the user - this could change in the future.
				sessionModel.setAccount(accounts.get(0));
			}
			
		} catch (CorruptedSessionException e1) {
			e1.printStackTrace();
			SessionService.redirectToLogin(session, request, response);
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

		SidePanelModel sidePanelModel = new SidePanelModel(this.DashboardService, sessionModel);
		try {
			sidePanelModel.generateDashboardLinks();
		} catch (CorruptedSessionException e) {
			SessionService.redirectToLogin(session, request, response);
		}

		
		Map<String, Object> Sidepanel = new HashMap<String, Object>();
		
		Sidepanel.put("path", "/WEB-INF/views/includes/sidepanel.jsp");
		Sidepanel.put("animate", false);
		Sidepanel.put("model", sidePanelModel);

		model.addAttribute("SIDEPANEL", Sidepanel);
		
		return new ModelAndView("application/dashboard");
	}
	
	@RequestMapping(value = "/addDashboard", method = RequestMethod.POST)
	public ModelAndView showAddDashboardForm() {
		return new ModelAndView("addDashboard");
	}
	
	@ModelAttribute("dashboardForm")
	public DashboardForm getDashboardForm() {
		return new DashboardForm();
	}
	
	/**
	 * Create a new dashboard
	 * 
	 * @param accountId - current account Id
	 * @param userId - current User Id
	 * @param name - Name given to the newly created dashboard
	 */
	@ResponseBody
	@RequestMapping(value = "/application/createDashboard", method = RequestMethod.POST)
	private String createDashboard(@ModelAttribute("ACCOUNT_ID") int accountId,
			  					   @ModelAttribute("USER_ID") int userId,
			  					   @ModelAttribute("dashboardForm")DashboardForm form,
			  					   BindingResult result,
			  					   @RequestParam(value = "name", defaultValue = "") String name) 
	{ 
		
		JSONObject dashboardData = new JSONObject();
		
		try {

			if (result.hasErrors()) {
				//TODO: Do something
			}
			
			//TODO: Change account number to active account number.
			int newDashboardId = this.DashboardService.addNewDashboard(1, null, name);
			String url = "application/" + newDashboardId;
			
			dashboardData.put("id", newDashboardId);
			dashboardData.put("name", name);
			dashboardData.put("url", url);

			
		} catch(Exception e) {
			logger.info("Could not add new dashboard");
			logger.info(e.getMessage());
		}
		
		return dashboardData.toString();
	}
	
	
	/**
	 * Delete a dashboard. Could delete any dashboard in account.
	 * 
	 * 
	 * @param userId - current User Id
	 * @param dashboardId - Id of dashboard to be deleted.
	 */
	@ResponseBody
	@RequestMapping(value = "/application/deleteDashboard", method = RequestMethod.POST)
	private String removeDashboard (@RequestParam("dashboardId") int dashboardId,
								  HttpSession session, HttpServletRequest request, HttpServletResponse response)
	{
		
		SessionModel sessionModel = new SessionModel(session);
		
		JSONObject result = new JSONObject();
		try {
			User user = sessionModel.getUser();
			this.DashboardService.deleteDashboard(user, dashboardId);
			
			result.put("dashboardId", dashboardId);
			
		} catch (CorruptedSessionException e) {
			e.printStackTrace();
			SessionService.redirectToLogin(session, request, response);
		} catch (JSONException e) {
			logger.info("Could not delete dashboard");
			e.printStackTrace();
		}
		
		return result.toString();
	}
	
	/* Spoof Attributes */
	@ModelAttribute("USER_ID")
	public int getUserId() {
		return 111111;
	}
	
	@ModelAttribute("ACCOUNT_ID")
	public int getAccountId() {
		return 1;
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
	
	

}
