package io.analytics.site.models.widgets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.analytics.model.GaData;
import com.google.api.services.analytics.model.GaData.ColumnHeaders;
import com.google.gson.Gson;

import io.analytics.service.interfaces.ICoreReportingService;
import io.analytics.service.interfaces.ISessionService;
import io.analytics.site.models.FilterModel;
import io.analytics.site.models.JSONSerializable;
import io.analytics.site.models.SettingsModel;

public class VisitorClusteringModel extends WidgetModel implements JSONSerializable {

	private ICoreReportingService coreReportingService;
	private ISessionService sessionService;
	private Credential credential;
	private int columnCount;
	private List<ColumnHeaders> columnHeaders;
	private List<List<String>> rawData;
	private List<List<Double>> numericalData;
	private List<List<Double>> finalRawCentroids;
	private List<List<String>> finalReadableCentroids;

	private ArrayList<Double> dataPointWeights = new ArrayList<Double>();
	private HashMap<Integer, Integer> clustering;

	public VisitorClusteringModel(ISessionService sessionService, ICoreReportingService coreReportingService) {
		super();
		this.sessionService = sessionService;
		this.coreReportingService = coreReportingService;
		this.credential = sessionService.getCredentials();
	}
	
	public void updateData() {
		this.updateData(500000);
	}
	public void updateData(int maxRows) {
		
		FilterModel filter = this.sessionService.getFilter();
		SettingsModel settings = this.sessionService.getUserSettings();
		Date endDate = filter.getActiveEndDate();
		Date startDate = filter.getActiveStartDate();
		//long msInDay = 24L * 60L * 60L * 1000L;
		//Date startDate = new Date(endDate.getTime() - 90L * msInDay); //90 days
		String profileId = settings.getActiveProfile().getId();
		
		GaData data = coreReportingService.getDenseVisitorInfo(credential, profileId, startDate, endDate, maxRows);
		this.columnHeaders = data.getColumnHeaders();
		this.columnCount = this.columnHeaders.size();
		this.rawData = data.getRows();
		normalizeData();
		List<Integer> clusterSizes = new ArrayList<Integer>();
		List<List<Double>> centroids = new ArrayList<List<Double>>();
		this.clustering = ClusteringUtils.WeightedEuclideanKMeans(this.numericalData, this.dataPointWeights, 11, centroids, clusterSizes);

		this.finalRawCentroids = centroids;
		this.finalReadableCentroids = new ArrayList<List<String>>();
		
		Gson g = new Gson();
		System.out.println("SUPER FORMATTED DATA");
		System.out.println("Hour\tDay of Week\tLongitude\tLatitude\tNew vs. Returning\tScreen Resolution\tAvg. Time on Site\tPageviews per Visit\tVisits");
		List<Double> averages = new ArrayList<Double>();
		Integer totalVisits = 0;
		for (int j=0; j<centroids.size(); j++) {
			List<Double> centroid = centroids.get(j);
			ArrayList<String> readableCentroid = new ArrayList<String>();
			for (int i=0; i<centroid.size(); i++) {
				if (averages.size() != centroid.size())
					averages.add(centroid.get(i) * clusterSizes.get(j));
				else
					averages.set(i, averages.get(i) + centroid.get(i) * clusterSizes.get(j));
				String readableValue = decodeColumn(centroid.get(i), i, true);
				readableCentroid.add(readableValue);
				System.out.print(readableValue + "\t");
			}
			totalVisits += clusterSizes.get(j);
			System.out.print(clusterSizes.get(j));
			System.out.println();
		}
		for (int i=0; i<centroids.get(0).size(); i++) {
			averages.set(i, averages.get(i) / totalVisits);
			System.out.print(decodeColumn(averages.get(i), i, true) + "\t");
		}
		

		System.out.println();
		System.out.println("FORMATTED DATA");
		System.out.println("Hour\tDay of Week\tLongitude\tLatitude\tNew vs. Returning\tScreen Resolution\tAvg. Time on Site\tPageviews per Visit\tVisits");
		for (int j=0; j<centroids.size(); j++) {
			List<Double>centroid = centroids.get(j);
			ArrayList<String> readableCentroid = new ArrayList<String>();
			for (int i=0; i<centroid.size(); i++) {
				String readableValue = decodeColumn(centroid.get(i), i, false);
				readableCentroid.add(readableValue);
				System.out.print(readableValue + "\t");
			}
			this.finalReadableCentroids.add(readableCentroid);
			System.out.print(clusterSizes.get(j));
			System.out.println();
		}
		for (int i=0; i<centroids.get(0).size(); i++) {
			System.out.print(decodeColumn(averages.get(i), i, false) + "\t");
		}

		System.out.println();
		System.out.println("RAW DATA");
		for (int i=0; i<this.columnHeaders.size(); i++)
			System.out.print(this.columnHeaders.get(i).getName() + "\t");
			//System.out.println("Hour\tDay of Week\tLongitude\tLatitude\tNew vs. Returning\tScreen Resolution\tAvg. Time on Site\tPageviews per Visit\tVisits");

		System.out.println();
		for (int j=0; j<centroids.size(); j++) {
			List<Double>centroid = centroids.get(j);
			for (int i=0; i<centroid.size(); i++) {
				System.out.print(centroid.get(i) + "\t");
			}
			System.out.print(clusterSizes.get(j));
			System.out.println();
		}
		
	}
	
