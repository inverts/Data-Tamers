package io.analytics.site.models.widgets;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.analytics.domain.PagePerformanceData;
import io.analytics.service.interfaces.IPagePerfomanceService;
import io.analytics.service.interfaces.ISessionService;

public class WebsitePerformanceModel extends WidgetModel {

	private JSONObject jsonData;
	private IPagePerfomanceService pagePerformanceService;
	private ISessionService sessionService;
	private String activeProfile;
	private Date startDate;
	private Date endDate;
	private ArrayList<String> pagePath;
	private Map<String,String> pathToTitleMap;
	private ArrayList<Integer> visits;
	private ArrayList<Double> visitsBounceRate;
	private ArrayList<Double> exitRate;
	private int visitsTotal;
	private String hostname;
	private List<PageData> pageDataList;
	private List<PageData> resultsPageData;

	private List<String> pageTitleResults;
	private JSONObject pageLinkResultsJson;
	private List<String> pageLinkResults;
	private List<Double> visitsPercentResults;
	private List<Double> bounceRateResults;
	private List<Double> exitRateResults;
	private List<Double> weightedAvgResults;


	private final String widgetClass = "pagePerformance";
	private final String widgetTitle = "websiteperformance.title";

	DateFormat presentationFormat = new SimpleDateFormat("MM/dd/yyyy"); 

	public WebsitePerformanceModel(ISessionService sessionService, IPagePerfomanceService pagePerformanceService) {	
		this.sessionService = sessionService;
		this.pagePerformanceService = pagePerformanceService;
		this.jsonData = new JSONObject();
		this.activeProfile = this.pagePerformanceService.getProfile();
		pageTitleResults = new ArrayList<String>();
		pageLinkResultsJson = new JSONObject();
		pageLinkResults = new ArrayList<String>();
		visitsPercentResults = new ArrayList<Double>();
		bounceRateResults = new ArrayList<Double>();
		exitRateResults = new ArrayList<Double>();
		pageDataList = new ArrayList<PageData>();
		resultsPageData = new ArrayList<PageData>();
		weightedAvgResults = new ArrayList<Double>();

		// default dates
		this.endDate = new Date();
		Calendar cal = new GregorianCalendar();
		cal.setTime(this.endDate);
		cal.add(Calendar.DAY_OF_MONTH, -30);
		this.startDate = cal.getTime();

		this.viewCount = 2;

		updateData();

	}

	public String getName() {
		return "Website Performance";
	}

