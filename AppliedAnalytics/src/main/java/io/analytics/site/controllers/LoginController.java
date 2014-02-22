package io.analytics.site.controllers;

import io.analytics.aspect.HeaderFooter;
import io.analytics.enums.HeaderType;
import io.analytics.forms.LoginForm;
import io.analytics.security.SiteAuthenticationProvider;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"loginForm", "validation"})
public class LoginController {
	
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@HeaderFooter(HeaderType.SIMPLE)
	@RequestMapping(value = "/login", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView loginPage(Model model, HttpSession session, HttpServletResponse response, HttpServletRequest request,
			@RequestParam(value = "errors", defaultValue = "0") boolean hasErrors,
			@RequestParam(value = "noaccount", defaultValue = "0") boolean noAccount)
	{
		// Everytime we load the login page, we want a clean form.

		session.invalidate();
		ModelAndView loginPage = new ModelAndView("login", "loginForm", getLoginForm());
		loginPage.addObject("hasErrors", hasErrors); // flag indicating there are errors.
		if (noAccount)
			loginPage.addObject("message", "There were no accounts for this user - please try user \"beta\".");
		return loginPage;
	}
	
	@RequestMapping(value = "/login-validation", method = RequestMethod.POST)
	public String login(@Valid @ModelAttribute("loginForm") LoginForm form, BindingResult result, Model model)
	{
		try {
			if (result.hasErrors()) {
				model.addAttribute("validation", result);
				return "redirect:login?errors=1";
			}	
		}
		catch(Exception e)
		{
			logger.info(e.getMessage());
			e.printStackTrace();
		}
		
		// This is how we invoke the siteAuthenticationProvider.
		return "forward:j_spring_security_check?j_username=" + form.getUser() + "&j_password=" + form.getPass();
	}
	
	@ModelAttribute("loginForm")
	public LoginForm getLoginForm() {
		return new LoginForm();
	}
	
	
	@RequestMapping(value="/signout", method = {RequestMethod.POST, RequestMethod.GET})
	public String logout(Model model, HttpSession session)
	{
			
		session.invalidate(); // kill the session.
		
		return "forward:j_spring_security_logout";
	}
}
