import com.google.gdata.client.analytics.AnalyticsService;
import com.google.gdata.client.analytics.DataQuery;
import com.google.gdata.data.analytics.AccountEntry;
import com.google.gdata.data.analytics.AccountFeed;
import com.google.gdata.data.analytics.DataEntry;
import com.google.gdata.data.analytics.DataFeed;

import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This simple program retrieves data from the Google Analytics API.
 * It first authorizes the user with Google Analytics via the
 * ClientLogin routine. It then retrieves 50 records from the 
 * Google Analytics Account Feed and 10 records from the Google Analytics
 * Data Feed.
 */
public class GettingStarted {

  // Credentials for Client Login Authorization.
  private static final String CLIENT_USERNAME = "seo@clearmarketingseattle.com";
  private static final String CLIENT_PASS = "@Temp-Pass";

  // Table ID constant
  private static final String TABLE_ID = "ga:4482952";

  public static void main(String args[]) {
    try {
      // Service Object to work with the Google Analytics Data Export API.
      AnalyticsService analyticsService = new AnalyticsService("gaExportAPI_acctSample_v2.0");

      // Client Login Authorization.
      analyticsService.setUserCredentials(CLIENT_USERNAME, CLIENT_PASS);

      // Get data from the Account Feed.
      //getAccountFeed(analyticsService);

      // Access the Data Feed if the Table Id has been set.
      if (!TABLE_ID.isEmpty()) {

          // Get view (profile) data from the Data Feed.
          getDataFeed(analyticsService);
      }

    } catch (AuthenticationException e) {
      System.err.println("Authentication failed : " + e.getMessage());
      return;
    } catch (IOException e) {
      System.err.println("Network error trying to retrieve feed: " + e.getMessage());
      return;
    } catch (ServiceException e) {
      System.err.println("Analytics API responded with an error message: " + e.getMessage());
      return;
    }
  }

  /**
   * Request 50 Google Analytics views (profiles) the authorized user has
   * access to and display Account Name, View (Profile) Name, View (Profile) ID 
   * and Table ID for each view (profile). The Table ID value is used 
   * to make requests to the Data Feed.
   * @param {AnalyticsService} Google Analytics service object that
   *     is authorized through Client Login.
   */
  private static void getAccountFeed(AnalyticsService analyticsService)
      throws IOException, MalformedURLException, ServiceException {

    // Construct query from a string.
    URL queryUrl = new URL(
        "https://www.google.com/analytics/feeds/accounts/default?max-results=50");

    // Make request to the API.
    AccountFeed accountFeed = analyticsService.getFeed(queryUrl, AccountFeed.class);

    // Output the data to the screen.
    System.out.println("-------- Account Feed Results --------");
    for (AccountEntry entry : accountFeed.getEntries()) {
      System.out.println(
        "\nAccount Name  = " + entry.getProperty("ga:accountName") +
        "\nView (Profile) Name  = " + entry.getTitle().getPlainText() +
        "\nView (Profile) Id    = " + entry.getProperty("ga:profileId") +
        "\nTable Id      = " + entry.getTableId().getValue());
    }
  }

  /**
   * Request the top 10 page paths, page titles and pageviews, 
   * in descending order by pageviews, for a particular view (profile).
   * @param {AnalyticsService} Google Analytics service object that
   *     is authorized through Client Login.
   */
  private static void getDataFeed(AnalyticsService analyticsService)
      throws IOException, MalformedURLException, ServiceException {

    // Create a query using the DataQuery Object.
    DataQuery query = new DataQuery(new URL(
        "https://www.google.com/analytics/feeds/data"));
    query.setStartDate("2009-04-01");
    query.setEndDate("2009-04-30");
    query.setDimensions("ga:pageTitle,ga:pagePath");
    query.setMetrics("ga:pageviews");
    query.setSort("-ga:pageviews");
    query.setMaxResults(10);
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
  }
}