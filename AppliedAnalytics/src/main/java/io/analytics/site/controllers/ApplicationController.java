package io.analytics.site.controllers;

import io.analytics.aspect.HeaderFooter;
import io.analytics.aspect.SidePanel;
import io.analytics.domain.GoogleUserData;
import io.analytics.repository.ManagementRepository.CredentialException;
import io.analytics.service.ManagementService;
import io.analytics.service.SecurityService;
import io.analytics.site.models.SettingsModel;

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
		
		boolean success = SecurityService.checkAuthorization(session);
		if (success) {
			//If authorization succeeded, the following must succeed.
			SettingsModel settings = (SettingsModel) session.getAttribute("settings");
			model.addAttribute("settings", settings);
		} else if (SecurityService.redirectToLogin(session, response)) {
			return null;
		} else {
			return new ModelAndView("unavailable");
		}
		
		return new ModelAndView("dashboard");
	}
	
}
