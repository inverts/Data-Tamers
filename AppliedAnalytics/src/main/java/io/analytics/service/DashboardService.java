package io.analytics.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.analytics.domain.User;
import io.analytics.repository.interfaces.IDashboardRepository;
import io.analytics.service.interfaces.IDashboardService;

@Service
public class DashboardService implements IDashboardService {

	@Autowired IDashboardRepository DashboardRepository;
	
	@Override
	public void addNewDashboard(User user, int accountId, String dashboardName) {
		// TODO Validate User permissions
		DashboardRepository.addNewDashboard(accountId, user.getUserId(), dashboardName);
	}

	@Override
	public void deleteDashboard(User user, int dashboardId) {
		// TODO Validate User permissions
		DashboardRepository.deleteDashboard(dashboardId);
	}

	@Override
	public void updateDashboardWidgetLayout(User user, int dashboardId) {
		// TODO Validate User permissions
		DashboardRepository.updateDashboardWidgetLayout(dashboardId);
	}

}
