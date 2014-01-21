package io.analytics.site.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import io.analytics.aspect.HeaderFooter;
import io.analytics.enums.HeaderType;
import io.analytics.forms.PersonalAccountInfo;
import io.analytics.service.ISessionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"personalForm"})
public class AccountController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired PasswordEncoder passwordEncoder;
	@Autowired ISessionService SessionService;
	
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
				response.sendRedirect("/appliedanalytics/accounts/newaccount?google=1");
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
	public ModelAndView newAccountPage(Model model, HttpServletResponse response, HttpSession session,
			@RequestParam(value = "google", defaultValue = "0") boolean googlePage,
			@RequestParam(value = "personal", defaultValue = "0") boolean personalPage,
			@RequestParam(value = "maingoal", defaultValue = "0") boolean maingoalPage)
	{
		if (googlePage){
			return new ModelAndView("account/google-info");
		}
		else if (personalPage) {
			return new ModelAndView("account/personal-info");
		}
		else if (maingoalPage) {
			return new ModelAndView("account/maingoal");
		}
		else
			return new ModelAndView("unavilable");
	}
	
	/**
	 * 
	 * @param model
	 * @param response
	 * @param session
	 */
	@RequestMapping(value = "/accounts/processgoogleinfo", method = RequestMethod.POST)
	public void processGooglePage(Model model, HttpServletResponse response, HttpSession session)
	{
		
		try {
			//TODO: validate data. If valid, put in session. 
			response.sendRedirect("/appliedanalytics/accounts/newaccount?personal=1");
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
	 */
	@RequestMapping(value = "/accounts/processpersonalinfo", method = RequestMethod.POST)
	public void processPersonalPage(@ModelAttribute("personalForm") @Valid PersonalAccountInfo accountInfo, BindingResult result, Model model, HttpServletResponse response) 
	{

		model.addAttribute("personalForm", accountInfo); //store the old form.
		
		try {
			//TODO: validate data. If valid, put in session.
			if (result.hasErrors())
				response.sendRedirect("/appliedanalytics/accounts/newaccount?personal=1");
			else
				response.sendRedirect("/appliedanalytics/accounts/newaccount?maingoal=1");
			
		}
		catch(IOException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}	
	}
	
	
	@ModelAttribute("personalForm")
	public PersonalAccountInfo getPersonalForm() {
		return new PersonalAccountInfo();
	}
	
	/**
	 * 
	 * @param model
	 * @param response
	 * @param session
	 */
	@RequestMapping(value = "/accounts/processgoalinfo", method = RequestMethod.POST)
	public void processGoalPage(Model model, HttpServletResponse response, HttpSession session)
	{
		try {
			//TODO: validate data. If valid, put in session. 
			// Else direct user back to page to correct with appropriate error message.
			
			//TODO: call user service and pass in all data in session to create a new account!
			//TODO: store new accountID and userID; find someway to zero out all of the other session data added.
			//TODO: Database should create a new dashboard page upon creating a new account. Get new dashboard ID
			// and direct to that dashboard!
			
			// redirect user to their new dashboard page!
			response.sendRedirect("/appliedanalytics/application/");
		}
		catch(IOException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
	}
	


}
