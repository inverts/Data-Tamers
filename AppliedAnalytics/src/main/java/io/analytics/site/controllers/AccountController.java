package io.analytics.site.controllers;

import java.io.IOException;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import io.analytics.aspect.HeaderFooter;
import io.analytics.domain.GoogleUserData;
import io.analytics.domain.User;
import io.analytics.enums.HeaderType;
import io.analytics.forms.NewAccountForm;
import io.analytics.service.interfaces.ISessionService;
import io.analytics.service.interfaces.IUserService;
import io.analytics.site.models.SettingsModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"accountForm", "validation", "googleAuthorization"})
public class AccountController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired ISessionService SessionService;
	@Autowired IUserService UserService;
	
	// some kind of user service.
	
	/**
	 * Starting page of the registration process.
	 * @param model
	 * @return terms page.
	 */
	@HeaderFooter(HeaderType.SIMPLE)
	@RequestMapping(value = "/accounts/getstarted", method = RequestMethod.GET)
	public ModelAndView start(Model model,
			@RequestParam(value = "terms-and-conditions", defaultValue = "0") boolean terms)
	{
		return (terms) ? new ModelAndView("account/terms") : new ModelAndView("account/start");
	}
	
	/**
	 * processes the accept terms request and if valid, 
	 * forwards to the google information page.
	 * @param model
	 * @param response
	 * @param accepted
	 */
	@RequestMapping(value = "/accounts/acceptterms", method = RequestMethod.POST)
	public void acceptTerms(Model model, HttpServletResponse response, HttpSession session,
			@RequestParam("accept_terms") boolean accepted)
	{
		try {
			
			if (!accepted) {
				// the user somehow bypassed the terms without accepting. Kill the session and start over.
				session.invalidate();
				response.sendRedirect("/appliedanalytics/");
			}
			else {
				session.setAttribute("terms", true);
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
	 * @param maingoalPage - page to input information about goals.
	 * @return
	 */
	@HeaderFooter(HeaderType.SIMPLE)
	@RequestMapping(value = "/accounts/newaccount", method = RequestMethod.GET)
	public ModelAndView newAccountPage(@ModelAttribute("googleAuthorization") String googleAuthorization, 
									   @ModelAttribute("accountForm") NewAccountForm form, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session, 
									   @RequestParam(value = "gaAuth", defaultValue = "0") boolean googleAuth)
	{
		ModelAndView page = new ModelAndView("account/personal-info");
		
		if (googleAuth) {
			if (SessionService.redirectToLogin(session, request, response))
				return null;
			
			return new ModelAndView("redirect:/accounts/newaccount");
		}
		
		// Flag indicating that user has selected to sign up using google.
		if (googleAuthorization.equals("success") && SessionService.checkAuthorization(session)) {
			
			SettingsModel settings = null;

			settings = (SettingsModel) session.getAttribute("settings");
			model.addAttribute("settings", settings);
			GoogleUserData googleData = SessionService.getUserSettings().getGoogleUserData();
			
			// Only pre-populate fields that the user has no input in.
			if (form.getFirstname().isEmpty())
				form.setFirstname(googleData.getName());
			if (form.getLastname().isEmpty())
				form.setLastname(googleData.getGiven_name());
			if (form.getEmail().isEmpty()) {
				form.setEmail(googleData.getEmail());
				form.setConfirmEmail(googleData.getEmail());
			}
			
			model.addAttribute("accountForm", form);
			//String googleEmail = googleData.getEmail();
			page.addObject("googleAccountName", googleData.getEmail());
			page.addObject("accountForm", form);
			
			// let the page know google authenticated successfully.
			page.addObject("googleSuccess", true);
		}
		else if (googleAuthorization.equals("fail"))
				page.addObject("googleFail", true);	
		
		return page;	

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
	 * Validates the 
	 * @param model
	 * @param response
	 * @param session
	 */
	@RequestMapping(value = "/accounts/ProcessNewAccountInfo", method = RequestMethod.POST)
	public String processAccountForm(@Valid @ModelAttribute("accountForm") NewAccountForm form, BindingResult result, 
											@RequestParam(value = "googleAuth", defaultValue = "0") boolean googleAuth, 
											HttpSession session, Model model) 
	{
		if (googleAuth) {
			return "redirect:/accounts/newaccount?gaAuth=1";
		}
		else if (result.hasErrors()) {
			// Don't retain passwords
			form.setPassword("");
			form.setConfirmPassword("");
			model.addAttribute("validation", result);
			return "redirect:/accounts/newaccount";
		}
		else {
			// TODO: Send form data to service for upload
			User u = new User(-1);
			u.setUsername(form.getUsername());
			u.setEmail(form.getEmail());
			u.setPassword(form.getPassword());
			u.setFirstName(form.getFirstname());
			u.setLastName(form.getLastname());
			u.setJoinDate(Calendar.getInstance());
			
			boolean success = UserService.addNewUser(u) != null;
			
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
	


}