	/**
	 * Translates the rawData of this model into numericalData, mapping any non-numeric fields 
	 * to numeric values so that they can be operated on in Euclidean space.
	 * In order for a field to be mapped, it must be registered/defined in the convertColumn function.
	 */
	private void normalizeData() {
		this.numericalData = new ArrayList<List<Double>>();
		for(List<String> row : this.rawData) {
			List<Double> newRow = new ArrayList<Double>();
			for (int i=0; i<this.columnCount; i++) {
				 if (this.columnHeaders.get(i).getName().equals("ga:visits"))
					 dataPointWeights.add(Double.parseDouble(row.get(i)));
				 else
					 newRow.add(convertColumn(row.get(i), i));
					 
			}
			this.numericalData.add(newRow);
		}
	}
	
	
	private double convertColumn(String value, int columnIndex) {
		
		//TODO: Can we do this a better way?
		//ga:visits,ga:timeOnSite,ga:uniquePageviews
		String columnName = this.columnHeaders.get(columnIndex).getName();
		if (columnName.equals("ga:hour")) 
			return scale(Double.parseDouble(value), 23.0, 100.0);
		else if (columnName.equals("ga:dayOfWeek")) 
			return scale(Double.parseDouble(value), 6.0, 100.0);
		else if (columnName.equals("ga:operatingSystemVersion"))
			return 0.0;
		else if (columnName.equals("ga:browserVersion"))
			return 0.0;
		else if (columnName.equals("ga:visitorType") || columnName.equals("ga:userType"))
			if (value.equals("Returning Visitor"))
				return 0;
			else if (value.equals("New Visitor"))
				return 50;
			else
				return -1;
		else if (columnName.equals("ga:screenResolution"))
			return scale(parseResolution(value).w, 1600.0, 100.0);
		else if (columnName.equals("ga:sessions"))
			return Double.parseDouble(value);
		else if (columnName.equals("ga:avgTimeOnSite"))
			return scale(Double.parseDouble(value), 300.0, 100.0);
		else if (columnName.equals("ga:pageviewsPerVisit"))
			return scale(Double.parseDouble(value), 8.0, 100.0);
		else if (columnName.equals("ga:deviceCategory"))
			if (value.equals("mobile"))
				return 0.0;
			else if (value.equals("tablet"))
				return 30.0;	//Tablets are more similar to mobile than desktop, so make them closer.
			else if (value.equals("desktop"))
				return 90.0;
			else
				return 100;
		else if (columnName.equals("ga:longitude"))
			return scale(180.0 + Double.parseDouble(value), 360.0, 100.0);
		else if (columnName.equals("ga:latitude"))
			return scale(90.0 + Double.parseDouble(value), 180.0, 100.0);
		else
			return 0.0;
	}
	
