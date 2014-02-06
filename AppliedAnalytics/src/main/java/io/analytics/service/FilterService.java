package io.analytics.service;

import io.analytics.domain.Filter;
import io.analytics.repository.FilterRepository;
import io.analytics.repository.interfaces.IFilterRepository;
import io.analytics.service.interfaces.IFilterService;

public class FilterService implements IFilterService {

	IFilterRepository filterRepository = new FilterRepository();
	
	@Override
	public Filter addNewFilter(Filter f) {
		Filter newFilter = filterRepository.addNewFilter(f);
		return newFilter;
	}

}
