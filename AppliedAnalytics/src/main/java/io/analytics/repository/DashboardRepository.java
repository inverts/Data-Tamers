package io.analytics.repository;

import javax.sql.DataSource;

import io.analytics.repository.interfaces.IDashboardRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DashboardRepository implements IDashboardRepository {

	private final JdbcTemplate jdbc;
	
	@Autowired
	public DashboardRepository(DataSource dataSource) {
		this.jdbc = new JdbcTemplate(dataSource);
	}

	//TODO: Add a datatype representing the widget layout
	@Override
	public void updateDashboardWidgetLayout(int dashboardId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addNewDashboard(int accountId, int userId, String dashboardName) {
		// TODO Create a new dashboard for user __ in account __ with dashboardName
	}

	@Override
	public void deleteDashboard(int dashboardId) {
		// TODO Remove dashboard with id.
		// We will validate they have permissions in the service level.
		
	}
	
}
