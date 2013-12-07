package io.analytics.repository;
import java.io.Serializable;
import io.analytics.domain.Traffic;

public interface ITrafficRepository extends Serializable{
	Traffic getTrafficSources();
}
