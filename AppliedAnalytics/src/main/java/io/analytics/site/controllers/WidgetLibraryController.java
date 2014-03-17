package io.analytics.site.controllers;

import java.util.List;

import io.analytics.domain.WidgetLibrary;
import io.analytics.domain.WidgetLibraryType;
import io.analytics.service.interfaces.IWidgetLibrariesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WidgetLibraryController {
	@Autowired
	IWidgetLibrariesService WidgetLibrariesService;
	

	@RequestMapping(value = "/widgetLibraries", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView widgetLibraries(Model viewMap, HttpServletRequest request, HttpServletResponse response, HttpSession session) 
	{
		List<WidgetLibrary> widgetLibraries = WidgetLibrariesService.getWidgetLibraries();
		JSONArray arr = new JSONArray();
		for (WidgetLibrary widgetLibrary : widgetLibraries) {
			try {
				arr.put(new JSONObject(widgetLibrary.getJSONSerialization()));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		viewMap.addAttribute("data", arr.toString());
		return new ModelAndView("plaintext");
		
	}
	

	@RequestMapping(value = "/widgetLibraries/{widgetLibraryId}/widgetTypes", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView widgetTypesInLibrary(Model viewMap, HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@PathVariable String widgetLibraryId) 
	{
		int libraryId;
		try {
			libraryId = Integer.parseInt(widgetLibraryId);
		} catch (NumberFormatException e) {
			//TODO: Return a standardized JSON error object.
			JSONObject errorObj = new JSONObject();
			viewMap.addAttribute("data", errorObj);
			return new ModelAndView("plaintext"); 
		}
		List<WidgetLibraryType> widgetLibraryTypes = WidgetLibrariesService.getTypesInLibrary(libraryId);
		JSONArray arr = new JSONArray();
		for (WidgetLibraryType widgetLibraryType : widgetLibraryTypes) {
			try {
				arr.put(new JSONObject(widgetLibraryType.getJSONSerialization()));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		viewMap.addAttribute("data", arr.toString());
		return new ModelAndView("plaintext");
		
	}
	
	@RequestMapping(value = "/widgetLibraries/{widgetLibraryId}", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView widgetLibrary(Model viewMap, HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@PathVariable String widgetLibraryId) 
	{
		int libraryId;
		try {
			libraryId = Integer.parseInt(widgetLibraryId);
		} catch (NumberFormatException e) {
			//TODO: Return a standardized JSON error object.
			JSONObject errorObj = new JSONObject();
			viewMap.addAttribute("data", errorObj);
			return new ModelAndView("plaintext"); 
		}
		WidgetLibrary widgetLibrary = WidgetLibrariesService.getWidgetLibrary(libraryId);
		viewMap.addAttribute("data", widgetLibrary.getJSONSerialization());
		return new ModelAndView("plaintext");
		
	}
	
}
