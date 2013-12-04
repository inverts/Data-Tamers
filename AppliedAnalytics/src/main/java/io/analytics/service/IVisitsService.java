package io.analytics.service;
import io.analytics.domain.Visits;

import java.io.Serializable;

public interface IVisitsService extends Serializable{
	Visits getAllVisitsCount();
}
