package io.analytics.site.models.widgets;



import org.json.*;

public class GrowingProblemsModel extends WidgetModel {

	private JSONObject jsonData;
	//private IBoostPerformanceService boostPerformanceService;
	//private ISessionService sessionService;	
	private String activeProfile;
	
	private final String widgetClass = "growingProblems";
	private final String widgetTitle = "growingproblems.title";
	
	
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
