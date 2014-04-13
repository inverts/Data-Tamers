package io.analytics.site.models.widgets;

import io.analytics.domain.KeywordInsightData;
import io.analytics.domain.OverviewData;
import io.analytics.service.interfaces.IKeywordInsightService;
import io.analytics.service.interfaces.IOverviewService;
import io.analytics.service.interfaces.ISessionService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OverviewModel extends WidgetModel{

	private JSONObject jsonData;
	private IOverviewService overviewService;
	private ISessionService sessionService;
	private String activeProfile;
	private Date startDate;
	private Date endDate;
	private int newVisits;
	private double percentNewVisits;
	private int visits;
	private double visitBounceRate;
	private double pageviewsPerVisit;
	private double avgTimePerVisit;

	private final String widgetClass = "overview";
	private final String widgetTitle = "overview.title";	

	DateFormat presentationFormat = new SimpleDateFormat("MM/dd/yyyy"); 

	public OverviewModel(ISessionService sessionService, IOverviewService overviewService) {

		this.sessionService = sessionService;
		this.overviewService = overviewService;
		this.jsonData = new JSONObject();
		this.activeProfile = this.overviewService.getProfile();

		// default dates
		this.endDate = new Date();
		Calendar cal = new GregorianCalendar();
		cal.setTime(this.endDate);
		cal.add(Calendar.DAY_OF_MONTH, -30);
		this.startDate = cal.getTime();

		this.viewCount = 3;
		//updateData();
	}

	public String getName() {
		return "Overview";
	}

	public String getDescription() {
		return "View summary data for website performance.";
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getActiveProfile() {
		return this.activeProfile;
	}

	/**
	 * TODO: Have this automatically occur when dependencies are updated.
	 * 
	 * TODO: http://en.wikipedia.org/wiki/Refactor
	 */
	public void updateData() {

		OverviewData dataObject = this.overviewService.getOverviewData(this.sessionService.getCredentials(), this.sessionService.getUserSettings().getActiveProfile().getId(), this.startDate, this.endDate);
		// over quota error returns no data, must refresh browser to try again
		if (dataObject==null){
			this.jsonData=null;
			return;
		}

		newVisits = dataObject.getNewVisitsTotal();
		percentNewVisits = dataObject.getPercentNewVisitsTotal();
		visits = dataObject.getVisitsTotal();
		visitBounceRate = dataObject.getVisitBounceRateTotal();
		pageviewsPerVisit = dataObject.getPageviewsPerVisit();
		avgTimePerVisit = dataObject.getAvgTimePerVisit();


		// put data into the JSON Object member jsonData
		this.loadJson(newVisits, percentNewVisits, visits, visitBounceRate, pageviewsPerVisit, avgTimePerVisit); 
	}

	// put data into JSON object to pass to the view website-performance.jsp 
	public void loadJson(int newVisits, double percentNewVisits, int visits, double visitBounceRate, double pageviewsPerVisit, double avgTimePerVisit)  {
		try {
			// Overview totals
			JSONObject overviewTotals = new JSONObject();
			
			String[] keys = new String[]{"New Visits", " New Visits (%)", "Visits", "Bounce Rate (%)", "Pageviews Per Visits", "Avg Visit Duration (sec)"};
			double[] ovArr = {newVisits, percentNewVisits, visits, visitBounceRate, pageviewsPerVisit, avgTimePerVisit}; 
			overviewTotals.put("title","Website Performance Overview");
			overviewTotals.put("data",ovArr);

			this.jsonData.put("overviewtotals", overviewTotals);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 

	public JSONObject getDataPoints() {
		return this.jsonData;
	}

	@Override
	public String getJSONSerialization() {
		JSONObject result = new JSONObject();
		try {
			result.put("name", this.getName());
			result.put("description", this.getDescription());
			//result.put("metric", this.getMetric());
			result.put("priority", this.getPositionPriority());
			result.put("data", this.getDataPoints());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result.toString();
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
		// TODO Auto-generated method stub
		return null;
	}
}
