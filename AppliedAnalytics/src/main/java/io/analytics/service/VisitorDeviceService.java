package io.analytics.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.analytics.model.GaData;
import com.google.api.services.analytics.model.GaData.ColumnHeaders;

import io.analytics.domain.OverviewData;
import io.analytics.domain.VisitorDeviceData;
import io.analytics.repository.CoreReportingRepository;
import io.analytics.repository.interfaces.ICoreReportingRepository;
import io.analytics.service.interfaces.ISessionService;
import io.analytics.service.interfaces.IVisitorDeviceService;
@Service
public class VisitorDeviceService implements IVisitorDeviceService {
	@Autowired private ICoreReportingRepository REPOSITORY;
	@Autowired private ISessionService sessionService;

	public VisitorDeviceData getVisitorDeviceData(Credential credential, String profileID, Date startDate, Date endDate){

		GaData gaData = null;
		//printColumnHeaders(gaData);
		//printDataTable(gaData);

		// REVISIT: Add GaData type checking
		ArrayList<String> dataTypes = new ArrayList<String>();
		// Create the domain class and add data
		VisitorDeviceData dataObject = new VisitorDeviceData();

		int visitsColumn = -1; 
		int deviceCatColumn = -1; 
		int osColumn = -1;
		int column = -1;

		boolean hasData = false;

		/* * * * * * * * *
		 *  mobile phone and tablet OS visits
		 */
		
		gaData = REPOSITORY.getMobileOS(credential, profileID, startDate, endDate, 10);

		if (gaData!=null) {

			for (ColumnHeaders header : gaData.getColumnHeaders()) {
				column++;
				String name = header.getName();
				if (name.equals("ga:deviceCategory"))
					deviceCatColumn = column;					
				if (name.equals("ga:visits"))
					visitsColumn = column;
				if (name.equals("ga:operatingSystem"))
					osColumn = column;

				dataTypes.add(header.getDataType());
			}

			ArrayList<String> deviceCatArr = new ArrayList<String>();
			ArrayList<Integer> visitsArr = new ArrayList<Integer>();
			ArrayList<String> osArr = new ArrayList<String>();

			List<List<String>> dataRows = gaData.getRows();

			//TODO: Handle this appropriately - this is just a quick fix.
			if (dataRows == null)
				return null;

			try {
				for(List<String> row : dataRows) {
					deviceCatArr.add(row.get(deviceCatColumn));
					visitsArr.add(Integer.parseInt(row.get(visitsColumn)));
					osArr.add(row.get(osColumn));
				}
			} catch (NumberFormatException e) {
				//The metric we are retrieving is not numeric.
				return null;
			}

			// add data to domain class
			dataObject.setMobileCategories(deviceCatArr);
			dataObject.setMobileOS(osArr);
			dataObject.setMobileVisits(visitsArr);

			hasData = true;
		}
		
		/* * * * * * * * *
		 *  desktop browser visits data
		 */
		column = -1;
		gaData = REPOSITORY.getDesktopBrowser(credential, profileID, startDate, endDate, 3);

		if (gaData!=null) {

			for (ColumnHeaders header : gaData.getColumnHeaders()) {
				column++;
				String name = header.getName();
				if (name.equals("ga:deviceCategory"))
					deviceCatColumn = column;					
				if (name.equals("ga:visits"))
					visitsColumn = column;
				if (name.equals("ga:browser"))
					osColumn = column;

				dataTypes.add(header.getDataType());
			}

			ArrayList<String> deviceCatArr = new ArrayList<String>();
			ArrayList<Integer> visitsArr = new ArrayList<Integer>();
			ArrayList<String> browser = new ArrayList<String>();

			List<List<String>> dataRows = gaData.getRows();

			//TODO: Handle this appropriately - this is just a quick fix.
			if (dataRows == null)
				return null;

			try {
				for(List<String> row : dataRows) {
					deviceCatArr.add(row.get(deviceCatColumn));
					visitsArr.add(Integer.parseInt(row.get(visitsColumn)));
					browser.add(row.get(osColumn));
				}
			} catch (NumberFormatException e) {
				//The metric we are retrieving is not numeric.
				return null;
			}

			// add data to domain class
			dataObject.setDesktopCategories(deviceCatArr);
			dataObject.setDesktopBrowser(browser);
			dataObject.setDesktopVisits(visitsArr);

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
