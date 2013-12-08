package io.analytics.site.controllers;

import io.analytics.aspect.HeaderFooter;
import io.analytics.aspect.SidePanel;
import io.analytics.domain.GoogleUserData;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.api.client.auth.oauth2.Credential;

@Controller
public class ApplicationController {
	
	private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@HeaderFooter
	@SidePanel(animate = true)
	@RequestMapping(value = "/application", method = RequestMethod.GET)
	public ModelAndView home(Locale locale, Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		/*
		 * Below is the authorization process. We may move this later.
		 */
		try {
			Credential credentials = (Credential) session.getAttribute("credentials");
			GoogleUserData userData = (GoogleUserData) session.getAttribute("userData");
			if (credentials == null || userData ==null) {
				//User is not logged in.
				String contextPath = session.getServletContext().getContextPath();
				try {
					//We cannot turn this into a returnable view, since it needs to direct to the servlet.
					response.sendRedirect(contextPath + "/login");
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
			model.addAttribute("accessToken", credentials.getAccessToken());
			model.addAttribute("refreshToken", credentials.getRefreshToken());
			model.addAttribute("firstName", userData.getName());
			model.addAttribute("fullName", userData.getName());
			model.addAttribute("email", userData.getEmail());
			model.addAttribute("picture", userData.getPicture());
			
		} catch (ClassCastException e) {
			logger.info("Corrupted session information. See below for more info.");
			logger.info(e.getMessage());
		}
		
		return new ModelAndView("dashboard");
	}

}
