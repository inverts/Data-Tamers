package io.analytics.site.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.api.client.auth.oauth2.Credential;

import io.analytics.domain.PagePerformanceData;
import io.analytics.service.interfaces.IPagePerfomanceService;
import io.analytics.service.interfaces.ISessionService;

public class WebsitePerformanceModel {
	
		private JSONObject jsonData;
		private IPagePerfomanceService pagePerformanceService;
		private ISessionService sessionService;
		private String activeProfile;
		private Date startDate;
		private Date endDate;
		private ArrayList<String> pagePath;
		private ArrayList<Integer> visits;
		private ArrayList<Double> visitsBounceRate;
		private ArrayList<Double> exitRate;
		private int visitsTotal;
        DateFormat presentationFormat = new SimpleDateFormat("MM/dd/yyyy"); 
        private static final int MS_IN_DAY = 86400000;
        
		public WebsitePerformanceModel(ISessionService sessionService, IPagePerfomanceService pagePerformanceService) {	
			this.sessionService = sessionService;
			this.pagePerformanceService = pagePerformanceService;
			this.jsonData = new JSONObject();
			this.activeProfile = this.pagePerformanceService.getProfile();
			
			// default dates
			this.endDate = new Date();
			Calendar cal = new GregorianCalendar();
			cal.setTime(this.endDate);
			cal.add(Calendar.DAY_OF_MONTH, -30);
			this.startDate = cal.getTime();
            
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
		
			PagePerformanceData dataObject = this.pagePerformanceService.getPagePerformanceData(this.sessionService.getCredentials(), this.sessionService.getUserSettings().getActiveProfile().getId(), this.startDate, this.endDate, 500);
			this.pagePath = dataObject.getPagePathData();
			this.visits = dataObject.getVisitsData();
			this.visitsBounceRate = dataObject.getVisitsBounceRateData();
			this.exitRate = dataObject.getExitRateData();
			this.visitsTotal = dataObject.getVisitsTotal();
			
			ArrayList<Double> weightedAvg = new ArrayList<Double>(pagePath.size());
			for (int i=0; i<pagePath.size(); i++){
				weightedAvg.add(i, visits.get(i)*visitsBounceRate.get(i) + 
						visits.get(i)*exitRate.get(i));
			}
			
			// iterate to find top 5 maximum weighted averages and save indices
			double max = weightedAvg.get(0);
			int maxIndex = 0;
			int[] worstI = new int[5];
			for (int i = 0; i<5; i++) {
				worstI[i]=-1;
			}
			for (int j=0; j<5; j++){
				for (int i=j; i<pagePath.size(); i++) {
					if (max < weightedAvg.get(i) && i!=worstI[0] && i!=worstI[1] && i!=worstI[2] &&
							i!=worstI[3] && i != worstI[4]) {
						max = weightedAvg.get(i);
						maxIndex = i;
					}
				}
				worstI[j]=maxIndex;
				System.out.println("Worst index = "+ maxIndex + ". Max = " + max);
				max = 0;
			}
			
		// put results into json object (print out results for now)
			System.out.println("Page               Visits(%)      ExitRate(%)       VisitsBounceRate(%) ");
			for (int i=0; i<5; i++){
				System.out.println(pagePath.get(worstI[i]) + ", " + (visits.get(worstI[i])*100)/visitsTotal + ", " + 
						exitRate.get(worstI[i]) + ", " + visitsBounceRate.get(worstI[i]));
			}
		}
		
		/* test method to pass data to javascript */
		
//		public JSONObject getDataPoints() {
//		
//			Double[] data = new Double[this.getDataPoints().length()];
//			int i;
//			for (i=0; i<this.getDataPoints().length(); i++) {
//				//data[i] = this.getDataPoints().get(i);
//			}
//			
//			float[] pts = new float[] { 5, 6, 7, 8, 10};
//			
//			 try {
//				 
//				 /* Feel free to create categories of data like I did for points. */
//				 JSONObject points = new JSONObject();
//				 points.put("values", Arrays.toString(data));
//				 
//				 /* this.data is the parent object that will contain ALL of
//				  * the data that will be processed by the visualization.
//				  */
//				 this.dataPoints.put("points", points);
//				
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			 return this.dataPoints;
//		}
		
		/*
		// Helper method for establishing the dropdown options which uses a map to set a boolean
		// for the selected item.
		private Map<String, String> setDropDownOptions (String selected, String[] values) {
			
			Map<String, String> result = new TreeMap<String, String>();
			
			for (String value : values) {
					result.put(value, value.equals(selected) ? "selected" : "");
			}
			
			return result;
			
		}
		*/
}