	public String getDescription() {
		return "View prioritized webpages that if improved will make the biggest impact on your website performance.";
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
	 */
	public void updateData() {

		PagePerformanceData dataObject = this.pagePerformanceService.getPagePerformanceData(this.sessionService.getCredentials(), this.sessionService.getUserSettings().getActiveProfile().getId(), this.startDate, this.endDate, 10000);
		// over quota error returns no data, must refresh browser to try again
		if (dataObject==null){
			setJsonKeys();
			return;
		}
		this.pagePath = dataObject.getPagePathData();
		this.visits = dataObject.getVisitsData();
		this.visitsBounceRate = dataObject.getVisitsBounceRateData();
		this.exitRate = dataObject.getExitRateData();
		this.visitsTotal = dataObject.getVisitsTotal();
		this.pathToTitleMap = dataObject.getPathToTitleMap();
		this.hostname = dataObject.getHostname();

		this.pageDataList.clear();				
		this.pageTitleResults.clear();
		this.visitsPercentResults.clear();
		this.bounceRateResults.clear();
		this.exitRateResults.clear();;
		this.weightedAvgResults.clear();
		this.resultsPageData.clear();
		this.pageLinkResults.clear();
		this.pageLinkResultsJson = new JSONObject();

		double vp = -1;  // visits percent
		double br = -1;  // bounce rate
		double er = -1;  // exit rate
		double wa = -1;  // weighted avg of visits that bounce and visits that exit
		String url = "";
		String title = "";
		for (int i=0; i<this.pagePath.size(); i++){
			wa = this.visits.get(i)*this.visitsBounceRate.get(i) + 
					this.visits.get(i)*this.exitRate.get(i);
			vp = Math.round(this.visits.get(i)*1000.00/this.visitsTotal)/10.0;
			br = Math.round(this.visitsBounceRate.get(i)*10.0)/10.0;
			er = Math.round(this.exitRate.get(i)*10.0)/10.0;
			wa = Math.round(wa*10.0)/10.0;
			url = "http://".concat(this.hostname).concat(this.pagePath.get(i));
			title = this.pathToTitleMap.get(this.pagePath.get(i));
			if (title.equals("(not set)")) {
				title = "(No title set)";
			}
			this.pageDataList.add(new PageData(url, title, vp, br, er, wa));
		} 

		// sort ascending
		Collections.sort(this.pageDataList);
		// then reverse the list (descending)
		Collections.reverse(this.pageDataList);

		Map<String,Integer> titleDupsMap = new HashMap<String,Integer>();

		int c = 0;
		Iterator<PageData> it = this.pageDataList.iterator();
		while (it.hasNext() && c < 5) {
			PageData pd = it.next();
			if (pd.pageLink.equals("http://".concat(this.hostname).concat("/"))){
				continue;
			}

			// if title is duplicated, append (dupCount) to it so it is unique.  
			if (titleDupsMap.containsKey(pd.pageTitle)){
				int dupCount = titleDupsMap.get(pd.pageTitle)+1;
				titleDupsMap.put(pd.pageTitle,dupCount);
				pd.pageTitle = pd.pageTitle+" ("+dupCount+")";
			} else {
				titleDupsMap.put(pd.pageTitle, 1);
			}

			// these arrays are for debug viewing
			this.resultsPageData.add(pd);
			this.pageLinkResults.add(pd.pageLink);

			// load result into arrays for transfer to JSON
			try {
				this.pageLinkResultsJson.put(pd.pageTitle,pd.pageLink);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.pageTitleResults.add(pd.pageTitle);
			this.visitsPercentResults.add(pd.visitsPercent);
			this.bounceRateResults.add(pd.bounceRate);
			this.exitRateResults.add(pd.exitRate);
			this.weightedAvgResults.add(pd.weightedAvg);

			c++;
		}

		this.setJsonDataPoints();
	}
		
	// GA return null, load json with the column headers and page title
	public void setJsonKeys(){
		String[] keys1 = new String[]{"Webpage Title/Link", "% Visits ", "% Bounce Rate", "% Exit Rate"};
		try {
			this.jsonData.put("keys", keys1);
			this.jsonData.put("title","Improve the performance of these important webpages:");
			this.jsonData.put("noData", true); // flag indicating there is no data.
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// put data into JSON object to be passed to the view website-performance.jsp 

	public void setJsonDataPoints()  {
		try {

			String[] keys1 = new String[]{"Webpage Title/Link", "% Visits ", "% Bounce Rate", "% Exit Rate"};

			this.jsonData.put(keys1[0], pageTitleResults);
			this.jsonData.put(keys1[1], visitsPercentResults);
			this.jsonData.put(keys1[2], bounceRateResults);
			this.jsonData.put(keys1[3], exitRateResults);

			this.jsonData.put("url", pageLinkResultsJson);
			this.jsonData.put("keys", keys1);
			this.jsonData.put("title","Improve the performance of these important webpages:");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public JSONObject getDataPoints(){
		return new JSONObject(this.jsonData);
	}

	@Override
	public String getJSONSerialization() {
		JSONObject result = new JSONObject();
		try {
			result.put("name", this.getName());
			result.put("description", this.getDescription());
			//result.put("metric", this.getMetric());
			result.put("priority", this.getPositionPriority());
			result.put("data", this.jsonData);
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

//class to holds word, a frequency count, a rank
class PageData implements Comparable<PageData>{
	public String pageLink;
	public String pageTitle;
	public double visitsPercent;
	public double bounceRate;
	public double exitRate;
	public double weightedAvg;

	public PageData(String pageLink, String pageTitle, double visitsPercent, double bounceRate, double exitRate, double weightedAvg ){
		this.pageLink = pageLink;
		this.pageTitle = pageTitle;
		this.visitsPercent = visitsPercent;
		this.bounceRate = bounceRate;
		this.exitRate = exitRate;
		this.weightedAvg = weightedAvg;
	}

	public int compareTo(PageData d){
		return Double.compare(this.weightedAvg, d.weightedAvg);
	}

}

