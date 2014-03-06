package io.analytics.site.models.widgets;

import org.json.*;

public class BoostPerformanceModel extends WidgetModel {

	private JSONObject jsonData;
	//private IBoostPerformanceService boostPerformanceService;
	//private ISessionService sessionService;	
	private String activeProfile;
	// traffic source
	
	// conversion rate
	
	// avg. value per visit
	
	// percentage of traffic
	
	private final String widgetClass = "boostPerformance";
	private final String widgetTitle = "boostperformance.title";
	
	
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

	@Override
	public String getJSONSerialization() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPositionPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getHTMLClass() {
		return this.widgetClass;
	}

	@Override
	public String getTitle() {
		return this.widgetTitle;
	}
	
	
}
