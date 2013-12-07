package io.analytics.service;
import io.analytics.domain.Traffic;
import java.io.Serializable;

public interface ITrafficService extends Serializable{
	Traffic getTrafficSources();
}
