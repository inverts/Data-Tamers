package io.analytics.site.models;

import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

import io.analytics.service.CoreReportingService;

public class RevenueSourcesModel {
	
		private JSONObject dataPoints;
		
		
		public RevenueSourcesModel() {	
			super();
			
			this.dataPoints = new JSONObject();			
		}
		
		public String getName() {
			return "Revenue Sources";
		}

		public String getDescription() {
			return "View the revenue sources for a business";
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
