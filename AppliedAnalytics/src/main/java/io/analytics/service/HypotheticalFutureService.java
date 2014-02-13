package io.analytics.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.analytics.model.GaData;

import io.analytics.domain.HypotheticalFutureData;
import io.analytics.repository.interfaces.ICoreReportingRepository;
import io.analytics.service.interfaces.IHypotheticalFutureService;
import io.analytics.service.interfaces.ISessionService;

@Service
public class HypotheticalFutureService implements IHypotheticalFutureService {

	@Autowired private ICoreReportingRepository REPOSITORY;
	@Autowired private ISessionService sessionService;
  
	public String getProfile() {
		//return this.profile;
		return sessionService.getUserSettings().getActiveProfile().getId();
	}
	
	public HypotheticalFutureData getHypotheticalFutureData(Credential credential, String profileID, String metric, Date startDate, Date endDate, int maxResults){
		GaData gaData = REPOSITORY.getTopTrafficSources1(credential, profileID, metric, startDate, endDate, maxResults);
		if (gaData == null) {
			System.out.println("HypotheticalFutureData domain object: gaData is null.");
			//throw exception;
			
		}
		
        /* Formatting data. */
        List<GaData.ColumnHeaders> columns = gaData.getColumnHeaders();
        List<String> stringColumns = new ArrayList<String>();
        for (GaData.ColumnHeaders columnHeader : columns) {
                stringColumns.add(columnHeader.getName());
        }
             
        int metricColumn = stringColumns.indexOf(metric);      
        ArrayList<String> trafficSources = new ArrayList<String>();
        List<List<String>> dataRows = gaData.getRows();
        if (dataRows.isEmpty()){
        	System.out.println("HypotheticalFutureData domain object: dataRows is empty.");
        	//throw exception
        }
        
        for (List<String> row : dataRows) {
            trafficSources.add(row.get(metricColumn));
        }     

		// Create the domain dataObject, add page performance data
		HypotheticalFutureData dataObject = new HypotheticalFutureData();
		dataObject.setTrafficSources(trafficSources);

		return dataObject;
	}
}
