package io.analytics.site.controllers;

import io.analytics.aspect.HeaderFooter;

import java.text.DateFormat;
import java.util.Date;
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

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@HeaderFooter
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate);
				
		return new ModelAndView("home");
	}
	
	
	@RequestMapping(value = "/success", method = RequestMethod.GET)
	public ModelAndView success(HttpSession session, Model model) { //@RequestParam("credentials") Credential credentials
		logger.info("Successfully authorized.");
		Credential credentials = (Credential) session.getAttribute("credentials");
		if (credentials != null) {
			model.addAttribute("accessToken", credentials.getAccessToken());
			model.addAttribute("refreshToken", credentials.getRefreshToken());
		} else {
			logger.info("Missing credentials.");
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
