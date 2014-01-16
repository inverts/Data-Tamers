package io.analytics.site.controllers;

import io.analytics.aspect.HeaderFooter;
import io.analytics.enums.HeaderType;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
	
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@HeaderFooter(HeaderType.SIMPLE)
	@RequestMapping(value = "/login", method = {RequestMethod.POST, RequestMethod.GET})
	public String login(Model model, 
			@RequestParam(value = "invalid_login", defaultValue = "0") boolean error) throws IOException
	{
		model.addAttribute("invalid", (error) ? "login.invalid" : null);
		return "login";
	}
	
	
	@RequestMapping(value="/signout", method = {RequestMethod.POST, RequestMethod.GET})
	public void logout(Model model, HttpServletResponse response, HttpSession session)
	{
		try {
			
			session.invalidate(); // kill the session.
			
			// redirect back to the home page.
			response.sendRedirect("/appliedanalytics/");
			
		} catch (IOException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
	}
}
