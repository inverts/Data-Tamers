package io.analytics.service;
import io.analytics.domain.Visitor;

import java.io.Serializable;

public interface IVisitorService extends Serializable{
	Visitor getAllVisitors();
}
