package io.analytics.domain;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestsOf_Visits {

	@Test
	public void testSetAndGetAllVisitsCount() {
		boolean passed = true;
		long[][] avc = {{1,2,3,4},{10,20,30,40}};
		Visits v = new Visits();
		v.setAllVisitsCount(avc);
		long[][] rdata = v.getAllVisitsCount();
		if (rdata.length != 2) {
			passed = false;
		}
		if (rdata[0].length != 4){
			passed = false;
		}
		assertTrue(passed);
	}

	
}