	private final String[] weekDays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
	private String decodeColumn(double value, int columnIndex, boolean pretty) {
		
		String columnName = this.columnHeaders.get(columnIndex).getName();
		if (columnName.equals("ga:hour")) {
			double num = scale(value, 100.0, 23.0);
			if (!pretty)
				return String.valueOf(num);
			int hour = (int)Math.round(num % 12);
			return String.valueOf(hour != 0 ? hour : 12) + (num / 12 >= 1.0 ? "pm" : "am");
		}
		else if (columnName.equals("ga:dayOfWeek")) {
			double num = scale(value, 100.0, 6.0);
			return pretty ? weekDays[(int)Math.round(num)] : String.valueOf(num);
		}
		else if (columnName.equals("ga:operatingSystemVersion"))
			return null;
		else if (columnName.equals("ga:browserVersion"))
			return null;
		else if (columnName.equals("ga:visitorType") || columnName.equals("ga:userType"))
			switch((int)Math.round(value)) {
				case 0: 	return "Returning Visitor";
				case 50: 	return "New Visitor";
				default: 	return "Unknown";
			}
		else if (columnName.equals("ga:screenResolution"))
			return String.valueOf(scale(value, 100.0, 1600.0));
		else if (columnName.equals("ga:sessions"))
			return String.valueOf(value);
		else if (columnName.equals("ga:avgTimeOnSite")) {
			double num = scale(value, 100.0, 300.0);
			if (!pretty)
				return String.valueOf(num);
			return String.valueOf((int)Math.floor(num/60)) + ":" + String.valueOf((int)Math.round(num % 60));
		}
		else if (columnName.equals("ga:pageviewsPerVisit"))
			return String.valueOf(scale(value, 100.0, 8.0));
		else if (columnName.equals("ga:deviceCategory"))
			switch ((int) value) {
				case 0: 	return "mobile";
				case 30: 	return "tablet";
				case 90: 	return "desktop";
				default: 	return null;
			}
		else if (columnName.equals("ga:longitude"))
			return String.valueOf(scale(value, 100.0, 360.0)-180.0);
		else if (columnName.equals("ga:latitude"))
			return String.valueOf(scale(value, 100.0, 180.0)-90);
		else
			return null;
	}
	/**
	 * Scales a positive integer from 0 to maxOutput.
	 * 
	 * @return
	 */
	private double scale(double input, double maxInput, double maxOutput){
		if (maxInput == 0) throw new IllegalArgumentException();
		return (input * maxOutput) / maxInput;
	}
	
	/**
	 * Parses a screen resolution represented as a string. Below are some examples.
	 * 1920x1080
	 * 800x600
	 * 
	 * Assumes the format WxH, where W is the screen width, and H is the screen height.
	 * 
	 * @param res
	 * @throws NumberFormatException - if the input string does not match the format.
	 * @return
	 */
	private Resolution<Integer, Integer> parseResolution(String res) throws NumberFormatException {
		int middle = res.indexOf("x");
		if (middle < 0)
			return new Resolution<Integer, Integer>(0, 0);
		int width = Integer.parseInt(res.substring(0, middle));
		int height = Integer.parseInt(res.substring(middle + 1));
		return new Resolution<Integer, Integer>(width, height);
	}
	
	private class Resolution<W, H> {
		private W w;
		private H h;
		private Resolution(W w, H h) {
			this.w = w;
			this.h = h;
		}
	}
	/**
	 * @return the finalRawCentroids
	 */
	public List<List<Double>> getFinalRawCentroids() {
		return finalRawCentroids;
	}

	/**
	 * @return the finalReadableCentroids
	 */
	public List<List<String>> getFinalReadableCentroids() {
		return finalReadableCentroids;
	}

	@Override
	public String getJSONSerialization() {
		Gson g = new Gson();
		JSONObject model = new JSONObject();
		JSONArray raw, numeric;
		try {
			raw = new JSONArray(g.toJson(this.rawData));
			numeric = new JSONArray(g.toJson(this.numericalData));
			//model.put("rawData", raw);
			//model.put("numericalData", numeric);
			model.put("clustering", this.clustering);
			model.put("finalRawCentroids", this.finalRawCentroids);
			model.put("finalReadableCentroids", this.finalReadableCentroids);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			return model.toString(4);
		} catch (JSONException e) {
			e.printStackTrace();
			return model.toString();
		}
	}

	@Override
	public String getName() {
		return "Visitor Clustering";
	}

	@Override
	public String getDescription() {
		return "No description currently available";
	}

