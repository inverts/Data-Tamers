package io.analytics.domain;

import java.util.ArrayList;
import java.util.List;

public class VisitorDeviceData {
	private List<String> mobileCat;
	private List<String> desktopCat;
	private List<String> mobileOS;
	private List<Integer> mobileVisits;
	private List<String> desktopBrowser;
	private List<Integer> desktopVisits;
	
	public VisitorDeviceData() {
		 mobileCat = new ArrayList<String>();
		 desktopCat = new ArrayList<String>();
		 mobileOS = new ArrayList<String>();
		 mobileVisits = new ArrayList<Integer>();
		 desktopBrowser = new ArrayList<String>();
		 desktopVisits = new ArrayList<Integer>();
	}
	
	public void setMobileCategories(List<String> mobileCat){
		this.mobileCat.clear();
		this.mobileCat.addAll(mobileCat);		
	}
	
	public ArrayList<String> getMobileCategories() {
		ArrayList<String> data = new ArrayList<String>(this.mobileCat);
		return data;
	}
	
	public void setMobileOS(List<String> os){
		this.mobileOS.clear();
		this.mobileOS.addAll(os);
	}
	
	public ArrayList<String> getMobileOS() {
		ArrayList<String> data = new ArrayList<String>(this.mobileOS);
		return data;
	}
	
	public void setMobileVisits(List<Integer> mobileVisits){
		this.mobileVisits.clear();
		this.mobileVisits.addAll(mobileVisits);
	}
	
	public ArrayList<Integer> getMobileVisits() {
		ArrayList<Integer> data = new ArrayList<Integer>(this.mobileVisits);
		return data;
	}
	
	public void setDesktopCategories(List<String> desktopCat){
		this.desktopCat.clear();
		this.desktopCat.addAll(desktopCat);
	}
	
	public ArrayList<String> getDesktopCategories() {
		ArrayList<String> data = new ArrayList<String>(this.desktopCat);
		return data;
	}
	
	public void setDesktopBrowser(List<String> desktopBrowser){
		this.desktopBrowser.clear();
		this.desktopBrowser.addAll(desktopBrowser);
	}
	
	public ArrayList<String> getDesktopBrowser() {
		ArrayList<String> data = new ArrayList<String>(this.desktopBrowser);
		return data;
	}
	
	public void setDesktopVisits(List<Integer> desktopVisits){
		this.desktopVisits.clear();
		this.desktopVisits.addAll(desktopVisits);
	}
	
	public ArrayList<Integer> getDesktopVisits() {
		ArrayList<Integer> data = new ArrayList<Integer>(this.desktopVisits);
		return data;
	}
	
}
