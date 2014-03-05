package io.analytics.site.models.widgits;

import io.analytics.domain.Widget;
import io.analytics.site.models.JSONSerializable;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DashboardModel implements JSONSerializable {

	private String name;
	private int id;
	private int filterId;
	private List<Integer> widgetIds;
	private List<Integer> widgetTypeIds;
	private List<Widget> widgets;
	
	public DashboardModel() {
		
	}
	public DashboardModel(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the filterId
	 */
	public int getFilterId() {
		return filterId;
	}

	/**
	 * @param filterId the filterId to set
	 */
	public void setFilterId(int filterId) {
		this.filterId = filterId;
	}

	/**
	 * @return the widgetIds
	 */
	public List<Integer> getWidgetIds() {
		return widgetIds;
	}

	/**
	 * @param widgetIds the widgetIds to set
	 */
	public void setWidgetIds(List<Integer> widgetIds) {
		this.widgetIds = widgetIds;
	}
	
	public void setWidgetIdsWithWidgets(List<Widget> widgets) {
		this.widgetIds = new ArrayList<Integer>();
		this.widgetTypeIds = new ArrayList<Integer>();
		for (Widget w : widgets) {
			this.widgetIds.add(w.getId());
			this.widgetTypeIds.add(w.getWidgetTypeId());
		}
	}
	
	public void setWidgets(List<Widget> widgets) {
		this.widgets = new ArrayList<Widget>(widgets);
	}


	@Override
	public String getJSONSerialization() {
		JSONObject result = new JSONObject();
		try {
			result.put("id", this.id);
			result.put("filterId", this.filterId);
			result.put("name", this.name);
			
			JSONArray widgets = new JSONArray();
			for (Widget w : this.widgets)
				widgets.put(new JSONObject(w.getJSONSerialization()));
			result.put("widgets", widgets);
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result.toString();
	}
	
}

