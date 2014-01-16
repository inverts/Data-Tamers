package io.analytics.site.controllers;

import io.analytics.aspect.HeaderFooter;
import io.analytics.domain.GoogleUserData;
import io.analytics.enums.HeaderType;

import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.api.client.auth.oauth2.Credential;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@HeaderFooter(HeaderType.HOME)
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView home(Locale locale, Model model) {
		model.addAttribute("isEntry", "something");
		return new ModelAndView("home/home");
	}
	

	/**
	 * This page is deprecated.
	 * 
	 * @deprecated
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/success", method = RequestMethod.GET)
	public ModelAndView success(HttpSession session, Model model) { //@RequestParam("credentials") Credential credentials
		logger.info("Successfully authorized.");
		try {
			Credential credentials = (Credential) session.getAttribute("credentials");
			GoogleUserData userData = (GoogleUserData) session.getAttribute("userData");
			if (credentials == null || userData ==null) {
				//User is not logged in.
				//TODO: Redirect the user to login again with a message.
				return new ModelAndView("success");
			}
			model.addAttribute("accessToken", credentials.getAccessToken());
			model.addAttribute("refreshToken", credentials.getRefreshToken());
			model.addAttribute("name", userData.getName());
			model.addAttribute("email", userData.getEmail());
			model.addAttribute("picture", userData.getPicture());
		} catch (ClassCastException e) {
			logger.info("Corrupted session information. See below for more info.");
			logger.info(e.getMessage());
		}
		return new ModelAndView("success");
	}
	
	/*
	 *  http://docs.spring.io/spring-security/site/docs/3.0.x/reference/technical-overview.html
	 *  
	 * "You shouldn't interact directly with the HttpSession for security purposes. 
	 * There is simply no justification for doing so - always use the SecurityContextHolder instead."
	 */
	
}
