package io.analytics.site.models;

import io.analytics.service.interfaces.ICoreReportingService;
import io.analytics.service.interfaces.ISessionService;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.*;

public class BoostPerformanceModel {

	private JSONObject jsonData;
	//private IBoostPerformanceService boostPerformanceService;
	//private ISessionService sessionService;	
	private String activeProfile;
	// traffic source
	
	// conversion rate
	
	// avg. value per visit
	
	// percentage of traffic
	
	
	public BoostPerformanceModel() {
		super();
		
		this.jsonData = new JSONObject();
	}
	
	public String getActiveProfile() {
		return this.activeProfile;
	}
	
	public String getName() {
		return "Boost Performance";
	}
	
	public String getDescription() {
		return "View sources that need a boost in traffic";
	}
	
	
}
