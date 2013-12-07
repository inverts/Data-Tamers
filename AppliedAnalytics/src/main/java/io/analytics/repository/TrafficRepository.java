package io.analytics.repository;
import io.analytics.domain.Traffic;

public class TrafficRepository implements ITrafficRepository {
	public Traffic getTrafficSources() {
		// GA library method call? 
		// Are the traffic sources always the same or do we 
		//   need to query GA for each user?
		String d = null;
		return trafficMapper(d);
	}
	
	private Traffic trafficMapper(String json){
		Traffic t = new Traffic();
		// parse json
		String[] ts = {"all visits",
				"new visitors",
				"returning visitors",
				"paid search traffic",
				"non-paid search traffic",
				"search traffic",
				"referral traffic",
				"visits with conversions",
				"visits with transactions",
				"mobile traffic",
				"non-bounce visits",
				"tablet visits"};
		t.setTrafficSources(ts);
		return t;
		
	}
}
