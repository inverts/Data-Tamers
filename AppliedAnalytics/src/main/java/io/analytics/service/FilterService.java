package io.analytics.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.analytics.domain.Filter;
import io.analytics.repository.FilterRepository;
import io.analytics.repository.interfaces.IFilterRepository;
import io.analytics.service.interfaces.IFilterService;

@Service
public class FilterService implements IFilterService {

	@Autowired private IFilterRepository filterRepository;
	//IFilterRepository filterRepository = new FilterRepository();
	
	@Override
	public Filter addNewFilter(Filter f) {
		Filter newFilter = filterRepository.addNewFilter(f);
		return newFilter;
	}

}
