package io.analytics.repository;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.analytics.domain.Filter;
import io.analytics.repository.interfaces.IFilterRepository;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class TestOf_FilterRepository {
	
	@Autowired private IFilterRepository filterRepository;
	
	@Test
	public void testAddNewFilter() {
		/*
		 * TODO: Replace DataSource with a mocked data source.
		 * Do not enable the code below, it will add bogus rows to the actual database.
		 * This is just here for some raw preliminary testing.
		 */
		//filterRepository = new FilterRepository();
		
		if (true) {
			Filter filter = Mockito.mock(Filter.class);
			try {

				Mockito.when(filter.getId()).thenReturn(0);	
				Mockito.when(filter.getParentAccountId()).thenReturn(-1);
				
				assert(filterRepository.addNewFilter(filter) == null);
		
				Mockito.when(filter.getStartDate()).thenReturn(Calendar.getInstance());
				assert(filterRepository.addNewFilter(filter) == null);
		
				Mockito.when(filter.getEndDate()).thenReturn(Calendar.getInstance());
				assert(filterRepository.addNewFilter(filter) == null);
				
				//This is the last filter property we should need in order to successfully edit
				Mockito.when(filter.getInterestMetric()).thenReturn("ga:bogus");
				assert(filterRepository.addNewFilter(filter) != null);
				
				Mockito.when(filter.getGoogleProfileId()).thenReturn("bogusID");
				Filter newFilter1 = filterRepository.addNewFilter(filter);
				//System.out.println("ID: " + newFilter1.getId());
				assert(newFilter1 != null);
				//assert(newFilter1.getId() != 0);
				
				//We attempt to construct a non-existent parent account ID, which should fail.
				Mockito.when(filter.getParentAccountId()).thenReturn(9999999);
				Filter newFilter2 = filterRepository.addNewFilter(filter);
				//System.out.println("ID: " + newFilter2.getId());
				assert(newFilter2 == null);
				//assert(newFilter2.getId() != 0);
				
				
			} catch (Exception e) {
				System.err.println("testAddNewFilter() encountered errors.");
				e.printStackTrace();
				fail();
			}
		}
	}

	/*@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		// TODO Auto-generated method stub
		
	}*/

}
