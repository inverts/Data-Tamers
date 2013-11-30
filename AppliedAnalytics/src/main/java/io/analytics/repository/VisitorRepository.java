package io.analytics.repository;
import io.analytics.domain.Visitor;

public class VisitorRepository implements IVisitorRepository{
	// calls the GA library function to return Visitors
	public Visitor getGAallVisitors() {
		String d = null ; // GA library method call
		return visitorMapper(d);
	}
	
	private Visitor visitorMapper(String json) {
		Visitor v = new Visitor();
		// parse json into a long
		long pd = 0;
		v.setAllVisitorCount(pd);
		// ...
		return v;
	}
	
	
}
