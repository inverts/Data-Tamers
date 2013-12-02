package io.analytics.site.controllers;

import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WidgetController {
	
	@RequestMapping(value = "/HypotheticalFuture", method = RequestMethod.GET)
	public ModelAndView hypotheticalFutureView(Locale locale, Model model) {
		
		return new ModelAndView("HypotheticalFuture");

	}

}
