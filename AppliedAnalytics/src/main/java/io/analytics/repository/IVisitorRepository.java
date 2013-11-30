package io.analytics.repository;

import java.io.Serializable;

import io.analytics.domain.Visitor;

public interface IVisitorRepository extends Serializable{ // may need implements

	Visitor getGAallVisitors();
}
