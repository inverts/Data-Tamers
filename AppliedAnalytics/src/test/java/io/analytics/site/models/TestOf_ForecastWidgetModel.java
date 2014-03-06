package io.analytics.site.models;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;




import io.analytics.domain.CoreReportingData;
import io.analytics.service.interfaces.ICoreReportingService;
import io.analytics.service.interfaces.ISessionService;
import io.analytics.site.models.widgets.ForecastWidgetModel;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.analytics.model.GaData;
import com.google.api.services.analytics.model.GaData.ColumnHeaders;
import com.google.api.services.analytics.model.Profile;

import org.mockito.Mockito;

public class TestOf_ForecastWidgetModel {

	@Autowired
	private ICoreReportingService CoreReportingService;
	@Autowired
	private ISessionService SessionService;

	@Test
	/**
	 * White-box testing of the ForecastWidgetModel's updateData function.
	 */
	public void testUpdateData() {
		Date endDate = new Date();
		Date startDate = new Date(endDate.getTime() - (86400000 * 30L));
		Calendar startDateC = Calendar.getInstance();
		startDateC.setTime(startDate);
		long timespan = (endDate.getTime() - startDate.getTime()) / 86400000L;
		int rows = (int) (ForecastWidgetModel.getExcessDataPointsNeeded(timespan + 1) + timespan);
		
		Random rng = new Random(1L);
		ArrayList<List<String>> data = new ArrayList<List<String>>(rows);
		for (int i = 0; i < rows; i++)
			data.add(Arrays.asList(String.format("%04d", i), Integer.toString(rng.nextInt(100))));

		ISessionService sessionService = Mockito.mock(ISessionService.class);
		SettingsModel settingsModel = Mockito.mock(SettingsModel.class);
		Profile profile = new Profile().setId("FAKE_ID");
		Mockito.when(sessionService.getCredentials()).thenReturn(null);
		Mockito.when(sessionService.getUserSettings()).thenReturn(settingsModel);
		Mockito.when(settingsModel.getActiveProfile()).thenReturn(profile);
		ICoreReportingService coreReportingService = buildICoreReportingService(data, new String[] { "ga:nthDay",
				"ga:visits" });
		ForecastWidgetModel forecastWidgetModel = new ForecastWidgetModel(sessionService, coreReportingService);

		assert forecastWidgetModel.updateData() : "updateData() did not complete successfully (returned false).";

		ArrayList<Double> yValues = forecastWidgetModel.getYValues();
		ArrayList<Double> yValuesNormalized = forecastWidgetModel.getyValuesNormalized();

		assert yValues != null : "Processed data was null.";
		assert yValuesNormalized != null : "Normalized processed data was null.";

		assert yValues.size() == yValuesNormalized.size() : "Normalized data size is different than regular data size.";

		assert yValues.size() == timespan : "Processed data array is not the correct length (" + data.size() + " vs. "
				+ yValues.size() + ").";
		for (int i = 0; i < timespan; i++)
			assert Double.parseDouble(data.get(data.size() - i - 1).get(1)) == yValues.get(yValues.size() - i - 1) : "Processed data array is not equivalent to input data (item #"
					+ i + ").";

		ArrayList<Double> yValuesForecast = forecastWidgetModel.getYValuesForecast();
		ArrayList<Double> yValuesForecastNormalized = forecastWidgetModel.getyValuesForecastNormalized();

		assert yValuesForecast != null : "Forecast data was null.";
		assert yValuesForecastNormalized != null : "Normalized forecast data was null.";

		assert yValuesForecastNormalized.size() == yValuesForecast.size() : "Normalized forecast size is different than regular forecast size.";

		// Should we allow a zero-length array of forecast data?

		for (int i = 0; i < yValuesForecast.size(); i++) {
			assert !yValuesForecast.get(i).isNaN() : "Forecast data returned NaN data points.";
			assert yValuesForecast.get(i) >= 0.0 : "Forecast data returned negative data points.";
		}

	}

	/**
	 * Builds a mock ICoreReportingService, given a set of data to return from
	 * the getMetricByDay method.
	 * 
	 * @param dataTable
	 * @return
	 */
	private ICoreReportingService buildICoreReportingService(List<List<String>> dataTable, String[] columnHeaders) {
		// These classes are final and cannot be mocked with Mockito.

		ColumnHeaders dimension = new ColumnHeaders();
		dimension.setName(columnHeaders[0]);

		ColumnHeaders metric = new ColumnHeaders();
		metric.setName(columnHeaders[1]);

		GaData gaData = new GaData();
		gaData.setColumnHeaders(Arrays.asList(dimension, metric));
		gaData.setRows(dataTable);

		CoreReportingData coreReportingData = Mockito.mock(CoreReportingData.class);
		Mockito.when(coreReportingData.getData()).thenReturn(gaData);

		ICoreReportingService coreReportingService = Mockito.mock(ICoreReportingService.class);
		Mockito.when(
				coreReportingService.getMetricByDay(Mockito.<Credential> any(), Mockito.anyString(),
						Mockito.anyString(), Mockito.<Date> any(), Mockito.<Date> any(), Mockito.anyInt())).thenReturn(
				coreReportingData);

		return coreReportingService;

	}
}
