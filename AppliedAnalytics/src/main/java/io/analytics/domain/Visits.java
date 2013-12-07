package io.analytics.domain;

import java.io.Serializable;

public class Visits implements Serializable{
	// private long newVistsCount;
	// add visitors/page
	private int[][] allVisitsCount;

	public int[][] getAllVisitsCount() {
		return allVisitsCount;
	}

	public void setAllVisitsCount(int[][] avc) {
		for (int i=0; i<avc.length; i++){
		System.arraycopy(avc[i], 0,
                this.allVisitsCount[i], 0, avc[i].length);
		}
	}
}
