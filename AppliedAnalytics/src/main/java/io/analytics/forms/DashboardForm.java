package io.analytics.forms;

import javax.validation.constraints.Size;

public class DashboardForm {
	
	@Size(max=25)
	private String dashboardName;
	
	public void setDashboardName(String dashboardName) {
		this.dashboardName = dashboardName;
	}
	
	public String getDashboardName() {
		return this.dashboardName;
	}

}
