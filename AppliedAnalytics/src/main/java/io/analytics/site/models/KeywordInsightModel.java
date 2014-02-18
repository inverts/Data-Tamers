package io.analytics.site.models;

import io.analytics.domain.KeywordInsightData;
import io.analytics.service.interfaces.IKeywordInsightService;
import io.analytics.service.interfaces.ISessionService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class KeywordInsightModel {
	private JSONObject jsonData;
	private IKeywordInsightService keywordInsightService;
	private ISessionService sessionService;
	private String activeProfile;
	private Date startDate;
	private Date endDate;
	private ArrayList<String> cpcKeywords;
	private ArrayList<String> organicKeywords;
	private ArrayList<Integer> cpcVisits;
	private ArrayList<Integer> organicVisits;
	private ArrayList<Double> cpcBounceRate;
	private ArrayList<Double> organicBounceRate;
	private int cpcVisitsTotal;
	private int privateOrganicVisitsTotal;
	private int organicVisitsTotal;
	private ArrayList<KeyData> cpcData;
	private ArrayList<KeyData> organicData;
    private String keywordSubstring;
	
    DateFormat presentationFormat = new SimpleDateFormat("MM/dd/yyyy"); 
    
	public KeywordInsightModel(ISessionService sessionService, IKeywordInsightService keywordInsightService) {	
		this.sessionService = sessionService;
		this.keywordInsightService = keywordInsightService;
		this.jsonData = new JSONObject();
		this.activeProfile = this.keywordInsightService.getProfile();
		
		// default dates
		this.endDate = new Date();
		Calendar cal = new GregorianCalendar();
		cal.setTime(this.endDate);
		cal.add(Calendar.DAY_OF_MONTH, -30);
		this.startDate = cal.getTime();
        this.keywordSubstring = "";
        this.cpcData = new ArrayList<KeyData>();
        this.organicData = new ArrayList<KeyData>();
        
        updateData();
		
	}
	
	public String getName() {
		return "Keyword Insight";
	}

	public String getDescription() {
		return "View suggestions for adding or removing keywords from Adwords and which keywords to support better.";
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
	
		KeywordInsightData dataObject = this.keywordInsightService.getKeywordInsightData(this.sessionService.getCredentials(), this.sessionService.getUserSettings().getActiveProfile().getId(), this.startDate, this.endDate, 500, this.keywordSubstring);
		organicKeywords = dataObject.getOrganicKeywords();
		cpcKeywords = dataObject.getCpcKeywords();
		organicVisits = dataObject.getOrganicVisits();
		cpcVisits = dataObject.getCpcVisits();
		organicBounceRate = dataObject.getOrganicVisitBounceRate();
		cpcBounceRate = dataObject.getCpcVisitBounceRate();	
		privateOrganicVisitsTotal = dataObject.getPrivateOrganicVisitsTotal();
		organicVisitsTotal = dataObject.getOrganicVisitsTotal();
		cpcVisitsTotal = dataObject.getCpcVisitsTotal();
		
		for (int i=0; i<organicKeywords.size(); i++){	
			organicData.add(new KeyData(organicKeywords.get(i),organicVisits.get(i),organicBounceRate.get(i)));
		}
		for (int i=0; i<cpcKeywords.size(); i++){
			cpcData.add(new KeyData(cpcKeywords.get(i),cpcVisits.get(i),cpcBounceRate.get(i)));
		}
		
		System.out.println("keyword Insight model reached");
		int breakpoint = 0;
	/*	ArrayList<Double> weightedAvg = new ArrayList<Double>(pagePath.size());
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
		
		this.getDataPoints(); */
	}
	
	// put data into JSON object to pass to the view website-performance.jsp 
	
/*	public JSONObject getDataPoints()  {
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
	} */
		
	
}

// class to hold related keyword, visits, visitBounceRate

class KeyData {
	public String keyword;
	public int visits;
	public double bounceRate;
	
	public KeyData(String keyword, int visits, double bounceRate){
		this.keyword = keyword;
		this.visits = visits;
		this.bounceRate = bounceRate;
	}
	
	public String getKeyword(){
		return new String(this.keyword);
	}
	
	public int getVisits(){
		return this.visits;
	}
	
	public double getBounceRate(){
		return this.bounceRate;
	}
}
