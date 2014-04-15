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
import java.util.TreeSet;

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
	private ArrayList<Integer> visits;
	private ArrayList<Double> bounceRate;
	private int visitsGaTotal;
	private ArrayList<String> medium;
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
			}
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		} 
		this.viewCount = 3;
		//updateData();
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
		keywords = dataObject.getKeywords();
		visits = dataObject.getVisits();
		bounceRate = dataObject.getVisitBounceRate();	
		visitsGaTotal = dataObject.getVisitsTotal();
		medium = dataObject.getMedium();

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

		// transfer organic and cpc keywords (if present) with data to 
		//    organic and cpc arrays
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

//		System.out.println("Number of keywords with a medium other than cpc or organic = "+countOtherMed);
//		System.out.println("Visits: cpc = "+visitsCpcTotal+" organic = "+visitsOrganicTotal+" GA total = "+visitsGaTotal);

		ArrayList<KeyData> data;
		ArrayList<WordData> words = new ArrayList<WordData>();
		Map<String,WordData> wordMap = new HashMap<String,WordData>();

		ArrayList<WordData> bestWords = new ArrayList<WordData>();
		ArrayList<WordData> worstWords = new ArrayList<WordData>();
		ArrayList<KeyData> removeKeywords = new ArrayList<KeyData>();
		ArrayList<KeyData> helpKeywords = new ArrayList<KeyData>();
		ArrayList<KeyData> bestKeywords = new ArrayList<KeyData>();
		ArrayList<KeyData> allKeywords = new ArrayList<KeyData>();


		// iterate twice to compute keyword data for organic search
		//  keywords and if available paid search keywords
		boolean isPaid = false;
		boolean hasPaid = false;
		int n = 1;  // n is the number of iterations, at least one

		// if there is cpc data
		if (visitsCpcTotal>0){
			n = 2; // two iterations
			hasPaid = true;		
		}

		// loop once for each data set
		for (int i=0; i<n; i++){
			// if organic loop
			if (i==0){
				data = organicData;
				isPaid = false; 
			} else { // cpc loop
				data = cpcData;
				isPaid = true;
				removeKeywords = new ArrayList<KeyData>();
				helpKeywords = new ArrayList<KeyData>();
				bestKeywords = new ArrayList<KeyData>();
				allKeywords = new ArrayList<KeyData>();
				words.clear();
				worstWords = new ArrayList<WordData>();
				bestWords = new ArrayList<WordData>();
				wordMap.clear();
			}

			// calculate visits percent and multipage visits for each keyword
			Iterator<KeyData> it = data.iterator();
			while (it.hasNext()){
				KeyData kd = it.next();
				kd.visitsPercent = Math.round(10000.0*kd.visits/(visitsCpcTotal+visitsOrganicTotal))/100.0;
				kd.multipageVisitsPercent= Math.round(100.0*kd.visitsPercent*(1.0-kd.bounceRate/100.0))/100.0;
			}
		

			// * * * * * * * * * * * * * * * * * * * * *
			// "Worst keywords:"
			// select keywords with low visits
			
			// order ascending
			Collections.sort(data);
			
			int count = 0;
			int dataSize = data.size();
			int oneHalfData = dataSize/2;
			int minNum = (5 <= oneHalfData)? 5 : oneHalfData;
			KeyData minKd; 
			it = data.iterator();
			if (it.hasNext()){
				minKd = it.next();
				// if the rank 
				if (minKd.multipageVisitsPercent < 1.e-9){
					removeKeywords.add(minKd);

					while (it.hasNext()){
						KeyData kd = it.next();
						if (Math.abs((minKd.multipageVisitsPercent - kd.multipageVisitsPercent)) < 1e-9 || count < minNum) {
							removeKeywords.add(kd);
							count++;
						}
						else {
							break;
						}
					}
				} else {
					while (it.hasNext()){
						KeyData kd = it.next();
						removeKeywords.add(kd);
						count++;
						if (count == minNum){
							break;
						}
					}
				}
				
			}

			// * * * * * * * * * * * * * * * * * * * * *
			// "Improve website to better address these keywords:"
			// select keywords with high visits and high bounce rate 

			// select keywords with visits>1% and bouncerate>50%

			// order ascending
			Collections.sort(data);
			// reverse so descending
			Collections.reverse(data);
			
			count = 0;
			it = data.iterator();
			while (it.hasNext()){
				KeyData kd = it.next();
				if ((kd.multipageVisitsPercent>=1.0 && kd.bounceRate>=50) || (kd.bounceRate>=50 && count < minNum)){
					kd.multipageVisitsPercent = Math.round(100.0*kd.multipageVisitsPercent)/100.0;
					helpKeywords.add(kd);
					count++;
				}
			}
		
			// sort ascending
			Collections.sort(helpKeywords);
			// reverse so descending
			Collections.reverse(helpKeywords);
			
			// * * * * * * * * * * * * * * * * * * * * *
			// "Best keywords:"
			// Find best performing keywords
			
			// sort ascending
			Collections.sort(helpKeywords);
			// reverse so descending
			Collections.reverse(helpKeywords);
            
			count = 0;
			it = data.iterator();
			while (it.hasNext()){
				KeyData kd = it.next();
				if (kd.multipageVisitsPercent>=.5 || count < minNum) {//&& kd.bounceRate<50){
					kd.multipageVisitsPercent = Math.round(100.0*kd.multipageVisitsPercent)/100.0;
					bestKeywords.add(kd);
					count++;
				}
			}
			// sort ascending
			Collections.sort(bestKeywords);
			// reverse so descending
			Collections.reverse(bestKeywords);

			// * * * * * * * * * * * * * * * * * * * * *
			// "All keywords:"

			allKeywords.addAll(data);
			// sort ascending
			Collections.sort(allKeywords);
			// then reverse so descending			
			Collections.reverse(allKeywords);

			// * * * * * * * * * * * * * * * * * * * * *
			// All words

			// Find word substrings: parse words and make a set

			// Collect a list of all words including duplicates

			List<String> newWordList = new ArrayList<String>();
			String kw;

			it = data.iterator();
			while (it.hasNext()){
				KeyData kd = it.next();
				kw = kd.keyword;

				// exclude (content targeting), and other non-keywords
				if (kw.matches("^\\(.*\\)$")){
					continue;
				}
				// replace + with space.
				if (kw.contains("+")){
					kw = kw.replaceAll("\\+", " ");
				}
				newWordList = Arrays.asList(kw.split(" "));

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
				//kd.setWordList(newWordList);
			}

			// all words array
			words.addAll(wordMap.values());

			// sort ascending (worst first)
			Collections.sort(words);

			// Pick off first worst words if they perform at 0
			count = 0;
			Iterator<WordData>itw = words.iterator();
			while (itw.hasNext()){
				WordData wordData = itw.next();
				if (wordData.multipageVisitsPercent < 1e-9 || count < minNum) {
					worstWords.add(wordData);
					count++;
				}
				else {
					break;
				}
			}
			// sort ascending (worst first)
			Collections.sort(worstWords);

			// sort ascending (worst first)
			Collections.sort(words);
			
			// reverse so is descending (best first)
			Collections.reverse(words);

			// Pick off first best words if the perform well
			count = 0;
			itw = words.iterator();
			while (itw.hasNext()){
				WordData wordData = itw.next();
				if (wordData.multipageVisitsPercent >= .5 || count < minNum) {
					wordData.multipageVisitsPercent = Math.round(100.0*wordData.multipageVisitsPercent)/100.0;
					bestWords.add(wordData);
					count++;
				}
				else{
					break;
				}
			}
			// sort ascending and then reverse so descending
			Collections.sort(bestWords);
			Collections.reverse(bestWords);
		
			// categorize the data for 4 quadrants of scatter plot
			
			// sort ascending and then reverse so descending			
			Collections.sort(data);
			Collections.reverse(data);
			
/*			// group keywords according to great, good, bad, terrible;	
			it = data.iterator();
			
			// find the distance from the previous in percent of max value
			KeyData prevkd = null;
			if (it.hasNext()){
				prevkd = it.next();
				prevkd.prevDist = 0.0;  // max mpvisit has no prev
			}
			double maxMVP = prevkd.multipageVisitsPercent;
			
			while (it.hasNext()){
				KeyData kd = it.next();
				kd.prevDist = 100* (prevkd.multipageVisitsPercent - kd.multipageVisitsPercent)/maxMVP;
				prevkd = kd;
			}
			
			// find percent of total number of points for each multipagePercent value
			Map<String,Integer> dupMap = new HashMap<String,Integer>();
			// count duplicates
			it = data.iterator();
			while(it.hasNext()){
				KeyData kd = it.next();
				if (dupMap.containsKey(kd.multipageVisitsPercent+""))
					dupMap.put(kd.multipageVisitsPercent+"", dupMap.get(kd.multipageVisitsPercent+"")+1);
				else 
				    dupMap.put(kd.multipageVisitsPercent+"", 1);
			}
			
			// compute duplicate percent of total number of datapoints (weight)
		//	for (String k : dupMap.keySet()){		
		//			dupMap.put(k, 100*dupMap.get(k)/data.size());
		//	}
		
			// if datapoint has less than 5% weight add to previous group
			it = data.iterator();
		
			if (it.hasNext()){
				prevkd = it.next();
				prevkd.group = 0;
			}
			
			while(it.hasNext()){
				KeyData kd = it.next();
			//	if (dupMap.get(kd.multipageVisitsPercent+"") < 3 && (kd.prevDist < 0 || kd.prevDist > 5)){
				if (kd.prevDist <0 || kd.prevDist > 5){
					kd.group = 0;
				} else {
					kd.group = 1;
				}
				prevkd = kd;
				
			}
*/			
			// group keywords according to level of performance 0-3, 0 is the best 
			Set<Double> mvpSet = new TreeSet<Double>();
			it = data.iterator();
			while (it.hasNext()){
			    mvpSet.add(it.next().multipageVisitsPercent);
			}
		    ArrayList<Double> mvpSetList = new ArrayList<Double>(mvpSet);
			Collections.sort(mvpSetList);
			Collections.reverse(mvpSetList);
			
			
			int mvpCount = mvpSetList.size();
			double limit = -1.0;
			if (mvpCount < 4){
				limit = mvpSetList.get(mvpCount-1); // get last value
				if (limit <= 0 && mvpCount>2){
					limit = mvpSetList.get(mvpCount-2); // get 2nd to last value		
				}
			} else {
				limit = mvpSetList.get(mvpCount*3/4);
			}
			
			//limit = (mvp.get(iq).multipageVisitsPercent<1.0 ? data.get(iq).multipageVisitsPercent : 1.0);

			it = data.iterator();			
			while(it.hasNext()){
				KeyData kd = it.next();
				if (kd.multipageVisitsPercent> limit)
					if (kd.bounceRate < 50 )
						kd.group = 0;
					else
						kd.group = 1;
				else
					if (kd.bounceRate < 50 )
						kd.group = 2;
					else
						kd.group = 3;
					
			}

			// test group is in the KeyData object
	/*		System.out.println();
			System.out.println("Print groups of all keywords:");
			System.out.println("limit = "+limit);
			
			it = data.iterator();
			// print group for each best keyword
			
			while (it.hasNext()){
				KeyData kd = it.next();
				//System.out.println(kd.group+", "+dupMap.get(kd.multipageVisitsPercent+"")+", "+kd.prevDist+", "+kd.multipageVisitsPercent+", "+kd.bounceRate+""+kd.keyword);
				System.out.println(kd.group+", "+kd.multipageVisitsPercent+", "+kd.bounceRate+", "+kd.visitsPercent+","+kd.keyword);
			}
			System.out.println("end group");

	*/	
			// put data into the JSON Object member jsonData
			this.loadJson(hasPaid, isPaid, helpKeywords, bestKeywords, removeKeywords, allKeywords, bestWords, worstWords); 
		}

	}

	public  void cluster1D() {

		int[] data = {1,8,9,3,10,12,-16,-2,-1,0,1,-3};

		int clusters = 2;
		int length = data.length;
		int[][] sums = new int[clusters][length];
		int[][] centroids = {{0, 0, 0}, {1, 8, 9}};
		int[] count = new int[clusters];
		int i, j, k;
		long minimum, difference;
		boolean converged = false;

		do {

			for(i = 0; i < clusters; i++) {
				centroids[0][i] = centroids[1][i];
				count[i] = 0;
				centroids[1][i] = 0;          }

			for(i = 0; i < length; i++) {
				sums[0][i] = 0;
				minimum = centroids[0][0] > data[i] ? centroids[0][0] - data[i] : data[i] - centroids[0][0];
				k = 0;

				for(j = 1; j < clusters; j++) {
					sums[j][i] = 0;
					difference = centroids[0][j] > data[i] ? centroids[0][j] - data[i] : data[i] - centroids[0][j];
					if(difference < minimum) {
						minimum = difference;
						k = j;
					}
				}
				sums[k][i] = data[i];
				count[k]++;
			}

			converged = true;

			for(i = 0; i < clusters; i++) {
				difference = 0;
				if(count[i] > 0) {
					for(j = 0; j < length; j++) {
						centroids[1][i] += sums[i][j] / count[i];
						difference += sums[i][j] % count[i];
						centroids[1][i] += difference / count[i];
						difference %= count[i];
					}
				}

				converged &= centroids[0][i] == centroids[1][i];
			}
		}while(!converged);

		for(i = 0; i < clusters; i++) {

			System.out.println(centroids[1][i]);
		}
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
	public void loadJson(boolean hasPaid, boolean isPaid, ArrayList<KeyData> hk, ArrayList<KeyData> bk, ArrayList<KeyData> rk, ArrayList<KeyData> ak, ArrayList<WordData> bw, ArrayList<WordData> ww)  {
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
			JSONArray allGroups = new JSONArray();


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

			Iterator<WordData> itwc = ww.iterator();
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
			
			// all keywords
			it = ak.iterator();
			while (it.hasNext()){
				KeyData d = it.next();
				allKeywords.put(d.keyword);			
				allVisitsPercent.put(d.visitsPercent);			
				allBounceRate.put(d.bounceRate);
				allMultipageVisitsPercent.put(Math.round(100.0*d.multipageVisitsPercent)/100.0);
				allGroups.put(d.group);
			}

			String[] keys1 = new String[]{"Keywords", "% Visits", "% Bounce Rate", "% Multipage Visits"};
			String[] keys2 = new String[]{"Substring Word", "Keyword Count", "% Multipage Visits"};
			String[] scatterKeys = new String[]{"allBounceRate", "allMultipageVisitsPercent"};
			String[] scatterAxisLabels = new String[]{"% Bounce Rate", "% Visits"};

			/* Scatterplot */
			JSONObject scatter = new JSONObject();

			scatter.put("title", "Keyword Insight Plot");
			scatter.put("label", scatterAxisLabels);

			scatter.put("allKeywords", allKeywords);
			scatter.put("allVisitsPercent", allVisitsPercent);
			scatter.put("allBounceRate", allBounceRate);
			scatter.put("allMultipageVisitsPercent", allMultipageVisitsPercent);
			scatter.put("allGroups", allGroups);
			scatter.put("keys", scatterKeys);
			/* Improve Keywords */
			JSONObject improve = new JSONObject();

			// title
			improve.put("title", "Improve website performance for these keywords:");

			// JSON key is the row category for use in Javascript
			improve.put(keys1[0], helpKeywords);
			improve.put(keys1[1], helpVisitsPercent);
			improve.put(keys1[2], helpBounceRate);
			improve.put(keys1[3], helpMultipageVisitsPercent);
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

			all.put(keys1[0], allKeywords);
			all.put(keys1[1], allVisitsPercent);
			all.put(keys1[2], allBounceRate);
			all.put(keys1[3], allMultipageVisitsPercent);
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

			if (isPaid) {				
				this.jsonData.put("paidscatter", scatter);
				this.jsonData.put("paidimprove", improve);
				this.jsonData.put("paidbest", best);
				this.jsonData.put("paidworst", worst);
				this.jsonData.put("paidall", all);
				this.jsonData.put("paidbestsubstr", bestSubStr);
				this.jsonData.put("paidworstsubstr", worstSubStr);

			} else {
				this.jsonData.put("scatter", scatter);
				this.jsonData.put("improve", improve);
				this.jsonData.put("best", best);
				this.jsonData.put("worst", worst);
				this.jsonData.put("all", all);
				this.jsonData.put("bestsubstr", bestSubStr);
				this.jsonData.put("worstsubstr", worstSubStr);

				if (!hasPaid){
					JSONObject nullobj = null;
					this.jsonData.put("paidscatter", nullobj);
					this.jsonData.put("paidimprove", nullobj);
					this.jsonData.put("paidbest", nullobj);
					this.jsonData.put("paidworst", nullobj);
					this.jsonData.put("paidall", nullobj);
					this.jsonData.put("paidbestsubstr", nullobj);
					this.jsonData.put("paidworstsubstr", nullobj);
				}
			}
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
	public int group;
	public double prevDist;
	// words contained in the keyword
	//private List<String> wordList = new ArrayList<String>();

	public KeyData(String keyword, int visits, double bounceRate){
		this.keyword = keyword;
		this.visits = visits;
		this.bounceRate = Math.round(100.0*bounceRate)/100.0;
		this.multipageVisitsPercent = -1.;
		this.visitsPercent = -1.;
		this.group = -1;
		this.prevDist = -1.0;
	}

	/*public List<String> getWordList(){
		List<String> newlist = new ArrayList<String>(wordList);
		return newlist;
	}

	public void setWordList(List<String> list) {
		wordList.clear();
		wordList.addAll(list);
	} */

	public int compareTo(KeyData kd){
		// sort multipage ascending
		int mpCmp = Double.compare(this.multipageVisitsPercent, kd.multipageVisitsPercent);
		int result=0;
		if (mpCmp != 0){
			result = mpCmp;
		} else {
			int brResult = Double.compare(this.bounceRate, kd.bounceRate);
			// sort bounce rate descending
			result = (brResult == 0)?  brResult : -brResult;
		}
        return result;
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




 
