package io.analytics.service;
import io.analytics.domain.Visitor;
import io.analytics.repository.IVisitorRepository;

public class VisitorService {
	private IVisitorRepository visitorRepository;
	
	public Visitor getAllVisitors() {
		// may need business logic here at times
		return visitorRepository.getGAallVisitors();
	}
}
