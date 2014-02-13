package io.analytics.domain;

import java.util.ArrayList;

public class ForecastData {

	private ArrayList<Double> xValuesByDay;
	private ArrayList<Double> yValuesByDay;
	private ArrayList<Double> xValuesByDayOfWeek;
	private ArrayList<Double> yValuesByDayOfWeek;
	
	public ForecastData(){
		xValuesByDay = new ArrayList<Double>();
		yValuesByDay = new ArrayList<Double>();
		xValuesByDayOfWeek = new ArrayList<Double>();
		yValuesByDayOfWeek = new ArrayList<Double>();
	}
	
	public void setXValuesByDay(ArrayList<Double> data) {
		this.xValuesByDay.clear();
		this.xValuesByDay.addAll(data);
	}
	
	public ArrayList<Double> getXValuesByDay() {
		ArrayList<Double> data = new ArrayList<Double>(this.xValuesByDay);
		return data;
	}
	
	public void setYValuesByDay(ArrayList<Double> data) {
		this.yValuesByDay.clear();
		this.yValuesByDay.addAll(data);
	}
	
	public ArrayList<Double> getYValuesByDay() {
		ArrayList<Double> data = new ArrayList<Double>(this.yValuesByDay);
		return data;
	}
	
	public void setXValuesByDayOfWeek(ArrayList<Double> data) {
		this.xValuesByDayOfWeek.clear();
		this.xValuesByDayOfWeek.addAll(data);
	}
	
	public ArrayList<Double> getXValuesByDayOfWeek() {
		ArrayList<Double> data = new ArrayList<Double>(this.xValuesByDayOfWeek);
		return data;
	}
	
	public void setYValuesByDayOfWeek(ArrayList<Double> data) {
		this.yValuesByDayOfWeek.clear();
		this.yValuesByDayOfWeek.addAll(data);
	}
	
	public ArrayList<Double> getYValuesByDayOfWeek() {
		ArrayList<Double> data = new ArrayList<Double>(this.yValuesByDayOfWeek);
		return data;
	}
	
}
