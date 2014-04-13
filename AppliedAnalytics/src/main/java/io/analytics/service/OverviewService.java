package io.analytics.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.analytics.domain.KeywordInsightData;
import io.analytics.domain.OverviewData;
import io.analytics.repository.interfaces.ICoreReportingRepository;
import io.analytics.service.interfaces.IOverviewService;
import io.analytics.service.interfaces.ISessionService;
import io.analytics.repository.CoreReportingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.analytics.model.GaData;
import com.google.api.services.analytics.model.GaData.ColumnHeaders;

@Service
public class OverviewService implements IOverviewService{

	@Autowired private ICoreReportingRepository REPOSITORY;
	@Autowired private ISessionService sessionService;

	public String getProfile() {
		return sessionService.getUserSettings().getActiveProfile().getId();
	}

	public OverviewData getOverviewData(Credential credential, String profileID, Date startDate, Date endDate){
		/* * * * * * * * * * * * * * * * * * * * *
		/* GA query organic keywords, visits, visitBounceRate
		/* * * * * * * * * * * * * * * * * * * * */

		GaData gaData =null;
		//printColumnHeaders(gaData);
		//printDataTable(gaData);

		// REVISIT: Add GaData type checking
		ArrayList<String> dataTypes = new ArrayList<String>();
		// Create the domain class and add data
		OverviewData dataObject = new OverviewData();

		int newVisitsColumn = -1; 
		int percentNewVisitsColumn = -1; 
		int visitsColumn = -1;
		int visitBounceRateColumn = -1;
		int pageviewsPerVisitColumn = -1;
		int avgTimeOnSiteColumn = -1;
		int column = -1;
		
		boolean hasData = false;

		// get overview data totals

		gaData = REPOSITORY.getOverview(credential, profileID, startDate, endDate);

		if (gaData!=null) {

			for (ColumnHeaders header : gaData.getColumnHeaders()) {
				column++;
				String name = header.getName();
				if (name.equals("ga:newVisits"))
					newVisitsColumn = column;
				if (name.equals("ga:percentNewVisits"))
					percentNewVisitsColumn = column;
				if (name.equals("ga:visits"))
					visitsColumn = column;
				if (name.equals("ga:visitBounceRate"))
					visitBounceRateColumn = column;
				if (name.equals("ga:pageviewsPerVisit"))
					pageviewsPerVisitColumn = column;
				if (name.equals("ga:avgTimeOnSite"))
					avgTimeOnSiteColumn = column;

				dataTypes.add(header.getDataType());
			}

			int newVisits = -1;
			double percentNewVisits = -1.0;
			int visits = -1;
			double visitBounceRate = -1.0;
			double pageviewsPerVisit = -1;
			double avgTimeOnSite = -1.0;

			List<List<String>> dataRows = gaData.getRows();
			try {
				for(List<String> row : dataRows) {
					newVisits = Integer.parseInt(row.get(newVisitsColumn));
					percentNewVisits = Double.parseDouble(row.get(percentNewVisitsColumn));
					visits = Integer.parseInt(row.get(visitsColumn));
					visitBounceRate = Double.parseDouble(row.get(visitBounceRateColumn));
					pageviewsPerVisit = Integer.parseInt(row.get(pageviewsPerVisitColumn));
					avgTimeOnSite = Double.parseDouble(row.get(avgTimeOnSiteColumn));
				}
			} catch (NumberFormatException e) {
				//The metric we are retrieving is not numeric.
				return null;
			}

			dataObject.setNewVisitsTotal(newVisits);
			dataObject.setPercentNewVisitsTotal(percentNewVisits);
			dataObject.setVisitsTotal(visits);
			dataObject.setVisitBounceRateTotal(visitBounceRate);
			dataObject.setPageviewsPerVisit(pageviewsPerVisit);
			dataObject.setAvgTimePerVisit(avgTimeOnSite);
			
			// get top two channel overview totals

			gaData = REPOSITORY.getOverviewDim(credential, profileID, startDate, endDate, 2, CoreReportingRepository.MEDIUM_DIMENSION);
// TODO parse this data into json
/*			if (gaData!=null) {

				for (ColumnHeaders header : gaData.getColumnHeaders()) {
					column++;
					String name = header.getName();
					if (name.equals("ga:newVisits"))
						newVisitsColumn  = column;
					if (name.equals("ga:percentNewVisits"))
						percentNewVisitsColumn = column;
					if (name.equals("ga:visits"))
						visitsColumn = column;
					if (name.equals("ga:visitBounceRate"))
						visitBounceRateColumn = column;
					if (name.equals("ga:pageviewsPerVisit"))
						pageviewsPerVisitColumn = column;
					if (name.equals("ga:avgTimeOnSite"))
						avgTimeOnSiteColumn = column;

					dataTypes.add(header.getDataType());
				}

				int newVisits = -1;
				double percentNewVisits = -1.0;
				int visits = -1;
				double visitBounceRate = -1.0;
				double pageviewsPerVisit = -1;
				double avgTimeOnSite = -1.0;

				List<List<String>> dataRows = gaData.getRows();
				try {
					for(List<String> row : dataRows) {
						newVisits = Integer.parseInt(row.get(newVisitsColumn));
						percentNewVisits = Double.parseDouble(row.get(percentNewVisitsColumn));
						visits = Integer.parseInt(row.get(visitsColumn));
						visitBounceRate = Double.parseDouble(row.get(visitBounceRateColumn));
						pageviewsPerVisit = Integer.parseInt(row.get(pageviewsPerVisitColumn));
						avgTimeOnSite = Double.parseDouble(row.get(avgTimeOnSiteColumn));
					}
				} catch (NumberFormatException e) {
					//The metric we are retrieving is not numeric.
					return null;
				}

				dataObject.setNewVisitsTotal(newVisits);
				dataObject.setPercentNewVisitsTotal(percentNewVisits);
				dataObject.setVisitsTotal(visits);
				dataObject.setVisitBounceRateTotal(visitBounceRate);
				dataObject.setPageviewsPerVisit(pageviewsPerVisit);
				dataObject.setAvgTimePerVisit(avgTimeOnSite);
*/
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
