package io.analytics.site.models;


public abstract class GraphWidget extends Widget {
	
	// Here we might require that GraphWidgets specify things like:
	//	-An array of data points (and maybe mouseover information?)
	//	-Y and X axis labels
	//	-A map of colors and legend labels
	
	public int getUnitHeight() {
		return 2;
	}

	public int getUnitWidth() {
		return 4;
	}
	
}