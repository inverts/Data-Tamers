package io.analytics.domain;

import java.io.Serializable;

public class Visitor implements Serializable{
// private long newVistorsCount;
// add visitors/page
private long allVisitorsCount;

public long getAllVisitorCount() {
	return allVisitorsCount;
}

public void setAllVisitorCount(long vc) {
	this.allVisitorsCount = vc;
}
}
