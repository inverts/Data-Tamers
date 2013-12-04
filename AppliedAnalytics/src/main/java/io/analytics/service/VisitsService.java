package io.analytics.service;
import io.analytics.domain.Visits;
import io.analytics.repository.IVisitsRepository;

public class VisitsService {
	private IVisitsRepository visitsRepository;
	
	public Visits getAllVisits() {
		// may need business logic here at times
		return visitsRepository.getAllVisitsCount();
	}
}
