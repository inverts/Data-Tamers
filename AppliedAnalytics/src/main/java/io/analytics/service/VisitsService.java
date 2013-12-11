package io.analytics.service;

import io.analytics.domain.Visits;
import io.analytics.repository.VisitsRepository;

public class VisitsService {
	private final VisitsRepository REPOSITORY;

	public VisitsService() {
		this.REPOSITORY = new VisitsRepository();
	}
	public Visits getAllVisits() {
		// may need business logic here at times
		return REPOSITORY.getAllVisitsCount();
	}
}
