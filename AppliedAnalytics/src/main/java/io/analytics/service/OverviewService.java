package io.analytics.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

		/* * * * * * * * * * *
		 * Overview totals data 
		 */

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
					percentNewVisits = Math.round(100.0*Double.parseDouble(row.get(percentNewVisitsColumn))/100.0);
					visits = Integer.parseInt(row.get(visitsColumn));
					visitBounceRate = Math.round(100.0*Double.parseDouble(row.get(visitBounceRateColumn))/100.0);
					pageviewsPerVisit = Math.round(100.0*Double.parseDouble(row.get(pageviewsPerVisitColumn))/100.0);
					avgTimeOnSite = Math.round(100.0*Double.parseDouble(row.get(avgTimeOnSiteColumn))/100.0);
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
		}

		/* * * * * * * * *
		 *  channel overview data
		 */

		int mediumColumn = -1;
		column = -1;
		
		gaData = REPOSITORY.getOverviewDim(credential, profileID, startDate, endDate, 10, CoreReportingRepository.MEDIUM_DIMENSION);

		if (gaData!=null) {

			for (ColumnHeaders header : gaData.getColumnHeaders()) {
				column++;
				String name = header.getName();
				if (name.equals("ga:medium"))
					mediumColumn = column;					
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

			ArrayList<String> mediumArr = new ArrayList<String>();
			ArrayList<Integer> newVisitsArr = new ArrayList<Integer>();
			ArrayList<Double> percentNewVisitsArr= new ArrayList<Double>();
			ArrayList<Integer> visitsArr = new ArrayList<Integer>();
			ArrayList<Double> visitBounceRateArr = new ArrayList<Double>();
			ArrayList<Double> pageviewsPerVisitArr = new ArrayList<Double>();
			ArrayList<Double> avgTimeOnSiteArr = new ArrayList<Double>();

			List<List<String>> dataRows = gaData.getRows();

			//TODO: Handle this appropriately - this is just a quick fix.
			if (dataRows == null)
				return null;


			try {
				for(List<String> row : dataRows) {
					mediumArr.add(row.get(mediumColumn));
					newVisitsArr.add(Integer.parseInt(row.get(newVisitsColumn)));
					percentNewVisitsArr.add(Math.round(100.0*Double.parseDouble(row.get(percentNewVisitsColumn)))/100.0);
					visitsArr.add(Integer.parseInt(row.get(visitsColumn)));
					visitBounceRateArr.add(Math.round(100.0*Double.parseDouble(row.get(visitBounceRateColumn)))/100.0);
					pageviewsPerVisitArr.add(Math.round(100.0*Double.parseDouble(row.get(pageviewsPerVisitColumn)))/100.0);
					avgTimeOnSiteArr.add(Math.round(100.0*Double.parseDouble(row.get(avgTimeOnSiteColumn)))/100.0);
				}
			} catch (NumberFormatException e) {
				//The metric we are retrieving is not numeric.
				return null;
			}

			// add data to domain class
			dataObject.setChannels(mediumArr);
			dataObject.setChannelNewVisits(newVisitsArr);
			dataObject.setChannelPercentNewVisits(percentNewVisitsArr);
			dataObject.setChannelVisits(newVisitsArr);
			dataObject.setChannelVisitBounceRate(visitBounceRateArr);
			dataObject.setChannelPageviewsPerVisit(pageviewsPerVisitArr);
			dataObject.setChannelAvgTimePerVisit(avgTimeOnSiteArr);

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
