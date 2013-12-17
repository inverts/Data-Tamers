package io.analytics.domain;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * This domain object holds data that have a variety of appropriate 
 * Java types. The data can be used directly for computation 
 * or display as numbers.
 * 
 * The first element in the aggregate list should be a list identifying
 * each remaining list.
 */
public class CoreReportingTypedData implements Serializable {
   
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Object> list;
   
	public CoreReportingTypedData (ArrayList<Object> l){
		this.list = new ArrayList<Object>(l);
	}
	
	public ArrayList<Object> getData() {
		return new ArrayList<Object>(list);
	}
	
	public void setData(ArrayList<Object> d) {
		this.list = new ArrayList<Object>(d);
	}   
}