	@Override
	public int getPositionPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getHTMLClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static class ClusteringUtils {
		//
		/**
		 * 
		 * 
		 * That the maximum value M for any dimension and the number of points P should be such that M * P < Double.MAX_VALUE
		 * to avoid overflow by a summation average when calculating means.
		 * 
		 * @param data
		 * @param weights
		 * @param k
		 * @return
		 */
		public static HashMap<Integer, Integer> WeightedEuclideanKMeans(List<List<Double>> data, List<Double> weights, int k, 
				List<List<Double>> centroidsOut, List<Integer> clusterSizesOut) {
			
			HashMap<Integer, Integer> keyClusterMap = new HashMap<Integer, Integer>();
			int[] clusterSizes = new int[k];
			int numOfPoints = data.size();
			int numOfDimensions = data.get(0).size();
			
			//Set up the initial centroids.
			List<Integer> initialCentroidIndices = chooseRandomIntegers(k, numOfPoints);	// Size k
			List<List<Double>> centroids = new ArrayList<List<Double>>(k);	// Size k
			List<List<Double>> newCentroids = new ArrayList<List<Double>>(k);	// Size k
			for (int j=0; j < k; j++) {
				centroids.add(data.get(initialCentroidIndices.get(j)));
			}
			
			boolean converged = false, timedout = false;
			int repetitions = 0;
			long start = System.currentTimeMillis();
			int timeoutLimit = 120000;
			while ( !converged && !timedout) {
				repetitions += 1;
				System.out.println("Clustering... (" + repetitions + ")");
				
				newCentroids = new ArrayList<List<Double>>();
				for (int j=0; j < k; j++) 
					newCentroids.add(new ArrayList<Double>(Collections.nCopies(numOfDimensions, 0.0)));
				clusterSizes = new int[k];
				double totalVisits = 0;
				// For each point in the data...
				for(int i=0; i < numOfPoints; i++) {
					//Determine which cluster it belongs to by min distance.
					double min = Double.POSITIVE_INFINITY;
					int selectedCluster = 0;
					for (int j=0; j < k; j++) {
						double dist = euclideanDistance(data.get(i), centroids.get(j));
						if (dist < min) {
							min = dist;
							selectedCluster = j;
						}
					}
					//Update the information we have about clusters.
					keyClusterMap.put(i, selectedCluster);
					clusterSizes[selectedCluster] += weights.get(i).intValue();
					totalVisits += weights.get(i);
					addPoints(data.get(i), newCentroids.get(selectedCluster), weights.get(i));
				}
				// Divide the sums of the points in a cluster to get the mean - the new centroid.
				for (int j=0; j < k; j++) 
					dividePoint(newCentroids.get(j), clusterSizes[j]);
				converged = areEqual(centroids, newCentroids);
				timedout = System.currentTimeMillis() - start > timeoutLimit;
				centroids = newCentroids;
				
			}
			centroidsOut.clear();
			centroidsOut.addAll(centroids);
			
			clusterSizesOut.clear();
			for (int i=0; i<clusterSizes.length; i++)
				clusterSizesOut.add(clusterSizes[i]);

			System.out.println("Performed " + repetitions + " iterations.");
			if (timedout)
				System.out.println("Clustering timed out.");
			else
				System.out.println("Clustering took " + ((System.currentTimeMillis() - start) / 1000) + " seconds.");
			
			return keyClusterMap;
		}
		
		private static boolean areEqual(List<List<Double>> L1, List<List<Double>> L2) {
			for (int i=0; i < L1.size(); i++)
				if ( !L1.get(i).equals(L2.get(i)) )
					return false;
			return true;
		}
		/**
		 * Adds one point into another as follows:
		 * into = into + from
		 */
		private static void addPoints(List<Double> from, List<Double> into) {
			for (int i=0; i < from.size(); i++)
				into.set(i, into.get(i) + from.get(i));
		}
		/**
		 * Adds one point into another a certain number of times, as follows:
		 * into = into + (from * times)
		 */
		private static void addPoints(List<Double> from, List<Double> into, double times) {
			for (int i=0; i < from.size(); i++)
				into.set(i, into.get(i) + (from.get(i) * times));
		}
		
		/**
		 * Divides all dimensions of a point by a scalar.
		 * point = point / divideBy
		 */
		private static void dividePoint(List<Double> point, double divideBy) {
			for (int i=0; i < point.size(); i++)
				point.set(i, point.get(i) / divideBy);
		}
		
		
		/**
		 * Generates random integers and returns them in a list.
		 * @param howMany The number of random integers to generate.
		 * @param maxExclusive The exclusive upper bound of the integers (lower bound is 0 inclusive).
		 * @return
		 */
		private static List<Integer> chooseRandomIntegers(int howMany, int maxExclusive) {
			ArrayList<Integer> numbers = new ArrayList<Integer>();
			Random rng = new Random();
			for (int i=0; i < howMany; i++)
				numbers.add(rng.nextInt(maxExclusive));
			return numbers;
		}
		
		private static double euclideanDistance(List<Double> p1, List<Double> p2) {
			Double inside = 0.0;
			for (int i=0; i < p1.size(); i++) {
				inside += Math.pow(p2.get(i) - p1.get(i), 2);
			}
			return Math.sqrt(inside);
		}
	}

	
	public static class LevenshteinDistance {
		private static int minimum(int a, int b, int c) {
			return Math.min(Math.min(a, b), c);
		}
	 
		public static int computeLevenshteinDistance(String str1,String str2) {
			int[][] distance = new int[str1.length() + 1][str2.length() + 1];
	 
			for (int i = 0; i <= str1.length(); i++)
				distance[i][0] = i;
			for (int j = 1; j <= str2.length(); j++)
				distance[0][j] = j;
	 
			for (int i = 1; i <= str1.length(); i++)
				for (int j = 1; j <= str2.length(); j++)
					distance[i][j] = minimum(
							distance[i - 1][j] + 1,
							distance[i][j - 1] + 1,
							distance[i - 1][j - 1]+ ((str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1));
	 
			return distance[str1.length()][str2.length()];    
		}
	}
}
