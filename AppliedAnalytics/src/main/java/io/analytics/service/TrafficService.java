package io.analytics.service;
import io.analytics.domain.Traffic;
import io.analytics.repository.ITrafficRepository;

public class TrafficService {
	private ITrafficRepository trafficRepository;
	
	public Traffic getAllVisits() {
		// may need business logic here at times
		return trafficRepository.getTrafficSources();
	}
}
