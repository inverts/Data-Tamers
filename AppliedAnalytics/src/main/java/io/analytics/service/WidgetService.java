package io.analytics.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.analytics.domain.Widget;
import io.analytics.repository.interfaces.IWidgetRepository;
import io.analytics.service.interfaces.IWidgetService;

@Service
public class WidgetService implements IWidgetService {

	@Autowired IWidgetRepository WidgetRepository;
	
	@Override
	public int addNewWidget(int defaultFilterId, int widgetTypeId, int dashboardId, int priority) {
		return WidgetRepository.addNewWidget(defaultFilterId, widgetTypeId, dashboardId, priority);
	}

	@Override
	public int addNewWidget(Widget w) {
		return WidgetRepository.addNewWidget(w);
	}

	@Override
	public boolean updateWidget(Widget w) {
		return WidgetRepository.updateWidget(w);
	}
	@Override
	public void deleteWidget(int widgetId) {
		WidgetRepository.deleteWidget(widgetId);
	}

	@Override
	public List<Widget> getDashboardWidgets(int dashboardId) {
		return WidgetRepository.getDashboardWidgets(dashboardId);
	}


}
