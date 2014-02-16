package io.analytics.site.controllers;

import io.analytics.aspect.HeaderFooter;
import io.analytics.aspect.SidePanel;
import io.analytics.domain.Account;
import io.analytics.domain.Dashboard;
import io.analytics.domain.User;
import io.analytics.enums.HeaderType;
import io.analytics.forms.DashboardForm;
import io.analytics.service.interfaces.IAccountService;
import io.analytics.service.interfaces.IDashboardService;
import io.analytics.service.interfaces.ISessionService;
import io.analytics.service.interfaces.IUserService;
import io.analytics.site.models.FilterModel;
import io.analytics.site.models.SettingsModel;
import io.analytics.site.models.SidePanelModel;

import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
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


@Controller
@SessionAttributes({"ACCOUNT_ID", "USER_ID", "DASHBOARD_ID"})
public class ApplicationController {
	
	private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);
	
	/* Services */
	@Autowired private IAccountService AccountService;
	@Autowired private ISessionService SessionService;
	@Autowired private IDashboardService DashboardService;
	@Autowired private IUserService UserService;
	
	
	@HeaderFooter(HeaderType.APPLICATION)
	@SidePanel(animate = false)
	@RequestMapping(value = "/application/trends", method = RequestMethod.GET)
	public ModelAndView trends(Model model) {
		return new ModelAndView("application/trends");
	}

	@HeaderFooter(HeaderType.APPLICATION)
	@SidePanel(animate = false)
	@RequestMapping(value = "/application/forecast", method = RequestMethod.GET)
	public ModelAndView forecast(Model model) {
		return new ModelAndView("application/forecast");
	}
	
	@HeaderFooter(HeaderType.APPLICATION)
	@SidePanel(animate = false)
	@RequestMapping(value = "/application/alerts", method = RequestMethod.GET)
	public ModelAndView suggestions(Model model) {
		return new ModelAndView("application/alerts");
	}
	
	
	

	
}
