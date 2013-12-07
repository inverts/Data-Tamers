package io.analytics.domain;

import java.io.Serializable;

public class Visits implements Serializable{
	// private long newVistsCount;
	// add visitors/page
	private long[][] allVisitsCount;

	public long[][] getAllVisitsCount() {
		return allVisitsCount;
	}

	public void setAllVisitsCount(long[][] avc) {
		for (int i=0; i<avc.length; i++){
		System.arraycopy(avc[i], 0,
                this.allVisitsCount[i], 0, avc[i].length);
		}
	}
}
