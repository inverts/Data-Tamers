package io.analytics.site.models;



import org.json.*;

public class GrowingProblemsModel {

	private JSONObject jsonData;
	//private IBoostPerformanceService boostPerformanceService;
	//private ISessionService sessionService;	
	private String activeProfile;
	
	
	
	public GrowingProblemsModel() {
		super();
		
		this.jsonData = new JSONObject();
	}
	
	public String getActiveProfile() {
		return this.activeProfile;
	}
	
	public String getName() {
		return "Growing Problems";
	}
	
	public String getDescription() {
		return "Highlights traffic sources with potential problems";
	}
	
	
}
