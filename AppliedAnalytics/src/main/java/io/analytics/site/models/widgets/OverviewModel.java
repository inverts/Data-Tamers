package io.analytics.site.models.widgets;

import io.analytics.domain.OverviewData;
import io.analytics.domain.VisitorDeviceData;
import io.analytics.service.interfaces.IOverviewService;
import io.analytics.service.interfaces.ISessionService;
import io.analytics.service.interfaces.IVisitorDeviceService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class OverviewModel extends WidgetModel{

	private JSONObject jsonData;
	private IOverviewService overviewService;
	private IVisitorDeviceService visitorDeviceService;
	private ISessionService sessionService;
	private String activeProfile;
	private Date startDate;
	private Date endDate;
	private int newVisits;
	private double percentNewVisits;
	private int visits;
	private double visitBounceRate;
	private double pageviewsPerVisit;
	private double avgTimePerVisit;
	private List<String> channels;
	private List<Integer> channelNewVisits;
	private List<Double> channelPercentNewVisits;
	private List<Integer> channelVisits;
	private List<Double> channelVisitBounceRate;
	private List<Double> channelPageviewsPerVisit;
	private List<Double> channelAvgTimePerVisit; 

	private List<String> mobileCat;
	private List<String> desktopCat;
	private List<String> mobileOS;
	private List<Integer> mobileVisits;
	private List<String> desktopBrowser;
	private List<Integer> desktopVisits; 
	private List<String> deviceCat;
	private List<String> deviceSoftware;
	private List<Integer> deviceVisits;

	private String[] overviewKeys = new String[]{"Channels", "New Visits", "% New Visits", "Visits", "% Bounce Rate", "Pages Viewed Per Visit", "Avg Visit Duration (sec)"};
	private String[] deviceKeys = new String[]{"Device Categories", "Browser or OS", "Visits"};
	private String[] totalsKeys = new String[]{"New Visits", "% New Visits", "Visits", "% Bounce Rate", "Pages Viewed Per Visit", "Avg Visit Duration (sec)"};
	private String visitsTitle = "Visits totals and breakdown by channel:";
	private String behaviorTitle = "Behavior totals and breakdown by channel:";
	private String deviceTitle = "Top visitor's devices and software interfaces:";
	private String totalsTitle = "Website performance:";

	private final String widgetClass = "overview";
	private final String widgetTitle = "overview.title";	

	DateFormat presentationFormat = new SimpleDateFormat("MM/dd/yyyy"); 

	public OverviewModel(ISessionService sessionService, IOverviewService overviewService, IVisitorDeviceService visitorDeviceService) {

		this.sessionService = sessionService;
		this.overviewService = overviewService;
		this.visitorDeviceService = visitorDeviceService;
		this.jsonData = new JSONObject();
		this.activeProfile = this.overviewService.getProfile();
		this.channels = new ArrayList<String>();
		this.channelNewVisits = new ArrayList<Integer>();
		this.channelPercentNewVisits = new ArrayList<Double>();
		this.channelVisits = new ArrayList<Integer>();
		this.channelVisitBounceRate = new ArrayList<Double>();
		this.channelPageviewsPerVisit = new ArrayList<Double>();
		this.channelAvgTimePerVisit = new ArrayList<Double>();
		mobileCat = new ArrayList<String>();
		desktopCat = new ArrayList<String>();
		mobileOS = new ArrayList<String>();
		mobileVisits = new ArrayList<Integer>();
		desktopBrowser = new ArrayList<String>();
		desktopVisits = new ArrayList<Integer>();
		deviceCat = new ArrayList<String>();
		deviceSoftware = new ArrayList<String>();
		deviceVisits = new ArrayList<Integer>();

		// default dates
		this.endDate = new Date();
		Calendar cal = new GregorianCalendar();
		cal.setTime(this.endDate);
		cal.add(Calendar.DAY_OF_MONTH, -30);
		this.startDate = cal.getTime();

		this.viewCount = 3;
		//updateData();
	}

	public String getName() {
		return "Overview";
	}

	public String getDescription() {
		return "View summary data that characterize your website performance across channels and visitor devices.";
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getActiveProfile() {
		return this.activeProfile;
	}

	/**
	 * TODO: Have this automatically occur when dependencies are updated.
	 * 
	 * TODO: http://en.wikipedia.org/wiki/Refactor
	 */
	public void updateData() {

		OverviewData dataObject = this.overviewService.getOverviewData(this.sessionService.getCredentials(), this.sessionService.getUserSettings().getActiveProfile().getId(), this.startDate, this.endDate);

		VisitorDeviceData vdDataObject = this.visitorDeviceService.getVisitorDeviceData(this.sessionService.getCredentials(), this.sessionService.getUserSettings().getActiveProfile().getId(), this.startDate, this.endDate);
		// over quota error returns no data, must refresh browser to try again
		if (dataObject==null){
			setJsonKeys();
			return;
		}

		newVisits = dataObject.getNewVisitsTotal();
		percentNewVisits = dataObject.getPercentNewVisitsTotal();
		visits = dataObject.getVisitsTotal();
		visitBounceRate = dataObject.getVisitBounceRateTotal();
		pageviewsPerVisit = dataObject.getPageviewsPerVisit();
		avgTimePerVisit = dataObject.getAvgTimePerVisit();
		this.channels.add("All Channels");
		this.channels.addAll(dataObject.getChannels());

		for (int i=0; i<channels.size(); i++){
			if (channels.get(i).equals("cpc"))
				channels.set(i,"Paid Search");
			if (!channels.get(i).startsWith("("))
				channels.set(i, channels.get(i).substring(0, 1).toUpperCase() + channels.get(i).substring(1));
			if (channels.get(i).equals("Organic"))
				channels.set(i, "Organic Search");
		}
		this.channelNewVisits.add(newVisits);
		this.channelNewVisits.addAll(dataObject.getChannelNewVisits());
		this.channelPercentNewVisits.add(this.percentNewVisits);
		this.channelPercentNewVisits.addAll(dataObject.getChannelPercentNewVisits());
		this.channelVisits.add(this.visits);
		this.channelVisits.addAll(dataObject.getChannelVisits());
		this.channelVisitBounceRate.add(this.visitBounceRate);
		this.channelVisitBounceRate.addAll(dataObject.getChannelVisitBounceRate());
		this.channelPageviewsPerVisit.add(this.pageviewsPerVisit);
		this.channelPageviewsPerVisit.addAll(dataObject.getChannelPageviewsPerVisit());
		this.channelAvgTimePerVisit.add(this.avgTimePerVisit);
		this.channelAvgTimePerVisit.addAll(dataObject.getChannelAvgTimePerVisit());

		// Get Visitor Device data
		this.mobileCat.addAll(vdDataObject.getMobileCategories()); 
		this.desktopCat.addAll(vdDataObject.getDesktopCategories());
		this.mobileOS.addAll(vdDataObject.getMobileOS());
		this.mobileVisits.addAll(vdDataObject.getMobileVisits());
		this.desktopBrowser.addAll(vdDataObject.getDesktopBrowser());
		this.desktopVisits.addAll(vdDataObject.getDesktopVisits());

		this.deviceCat.addAll(vdDataObject.getDesktopCategories());
		this.deviceCat.addAll(vdDataObject.getMobileCategories());
		// Capitalize device categories
		for (int i = 0; i<deviceCat.size(); i++) {
			if (!deviceCat.get(i).startsWith("("))
				deviceCat.set(i, deviceCat.get(i).substring(0, 1).toUpperCase() + deviceCat.get(i).substring(1));	
		}
		this.deviceSoftware.addAll(vdDataObject.getDesktopBrowser());
		this.deviceSoftware.addAll(vdDataObject.getMobileOS());
		this.deviceVisits.addAll(vdDataObject.getDesktopVisits());
		this.deviceVisits.addAll(vdDataObject.getMobileVisits());

		List<DeviceData> devices = new ArrayList<DeviceData>();
		Iterator<String> itc = deviceCat.iterator();
		Iterator<String> its = deviceSoftware.iterator();
		Iterator<Integer> itv = deviceVisits.iterator();

		while (itc.hasNext()){
			devices.add(new DeviceData(itc.next(),its.next(),itv.next()));
		}

		// sort ascending, reverse so descending
		Collections.sort(devices);
		Collections.reverse(devices);

		// put back into original arrays for loading in json
		deviceCat.clear();
		deviceSoftware.clear();
		deviceVisits.clear();

		Iterator<DeviceData> itd = devices.iterator();

		while (itd.hasNext()) {
			DeviceData dd = itd.next();
			deviceCat.add(dd.device);
			deviceSoftware.add(dd.software);
			deviceVisits.add(dd.visits);
		}

		// put data into the JSON Object member jsonData
		this.setJson(newVisits, percentNewVisits, visits, visitBounceRate, pageviewsPerVisit, avgTimePerVisit); 
		this.setJsonChannelData(channels,channelNewVisits,channelPercentNewVisits,channelVisits,channelVisitBounceRate, channelPageviewsPerVisit, channelAvgTimePerVisit);
		this.setJsonDevice();
	}

	public void setJsonKeys(){
		JSONObject totals = new JSONObject();
		JSONObject channels = new JSONObject();
		JSONObject devices = new JSONObject();

		try {
			totals.put("keys", totalsKeys);
			totals.put("title", totalsTitle);
			channels.put("title1", visitsTitle);
			channels.put("title2", behaviorTitle);
			channels.put("keys", overviewKeys);			
			devices.put("keys", deviceKeys);
			devices.put("title", deviceTitle);
			this.jsonData.put("totals", totals);
			this.jsonData.put("total", channels);
			this.jsonData.put("devices", devices);
			this.jsonData.put("noData", true); // flag indicating there is no data.

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Device data: breakdown by desktop, mobile, tablet and software interface
	public void setJsonDevice()  {
		try {
			JSONObject devices = new JSONObject();

			devices.put("keys", deviceKeys);
			devices.put("title", deviceTitle);
			devices.put(deviceKeys[0], this.deviceCat);
			devices.put(deviceKeys[1], this.deviceSoftware);
			devices.put(deviceKeys[2], this.deviceVisits);
			this.jsonData.put("devices", devices);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 

	// put data into JSON object to pass to the view overview.jsp
	public void setJson(int newVisits, double percentNewVisits, int visits, double visitBounceRate, double pageviewsPerVisit, double avgTimePerVisit)  {
		try {
			// Overview totals (unused)
			JSONObject total = new JSONObject();

			double[] ovArr = {newVisits, percentNewVisits, visits, visitBounceRate, pageviewsPerVisit, avgTimePerVisit}; 
			total.put("title",totalsTitle);
			total.put("data",ovArr);
			total.put("keys", totalsKeys);

			this.jsonData.put("totals", total);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 

	public void setJsonChannelData(List<String>channelsArr, List<Integer> newVisits, List<Double> percentNewVisits, List<Integer> visits, List<Double> visitBounceRate, List<Double> pageviewsPerVisit, List<Double> avgTimePerVisit)  {
		try {
			// Overview visits and behavior data
			JSONObject channels = new JSONObject();

			channels.put("keys",overviewKeys);
			channels.put("title1", visitsTitle);
			channels.put("title2", behaviorTitle);
			channels.put(overviewKeys[0],channelsArr);
			channels.put(overviewKeys[1], newVisits);
			channels.put(overviewKeys[2],percentNewVisits);
			channels.put(overviewKeys[3],visits);
			channels.put(overviewKeys[4],visitBounceRate);
			channels.put(overviewKeys[5],pageviewsPerVisit);
			channels.put(overviewKeys[6],avgTimePerVisit);

			this.jsonData.put("total", channels);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 

	public JSONObject getDataPoints() {
		return this.jsonData;
	}

	@Override
	public String getJSONSerialization() {
		JSONObject result = new JSONObject();
		try {
			result.put("name", this.getName());
			result.put("description", this.getDescription());
			result.put("priority", this.getPositionPriority());
			result.put("data", this.getDataPoints());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result.toString();
	}

	@Override
	public int getPositionPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getHTMLClass() {
		return this.widgetClass;
	}


	@Override
	public String getTitle() {		
		return this.widgetTitle;
	}
}

//class to hold related device, software, and visits
class DeviceData implements Comparable<DeviceData>{
	public String device;
	public String software;
	public int visits;


	public DeviceData(String d, String sw, int v){
		this.device = d;
		this.software = sw;
		this.visits = v;
	}

	public int compareTo(DeviceData dd){
		// sort visits ascending
		int vCmp = Integer.compare(this.visits, dd.visits);
		int result=0;
		if (vCmp != 0){
			result = vCmp;
		} else {
			int catCmp = device.compareTo(dd.device);
			// sort category descending
			result = (catCmp == 0)?  catCmp : -catCmp;
		}
		return result;
	}
}