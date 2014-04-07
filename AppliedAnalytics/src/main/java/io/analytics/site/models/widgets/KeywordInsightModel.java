package io.analytics.site.models.widgets;

import io.analytics.domain.KeywordInsightData;
import io.analytics.service.interfaces.IKeywordInsightService;
import io.analytics.service.interfaces.ISessionService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class KeywordInsightModel extends WidgetModel {
	private JSONObject jsonData;
	private IKeywordInsightService keywordInsightService;
	private ISessionService sessionService;
	private String activeProfile;
	private Date startDate;
	private Date endDate;
	private ArrayList<String> keywords;
	//private ArrayList<String> organicKeywords;
	private ArrayList<Integer> visits;
	//private ArrayList<Integer> organicVisits;
	private ArrayList<Double> bounceRate;
	//private ArrayList<Double> organicBounceRate;
	private int visitsGaTotal;
	//private int privateOrganicVisitsTotal;
	//private int organicVisitsTotal;
	private ArrayList<String> medium;
	private ArrayList<String> hostname;
	private ArrayList<KeyData> cpcData;
	private ArrayList<KeyData> organicData;
	private Set<String> stopWordsSet;

	private final String widgetClass = "keywordInsight";
	private final String widgetTitle = "keywordinsight.title";

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

		this.cpcData = new ArrayList<KeyData>();
		this.organicData = new ArrayList<KeyData>();		
		this.stopWordsSet = new HashSet<String>();

		// read stopwords file into map
		try {
			// stopwords.txt is located in src/main/resources directory
			Resource resource = new ClassPathResource("stopWords.txt");
			InputStream is = resource.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = br.readLine()) != null){
				this.stopWordsSet.add(line);
				//System.out.println(line);
			}
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		} 
		this.viewCount = 3;
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
	 * 
	 * TODO: http://en.wikipedia.org/wiki/Refactor
	 */
	public void updateData() {

		KeywordInsightData dataObject = this.keywordInsightService.getKeywordInsightData(this.sessionService.getCredentials(), this.sessionService.getUserSettings().getActiveProfile().getId(), this.startDate, this.endDate, 1500);
		// over quota error returns no data, try again
		if (dataObject==null){
			this.jsonData=null;
			return;
		}
		//organicKeywords = dataObject.getOrganicKeywords();
		keywords = dataObject.getKeywords();
		//organicVisits = dataObject.getOrganicVisits();
		visits = dataObject.getVisits();
		//organicBounceRate = dataObject.getOrganicVisitBounceRate();
		bounceRate = dataObject.getVisitBounceRate();	
		//privateOrganicVisitsTotal = dataObject.getPrivateOrganicVisitsTotal();
		//organicVisitsTotal = dataObject.getOrganicVisitsTotal();
		visitsGaTotal = dataObject.getVisitsTotal();
		medium = dataObject.getMedium();
		hostname = dataObject.getHostname();
		/*	
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
			kd.multipageVisitsPercent= Math.round(100.0*kd.visitsPercent*(1.0-kd.bounceRate/100.0))/100.0;
		}
		Collections.sort(organicData);
		 */
		Iterator<String> itk = keywords.iterator();
		Iterator<Integer> itv = visits.iterator();
		Iterator<Double> itb = bounceRate.iterator();
		Iterator<String> itm = medium.iterator();
		cpcData = new ArrayList<KeyData>();
		organicData = new ArrayList<KeyData>();

		String keyword = "";
		String mediumVal = "";
		int v = 0;
		int countOtherMed = 0;
		int visitsCpcTotal = 0;
		int visitsOrganicTotal = 0;

		while(itk.hasNext()) {
			keyword = itk.next();
			// remove non-alpha characters from keywords
			//keyword = keyword.replaceAll("[^a-zA-Z0-9 ]","");

			mediumVal = itm.next();
			v = itv.next();
			if (mediumVal.equals("cpc")) {
				cpcData.add(new KeyData(keyword,v,itb.next()));
				visitsCpcTotal += v;
			} else if (mediumVal.equals("organic")) {
				organicData.add(new KeyData(keyword,v,itb.next()));
				visitsOrganicTotal += v;
			} else {
				countOtherMed++;
			}
		}

		System.out.println("Number of keywords with a medium other than cpc or organic = "+countOtherMed);
		System.out.println("Visits: cpc = "+visitsCpcTotal+" organic = "+visitsOrganicTotal+" GA total = "+visitsGaTotal);


		ArrayList<KeyData> data;
		ArrayList<WordData> bestWords = new ArrayList<WordData>();
		ArrayList<String> allWords = new ArrayList<String>(); 
		ArrayList<WordData> worstWords = new ArrayList<WordData>();
		ArrayList<KeyData> removeKeywords = new ArrayList<KeyData>();
		ArrayList<KeyData> helpKeywords = new ArrayList<KeyData>();
		ArrayList<KeyData> bestKeywords = new ArrayList<KeyData>();
		ArrayList<KeyData> allKeywords = new ArrayList<KeyData>();
		ArrayList<WordData> words = new ArrayList<WordData>();
		Map<String,WordData> wordMap = new HashMap<String,WordData>();

		this.createJson(removeKeywords, helpKeywords, bestKeywords, allKeywords, words, worstWords, bestWords); 

		int n = 1;
		if (visitsCpcTotal>0)
			n = 2;

		for (int i=0; i<n; i++){
			if (i==0){
				data = organicData;
				System.out.println("Organic loop");
			} else {
				data = cpcData;
				removeKeywords.clear();
				helpKeywords.clear();
				bestKeywords.clear();
				allKeywords.clear();
				words.clear();
				worstWords.clear();
				bestWords.clear();
				wordMap.clear();
				System.out.println("CPC loop");
			}
			Iterator<KeyData> it = data.iterator();
			while (it.hasNext()){
				KeyData kd = it.next();
				kd.visitsPercent = Math.round(10000.0*kd.visits/(visitsCpcTotal+visitsOrganicTotal))/100.0;
				kd.multipageVisitsPercent= Math.round(100.0*kd.visitsPercent*(1.0-kd.bounceRate/100.0))/100.0;
			}
			// order ascending
			Collections.sort(data);

			int breakpoint = 0;	
			int val = breakpoint;

			// * * * * * * * * * * * * * * * * * * * * *
			// "Consider removing these keywords from AdWords:"
			// select cpc keywords with low visits

			KeyData minKd;
			it = data.iterator();
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


			it = data.iterator();
			while (it.hasNext()){
				KeyData kd = it.next();
				if (kd.multipageVisitsPercent>=1.0 && kd.bounceRate>=50){
					kd.multipageVisitsPercent = Math.round(100.0*kd.multipageVisitsPercent)/100.0;
					helpKeywords.add(kd);
				}
			}
			// sort ascending
			Collections.sort(helpKeywords);
			// reverse so descending
			Collections.reverse(helpKeywords);

			// * * * * * * * * * * * * * * * * * * * * *
			// Find best performing keywords
			//    (organic and cpc separate)

			// only cpc right now


			it = data.iterator();
			while (it.hasNext()){
				KeyData kd = it.next();
				if (kd.multipageVisitsPercent>=.5) {//&& kd.bounceRate<50){
					kd.multipageVisitsPercent = Math.round(100.0*kd.multipageVisitsPercent)/100.0;
					bestKeywords.add(kd);
				}
			}
			// sort ascending
			Collections.sort(bestKeywords);
			// reverse so descending
			Collections.reverse(bestKeywords);

			// * * * * * * * * * * * * * * * * * * * * *
			// List all keywords
			//    (organic and cpc separate)

			// only cpc right now

			allKeywords.addAll(data);
			// sort ascending
			Collections.sort(allKeywords);
			// then reverse so descending			
			Collections.reverse(allKeywords);

			// * * * * * * * * * * * * * * * * * * * * *
			// Find all keywords that contain the user entered substring
			//    (organic and cpc separate)

			// Find word substrings: parse words and make a set

			// Collect a list of all words including duplicates

			List<String> newWordList = new ArrayList<String>();


			it = data.iterator();
			while (it.hasNext()){
				KeyData kd = it.next();
				newWordList = Arrays.asList(kd.keyword.split(" "));

				for (int j=0; j<newWordList.size(); j++){
					String word = newWordList.get(j);
					// remove non-alpha characters from beginning and end of word
					word = word.replaceAll("^[^a-zA-Z0-9 ]+","");
					word = word.replaceAll("[^a-zA-Z0-9 ]+$","");

					// update word for keyword obj list
					newWordList.set(j, word);

					// leave stopwords or numbers off the map word list
					if (stopWordsSet.contains(word) || word.matches("[0-9]*")){
						continue;
					}
					// add word and update multipage visits
					if (wordMap.containsKey(word)){
						WordData wd = wordMap.get(word);
						wd.count += 1;
						wd.multipageVisitsPercent += kd.multipageVisitsPercent;
						wordMap.put(word, wd);
					} else {
						wordMap.put(word, new WordData(word, 1, kd.multipageVisitsPercent));
					}
				}
				kd.setWordList(newWordList);
			}



			// combine data for words that are substrings of the url into the url	

			String url = hostname.get(0);
			//urlCore = url.replaceAll("www.","");
			//urlCore = urlCore.replaceAll(".com","");
			//WordData wdUrlCore = new WordData(urlCore, 0);
			WordData wdUrl = new WordData(url, 0);
			Iterator<Map.Entry<String,WordData>> itwm = wordMap.entrySet().iterator();
			while (itwm.hasNext()) {
				Map.Entry<String,WordData> e = itwm.next();
				if (url.contains(e.getValue().word)) {
					wdUrl.count += e.getValue().count;
					wdUrl.multipageVisitsPercent += e.getValue().multipageVisitsPercent;
					itwm.remove();
				}
			}

			//if url is substring of a word add, wdUrl data to it o.w. add wdUrl
			boolean isUrl = false;
			itwm = wordMap.entrySet().iterator();
			while (itwm.hasNext()) {
				Map.Entry<String,WordData> e = itwm.next();
				if (e.getValue().word.contains(wdUrl.word)) {
					e.getValue().count += wdUrl.count;
					e.getValue().multipageVisitsPercent += wdUrl.multipageVisitsPercent;
					isUrl = true;
				}
			}
			if (!isUrl){
				wordMap.put(wdUrl.word,wdUrl);
			}

			words.addAll(wordMap.values());

			// sort ascending
			Collections.sort(words);

			// Pick off first worst words if they perform at 0
			Iterator<WordData>itw = words.iterator();
			while (itw.hasNext()){
				WordData wordData = itw.next();
				if (wordData.multipageVisitsPercent < 1e-9) {
					worstWords.add(wordData);
				}
				else {
					break;
				}
			}
			// sort ascending 
			Collections.sort(worstWords);

			// reverse so is descending
			Collections.reverse(words);

			// Pick off first best words if the perform well
			itw = words.iterator();
			while (itw.hasNext()){
				WordData wordData = itw.next();
				if (wordData.multipageVisitsPercent >= .5) {
					wordData.multipageVisitsPercent = Math.round(100.0*wordData.multipageVisitsPercent)/100.0;
					bestWords.add(wordData);
				}
				else{
					break;
				}
			}
			// sort descending (sort then reverse)
			Collections.sort(bestWords);
			Collections.reverse(bestWords);

		}

		System.out.println("Hostname = "+hostname.get(0));

		// * * * * * * * * * * * * * * * * * * * * *
		// "Consider adding these keywords to AdWords:"
		// select organic keywords with high visits that are not cpc keywords.

		// put data into the JSON Object member jsonData
		this.createJson(removeKeywords, helpKeywords, bestKeywords, allKeywords, words, worstWords, bestWords); 

	}


	public int editDistance (String s0, String s1) {
		int len0 = s0.length()+1;
		int len1 = s1.length()+1;

		// the array of distances
		int[] cost = new int[len0];
		int[] newcost = new int[len0];

		// initial cost of skipping prefix in String s0
		for(int i=0;i<len0;i++) cost[i]=i;

		// dynamically computing the array of distances

		// transformation cost for each letter in s1
		for(int j=1;j<len1;j++) {

			// initial cost of skipping prefix in String s1
			newcost[0]=j-1;

			// transformation cost for each letter in s0
			for(int i=1;i<len0;i++) {

				// matching current letters in both strings
				int match = (s0.charAt(i-1)==s1.charAt(j-1))?0:1;

				// computing cost for each transformation
				int cost_replace = cost[i-1]+match;
				int cost_insert  = cost[i]+1;
				int cost_delete  = newcost[i-1]+1;

				// keep minimum cost
				newcost[i] = Math.min(Math.min(cost_insert, cost_delete),cost_replace );
			}

			// swap cost/newcost arrays
			int[] swap=cost; cost=newcost; newcost=swap;
		}

		// the distance is the cost for transforming all letters in both strings
		return cost[len0-1];
	}

	// put data into JSON object to pass to the view website-performance.jsp 

	public void createJson(ArrayList<KeyData> rk, ArrayList<KeyData> hk, ArrayList<KeyData> bk, ArrayList<KeyData> ak, ArrayList<WordData> wc, ArrayList<WordData> ww, ArrayList<WordData> bw)  {
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
			JSONArray wordData = new JSONArray();
			JSONArray multipageVisitsPercent = new JSONArray();
			JSONArray worstWords = new JSONArray();
			JSONArray worstWordsCount = new JSONArray();
			JSONArray worstWordsMultipageVisitsPercent = new JSONArray();
			JSONArray bestWords = new JSONArray();
			JSONArray bestWordsCount = new JSONArray();
			JSONArray bestWordsMultipageVisitsPercent = new JSONArray();
			JSONArray allKeywords = new JSONArray();
			JSONArray allVisitsPercent = new JSONArray();
			JSONArray allBounceRate = new JSONArray();
			JSONArray allMultipageVisitsPercent = new JSONArray();

			Iterator<KeyData> it = rk.iterator();
			while (it.hasNext()){
				KeyData d = it.next();
				removeKeywords.put(d.keyword);
				removeVisitsPercent.put(d.visitsPercent);
				removeBounceRate.put(d.bounceRate);
				removeMultipageVisitsPercent.put(Math.round(100.0*d.multipageVisitsPercent)/100.0);
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

			Iterator<WordData> itwc = wc.iterator();
			while (itwc.hasNext()){
				WordData d = itwc.next();
				words.put(d.word);
				wordData.put(d.count);
				multipageVisitsPercent.put(d.multipageVisitsPercent);
			}

			itwc = ww.iterator();
			while (itwc.hasNext()){
				WordData d = itwc.next();
				worstWords.put(d.word);
				worstWordsCount.put(d.count);
				worstWordsMultipageVisitsPercent.put(d.multipageVisitsPercent);
			}

			itwc = bw.iterator();
			while (itwc.hasNext()){
				WordData d = itwc.next();
				bestWords.put(d.word);
				bestWordsCount.put(d.count);
				bestWordsMultipageVisitsPercent.put(d.multipageVisitsPercent);
			}

			// Scatter plot data
			it = ak.iterator();
			while (it.hasNext()){
				KeyData d = it.next();
				allKeywords.put(d.keyword);			
				allVisitsPercent.put(d.visitsPercent);			
				allBounceRate.put(d.bounceRate);
				allMultipageVisitsPercent.put(Math.round(100.0*d.multipageVisitsPercent)/100.0);
			}

			String[] keys1 = new String[]{"Keywords", "Visits (%)", "Bounce Rate (%)", "Multipage Visits (%)"};
			String[] keys2 = new String[]{"Word Substring", "Keyword Count", "Multipage Visits (%)"};
			String[] scatterKeys = new String[]{"allBounceRate", "allVisitsPercent"};
			String[] scatterAxisLabels = new String[]{"Bounce Rate (%)", "Visits (%)"};

			/* Scatterplot */
			JSONObject scatter = new JSONObject();

			scatter.put("title", "Keyword Insight Plot");
			scatter.put("label", scatterAxisLabels);

			scatter.put("allKeywords", allKeywords);
			scatter.put("allVisitsPercent", allVisitsPercent);
			scatter.put("allBounceRate", allBounceRate);
			scatter.put("allMultipageVisitsPercent", allMultipageVisitsPercent);
			scatter.put("keys", scatterKeys);
			/* Improve Keywords */
			JSONObject improve = new JSONObject();

			// title
			improve.put("title", "Improve website performance for these keywords:");

			// JSON key is the row category for use in Javascript
			improve.put(keys1[0], helpKeywords);
			improve.put(keys1[1], helpVisitsPercent);
			improve.put(keys1[2], helpBounceRate);
			improve.put(keys1[3], multipageVisitsPercent);
			improve.put("keys", keys1);

			/* Best Keywords */
			JSONObject best = new JSONObject();

			best.put("title", "Best performing keywords:");

			best.put(keys1[0], bestKeywords);
			best.put(keys1[1], bestVisitsPercent);
			best.put(keys1[2], bestBounceRate);
			best.put(keys1[3], bestMultipageVisitsPercent);
			best.put("keys", keys1);

			/* Remove Keywords */
			JSONObject worst = new JSONObject();

			worst.put("title", "Worst performing keywords:");

			worst.put(keys1[0], removeKeywords);
			worst.put(keys1[1], removeVisitsPercent);
			worst.put(keys1[2], removeBounceRate);
			worst.put(keys1[3], removeMultipageVisitsPercent);
			worst.put("keys", keys1);

			/* All Keywords */
			JSONObject all = new JSONObject();

			// title
			all.put("title", "All keywords:");

			all.put(keys1[0], allCpcKeywords);
			all.put(keys1[1], allCpcVisitsPercent);
			all.put(keys1[2], allCpcBounceRate);
			all.put(keys1[3], allCpcMultipageVisitsPercent);
			all.put("keys", keys1);

			/* Best Keyword Substrings */
			JSONObject bestSubStr = new JSONObject();

			bestSubStr.put("title", "Best performing keyword substrings:");

			bestSubStr.put(keys2[0], bestWords);
			bestSubStr.put(keys2[1], bestWordsCount);
			bestSubStr.put(keys2[2], bestWordsMultipageVisitsPercent);
			bestSubStr.put("keys", keys2);

			/* Worst Keyword Substrings */
			JSONObject worstSubStr = new JSONObject();

			worstSubStr.put("title", "Worst performing keyword substrings:");

			worstSubStr.put(keys2[0], worstWords);
			worstSubStr.put(keys2[1], worstWordsCount);
			worstSubStr.put(keys2[2], worstWordsMultipageVisitsPercent);
			worstSubStr.put("keys", keys2);


			/*
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
			 this.jsonData.put("wordData", wordData);
			 this.jsonData.put("multipageVisitsPercent", multipageVisitsPercent);
			 this.jsonData.put("worstWords", worstWords);
			 this.jsonData.put("worstWordsCount", worstWordsCount);
			 this.jsonData.put("worstWordsMultipageVisitsPercent", worstWordsMultipageVisitsPercent);
			 this.jsonData.put("bestWords", bestWords);
			 this.jsonData.put("bestWordsCount", bestWordsCount);
			 this.jsonData.put("bestWordsMultipageVisitsPercent", bestWordsMultipageVisitsPercent);
			 */
			this.jsonData.put("scatter", scatter);
			this.jsonData.put("improve", improve);
			this.jsonData.put("best", best);
			this.jsonData.put("worst", worst);
			this.jsonData.put("all", all);
			this.jsonData.put("bestsubstr", bestSubStr);
			this.jsonData.put("worstsubstr", worstSubStr);

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

// class to hold related keyword, visits, visitBounceRate

class KeyData implements Comparable<KeyData>{
	public String keyword;
	public int visits;
	public double bounceRate;
	public double multipageVisitsPercent;
	public double visitsPercent;
	private List<String> wordList = new ArrayList<String>();

	public KeyData(String keyword, int visits, double bounceRate){
		this.keyword = keyword;
		this.visits = visits;
		this.bounceRate = Math.round(100.0*bounceRate)/100.0;
		this.multipageVisitsPercent = -1.;
		this.visitsPercent = -1.;
	}

	public List<String> getWordList(){
		List<String> newlist = new ArrayList<String>(wordList);
		return newlist;
	}

	public void setWordList(List<String> list) {
		wordList.clear();
		wordList.addAll(list);
	}

	public int compareTo(KeyData kd){
		return Double.compare(this.multipageVisitsPercent, kd.multipageVisitsPercent);
	}

}

//class to holds word, a frequency count, a rank
class WordData implements Comparable<WordData>{
	public String word;
	public int count;
	public double multipageVisitsPercent;

	public WordData(String word, int count){
		this.word = word;
		this.count = count;
		this.multipageVisitsPercent = 0;
	}

	public WordData(String word, int count, double multipageVisitsPercent){
		this.word = word;
		this.count = count;
		this.multipageVisitsPercent = multipageVisitsPercent;
	}

	public int compareTo(WordData kc){
		return Double.compare(this.multipageVisitsPercent, kc.multipageVisitsPercent);
	}

}



