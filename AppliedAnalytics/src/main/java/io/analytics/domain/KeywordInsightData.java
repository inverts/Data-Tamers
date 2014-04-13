package io.analytics.domain;

import java.util.ArrayList;
	/**
	 * KeywordsInsightData: Domain class for Keywords Insight widgets.
	 * 	 Contains all the Google Analytics data for the KeywordsInsight 
	 *   Model computations and for the display of the Keywords Insight 
	 *   widget.
	 *   
	 * @author gak
	 *
	 */
public class KeywordInsightData {
	
		private ArrayList<String> keywords;
		private ArrayList<Integer> visits;
		private ArrayList<Double> visitBounceRate;
		private ArrayList<String> medium;
		private ArrayList<String> hostname;
		private int visitsTotal;
		
		public KeywordInsightData(){
			keywords = new ArrayList<String>();
			visits = new ArrayList<Integer>();
			visitBounceRate = new ArrayList<Double>();
			medium = new ArrayList<String>();
			hostname = new ArrayList<String>();
			visitsTotal = -1;
		}

		public void setKeywords(ArrayList<String> data) {
			this.keywords.clear();
			this.keywords.addAll(data);
		}
		
		public ArrayList<String> getKeywords() {
			ArrayList<String> data = new ArrayList<String>(this.keywords);
			return data;
		}
		
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

		public void setVisitBounceRate(ArrayList<Double> data){
			this.visitBounceRate.clear();
			this.visitBounceRate.addAll(data);
		}
		
		public ArrayList<Double> getVisitBounceRate() {
			ArrayList<Double> data = new ArrayList<Double>(this.visitBounceRate);
			return data;
		}

		public void setVisitsTotal(int datum) {
			this.visitsTotal = datum;
		}
		
		public int getVisitsTotal() {
			return this.visitsTotal;
		}
}
