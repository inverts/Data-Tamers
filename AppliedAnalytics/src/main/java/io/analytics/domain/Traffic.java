package io.analytics.domain;
import java.io.Serializable;

public class Traffic implements Serializable{	
	private String[] trafficSources;

	public String[] getTrafficSources() {
		return trafficSources;
	}

	public void setTrafficSources(String[] ts) {
		for (int i=0; i<ts.length; i++){
			this.trafficSources[i] = ts[i];
			}
	}
}
