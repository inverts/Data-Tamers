package io.analytics.site.models.widgets;

import io.analytics.domain.KeywordInsightData;
import io.analytics.service.interfaces.IKeywordInsightService;
import io.analytics.service.interfaces.ISessionService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class KeywordInsightModel extends WidgetModel {
	private JSONObject jsonData;
	private JSONObject jsonScatterPlotData;
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
    
    private final String widgetClass = "keywordInsight";
    private final String widgetTitle = "keywordinsight.title";
	
    DateFormat presentationFormat = new SimpleDateFormat("MM/dd/yyyy"); 
    
	public KeywordInsightModel(ISessionService sessionService, IKeywordInsightService keywordInsightService) {	
		this.sessionService = sessionService;
		this.keywordInsightService = keywordInsightService;
		this.jsonData = new JSONObject();
		this.jsonScatterPlotData = new JSONObject();
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
		// over quota error returns no data, try again
		if (dataObject==null){
			this.jsonData=null;
			return;
		}
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
			organicData.add(new KeyData(itk.next(),itv.next(),itb.next(), "organic"));
		}
      
		Iterator<KeyData> it = organicData.iterator();
		while (it.hasNext()){
			KeyData kd = it.next();
			kd.visitsPercent = Math.round(10000.0*kd.visits/(this.organicVisitsTotal-this.privateOrganicVisitsTotal))/100.0;
			kd.multipageVisitsPercent= kd.visitsPercent*(1.0-kd.bounceRate/100.0);
			}
		Collections.sort(organicData);
		
		itk = cpcKeywords.iterator();
		itv = cpcVisits.iterator();
		itb = cpcBounceRate.iterator();
		cpcData = new ArrayList<KeyData>(cpcKeywords.size());
		
		while(itk.hasNext()) {
			cpcData.add(new KeyData(itk.next(),itv.next(),itb.next(), "cpc"));
		}
		
		it = cpcData.iterator();
		while (it.hasNext()){
			KeyData kd = it.next();
			kd.visitsPercent = Math.round(10000.0*kd.visits/this.cpcVisitsTotal)/100.0;
			kd.multipageVisitsPercent= kd.visitsPercent*(1.0-kd.bounceRate/100.0);
		}
		// order ascending
		Collections.sort(cpcData);
		
		int breakpoint = 0;	
		int val = breakpoint;
		
		// * * * * * * * * * * * * * * * * * * * * *
		// "Consider removing these keywords from AdWords:"
		// select cpc keywords with low visits
		ArrayList<KeyData> removeKeywords = new ArrayList<KeyData>();
		
		KeyData minKd;
		it = cpcData.iterator();
		if (it.hasNext()){
			 minKd = it.next();
			 // if the rank 
			 if (minKd.multipageVisitsPercent < 1.e-9){
				 removeKeywords.add(minKd);
				 
				 while (it.hasNext()){
						KeyData kd = it.next();
						if (Math.abs((minKd.multipageVisitsPercent - kd.multipageVisitsPercent)) < 1e-9) {
							removeKeywords.add(kd);
						}
						else {
							break;
						}
				}
			 }
		}

		// * * * * * * * * * * * * * * * * * * * * *
		// "Change website to better address these keywords:"
		// select keywords with high visits and high bounce rate that
		//    are either organic or cpc keywords.
		 
		// only cpc keywords right now
		// select keywords with visits>5% and bouncerate>50%
		
		ArrayList<KeyData> helpKeywords = new ArrayList<KeyData>();
		it = cpcData.iterator();
		while (it.hasNext()){
			KeyData kd = it.next();
			if (kd.multipageVisitsPercent>=1.0 && kd.bounceRate>=50){
				helpKeywords.add(kd);
			}
		}
		// order descending
		Collections.reverse(helpKeywords);
		
		// * * * * * * * * * * * * * * * * * * * * *
		// Find best performing keywords
		//    (organic and cpc separate)
		
		// only cpc right now
	
		ArrayList<KeyData> bestKeywords = new ArrayList<KeyData>();
		it = cpcData.iterator();
		while (it.hasNext()){
			KeyData kd = it.next();
			if (kd.multipageVisitsPercent>=1.0 && kd.bounceRate<50){
				bestKeywords.add(kd);
			}
		}
		// order descending
		Collections.reverse(bestKeywords);
		
		// * * * * * * * * * * * * * * * * * * * * *
		// List all keywords
		//    (organic and cpc separate)
		
		// only cpc right now
		ArrayList<KeyData> allCpcKeywords = new ArrayList<KeyData>();
		allCpcKeywords.addAll(cpcData);
		// order descending
		Collections.reverse(allCpcKeywords);
		
		// * * * * * * * * * * * * * * * * * * * * *
		// Find all keywords that contain the user entered substring
		//    (organic and cpc separate)
		
		// Find word substrings: parse words and make a set
		
		// Collect a list of all words including duplicates
		ArrayList<String> allWords = new ArrayList<String>();
		it = cpcData.iterator();
		while (it.hasNext()){
			KeyData kd = it.next();
			allWords.addAll(Arrays.asList(kd.keyword.split(" ")));
		}
		
		// eliminate duplicates
		Set<String> wordsSet = new HashSet<String>(allWords);
		// count duplicates
		ArrayList<WordCount> words = new ArrayList<WordCount>();
		for ( String word : wordsSet) {
			words.add(new WordCount(word, Collections.frequency(allWords, word))) ; 
		}
		
		// add up multipage visits for each keyword with word
		Iterator<WordCount> itwc = words.iterator();
		while (itwc.hasNext()) {
			WordCount wordCount = itwc.next();
			it = cpcData.iterator();
			while (it.hasNext()){
				KeyData kd = it.next();
				if (kd.keyword.contains(wordCount.word)){
					wordCount.multipageVisitsPercent += kd.multipageVisitsPercent;
				}
			}
		}
		
		// sort ascending
		Collections.sort(words);
		// Pick off first worst words if they perform at 0
		ArrayList<WordCount> worstWords = new ArrayList<WordCount>();
	    itwc = words.iterator();
	    while (itwc.hasNext()){
	       WordCount wordCount = itwc.next();
	       if (wordCount.multipageVisitsPercent < 1e-9) {
	    	   worstWords.add(wordCount);
	        }
	        else {
	        	break;
	        }
	    }
		
	    // sort descending
		Collections.reverse(words);
		// Pick off first best words if the perform well
		ArrayList<WordCount> bestWords = new ArrayList<WordCount>();
	    itwc = words.iterator();
	    while (itwc.hasNext()){
	        WordCount wordCount = itwc.next();
	        if (wordCount.multipageVisitsPercent >= 5.0) {
	        	bestWords.add(wordCount);
	        }
	        else{
	        	break;
	        }
	    }
		
		// * * * * * * * * * * * * * * * * * * * * *
		// "Consider adding these keywords to AdWords:"
		// select organic keywords with high visits that are not cpc keywords.
		
		
		// put data into the JSON Object member jsonData and jsonScatterPlotData
	    this.createScatterPlotJson(allCpcKeywords);
		this.createJson(removeKeywords, helpKeywords, bestKeywords, allCpcKeywords, words, worstWords, bestWords); 
	}
	
	// put data into JSON object to pass to the view website-performance.jsp 
	
	public void createJson(ArrayList<KeyData> rk, ArrayList<KeyData> hk, ArrayList<KeyData> bk, ArrayList<KeyData> ak, ArrayList<WordCount> wc, ArrayList<WordCount> ww, ArrayList<WordCount> bw)  {
		 try {	
			 JSONArray removeKeywords = new JSONArray();
			 JSONArray removeVisitsPercent = new JSONArray();
			 JSONArray removeBounceRate = new JSONArray();
			 JSONArray removeMultipageVisitsPercent = new JSONArray();
			 JSONArray helpKeywords = new JSONArray();
			 JSONArray helpVisitsPercent = new JSONArray();
			 JSONArray helpBounceRate = new JSONArray();
			 JSONArray helpMultipageVisitsPercent = new JSONArray();
			 JSONArray bestKeywords = new JSONArray();
			 JSONArray bestVisitsPercent = new JSONArray();
			 JSONArray bestBounceRate = new JSONArray();
			 JSONArray bestMultipageVisitsPercent = new JSONArray();
			 JSONArray allCpcKeywords = new JSONArray();
			 JSONArray allCpcVisitsPercent = new JSONArray();
			 JSONArray allCpcBounceRate = new JSONArray();
			 JSONArray allCpcMultipageVisitsPercent = new JSONArray();
			 JSONArray words = new JSONArray();
			 JSONArray wordCount = new JSONArray();
			 JSONArray multipageVisitsPercent = new JSONArray();
			 JSONArray worstWords = new JSONArray();
			 JSONArray worstWordsCount = new JSONArray();
			 JSONArray worstWordsMultipageVisitsPercent = new JSONArray();
			 JSONArray bestWords = new JSONArray();
			 JSONArray bestWordsCount = new JSONArray();
			 JSONArray bestWordsMultipageVisitsPercent = new JSONArray();
			 
			 Iterator<KeyData> it = rk.iterator();
			 while (it.hasNext()){
				 KeyData d = it.next();
				 removeKeywords.put(d.keyword);
				 removeVisitsPercent.put(d.visitsPercent);
				 removeBounceRate.put(d.bounceRate);
				 removeMultipageVisitsPercent.put(d.multipageVisitsPercent);
			 }
			
			 it = hk.iterator();
			 while (it.hasNext()){
				 KeyData d = it.next();
				 helpKeywords.put(d.keyword);
				 helpVisitsPercent.put(d.visitsPercent);
				 helpBounceRate.put(d.bounceRate);
				 helpMultipageVisitsPercent.put(d.multipageVisitsPercent);
			 }
			 
			 it = bk.iterator();
			 while (it.hasNext()){
				 KeyData d = it.next();
				 bestKeywords.put(d.keyword);
				 bestVisitsPercent.put(d.visitsPercent);
				 bestBounceRate.put(d.bounceRate);
				 bestMultipageVisitsPercent.put(d.multipageVisitsPercent);
			 }

			 it = ak.iterator();
			 while (it.hasNext()){
				 KeyData d = it.next();
				 allCpcKeywords.put(d.keyword);
				 allCpcVisitsPercent.put(d.visitsPercent);
				 allCpcBounceRate.put(d.bounceRate);
				 allCpcMultipageVisitsPercent.put(d.multipageVisitsPercent);
			 }
			 
			 Iterator<WordCount> itwc = wc.iterator();
			 while (itwc.hasNext()){
				 WordCount d = itwc.next();
				 words.put(d.word);
				 wordCount.put(d.count);
				 multipageVisitsPercent.put(d.multipageVisitsPercent);
			 }
			 
			 itwc = ww.iterator();
			 while (itwc.hasNext()){
				 WordCount d = itwc.next();
				 worstWords.put(d.word);
				 worstWordsCount.put(d.count);
				 worstWordsMultipageVisitsPercent.put(d.multipageVisitsPercent);
			 }
			 
			 itwc = bw.iterator();
			 while (itwc.hasNext()){
				 WordCount d = itwc.next();
				 bestWords.put(d.word);
				 bestWordsCount.put(d.count);
				 bestWordsMultipageVisitsPercent.put(d.multipageVisitsPercent);
			 }
			 
			 this.jsonData.put("removeKeywords", removeKeywords);
			 this.jsonData.put("removeVisitsPercent", removeVisitsPercent);
			 this.jsonData.put("removeBounceRate", removeBounceRate);
			 this.jsonData.put("removeMultipageVisitsPercent", removeMultipageVisitsPercent);
			 this.jsonData.put("helpKeywords", helpKeywords);
			 this.jsonData.put("helpVisitsPercent", helpVisitsPercent);
			 this.jsonData.put("helpBounceRate", helpBounceRate);
			 this.jsonData.put("helpMultipageVisitsPercent", helpMultipageVisitsPercent);
			 this.jsonData.put("bestKeywords", bestKeywords);
			 this.jsonData.put("bestVisitsPercent", bestVisitsPercent);
			 this.jsonData.put("bestBounceRate", bestBounceRate);
			 this.jsonData.put("bestMultipageVisitsPercent", bestMultipageVisitsPercent);
			 this.jsonData.put("allCpcKeywords", allCpcKeywords);
			 this.jsonData.put("allCpcVisitsPercent", allCpcVisitsPercent);
			 this.jsonData.put("allCpcBounceRate", allCpcBounceRate);
			 this.jsonData.put("allCpcMultipageVisitsPercent", allCpcMultipageVisitsPercent);
			 this.jsonData.put("words", words);
			 this.jsonData.put("wordCount", wordCount);
			 this.jsonData.put("multipageVisitsPercent", multipageVisitsPercent);
			 this.jsonData.put("worstWords", worstWords);
			 this.jsonData.put("worstWordsCount", worstWordsCount);
			 this.jsonData.put("worstWordsMultipageVisitsPercent", worstWordsMultipageVisitsPercent);
			 this.jsonData.put("bestWords", bestWords);
			 this.jsonData.put("bestWordsCount", bestWordsCount);
			 this.jsonData.put("bestWordsMultipageVisitsPercent", bestWordsMultipageVisitsPercent);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	} 
	
	public void createScatterPlotJson(ArrayList<KeyData> ak){
		 JSONArray allKeywords = new JSONArray();
		 JSONArray allVisitsPercent = new JSONArray();
		 JSONArray allBounceRate = new JSONArray();
		 JSONArray allMultipageVisitsPercent = new JSONArray();
		 
		 try {
		 Iterator<KeyData> it = ak.iterator();
		 while (it.hasNext()){
			 KeyData d = it.next();
			 allKeywords.put(d.keyword);			
			 allVisitsPercent.put(d.visitsPercent);			
			 allBounceRate.put(d.bounceRate);
			 allMultipageVisitsPercent.put(Math.round(100.0*d.multipageVisitsPercent)/100.0);
		 }
		 
		 this.jsonScatterPlotData.put("allKeywords", allKeywords);
		 this.jsonScatterPlotData.put("allVisitsPercent", allVisitsPercent);
		 this.jsonScatterPlotData.put("allBounceRate", allBounceRate);
		 this.jsonScatterPlotData.put("allMultipageVisitsPercent", allMultipageVisitsPercent);
		 } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	public JSONObject getDataPoints() {
		return this.jsonData;
	}
	
	public JSONObject getScatterPlotDataPoints() {
		return this.jsonScatterPlotData;
	}

	@Override
	public String getJSONSerialization() {
		JSONObject result = new JSONObject();
		try {
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

// class to hold related keyword, visits, visitBounceRate

 class KeyData implements Comparable<KeyData>{
	public String keyword;
	public int visits;
	public double bounceRate;
	public double multipageVisitsPercent;
	public double visitsPercent;
	public String medium;
	
	public KeyData(String keyword, int visits, double bounceRate, String medium){
		this.keyword = keyword;
		this.visits = visits;
		this.bounceRate = Math.round(100.0*bounceRate)/100.0;
		this.multipageVisitsPercent = -1.;
		this.visitsPercent = -1.;
		this.medium = medium;
	}
	
	public int compareTo(KeyData kd){
		return Double.compare(this.multipageVisitsPercent, kd.multipageVisitsPercent);
	}
	
}
 
//class to holds word, a frequency count, a rank
class WordCount implements Comparable<WordCount>{
	public String word;
	public int count;
	public double multipageVisitsPercent;
	
	public WordCount(String word, int count){
		this.word = word;
		this.count = count;
		this.multipageVisitsPercent = 0;
	}
	
	public int compareTo(WordCount kc){
		return Double.compare(this.multipageVisitsPercent, kc.multipageVisitsPercent);
	}
	
}
