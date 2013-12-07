package io.analytics.repository;
import java.io.Serializable;
import io.analytics.domain.Visits;

public interface IVisitsRepository extends Serializable{ 
	
	Visits getAllVisitsCount();
}
