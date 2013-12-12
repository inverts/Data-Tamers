package io.analytics.repository;

import static org.junit.Assert.*;
import io.analytics.domain.Visits;

import org.junit.Ignore;
import org.junit.Test;

public class TestsOf_VisitsRepository {

	@Test
	@Ignore
	public void testGetAllVisitsCount() {
		VisitsRepository vr = new VisitsRepository();
		Visits v  = vr.getAllVisitsCount();
		assertTrue(30 == v.getAllVisitsCount()[0].length);
	}

}
