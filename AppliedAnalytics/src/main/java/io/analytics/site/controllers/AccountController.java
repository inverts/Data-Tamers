package io.analytics.site.controllers;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import io.analytics.aspect.HeaderFooter;
import io.analytics.domain.Account;
import io.analytics.domain.Filter;
import io.analytics.domain.GoogleAccount;
import io.analytics.domain.GoogleUserData;
import io.analytics.domain.User;
import io.analytics.domain.WidgetType;
import io.analytics.enums.HeaderType;
import io.analytics.forms.NewAccountForm;
import io.analytics.service.interfaces.IAccountService;
import io.analytics.service.interfaces.IDashboardService;
import io.analytics.service.interfaces.IFilterService;
import io.analytics.service.interfaces.IGoogleAccountService;
import io.analytics.service.interfaces.ISessionService;
import io.analytics.service.interfaces.IUserService;
import io.analytics.service.interfaces.IWidgetService;
import io.analytics.site.models.FilterModel;
import io.analytics.site.models.SettingsModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.analytics.model.Profile;

@Controller
@SessionAttributes({"accountForm", "validation", "googleAuthorization", "acceptTerms"})
public class AccountController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired ISessionService SessionService;
	@Autowired IUserService UserService;
	@Autowired IGoogleAccountService GoogleAccountService;
	@Autowired IAccountService AccountService;
	@Autowired IFilterService FilterService;
	@Autowired IDashboardService DashboardService;
	@Autowired IWidgetService WidgetService;
	
	
	/**
	 * Account registration page
	 * @param model
	 * @return new accounts page
	 */
	@HeaderFooter(HeaderType.SIMPLE)
	@RequestMapping(value = "/accounts/getstarted", method = RequestMethod.GET)
	public ModelAndView start(Model model)
	{
		return new ModelAndView("account/terms"); 
	}
	
	
	
	/**
	 * processes the accept terms request and if valid, 
	 * forwards to the google information page.
	 * @param model
	 * @param response
	 * @param accepted
	 */
	@RequestMapping(value = "/accounts/acceptterms", method = RequestMethod.POST)
	private void acceptTerms(Model model, HttpServletResponse response, HttpSession session,
							@RequestParam("accept_terms") boolean accepted)
	{
		try {
			
			if (!accepted) {
				// the user somehow bypassed the terms without accepting. Kill the session and start over.
				session.invalidate();
				response.sendRedirect("/appliedanalytics/");
			}
			else {
				model.addAttribute("acceptTerms", accepted);
				response.sendRedirect("/appliedanalytics/accounts/newaccount");
			}
		}
		catch(IOException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
	
	}
	
	/**
	 * 
	 * @param model
	 * @param response
	 * @param session
	 * @param googlePage - page to input google account information.
	 * @param personalPage - page to input personal information.
	 * @return
	 */
	@HeaderFooter(HeaderType.SIMPLE)
	@RequestMapping(value = "/accounts/newaccount", method = RequestMethod.GET)
	public ModelAndView newAccountPage(@ModelAttribute("googleAuthorization") String googleAuthorization, 
							     	   @ModelAttribute("accountForm") NewAccountForm form, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session, 
							     	   @RequestParam(value = "gaAuth", defaultValue = "0") boolean googleAuth,
							     	   @ModelAttribute("acceptTerms") boolean acceptedTerms)
	{
		try {
		if (!acceptedTerms)
			response.sendRedirect("getstarted");
		} catch(Exception e) {
			
		}
		
		ModelAndView formpage = new ModelAndView("account/new-account");
		
		
		// Need to authorize google analytics
		if (googleAuth) {
				if (SessionService.redirectToLogin(session, request, response))
					return null;
				
				return new ModelAndView("accounts/newaccount");
		}
		
		// Flag indicating that user has selected to sign up using google.
		if (googleAuthorization.equals("success") && SessionService.checkAuthorization(session)) {
			
			SettingsModel settings = null;

			settings = (SettingsModel) session.getAttribute("settings");
			model.addAttribute("settings", settings);
			GoogleUserData googleData = SessionService.getUserSettings().getGoogleUserData();
			
			// Only pre-populate fields that the user has no input in.
			if (form.getFirstname().isEmpty())
				form.setFirstname(googleData.getGiven_name());
			if (form.getLastname().isEmpty())
				form.setLastname(googleData.getFamily_name());
			if (form.getEmail().isEmpty()) {
				form.setEmail(googleData.getEmail());
				form.setConfirmEmail(googleData.getEmail());
			}
			
			model.addAttribute("accountForm", form);
			//String googleEmail = googleData.getEmail();
			formpage.addObject("googleAccountName", googleData.getEmail());
			formpage.addObject("accountForm", form);
			
			// let the page know google authenticated successfully.
			formpage.addObject("googleSuccess", true);
		}
		else if (googleAuthorization.equals("fail"))
			formpage.addObject("googleFail", true);	
		
		
		return formpage;
	}
	
	@ResponseBody
	@RequestMapping(value = "/accounts/accountform", method = RequestMethod.POST)
	private ModelAndView accountForm( Model model, HttpSession session, @ModelAttribute("googleAuthorization") String googleAuthorization,
									  @ModelAttribute("accountForm") NewAccountForm form, HttpServletRequest request) {
		
		ModelAndView newAccountForm = new ModelAndView("newAccountForm");

		if (googleAuthorization.equals("success") && SessionService.checkAuthorization(session))
			newAccountForm.addObject("googleSuccess", true);
		else if (googleAuthorization.equals("fail"))
			newAccountForm.addObject("googleFail", true);

		Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
		if (inputFlashMap != null)
			newAccountForm.addObject("validation", inputFlashMap.get("validation"));
		
		return newAccountForm;
	}
	
	/**
	 * Handler used to authenticate google then return to the new account page.
	 * Has to use a handler since the google authentication servlet does not
	 * preserve URL parameters.
	 * 
	 * @param model
	 * @param response
	 * @param session
	 */
	@RequestMapping(value = "/accounts/GoogleAuthenticateHandler", method = RequestMethod.POST)
	public String processGooglePage(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(value = "login", defaultValue = "0") boolean gaLogin)
	{
		if (gaLogin && SessionService.redirectToLogin(session, request, response))
			return null;
		
		return "redirect:/accounts/newaccount";
	}
	
	
	
	/**
	 * Validates the new account information and creates several database entities
	 * and relationships for this user.
	 * 
	 * @param model
	 * @param response
	 * @param session
	 */
	@RequestMapping(value = "/accounts/ProcessNewAccountInfo", method = RequestMethod.POST)
	public String processAccountForm(@Valid @ModelAttribute("accountForm") NewAccountForm form, BindingResult result,
											RedirectAttributes redirect,
											@RequestParam(value = "googleAuth", defaultValue = "0") boolean googleAuth, 
											HttpSession session, HttpServletRequest request) 
	{
		if (googleAuth) {
			return "redirect:/accounts/newaccount?gaAuth=1";
		}
		else if (result.hasErrors()) {
			// Don't retain passwords
			form.setPassword("");
			form.setConfirmPassword("");
			redirect.addFlashAttribute("validation", result);
			return "redirect:/accounts/newaccount";
		}
		else {
			
			// TODO: Fill in with the user's specified goal metric.
			String goalMetric = "ga:visits";
			
			// TODO: Send form data to service for upload
			User u = new User(-1);
			u.setUsername(form.getUsername());
			u.setEmail(form.getEmail());
			u.setPassword(form.getPassword());
			u.setFirstName(form.getFirstname());
			u.setLastName(form.getLastname());
			u.setJoinDate(Calendar.getInstance());
			
			/* Add User to database. */
			u = UserService.addNewUser(u);
			
			/* TODO: Handle more appropriately with messages to the user.
			 * This indicates that the user details didn't meet the constraints of the DB (most likely),
			 * or that there was a DB connection error.
			 */
			if (u == null)
				return "redirect:/accounts/newaccount";
			
			/* Add Filter to database. */
			Profile activeProfile = SessionService.getUserSettings().getActiveProfile();
			Filter f = new Filter(0);
			FilterModel fm = SessionService.getFilter();
			Calendar start = Calendar.getInstance();
			start.setTime(fm.getActiveStartDate());
			Calendar end = Calendar.getInstance();
			end.setTime(fm.getActiveEndDate());
			f.setStartDate(start);
			f.setEndDate(end);
			f.setGoogleProfileId(activeProfile.getId());
			f.setInterestMetric(goalMetric);
			f = FilterService.addNewFilter(f);
			if (f == null)
				return "redirect:/accounts/newaccount";
			int filterId = f.getId();
			
			/* Add Account to database. */
			Account a = AccountService.createAndSaveAccount(u.getId(), filterId);
			if (a == null)
				return "redirect:/accounts/newaccount";
			int accountId = a.getId();

			/* Add GoogleAccount to database. */
			Credential c = SessionService.getCredentials();
			if (c == null || c.getRefreshToken() == null)
				return "redirect:/accounts/newaccount";
			GoogleAccount ga = new GoogleAccount(0);
			ga.setActiveRefreshToken(c.getRefreshToken());
			ga.setOwnerAccountId(accountId);
			ga = GoogleAccountService.addNewGoogleAccount(ga);
			GoogleAccountService.addGoogleAccountToAccount(ga.getId(), accountId);
			
			/* Add a default dashboard. */
			int dashboardId = DashboardService.addNewDashboard(accountId, null, "Default Dashboard");
			List<WidgetType> widgetTypes = WidgetService.getAllWidgetTypes();
			for (WidgetType wt : widgetTypes) {
				WidgetService.addNewWidget(null, wt.getId(), dashboardId, 0);
			}
			
			/* We've made it! */
			return "redirect:/application";
			
		}
	}
	
	
	@ModelAttribute("accountForm")
	public NewAccountForm getPersonalForm() {
		return new NewAccountForm();
	}
	
	@ModelAttribute("googleAuthorization")
	public String defaultAuthorization() {
		return "none";
	}
	
	@ModelAttribute("acceptTerms")
	public boolean acceptTerms() {
		return false;
	}


}
