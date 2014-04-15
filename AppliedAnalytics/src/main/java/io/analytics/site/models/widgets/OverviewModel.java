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
import java.util.List;

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
	private List<String> channels;
	private List<Integer> channelNewVisits;
	private List<Double> channelPercentNewVisits;
	private List<Integer> channelVisits;
	private List<Double> channelVisitBounceRate;
	private List<Double> channelPageviewsPerVisit;
	private List<Double> channelAvgTimePerVisit;  

	private final String widgetClass = "overview";
	private final String widgetTitle = "overview.title";	

	DateFormat presentationFormat = new SimpleDateFormat("MM/dd/yyyy"); 

	public OverviewModel(ISessionService sessionService, IOverviewService overviewService) {

		this.sessionService = sessionService;
		this.overviewService = overviewService;
		this.jsonData = new JSONObject();
		this.activeProfile = this.overviewService.getProfile();
		this.channels = new ArrayList<String>();
		this.channelNewVisits = new ArrayList<Integer>();
		this.channelPercentNewVisits = new ArrayList<Double>();
		this.channelVisits = new ArrayList<Integer>();
		this.channelVisitBounceRate = new ArrayList<Double>();
		this.channelPageviewsPerVisit = new ArrayList<Double>();
		this.channelAvgTimePerVisit = new ArrayList<Double>();

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
		this.channels.add("All");
		this.channels.addAll(dataObject.getChannels());
		
		for (int i=0; i<channels.size(); i++){
			if (channels.get(i).equals("cpc"))
				channels.set(i,"Paid");
			if (!channels.get(i).startsWith("("))
				channels.set(i, channels.get(i).substring(0, 1).toUpperCase() + channels.get(i).substring(1)); 
		}
		this.channelNewVisits.add(newVisits);
		this.channelNewVisits.addAll(dataObject.getChannelNewVisits());
		this.channelPercentNewVisits.add(this.percentNewVisits);
		this.channelPercentNewVisits.addAll(dataObject.getChannelPercentNewVisits());
		this.channelVisits.add(this.visits);
		this.channelVisits.addAll(dataObject.getChannelVisits());
		this.channelVisitBounceRate.add(this.visitBounceRate);
		this.channelVisitBounceRate.addAll(dataObject.getChannelVisitBounceRate());
		this.channelPageviewsPerVisit.add(this.pageviewsPerVisit);
		this.channelPageviewsPerVisit.addAll(dataObject.getChannelPageviewsPerVisit());
		this.channelAvgTimePerVisit.add(this.avgTimePerVisit);
		this.channelAvgTimePerVisit.addAll(dataObject.getChannelAvgTimePerVisit());

		// put data into the JSON Object member jsonData
		this.loadJson(newVisits, percentNewVisits, visits, visitBounceRate, pageviewsPerVisit, avgTimePerVisit); 
		this.loadJsonChannelData(channels,channelNewVisits,channelPercentNewVisits,channelVisits,channelVisitBounceRate, channelPageviewsPerVisit, channelAvgTimePerVisit);
	}

	// put data into JSON object to pass to the view website-performance.jsp 
	public void loadJson(int newVisits, double percentNewVisits, int visits, double visitBounceRate, double pageviewsPerVisit, double avgTimePerVisit)  {
		try {
			// Overview totals
			JSONObject totals = new JSONObject();
			
			String[] keys = new String[]{"New Visits", "% New Visits", "Visits", "% Bounce Rate", "Pageviews Per Visits", "Avg Visit Duration (sec)"};
			double[] ovArr = {newVisits, percentNewVisits, visits, visitBounceRate, pageviewsPerVisit, avgTimePerVisit}; 
			totals.put("title","Website Performance");
			totals.put("data",ovArr);
            totals.put("keys", keys);
			this.jsonData.put("totals", totals);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	
	// put data into JSON object to pass to the view website-performance.jsp 
	public void loadJsonChannelData(List<String>channelsArr, List<Integer> newVisits, List<Double> percentNewVisits, List<Integer> visits, List<Double> visitBounceRate, List<Double> pageviewsPerVisit, List<Double> avgTimePerVisit)  {
		try {
			// Overview totals
			JSONObject channels = new JSONObject();
			
			String[] keys = new String[]{"Channels", "New Visits", "% New Visits", "Visits", "% Bounce Rate", "Pages Viewed Per Visit", "Avg Visit Duration (sec)"};

			channels.put("keys",keys);
			channels.put("title","Top Channels"); // use if channels is only one widget page
			channels.put("title1","Visits totals and channel breakdown:");
			channels.put("title2","Behavior totals and channel breakdown:");
			channels.put(keys[0],channelsArr);
			channels.put(keys[1], newVisits);
			channels.put(keys[2],percentNewVisits);
			channels.put(keys[3],visits);
			channels.put(keys[4],visitBounceRate);
			channels.put(keys[5],pageviewsPerVisit);
			channels.put(keys[6],avgTimePerVisit);
						
			this.jsonData.put("total", channels);
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
		return this.widgetTitle;
	}
}
