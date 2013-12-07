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

	    // Output data to the screen.
	    System.out.println("----------- Data Feed Results ----------");
	    for (DataEntry entry : dataFeed.getEntries()) {
	      System.out.println(
	        "\nPage Title = " + entry.stringValueOf("ga:pageTitle") +
	        "\nPage Path  = " + entry.stringValueOf("ga:pagePath") +
	        "\nPageviews  = " + entry.stringValueOf("ga:pageviews"));
	    }
	    return dataFeed;
	  }

	private Visits visitsMapper(DataFeed dataFeed) {
		Visits v = new Visits();
	    // parse dataFeed
			
	    //if (dataFeed.getEntries().isEmpty()) {
			// set v.setAllVisitsCount to indicate error condition
	    //  return null;
	    //}
	    DataEntry singleEntry = dataFeed.getEntries().get(0);
	    // dimensions = day number
	    List<String> feedDataDay = new ArrayList<String>();
	    List<String> metricNames = new ArrayList<String>();
	    List<Long> feedDataValues = new ArrayList<Long>();

	    // get the metric names. There should only be one.
	    for (Metric metric : singleEntry.getMetrics()) {
	         metricNames.add(metric.getName());
	    }
	    
	   // if (metricNames.size()>1){
	    	// set v.setAllVisitsCount to indicate error condition
	    //	return null; 
	   // }
	    	// Error, expect only one metric, visits.
	    // Put all the dimensions into an array.
	    for (Dimension dimension : singleEntry.getDimensions()) {
	    	feedDataDay.add(dimension.getName());
	    }

	    // Put the metric value for each day into the list.
	    // REVISIT? Is there an entry for each day or is there one 
	    //   entry for the whole metric?
	    for (DataEntry entry : dataFeed.getEntries()) {  
	      for (String dataDay : feedDataDay) {
	    	  feedDataValues.add(entry.longValueOf(dataDay));
	      }
	    }
	    
	    // create data array
	    long[][] twoDimData = new long[2][feedDataValues.size()];
	    
	    //System.out.println("");
	    for (int i = 0; i<twoDimData.length; i++){
			twoDimData[0][i] = Long.parseLong(feedDataDay.get(i));
			twoDimData[1][i] = feedDataValues.get(i);
			// Output data to the screen
			//System.out.println(""+twoDimData[0][i]+", "+twoDimData[1][i]);
	    }
	    //System.out.println("");
		
		/*int[] tmp = {88,135,114,131,104,139,138,106,102,85,137,139,132,109,88,114,92,90,149,138,134,108,106,95,132,112,104,76,96,91};
		int[][] pd = new int[2][30];
		for (int i = 0; i<30; i++) {
			pd[0][i]=i+1;
			pd[1][i] = tmp[i];
		}*/
		v.setAllVisitsCount(twoDimData);
		return v;
	}	
		
}
