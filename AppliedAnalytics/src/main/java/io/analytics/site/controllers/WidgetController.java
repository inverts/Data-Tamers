package io.analytics.site.controllers;

import io.analytics.site.models.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WidgetController {
	

	@RequestMapping(value = "/HypotheticalFuture", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView hypotheticalFutureView(Model viewMap,	// note: in order for @RequestParam to work, you do need a default value
												@RequestParam(value = "change", defaultValue = "05") String adjustBy,
												@RequestParam(value = "source", defaultValue = "") String source) {

		
		// The Model will map the data via the @ModelAttribute 
		// annotation located on each getter method.
		HypotheticalFutureModel hypotheticalFuture = new HypotheticalFutureModel(adjustBy, source);
		
		viewMap.addAttribute("changeOptions", hypotheticalFuture.getChangePercentOptions());
	
		return new ModelAndView("HypotheticalFuture");

	}

}
