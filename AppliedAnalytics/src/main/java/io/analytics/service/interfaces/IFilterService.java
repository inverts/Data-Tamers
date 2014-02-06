package io.analytics.service.interfaces;

import org.springframework.stereotype.Service;

import io.analytics.domain.Filter;

@Service
public interface IFilterService {
	
	/**
	 * Adds a new filter to the database. The filter must have every property set,
	 * with the exception of a parent account ID and a Filter ID.
	 * 
	 * @param f
	 * @return Returns the filter inserted, including an ID.
	 */
	public Filter addNewFilter(Filter f);
	
}
