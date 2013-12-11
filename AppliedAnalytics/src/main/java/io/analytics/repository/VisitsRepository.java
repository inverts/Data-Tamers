package io.analytics.repository;
import com.google.gdata.client.analytics.AnalyticsService;
import com.google.gdata.client.analytics.DataQuery;
import com.google.gdata.data.analytics.DataEntry;
import com.google.gdata.data.analytics.DataFeed;
import com.google.gdata.data.analytics.Dimension;
import com.google.gdata.data.analytics.Metric;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import io.analytics.domain.Visits;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


// calls the GA library function 
public class VisitsRepository implements IVisitsRepository{
	// Data retrieved from Google Analytics
	DataFeed dataFeed = null;
	// Credentials for Client Login Authorization. (will be passed in)
	private static final String CLIENT_USERNAME = "seo@clearmarketingseattle.com";
	private static final String CLIENT_PASS = "@Temp-Pass";
	// Name of our application
	private static final String APPLICATION_NAME = "DataTamers-AppliedAnalytics";
	// table_id (will be passed in) is the reporting profile id that data is retrieved from   
	private static final String TABLE_ID = "ga:4482952"; // is user specific
    
	public Visits getAllVisitsCount() {
		try {
	      // Service Object to work with the Google Analytics Data Export API.
	      AnalyticsService analyticsService = new AnalyticsService(APPLICATION_NAME);

	      // Client Login Authorization.
	      analyticsService.setUserCredentials(CLIENT_USERNAME, CLIENT_PASS);

	      // Get data from the Account Feed.
	      //getAccountFeed(analyticsService);

	      // Access the Data Feed if the Table Id has been set.
	      if (!TABLE_ID.isEmpty()) {

	          // Get view (profile) data from the Data Feed.
	          dataFeed = getDataFeed(analyticsService);
	      }

	    } catch (AuthenticationException e) {
	      System.err.println("Authentication failed : " + e.getMessage());
	      return null;
	    } catch (IOException e) {
	      System.err.println("Network error trying to retrieve feed: " + e.getMessage());
	      return null;
	    } catch (ServiceException e) {
	      System.err.println("Analytics API responded with an error message: " + e.getMessage());
	      return null;
	    }

		return visitsMapper(dataFeed);
	}
	
	  /**
	   * 
	   * @param {AnalyticsService} Google Analytics service object that
	   *     is authorized through Client Login.
	   */
	  private static DataFeed getDataFeed(AnalyticsService analyticsService)
	      throws IOException, MalformedURLException, ServiceException {

	    // Create a query using the DataQuery Object.
	    DataQuery query = new DataQuery(new URL(
	        "https://www.google.com/analytics/feeds/data"));
	    query.setStartDate("2013-11-01");
	    query.setEndDate("2013-12-01");
	    query.setDimensions("ga:day");
	    query.setMetrics("ga:visits");
	    query.setSort("ga:day");
	    query.setMaxResults(31);
	    query.setIds(TABLE_ID);

	    // Make a request to the API.
	    DataFeed dataFeed = analyticsService.getFeed(query.getUrl(), DataFeed.class);
	    
	    return dataFeed;
	  }

	private Visits visitsMapper(DataFeed dataFeed) {
		Visits v = new Visits();
		
		// parse dataFeed
	
		// Check for error conditions (What action to take upon error?)

		//if (dataFeed.getEntries().isEmpty()) {
			// set v.setAllVisitsCount to indicate error condition
	    //  return null;
	    //}

	    // Error, expect only one metric, visits. 
	    //for (Metric metric : singleEntry.getMetrics()) {
	    //      metricNames.add(metric.getName());
	    // }  
	    // if (metricNames.size()>1){
	    	// set v.setAllVisitsCount to indicate error condition
	    //	return null; 
	    // }
	    	
	    // containers for retrieved data
	    List<String> dayList = new ArrayList<String>();
	    List<Long> visitsList = new ArrayList<Long>();
		
	    // Retrieve data from DataFeed
	    for (DataEntry entry : dataFeed.getEntries()) {
		      dayList.add(entry.stringValueOf("ga:day"));
		      visitsList.add(entry.longValueOf("ga:visits"));
		    }
	   
	    // create the return object
	    long[][] twoDimData = new long[2][dayList.size()];
	  
	    System.out.println("Day    Visits");
	    // put data into return object 
	    for (int i = 0; i<dayList.size(); i++){	
			twoDimData[0][i] = Long.parseLong(dayList.get(i));
			twoDimData[1][i] = visitsList.get(i);
			// Output data to the screen
			System.out.println(""+twoDimData[0][i]+", "+twoDimData[1][i]);
	    }
	    System.out.println("");
		
		v.setAllVisitsCount(twoDimData);
		return v;
	}	
	
	// print method for debugging
  private static void printData(String title, DataFeed dataFeed) {
	    System.out.println(title);
	    for (DataEntry entry : dataFeed.getEntries()) {
	      System.out.println("\t\tDay: " + entry.stringValueOf("ga:day"));
	      System.out.println("\t\tVisits: " + entry.stringValueOf("ga:visits"));
	    }
	    System.out.println();
	  }
		
}
