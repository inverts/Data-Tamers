package io.analytics.site.models.widgets;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
		private Map<String,String> pathToTitle;
		private ArrayList<Integer> visits;
		private ArrayList<Double> visitsBounceRate;
		private ArrayList<Double> exitRate;
		private int visitsTotal;
		private String hostname;
		
		private String[] pagePathResults;
		private String[] pageTitleResults;
		private String[] pageLinkResults;
		private double[] visitsPercentResults;
		private double[] bounceRateResults;
		private double[] exitRateResults;
		
		
		private final String widgetClass = "pagePerformance";
		private final String widgetTitle = "websiteperformance.title";

        DateFormat presentationFormat = new SimpleDateFormat("MM/dd/yyyy"); 
        
		public WebsitePerformanceModel(ISessionService sessionService, IPagePerfomanceService pagePerformanceService) {	
			this.sessionService = sessionService;
			this.pagePerformanceService = pagePerformanceService;
			this.jsonData = new JSONObject();
			this.activeProfile = this.pagePerformanceService.getProfile();
			pagePathResults = new String[5];
			pageTitleResults = new String[5];
			pageLinkResults = new String[5];
			visitsPercentResults = new double[5];
			bounceRateResults = new double[5];
			exitRateResults = new double[5];
			

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
			return "View the website performance statistics for a business";
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
			this.pagePath = dataObject.getPagePathData();
			this.visits = dataObject.getVisitsData();
			this.visitsBounceRate = dataObject.getVisitsBounceRateData();
			this.exitRate = dataObject.getExitRateData();
			this.visitsTotal = dataObject.getVisitsTotal();
			this.pathToTitle = dataObject.getUrlToTitle();
			this.hostname = dataObject.getHostname();

			ArrayList<Double> weightedAvg = new ArrayList<Double>(pagePath.size());
			for (int i=0; i<pagePath.size(); i++){
				weightedAvg.add(i, visits.get(i)*visitsBounceRate.get(i) + 
						visits.get(i)*exitRate.get(i));
			}

			// iterate to find top 6 maximum weighted averages and save indices
			double max = weightedAvg.get(0);
			int maxIndex = 0;
			int[] worstI = new int[6];
			for (int i = 0; i<6; i++) {
				worstI[i]=-1;
			}
			for (int j=0; j<6; j++){
				for (int i=j; i<pagePath.size(); i++) {
					if (max < weightedAvg.get(i) && i!=worstI[0] && i!=worstI[1] && i!=worstI[2] &&
							i!=worstI[3] && i != worstI[4] && i != worstI[5]) {
						max = weightedAvg.get(i);
						maxIndex = i;
					}
				}
				worstI[j]=maxIndex;
				max = 0;
			}

		// Only keep top 5, but not the homepage
			int c = 0;
			for (int i=0; i<6; i++){
				if (pagePath.get(worstI[i]).equals("/") || c == 5){
					continue;
				}
				pagePathResults[c] = pagePath.get(worstI[i]);
				visitsPercentResults[c] = Math.round(visits.get(worstI[i])*100.00/visitsTotal*10.0)/10.0;
				bounceRateResults[c] = Math.round(visitsBounceRate.get(worstI[i])*10.0)/10.0;
				exitRateResults[c] = Math.round(exitRate.get(worstI[i])*10.0)/10.0;
				if (!pathToTitle.containsKey(pagePathResults[c])){
					pageTitleResults[c]= pagePathResults[c];
				} else {
					pageTitleResults[c] = pathToTitle.get(pagePathResults[c]);
				}
				pageLinkResults[c] = "http://".concat(hostname.concat(pagePathResults[c]));
				c++;
			}		
			
			this.getDataPoints();
		}
		
		// put data into JSON object to pass to the view website-performance.jsp 

		public JSONObject getDataPoints()  {
			 try {
				 
				 /*JSONArray arr1 = new JSONArray();
				 JSONArray arr2 = new JSONArray();
				 JSONArray arr3 = new JSONArray();
				 JSONArray arr4 = new JSONArray();
				 JSONArray arr5 = new JSONArray();
				 JSONArray arr6 = new JSONArray();*/
				 
				 String[] keys1 = new String[]{"Webpath Path", "Visits (%)", "Bounce Rate (%)", "Exit Rate (%)"};
				 
				 //this.jsonData.put(keys1[0], pagePathResults);
				 this.jsonData.put(keys1[0], pageTitleResults);
				 this.jsonData.put(keys1[1], visitsPercentResults);
				 this.jsonData.put(keys1[2], bounceRateResults);
				 this.jsonData.put(keys1[3], exitRateResults);
				 //this.jsonData.put("domain", pageTitleResults);
				 this.jsonData.put("url", pageLinkResults);
				 this.jsonData.put("keys", keys1);
				 
				 
				 
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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
