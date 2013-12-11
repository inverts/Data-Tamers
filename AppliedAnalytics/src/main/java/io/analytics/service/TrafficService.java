package io.analytics.service;

import io.analytics.domain.Traffic;
import io.analytics.repository.TrafficRepository;

public class TrafficService {
	private final TrafficRepository REPOSITORY;
	
	public TrafficService() {
		this. REPOSITORY = new TrafficRepository();
	}
	
	public Traffic getAllVisits() {
		// may need business logic here at times
		return REPOSITORY.getTrafficSources();
	}
}
