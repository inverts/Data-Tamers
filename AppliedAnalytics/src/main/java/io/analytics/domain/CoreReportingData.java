package io.analytics.domain;

import java.io.Serializable;

import com.google.api.services.analytics.model.GaData;

public class CoreReportingData implements Serializable{
	private GaData data;

	public CoreReportingData (GaData d){
		this.data = d;
	}
	
	public GaData getData() {
		return data;
	}
	
	public void setData(GaData d) {
	    this.data = d;
	}
}
