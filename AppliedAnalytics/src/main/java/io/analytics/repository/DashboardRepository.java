package io.analytics.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import io.analytics.domain.Dashboard;
import io.analytics.domain.User;
import io.analytics.repository.interfaces.IDashboardRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class DashboardRepository implements IDashboardRepository {

	private final JdbcTemplate jdbc;
	
	@Autowired
	public DashboardRepository(DataSource dataSource) {
		this.jdbc = new JdbcTemplate(dataSource);
	}
	
	
	private static final class DashboardMapper implements RowMapper<Dashboard> {

		@Override
		public Dashboard mapRow(ResultSet arg0, int arg1) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	//TODO: Add a datatype representing the widget layout
	@Override
	public void updateDashboardWidgetLayout(int dashboardId) {
		// TODO Auto-generated method stub
	}

	@Override
	public int addNewDashboard(int accountId, int userId, String dashboardName) {
		// TODO Create a new dashboard for user __ in account __ with dashboardName
		// return new dashboard id
		return 0;
	}

	@Override
	public void deleteDashboard(int dashboardId) {
		// TODO Remove dashboard with id.
		// We will validate they have permissions in the service level.
	}

	@Override
	public Dashboard getDashboard(int dashboardId) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
