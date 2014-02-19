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
import java.util.Iterator;

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
		
		Iterator<String> itk = organicKeywords.iterator();
		Iterator<Integer> itv = organicVisits.iterator();
		Iterator<Double> itb = organicBounceRate.iterator();
		organicData = new ArrayList<KeyData>(organicKeywords.size());
	
		while(itk.hasNext()) {
			organicData.add(new KeyData(itk.next(),itv.next(),itb.next()));
		}
      
		Iterator<KeyData> it = organicData.iterator();
		while (it.hasNext()){
			KeyData kd = it.next();
			kd.rank= kd.visits*kd.bounceRate;
			kd.visitsPercent = Math.round(10000.0*kd.visits/(this.organicVisitsTotal-this.privateOrganicVisitsTotal))/100.0;
		}
		
		itk = cpcKeywords.iterator();
		itv = cpcVisits.iterator();
		itb = cpcBounceRate.iterator();
		cpcData = new ArrayList<KeyData>(cpcKeywords.size());
		
		while(itk.hasNext()) {
			cpcData.add(new KeyData(itk.next(),itv.next(),itb.next()));
		}
		
		it = cpcData.iterator();
		while (it.hasNext()){
			KeyData kd = it.next();
			kd.rank= kd.visits*kd.bounceRate;
			kd.visitsPercent = Math.round(10000.0*kd.visits/this.cpcVisitsTotal)/100.0;
		}
		
		// * * * * * * * * * * * * * * * * * * * * *
		// "Consider adding these keywords to AdWords:"
		// select organic keywords with high visits that are not cpc keywords.
		
		
		// * * * * * * * * * * * * * * * * * * * * *
		// "Consider removing these keywords from AdWords:"
		// select cpc keywords with low visits
		ArrayList<KeyData> removeKeywords = new ArrayList<KeyData>();
		it = cpcData.iterator();
		while (it.hasNext()){
			KeyData kd = it.next();
			if (kd.visitsPercent<.1 && kd.visits<2){
				removeKeywords.add(kd);
			}
		}

		// * * * * * * * * * * * * * * * * * * * * *
		// "Change website to better address these keywords:"
		// select keywords with high visits and high bounce rate that
		//    are either organic or cpc keywords.
		 
		// doing only cpc keywords right now
		// select keywords with visits>5% and bouncerate<50%
		
		ArrayList<KeyData> helpKeywords = new ArrayList<KeyData>();
		it = cpcData.iterator();
		while (it.hasNext()){
			KeyData kd = it.next();
			if (kd.visitsPercent>5.0 && kd.bounceRate<50){
				helpKeywords.add(kd);
			}
		}
		
		
		// * * * * * * * * * * * * * * * * * * * * *
		// sort organicData based on rank
		
		
		// sort cpcData based on rank
		

		// Find all keywords that contain the user entered substring
		//    (organic and cpc separate)
		
		
		System.out.println("Keyword Insight model reached");
		int breakpoint = 0;	
		
		// put data into the JSON Object member jsonData
		this.createJson(removeKeywords, helpKeywords); 
		
	}
	
	// put data into JSON object to pass to the view website-performance.jsp 
	
	public void createJson(ArrayList<KeyData> rk, ArrayList<KeyData> hk)  {
		 try {	
			 JSONArray removeKeywords = new JSONArray();
			 JSONArray removeVisitsPercent = new JSONArray();
			 JSONArray helpKeywords = new JSONArray();
			 JSONArray helpVisitsPercent = new JSONArray();
			 JSONArray helpBounceRate = new JSONArray();
			 
			 Iterator<KeyData> it = rk.iterator();
			 while (it.hasNext()){
				 KeyData d = it.next();
				 removeKeywords.put(d.keyword);
				 removeVisitsPercent.put(d.visitsPercent);
			 }
			
			 it = hk.iterator();
			 while (it.hasNext()){
				 KeyData d = it.next();
				 helpKeywords.put(d.keyword);
				 helpVisitsPercent.put(d.visitsPercent);
				 helpBounceRate.put(d.bounceRate);
			 }
			 
			 this.jsonData.put("removeKeywords", removeKeywords);
			 this.jsonData.put("removeVisitsPercent", removeVisitsPercent);
			 this.jsonData.put("helpKeywords", helpKeywords);
			 this.jsonData.put("helpVisitsPercent", helpVisitsPercent);
			 this.jsonData.put("helpBounceRate", helpBounceRate);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	} 
		
	public JSONObject getDataPoints() {
		return this.jsonData;
	}
}

// class to hold related keyword, visits, visitBounceRate

 class KeyData {
	public String keyword;
	public int visits;
	public double bounceRate;
	public double rank;
	public double visitsPercent;
	
	public KeyData(String keyword, int visits, double bounceRate){
		this.keyword = keyword;
		this.visits = visits;
		this.bounceRate = bounceRate;
		this.rank = -1.;
		this.visitsPercent = -1.;
	}
	
}
