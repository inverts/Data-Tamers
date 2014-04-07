package io.analytics.domain;

import java.util.ArrayList;
	/**
	 * KeywordsInsightData: Domain class for Search Keywords widgets.
	 * 	 Contains all the Google Analytics data for the KeywordsInsight 
	 *   Model computations and for the display of the Keywords Insight 
	 *   widgets.
	 *   
	 * @author gak
	 *
	 */
public class KeywordInsightData {
	
		private ArrayList<String> keywords;
		//private ArrayList<String> organicKeywords;
		private ArrayList<Integer> visits;
		//private ArrayList<Integer> organicVisits;
		private ArrayList<Double> visitBounceRate;
		//private ArrayList<Double> organicVisitBounceRate;
		private ArrayList<String> medium;
		private ArrayList<String> hostname;
		private int visitsTotal;
		//private int privateOrganicVisitsTotal;
		//private int organicVisitsTotal;
		
		public KeywordInsightData(){
			keywords = new ArrayList<String>();
			//organicKeywords = new ArrayList<String>();
			visits = new ArrayList<Integer>();
			//organicVisits = new ArrayList<Integer>();
			visitBounceRate = new ArrayList<Double>();
			//organicVisitBounceRate = new ArrayList<Double>();
			medium = new ArrayList<String>();
			hostname = new ArrayList<String>();
			visitsTotal = -1;
			//privateOrganicVisitsTotal = -1;
			//organicVisitsTotal = -1;
		}
/*	
		public void setOrganicKeywords(ArrayList<String> data) {
			this.organicKeywords.clear();
			this.organicKeywords.addAll(data);
		}
		
		public ArrayList<String> getOrganicKeywords() {
			ArrayList<String> data = new ArrayList<String>(this.organicKeywords);
			return data;
		}
*/		
		public void setKeywords(ArrayList<String> data) {
			this.keywords.clear();
			this.keywords.addAll(data);
		}
		
		public ArrayList<String> getKeywords() {
			ArrayList<String> data = new ArrayList<String>(this.keywords);
			return data;
		}
	/*	
		public void setOrganicVisits(ArrayList<Integer> data){
			this.organicVisits.clear();
			this.organicVisits.addAll(data);
		}
		
		public ArrayList<Integer> getOrganicVisits() {
			ArrayList<Integer> data = new ArrayList<Integer>(this.organicVisits);
			return data;
		} 
	*/	
		public void setVisits(ArrayList<Integer> data){
			this.visits.clear();
			this.visits.addAll(data);
		}
		
		public ArrayList<Integer> getVisits() {
			ArrayList<Integer> data = new ArrayList<Integer>(this.visits);
			return data;
		} 
		
		public void setMedium(ArrayList<String> data){
			this.medium.clear();
			this.medium.addAll(data);
		}
		
		public ArrayList<String> getMedium() {
			ArrayList<String> data = new ArrayList<String>(this.medium);
			return data;
		} 
		
		public void setHostname(ArrayList<String> data){
			this.hostname.clear();
			this.hostname.addAll(data);
		}
		
		public ArrayList<String> getHostname() {
			ArrayList<String> data = new ArrayList<String>(this.hostname);
			return data;
		} 
/*		
		public void setOrganicVisitBounceRate(ArrayList<Double> data){
			this.organicVisitBounceRate.clear();
			this.organicVisitBounceRate.addAll(data);
		}
		
		public ArrayList<Double> getOrganicVisitBounceRate() {
			ArrayList<Double> data = new ArrayList<Double>(this.organicVisitBounceRate);
			return data;
		}
*/		
		public void setVisitBounceRate(ArrayList<Double> data){
			this.visitBounceRate.clear();
			this.visitBounceRate.addAll(data);
		}
		
		public ArrayList<Double> getVisitBounceRate() {
			ArrayList<Double> data = new ArrayList<Double>(this.visitBounceRate);
			return data;
		}
/*		
		public void setPrivateOrganicVisitsTotal(int datum) {
			this.privateOrganicVisitsTotal = datum;
		}
		
		public int getPrivateOrganicVisitsTotal() {
			return this.privateOrganicVisitsTotal;
		}
	
		public void setOrganicVisitsTotal(int datum) {
			this.organicVisitsTotal = datum;
		}
		
		public int getOrganicVisitsTotal() {
			return this.organicVisitsTotal;
		}
*/		
		public void setVisitsTotal(int datum) {
			this.visitsTotal = datum;
		}
		
		public int getVisitsTotal() {
			return this.visitsTotal;
		}
}
