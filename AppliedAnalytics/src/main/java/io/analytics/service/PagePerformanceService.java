package io.analytics.service;
import io.analytics.domain.PagePerformanceData;
import io.analytics.repository.interfaces.ICoreReportingRepository;
import io.analytics.repository.CoreReportingRepository;
import io.analytics.service.interfaces.IPagePerfomanceService;
import io.analytics.service.interfaces.ISessionService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.analytics.model.GaData;
import com.google.api.services.analytics.model.GaData.ColumnHeaders;

@Service
public class PagePerformanceService implements IPagePerfomanceService{
	
	/*
	 *  Class: PagePerformanceService 
	 *  
	 *  Description: Collects the Google Analytic data for the Website Performance widget
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
	
	public PagePerformanceData getPagePerformanceData(Credential credential, String profileID, Date startDate, Date endDate, int maxResults){
		
		GaData gaData = REPOSITORY.getPagePerformance(credential, profileID, startDate, endDate, maxResults);
	
		//printColumnHeaders(gaData);
		//printDataTable(gaData);
		
		// REVISIT: Add GaData type checking
		ArrayList<String> dataTypes = new ArrayList<String>();
	
		int pagePathColumn = -1; 
		int visitsColumn = -1; 
		int visitBounceRateColumn = -1; 
		int exitRateColumn = -1; 
		int column = -1;
		for (ColumnHeaders header : gaData.getColumnHeaders()) {
			column++;
			String name = header.getName();
			if (name.equals("ga:pagePath"))
				pagePathColumn = column;
		    if (name.equals("ga:visits"))
				visitsColumn = column;
			if (name.equals("ga:visitBounceRate"))
				visitBounceRateColumn = column;
			if (name.equals("ga:exitRate"))
				exitRateColumn = column;
				
			dataTypes.add(header.getDataType());
		}
		
		ArrayList<String> pagePath = new ArrayList<String>();
		ArrayList<Integer> visits= new ArrayList<Integer>();
		ArrayList<Double> visitBounceRate = new ArrayList<Double>();
		ArrayList<Double> exitRate = new ArrayList<Double>();
		
		List<List<String>> dataRows = gaData.getRows();
		try {
		for(List<String> row : dataRows) {
			String one = row.get(pagePathColumn);
			int two = Integer.parseInt(row.get(visitsColumn));
			double three = Double.parseDouble(row.get(visitBounceRateColumn));
			double four = Double.parseDouble(row.get(exitRateColumn));
			pagePath.add(one);
			visits.add(two);
			visitBounceRate.add(three);
			exitRate.add(four);
		}
		} catch (NumberFormatException e) {
			//The metric we are retrieving is not numeric.
			return null;
		}
		
		pagePath.trimToSize();
		visits.trimToSize();
		visitBounceRate.trimToSize();
		exitRate.trimToSize();
		
		// Create the domain dataObject, add page performance data
		PagePerformanceData dataObject = new PagePerformanceData();
		dataObject.setPagePathData(pagePath);
		dataObject.setVisitsData(visits);
		dataObject.setVisitsBounceRateData(visitBounceRate);
		dataObject.setExitRateData(exitRate);

		// ********************
		// GA query visits (total)
		
		gaData = REPOSITORY.getTotalMetric(credential, profileID,  CoreReportingRepository.VISITS_METRIC, startDate, endDate);
		
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
		dataRows = gaData.getRows();
		try {
		for(List<String> row : dataRows) {
			datum = Integer.parseInt(row.get(visitsColumn));
		}
		} catch (NumberFormatException e) {
			//The metric we are retrieving is not numeric.
			return null;
		}
		
		// domain dataObject, add visits total
		dataObject.setVisitsTotal(datum);
		return dataObject;
	}		

}
