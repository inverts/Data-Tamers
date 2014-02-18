package io.analytics.domain;

import java.util.ArrayList;
	/**
	 * KeywordsInsightData: Domain class for Search Keywords widgets.
	 * 	 Contains all the Google Analytics data for the KeywordsInsight 
	 *   Model computations and for the display of the Keywords Insight 
	 *   widgets.
	 *   
	 * @author Gwen Knight
	 *
	 */
public class KeywordInsightData {
	
		private ArrayList<String> cpcKeywords;
		private ArrayList<String> organicKeywords;
		private ArrayList<Integer> cpcVisits;
		private ArrayList<Integer> organicVisits;
		private ArrayList<Double> cpcVisitBounceRate;
		private ArrayList<Double> organicVisitBounceRate;
		private int cpcVisitsTotal;
		private int privateOrganicVisitsTotal;
		private int organicVisitsTotal;
		
		public KeywordInsightData(){
			cpcKeywords = new ArrayList<String>();
			organicKeywords = new ArrayList<String>();
			cpcVisits = new ArrayList<Integer>();
			organicVisits = new ArrayList<Integer>();
			cpcVisitBounceRate = new ArrayList<Double>();
			organicVisitBounceRate = new ArrayList<Double>();
			cpcVisitsTotal = -1;
			privateOrganicVisitsTotal = -1;
			organicVisitsTotal = -1;
		}
	
		public void setOrganicKeywords(ArrayList<String> data) {
			this.organicKeywords.clear();
			this.organicKeywords.addAll(data);
		}
		
		public ArrayList<String> getOrganicKeywords() {
			ArrayList<String> data = new ArrayList<String>(this.organicKeywords);
			return data;
		}
		
		public void setCpcKeywords(ArrayList<String> data) {
			this.cpcKeywords.clear();
			this.cpcKeywords.addAll(data);
		}
		
		public ArrayList<String> getCpcKeywords() {
			ArrayList<String> data = new ArrayList<String>(this.cpcKeywords);
			return data;
		}
		
		public void setOrganicVisits(ArrayList<Integer> data){
			this.organicVisits.clear();
			this.organicVisits.addAll(data);
		}
		
		public ArrayList<Integer> getOrganicVisits() {
			ArrayList<Integer> data = new ArrayList<Integer>(this.organicVisits);
			return data;
		} 
		
		public void setCpcVisits(ArrayList<Integer> data){
			this.cpcVisits.clear();
			this.cpcVisits.addAll(data);
		}
		
		public ArrayList<Integer> getCpcVisits() {
			ArrayList<Integer> data = new ArrayList<Integer>(this.cpcVisits);
			return data;
		} 
		
		public void setOrganicVisitBounceRate(ArrayList<Double> data){
			this.organicVisitBounceRate.clear();
			this.organicVisitBounceRate.addAll(data);
		}
		
		public ArrayList<Double> getOrganicVisitBounceRate() {
			ArrayList<Double> data = new ArrayList<Double>(this.organicVisitBounceRate);
			return data;
		}
		
		public void setCpcVisitBounceRate(ArrayList<Double> data){
			this.cpcVisitBounceRate.clear();
			this.cpcVisitBounceRate.addAll(data);
		}
		
		public ArrayList<Double> getCpcVisitBounceRate() {
			ArrayList<Double> data = new ArrayList<Double>(this.cpcVisitBounceRate);
			return data;
		}
		
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
		
		public void setCpcVisitsTotal(int datum) {
			this.cpcVisitsTotal = datum;
		}
		
		public int getCpcVisitsTotal() {
			return this.cpcVisitsTotal;
		}
}
