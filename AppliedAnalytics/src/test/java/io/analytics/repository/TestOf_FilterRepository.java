package io.analytics.repository;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Test;
import org.mockito.Mockito;

import io.analytics.domain.Filter;
import io.analytics.repository.interfaces.IFilterRepository;

public class TestOf_FilterRepository {
	
	
	private IFilterRepository FilterRepository;
	
	@Test
	public void testAddNewFilter() {
		/*
		 * TODO: Replace DataSource with a mocked data source.
		 * Do not enable the code below, it will add bogus rows to the actual database.
		 * This is just here for some raw preliminary testing.
		 */
		if (false) {
			Filter filter = Mockito.mock(Filter.class);
			try {
					
				assert(FilterRepository.addNewFilter(filter) == false);
		
				Mockito.when(filter.getStartDate()).thenReturn(Calendar.getInstance());
				assert(FilterRepository.addNewFilter(filter) == false);
		
				Mockito.when(filter.getEndDate()).thenReturn(Calendar.getInstance());
				assert(FilterRepository.addNewFilter(filter) == false);
				
				Mockito.when(filter.getInterestMetric()).thenReturn("ga:bogus");
				assert(FilterRepository.addNewFilter(filter) == false);
				
				//This is the last filter property we should need in order to successfully edit
				Mockito.when(filter.getGoogleProfileId()).thenReturn("bogusID");
				assert(FilterRepository.addNewFilter(filter) == true);
				
				Mockito.when(filter.getParentAccountId()).thenReturn(9999999);
				assert(FilterRepository.addNewFilter(filter) == false);
				
			} catch (Exception e) {
				System.err.println("testAddNewFilter() encountered errors.");
				e.printStackTrace();
				fail();
			}
		}
	}
}
