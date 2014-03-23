package io.analytics.site.models.widgets;



import java.util.List;

import org.json.*;

public class GrowingProblemsModel extends WidgetModel {
	
	private List<TrafficSourceData> trafficSourceDataList;

	//TODO: Find out if we're still using this stuff:
	private final String widgetClass = "growingProblems";
	private final String widgetTitle = "growingproblems.title";
	
	
	public GrowingProblemsModel() {
		super();
	}
	
	public String getName() {
		return "Growing Problems";
	}
	
	public String getDescription() {
		return "Highlights traffic sources that have been steadily decreasing in performance.";
	}

	public String getActiveProfile() {
		return null;
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
	
	private class TrafficSourceData {
		private String sourceName;
		private Double slope;
		private Double percentage;
		
		public TrafficSourceData(String sourceName, Double slope, Double percentage) {
			this.sourceName = sourceName;
			this.slope = slope;
			this.percentage = percentage;
		}

		public String getSourceName() {
			return sourceName;
		}

		public Double getSlope() {
			return slope;
		}

		public Double getPercentage() {
			return percentage;
		}
		
		
	}
	
	
}
