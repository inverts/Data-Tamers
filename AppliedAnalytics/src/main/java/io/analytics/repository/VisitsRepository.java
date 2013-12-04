package io.analytics.repository;
import io.analytics.domain.Visits;

public class VisitsRepository implements IVisitsRepository{
	// calls the GA library function to return v
	public Visits getAllVisitsCount() {
		String d = null ; // GA library method call
		return visitsMapper(d);
	}
	
	private Visits visitsMapper(String json) {
		Visits v = new Visits();
		// parse json 
		int[]tmp = {88,135,114,131,104,139,138,106,102,85,137,139,132,109,88,114,92,90,149,138,134,108,106,95,132,112,104,76,96,91};
		int[][] pd = new int[2][30];
		for (int i = 0; i<30; i++) {
			pd[0][i]=i+1;
			pd[1][i] = tmp[i];
		}
		v.setAllVisitsCount(pd);
		// ...
		return v;
	}
	
	
}
