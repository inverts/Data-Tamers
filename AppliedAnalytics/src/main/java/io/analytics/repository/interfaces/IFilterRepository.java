package io.analytics.repository.interfaces;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import io.analytics.domain.Filter;

@Repository
@Component
public interface IFilterRepository {
	public Filter addNewFilter(Filter f);
}
