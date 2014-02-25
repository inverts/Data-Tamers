package io.analytics.site.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
		private String[] pagePathResults;
		private double[] visitsPercentResults;
		private double[] bounceRateResults;
		private double[] exitRateResults;

        DateFormat presentationFormat = new SimpleDateFormat("MM/dd/yyyy"); 
        
		public WebsitePerformanceModel(ISessionService sessionService, IPagePerfomanceService pagePerformanceService) {	
			this.sessionService = sessionService;
			this.pagePerformanceService = pagePerformanceService;
			this.jsonData = new JSONObject();
			this.activeProfile = this.pagePerformanceService.getProfile();
			pagePathResults = new String[5];
			visitsPercentResults = new double[5];
			bounceRateResults = new double[5];
			exitRateResults = new double[5];	

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
				max = 0;
			}

		// put results into arrays
			for (int i=0; i<5; i++){
				pagePathResults[i] = pagePath.get(worstI[i]);
				visitsPercentResults[i] = Math.round(visits.get(worstI[i])*100.00/visitsTotal*10.0)/10.0;
				bounceRateResults[i] = Math.round(visitsBounceRate.get(worstI[i])*10.0)/10.0;
				exitRateResults[i] = Math.round(exitRate.get(worstI[i])*10.0)/10.0;
			}
			this.getDataPoints();
		}

		// put data into JSON object to pass to the view website-performance.jsp 

		public JSONObject getDataPoints()  {
			 try {	
				 JSONArray arr1 = new JSONArray();
				 JSONArray arr2 = new JSONArray();
				 JSONArray arr3 = new JSONArray();
				 JSONArray arr4 = new JSONArray();

				 this.jsonData.put("pagePath", arr1.put(pagePathResults));
				 this.jsonData.put("visitsPercent", arr2.put(visitsPercentResults));
				 this.jsonData.put("bounceRate", arr3.put(bounceRateResults));
				 this.jsonData.put("exitRate", arr4.put(exitRateResults));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			 return this.jsonData;
		}
}
