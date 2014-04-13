package io.analytics.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.analytics.model.GaData;
import com.google.api.services.analytics.model.GaData.ColumnHeaders;

import io.analytics.domain.KeywordInsightData;
import io.analytics.repository.CoreReportingRepository;
import io.analytics.repository.interfaces.ICoreReportingRepository;
import io.analytics.service.interfaces.IKeywordInsightService;
import io.analytics.service.interfaces.ISessionService;

@Service
public class KeywordInsightService implements IKeywordInsightService {

	/*
	 *  Class: KeywordInsightsService
	 *  
	 *  Description: Collects the Google Analytic data for the Keywords Insight widget
	 *  
	 *  Input: credential, profileID, metric, startDate, endDate, number of results.
	 *  Output: domain object PagePerformanceData.
	 *  
	 *  @author gak
	 */	

	@Autowired private ICoreReportingRepository REPOSITORY;
	@Autowired private ISessionService sessionService;

	public String getProfile() {
		//return this.profile;
		return sessionService.getUserSettings().getActiveProfile().getId();
	}

	public KeywordInsightData getKeywordInsightData(Credential credential, String profileID, Date startDate, Date endDate, int maxResults){

		/* * * * * * * * * * * * * * * * * * * * *
		/* GA query organic keywords, visits, visitBounceRate
		/* * * * * * * * * * * * * * * * * * * * */
		
		GaData gaData =null;
		//printColumnHeaders(gaData);
		//printDataTable(gaData);

		ArrayList<String> dataTypes = new ArrayList<String>();
		// Create the domain class and add data
		KeywordInsightData dataObject = new KeywordInsightData();
		
		int keywordColumn = -1; 
		int visitsColumn = -1; 
		int visitBounceRateColumn = -1;
		int mediumColumn = -1;
		int hostnameColumn = -1;
		int column = -1;
		boolean hasData = false;


		/* * * * * * * * * * * * * * * * * * * * *
		/* GA query keywords, visits, visitBounceRate, medium (cpc, organic)
		/* * * * * * * * * * * * * * * * * * * * */
		gaData = REPOSITORY.getKeywords(credential, profileID, startDate, endDate, maxResults, CoreReportingRepository.KEYWORD_FILTER_NOT_PROVIDED_AND_NOT_SET);
		if (gaData!=null){
			//printColumnHeaders(gaData);
			//printDataTable(gaData);

			// REVISIT: Add GaData type checking
			dataTypes.clear();

			// get Organic keyword data
			keywordColumn = -1; 
			visitsColumn = -1; 
			visitBounceRateColumn = -1; 
			mediumColumn = -1;
			column = -1;
			for (ColumnHeaders header : gaData.getColumnHeaders()) {
				column++;
				String name = header.getName();
				if (name.equals("ga:keyword"))
					keywordColumn = column;
				if (name.equals("ga:visits"))
					visitsColumn = column;
				if (name.equals("ga:visitBounceRate"))
					visitBounceRateColumn = column;
				if (name.equals("ga:medium"))
					mediumColumn = column;
				if (name.equals("ga:hostname"))
					hostnameColumn = column;

				dataTypes.add(header.getDataType());
			}

			ArrayList<String> keywords = new ArrayList<String>();
			ArrayList<Integer> visits= new ArrayList<Integer>();
			ArrayList<Double> visitBounceRate = new ArrayList<Double>();
			ArrayList<String> medium = new ArrayList<String>();
			ArrayList<String> hostname = new ArrayList<String>();
			
			List<List<String>> dataRows = gaData.getRows();

			//TODO: Handle this appropriately - this is just a quick fix.
			if (dataRows == null)
				return null;

			try {
				for(List<String> row : dataRows) {
					keywords.add(row.get(keywordColumn));
					visits.add(Integer.parseInt(row.get(visitsColumn)));
					visitBounceRate.add(Double.parseDouble(row.get(visitBounceRateColumn)));
					medium.add(row.get(mediumColumn));
					hostname.add(row.get(hostnameColumn));
				}
			} catch (NumberFormatException e) {
				//The metric we are retrieving is not numeric.
				return null;
			}

			keywords.trimToSize();
			visits.trimToSize();
			visitBounceRate.trimToSize();

			// add data to domain class
			dataObject.setKeywords(keywords);
			dataObject.setVisits(visits);
			dataObject.setVisitBounceRate(visitBounceRate);
			dataObject.setMedium(medium);
			dataObject.setHostname(hostname);
			
			hasData = true;

		}

		/* * * * * * * * * * * * * * * * * * * * *
		/* GA query total visits
		/* * * * * * * * * * * * * * * * * * * * */

		gaData = REPOSITORY.getTotalMetric(credential, profileID, "ga:visits", startDate, endDate);
		if (gaData!=null){
			//printColumnHeaders(gaData);
			//printDataTable(gaData);
	
			visitsColumn = -1; 
			column = -1;
			for (ColumnHeaders header : gaData.getColumnHeaders()) {
				column++;
				String name = header.getName();
				if (name.equals(CoreReportingRepository.VISITS_METRIC))
					visitsColumn = column;
			}

			int datum = -1;
			List<List<String>> dataRows = gaData.getRows();
			try {
				for(List<String> row : dataRows) {
					datum = Integer.parseInt(row.get(visitsColumn));
				}
			} catch (NumberFormatException e) {
				//The metric we are retrieving is not numeric.
				return null;
			}
			dataObject.setVisitsTotal(datum);
			hasData = true;
		}
		
		if (hasData){
			return dataObject;
		}
		else {
			return null;
		}
		
	
	}		
}
