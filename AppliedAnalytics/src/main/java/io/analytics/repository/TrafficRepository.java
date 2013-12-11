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
		// temporary hardcoded data
		String[] ts = {"all"};	
		t.setTrafficSources(ts);
		return t;
		
	}
}
