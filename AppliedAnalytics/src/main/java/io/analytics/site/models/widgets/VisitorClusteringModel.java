package io.analytics.site.models.widgets;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.analytics.model.GaData;
import com.google.api.services.analytics.model.GaData.ColumnHeaders;
import com.google.gson.Gson;

import io.analytics.service.interfaces.ICoreReportingService;
import io.analytics.service.interfaces.ISessionService;
import io.analytics.site.models.FilterModel;
import io.analytics.site.models.JSONSerializable;
import io.analytics.site.models.SettingsModel;

public class VisitorClusteringModel extends WidgetModel implements JSONSerializable {

	private ICoreReportingService coreReportingService;
	private ISessionService sessionService;
	private Credential credential;
	private int columnCount;
	private List<ColumnHeaders> columnHeaders;
	private List<List<String>> rawData;
	private List<List<Integer>> numericalData;
	private HashMap<String, Integer> visitorTypeMap = new HashMap<String, Integer>(2);

	public VisitorClusteringModel(ISessionService sessionService, ICoreReportingService coreReportingService) {
		super();
		this.sessionService = sessionService;
		this.coreReportingService = coreReportingService;
		this.credential = sessionService.getCredentials();
		visitorTypeMap.put("Returning Visitor", 0);
		visitorTypeMap.put("New Visitor", 50);
	}
	
	public void updateData() {
		
		FilterModel filter = this.sessionService.getFilter();
		SettingsModel settings = this.sessionService.getUserSettings();
		Date endDate = filter.getActiveEndDate();
		long msInDay = 24L * 60L * 60L * 1000L;
		Date startDate = new Date(endDate.getTime() - 90L * msInDay); //90 days
		String profileId = settings.getActiveProfile().getId();
		
		GaData data = coreReportingService.getDenseVisitorInfo(credential, profileId, startDate, endDate);
		this.columnHeaders = data.getColumnHeaders();
		this.columnCount = this.columnHeaders.size();
		this.rawData = data.getRows();
		normalizeData();
	}
	
	private void normalizeData() {
		this.numericalData = new ArrayList<List<Integer>>();
		for(List<String> row : this.rawData) {
			List<Integer> newRow = new ArrayList<Integer>();
			for (int i=0; i<this.columnCount; i++) {
				newRow.add(convertColumn(row.get(i), i));
			}
			this.numericalData.add(newRow);
		}
	}
	
	private int convertColumn(String value, int columnIndex) {
		
		//TODO: Can we do this a better way?
		//ga:visits,ga:timeOnSite,ga:uniquePageviews
		String columnName = this.columnHeaders.get(columnIndex).getName();
		if (columnName.equals("ga:hour")) 
			return scale(Integer.parseInt(value), 23, 100);
		else if (columnName.equals("ga:dayOfWeek")) 
			return scale(Integer.parseInt(value), 6, 100);
		else if (columnName.equals("ga:operatingSystemVersion"))
			return 0;
		else if (columnName.equals("ga:browserVersion"))
			return 0;
		else if (columnName.equals("ga:visitorType"))
			return visitorTypeMap.get(value);
		else if (columnName.equals("ga:screenResolution"))
			return scale(parseResolution(value).w, 2000, 100);
		else if (columnName.equals("ga:visits"))
			return Integer.parseInt(value);
		else if (columnName.equals("ga:avgTimeOnSite"))
			return scale((int) Double.parseDouble(value), 1800, 100);
		else if (columnName.equals("ga:pageviewsPerVisit"))
			return scale((int) Double.parseDouble(value), 15, 100);
		else
			return 0;
	}
	/**
	 * Scales a positive integer from 0 to maxOutput.
	 * 
	 * @return
	 */
	private int scale(int input, int maxInput, int maxOutput){
		if (maxInput == 0) throw new IllegalArgumentException();
		return (input * maxOutput) / maxInput;
	}
	
	/**
	 * Parses a screen resolution represented as a string. Below are some examples.
	 * 1920x1080
	 * 800x600
	 * 
	 * Assumes the format WxH, where W is the screen width, and H is the screen height.
	 * 
	 * @param res
	 * @throws NumberFormatException - if the input string does not match the format.
	 * @return
	 */
	private Resolution<Integer, Integer> parseResolution(String res) throws NumberFormatException {
		int middle = res.indexOf("x");
		if (middle < 0)
			return null;
		int width = Integer.parseInt(res.substring(0, middle));
		int height = Integer.parseInt(res.substring(middle + 1));
		return new Resolution<Integer, Integer>(width, height);
	}
	
	private class Resolution<W, H> {
		private W w;
		private H h;
		private Resolution(W w, H h) {
			this.w = w;
			this.h = h;
		}
	}
	@Override
	public String getJSONSerialization() {
		Gson g = new Gson();
		JSONObject model = new JSONObject();
		JSONArray raw, numeric;
		try {
			raw = new JSONArray(g.toJson(this.rawData));
			numeric = new JSONArray(g.toJson(this.numericalData));
			model.put("rawData", raw);
			model.put("numericalData", numeric);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return model.toString();
	}

	@Override
	public String getName() {
		return "Visitor Clustering";
	}

	@Override
	public String getDescription() {
		return "No description currently available";
	}

	@Override
	public int getPositionPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getHTMLClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
