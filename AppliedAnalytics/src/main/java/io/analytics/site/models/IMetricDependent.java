package io.analytics.site.models;

public interface IMetricDependent {
	
	/*
	 * TODO: Possibly allow for multiple metrics at once (like viewing visits and bounce rate together)
	 */
	public abstract void setMetric(String metric);
	
	public abstract String getMetric();
}
