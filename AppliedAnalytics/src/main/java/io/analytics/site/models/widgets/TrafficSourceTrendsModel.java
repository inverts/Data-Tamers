package io.analytics.site.models.widgets;



import io.analytics.domain.CoreReportingData;
import io.analytics.service.interfaces.ICoreReportingService;
import io.analytics.service.interfaces.ISessionService;
import io.analytics.site.models.FilterModel;
import io.analytics.site.models.SettingsModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.json.*;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.analytics.model.GaData;
import com.google.gson.Gson;

public class TrafficSourceTrendsModel extends WidgetModel {
	
	private List<TrafficSourceData> trafficSourceDataList;

	//TODO: Find out if we're still using this stuff:
	private final String widgetClass = "growingProblems";
	private final String widgetTitle = "growingproblems.title";
	
	private final String name = "Traffic Source Trends";
	private final String description = "Highlights traffic sources that have been steadily increasing or decreasing in performance.";

	private ICoreReportingService coreReportingService;
	private ISessionService sessionService;
	private Credential credential;
	
	private Date startDate, endDate;
	private String profileId, metric;
	
	public TrafficSourceTrendsModel(ISessionService sessionService, ICoreReportingService coreReportingService) {
		super();
		this.sessionService = sessionService;
		this.coreReportingService = coreReportingService;
		this.credential = sessionService.getCredentials();
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}

	public String getActiveProfile() {
		return sessionService.getUserSettings().getActiveProfile().getId();
	}
	
	public List<TrafficSourceData> getTrafficSourceDataList() {
		
		FilterModel filter = this.sessionService.getFilter();
		SettingsModel settings = this.sessionService.getUserSettings();
		Date endDate = filter.getActiveEndDate();
		long msInDay = 24L * 60L * 60L * 1000L;
		Date startDate = new Date(endDate.getTime() - 91L * msInDay);
		String profileId = settings.getActiveProfile().getId();
		String metric = filter.getActiveMetric();
		
		//If nothing has changed, return the stored data.
		if (startDate.equals(this.startDate) && endDate.equals(this.endDate) && profileId.equals(this.profileId) && metric.equals(this.metric)) {
			return this.trafficSourceDataList;
		} else {
			return updateTrafficSourceDataList(startDate, endDate, profileId, metric);
		}
	}
	
	private List<TrafficSourceData> updateTrafficSourceDataList(Date startDate, Date endDate, String profileId, String metric) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.profileId = profileId;
		this.metric = metric;
		
		CoreReportingData reportingData = coreReportingService.getTopTrafficSources(credential, profileId, metric, startDate, endDate, 50);
		GaData gaData = reportingData.getData();
		List<String> dimensions = new ArrayList<String>();
		Double totalMetricValue = 0.0;
		//Rows should follow this format: [source, metric]
		for (List<String> row : gaData.getRows()) {
			dimensions.add(row.get(0));
			totalMetricValue += Double.parseDouble(row.get(1)); //TODO: Careful.
		}
		int totalRows = dimensions.size() * 91; //TODO: This cannot exceed 10,000 or the results will be truncated...
		reportingData = coreReportingService.getDimensionsByDay(credential, profileId, metric, "ga:source", dimensions, startDate, endDate, totalRows);
		gaData = reportingData.getData();
		
		SimpleRegression stats = new SimpleRegression();
		String currentDimension = gaData.getRows().get(0).get(0);
		double dayCounter = 0.0;
		Double metricValue = 0.0;
		Double metricDayValue;
		List<TrafficSourceData> newData = new ArrayList<TrafficSourceData>();
		
		//Rows should follow this format: [source, day number, metric]
		for (List<String> row : gaData.getRows()) {
			if (!row.get(0).equals(currentDimension)) {
				//This has been deemed irrelevant/redundant.
				//Double proportion = metricValue / totalMetricValue;
				Double slope = stats.getSlope();
				Double confidence = stats.getSlopeConfidenceInterval();
				if (!Double.isNaN(slope) && !Double.isNaN(confidence))
					newData.add(new TrafficSourceData(currentDimension, slope, confidence));
				dayCounter = 0.0;
				metricValue = 0.0;
				currentDimension = row.get(0);
				stats.clear();
			}
			metricDayValue = Double.parseDouble(row.get(2)); //TODO: Careful.
			metricValue += metricDayValue; 
			stats.addData(dayCounter, metricDayValue); //The SimpleRegression takes in this data and will provide us with the slope
			dayCounter += 1.0;
		}
		this.trafficSourceDataList = newData;
		
		return newData;
	}
	
	
	
	@Override
	public String getJSONSerialization() {

		//Sort the data by smallest slope first (worst)
		Collections.sort(this.trafficSourceDataList, new Comparator<TrafficSourceData> () {
			@Override
			public int compare(TrafficSourceData o1, TrafficSourceData o2) {
				return Double.compare(o1.getSlope(), o2.getSlope());
			}
			
		});
		
		JSONObject data = new JSONObject();
		try {
			data.put("name", this.name);
			JSONArray dataList = new JSONArray();
			Gson g = new Gson();
			for (TrafficSourceData tsd : trafficSourceDataList) {
				dataList.put(new JSONObject(g.toJson(tsd)));
			}
			data.put("description", this.description);
			data.put("startDate", this.startDate.toString());
			data.put("endDate", this.endDate.toString());
			data.put("metric", this.metric);
			data.put("trafficSourceDataList", dataList);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data.toString();
		
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
		private Double confidenceHalfWidth;
		
		public TrafficSourceData(String sourceName, Double slope, Double confidenceHalfWidth) {
			this.sourceName = sourceName;
			this.slope = slope;
			this.confidenceHalfWidth = confidenceHalfWidth;
		}

		public String getSourceName() {
			return sourceName;
		}

		public Double getSlope() {
			return slope;
		}
		
		/**
		 * This returns the half-width of the 95% confidence interval of the slope.
		 * This can be used to compare the accuracy of the slope of one traffic source 
		 * compared to another.
		 * 
		 * @return
		 */
		public Double getConfidenceHalfWidth() {
			return confidenceHalfWidth;
		}

		
	}
	
	
}
