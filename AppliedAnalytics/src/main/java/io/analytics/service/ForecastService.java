package io.analytics.service;

import io.analytics.domain.ForecastData;
import io.analytics.repository.CoreReportingRepository;
import io.analytics.repository.interfaces.ICoreReportingRepository;
import io.analytics.service.interfaces.IForecastService;
import io.analytics.service.interfaces.ISessionService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.analytics.model.GaData;

@Service
public class ForecastService implements IForecastService {

	@Autowired private ICoreReportingRepository REPOSITORY;
	@Autowired private ISessionService sessionService;
  
	public ForecastData getForecastData (Credential credential, String profileID, String metric, Date startDate, Date endDate, int maxResults){
		/* * * * * * * * * * * * * * * 
		 * get 2D metric data by day
		 */
		
		GaData gaData = REPOSITORY.getMetricForGaDimension(credential, profileID, metric, CoreReportingRepository.NDAY_DIMENSION, startDate, endDate, maxResults);
		if (gaData == null) {
			//throw exception;
		}
		
        /* Formatting data. */
        List<GaData.ColumnHeaders> columns = gaData.getColumnHeaders();
        List<String> stringColumns = new ArrayList<String>();
        for (GaData.ColumnHeaders columnHeader : columns) {
                stringColumns.add(columnHeader.getName());
        }

        List<List<String>> dataRows = gaData.getRows();
        if (dataRows.isEmpty()){
        	//throw exception
        }
             
        int metricColumn = stringColumns.indexOf(metric);
        int dayColumn = stringColumns.indexOf(CoreReportingRepository.NDAY_DIMENSION);
        
        ArrayList<Double> xValues = new ArrayList<Double>();
        ArrayList<Double> yValues = new ArrayList<Double>();
        
        for (int i=0; i < dataRows.size(); i++) {
            xValues.add(Double.parseDouble(dataRows.get(i).get(dayColumn)));
            yValues.add(Double.parseDouble(dataRows.get(i).get(metricColumn)));
        }     

		// Create the domain dataObject, add page performance data
		ForecastData dataObject = new ForecastData();
		dataObject.setXValuesByDay(xValues);
		dataObject.setYValuesByDay(yValues);

		/* * * * * * * * * * * * * * * * * * * *
		 * get 2D metric data by day of the week
		 */ 
		
		gaData = REPOSITORY.getMetricForGaDimension(credential, profileID, metric, CoreReportingRepository.DAYOFWEEK_DIMENSION , startDate, endDate, maxResults);
		if (gaData == null) {
			//throw exception;
		}
		
        /* Formatting data. */
        columns = gaData.getColumnHeaders();
        stringColumns.clear();
        for (GaData.ColumnHeaders columnHeader : columns) {
                stringColumns.add(columnHeader.getName());
        }

        dataRows = gaData.getRows();
        if (dataRows.isEmpty()){
        	//throw exception
        }
             
        metricColumn = stringColumns.indexOf(metric);
        int dayOfWeekColumn = stringColumns.indexOf(CoreReportingRepository.DAYOFWEEK_DIMENSION);
        
        xValues.clear();
        yValues.clear();
        
        for (int i=0; i < dataRows.size(); i++) {
            xValues.add(Double.parseDouble(dataRows.get(i).get(dayOfWeekColumn)));
            yValues.add(Double.parseDouble(dataRows.get(i).get(metricColumn)));
        }     

		dataObject.setXValuesByDayOfWeek(xValues);
		dataObject.setYValuesByDayOfWeek(yValues);		
		

		return dataObject;
	}
}
