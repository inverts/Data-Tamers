package io.analytics.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.analytics.domain.Dashboard;
import io.analytics.domain.User;
import io.analytics.repository.interfaces.IDashboardRepository;
import io.analytics.service.interfaces.IDashboardService;

@Service
public class DashboardService implements IDashboardService {

	@Autowired IDashboardRepository DashboardRepository;
	
	@Override
	public int addNewDashboard(int accountId, Integer defaultFilterId, String dashboardName) {
		// TODO Validate User permissions
		return DashboardRepository.addNewDashboard(accountId, defaultFilterId, dashboardName);
	}

	@Override
	public void deleteDashboard(User user, int dashboardId) {
		// TODO Validate User permissions
		DashboardRepository.deleteDashboard(dashboardId);
	}

	/**
	 * @deprecated
	 */
	@Override
	public void updateDashboardWidgetLayout(User user, int dashboardId) {
		
	}

	@Override
	public Dashboard getDashboard(int dashboardId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Dashboard> getAccountDashboards(int accountId) {
		return DashboardRepository.getAccountDashboards(accountId);
	}

}
