package io.analytics.repository;

import static org.junit.Assert.*;


import java.util.Calendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.analytics.domain.Filter;
import io.analytics.repository.interfaces.IFilterRepository;

public class TestOf_FilterRepository   {
	
	private IFilterRepository filterRepository;
	
	@Test
	public void testAddNewFilter() {
		/*
		 * TODO: Replace DataSource with a mocked data source.
		 * Do not enable the code below, it will add bogus rows to the actual database.
		 * This is just here for some raw preliminary testing.
		 */
		filterRepository = new FilterRepository();
		
		if (true) {
			Filter filter = Mockito.mock(Filter.class);
			try {
					
				assert(filterRepository.addNewFilter(filter) == false);
		
				Mockito.when(filter.getStartDate()).thenReturn(Calendar.getInstance());
				assert(filterRepository.addNewFilter(filter) == false);
		
				Mockito.when(filter.getEndDate()).thenReturn(Calendar.getInstance());
				assert(filterRepository.addNewFilter(filter) == false);
				
				Mockito.when(filter.getInterestMetric()).thenReturn("ga:bogus");
				assert(filterRepository.addNewFilter(filter) == false);
				
				//This is the last filter property we should need in order to successfully edit
				Mockito.when(filter.getGoogleProfileId()).thenReturn("bogusID");
				assert(filterRepository.addNewFilter(filter) == true);
				
				Mockito.when(filter.getParentAccountId()).thenReturn(9999999);
				assert(filterRepository.addNewFilter(filter) == false);
				
			} catch (Exception e) {
				System.err.println("testAddNewFilter() encountered errors.");
				e.printStackTrace();
				fail();
			}
		}
	}

}
