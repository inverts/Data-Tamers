package io.analytics.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import io.analytics.domain.Account;
import io.analytics.domain.Dashboard;
import io.analytics.domain.User;
import io.analytics.repository.interfaces.IDashboardRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 * A repository for querying and updating Dashboards in the database.
 * 
 * @author Dave Wong
 *
 */
@Repository
public class DashboardRepository implements IDashboardRepository {

	private JdbcTemplate jdbc;
	private SimpleJdbcInsert jdbcInsert;
	
	@Autowired
	public DashboardRepository(DataSource dataSource) {
		this.jdbc = new JdbcTemplate(dataSource);
		jdbcInsert = new SimpleJdbcInsert(this.jdbc).withTableName(DASHBOARD_TABLE).usingGeneratedKeyColumns(DashboardTable.DASHBOARD_ID);
	}

	/**
	 * Sets
	 * @param jdbcInsert
	 */
	public void setJdbcInsert(SimpleJdbcInsert jdbcInsert) {
		this.jdbcInsert = jdbcInsert;
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbc = jdbcTemplate;
	}

	public static final String DASHBOARD_TABLE = "Dashboards";
	private static final class DashboardTable {
		public static final String DASHBOARD_ID = "idDashboards";
		public static final String ACCOUNT_ID = "Accounts_idAccount";
		public static final String DEFAULT_FILTER_ID = "defaultFilterId";
		public static final String NAME = "dashboardName";
	}
	
	private static final class DashboardMapper implements RowMapper<Dashboard> {

		@Override
		public Dashboard mapRow(ResultSet rs, int row) throws SQLException {
			Dashboard d = new Dashboard(rs.getInt(DashboardTable.DASHBOARD_ID));
			d.setAccountId(rs.getInt(DashboardTable.ACCOUNT_ID));
			d.setDefaultFilterId(rs.getInt(DashboardTable.DEFAULT_FILTER_ID));
			d.setName(rs.getString(DashboardTable.NAME));
			return d;
		}
	}
	

	@Override
	public int addNewDashboard(int accountId, int defaultFilterId, String dashboardName) {
		if (accountId < 0 || defaultFilterId < 0 || dashboardName == null)
			return -1;
		Map<String, Object> insertParams = new HashMap<String, Object>();
        insertParams.put(DashboardTable.ACCOUNT_ID, accountId);
        insertParams.put(DashboardTable.DEFAULT_FILTER_ID, defaultFilterId);
        insertParams.put(DashboardTable.NAME, dashboardName);
        Number newDashboardId;
        try {
        	newDashboardId = jdbcInsert.executeAndReturnKey(insertParams);
        } catch (Exception e) {
        	//Not sure what exceptions can be thrown, the documentation simply says:
        	//"This method will always return a key or throw an exception if a key was not returned."
        	//I would imagine we'll see SQLExceptions.
        	e.printStackTrace();
        	return -1;
        }
        return newDashboardId.intValue();
	}
	
	@Override
	public int addNewDashboard(Dashboard d) {
		return this.addNewDashboard(d.getAccountId(), d.getDefaultFilterId(), d.getName());
	}

	@Override
	public void deleteDashboard(int dashboardId) {
		// TODO Remove dashboard with id.
		// We will validate they have permissions in the service level.
	}

	@Override
	public Dashboard getDashboard(int dashboardId) {
		if (dashboardId < 0)
			return null;
		
		String preStatement;
		Object[] args;
		int[] argTypes;

		preStatement = String.format("SELECT * FROM `%s` WHERE `%s`=?", DASHBOARD_TABLE, DashboardTable.DASHBOARD_ID);
		args = new Object[] { dashboardId };
		argTypes = new int[] { Types.INTEGER };

		try {
			List<Dashboard> dashboards = jdbc.query(preStatement, args, argTypes, new DashboardMapper());
			if (dashboards.isEmpty())
				return null;
			else
				return dashboards.get(0);
			
		} catch (DataAccessException e) {
			//TODO: Standardize error handling for the database.
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Dashboard> getAccountDashboards(int accountId) {
		if (accountId < 0)
			return null;
		
		String preStatement;
		Object[] args;
		int[] argTypes;

		preStatement = String.format("SELECT * FROM `%s` WHERE `%s`=?", DASHBOARD_TABLE, DashboardTable.ACCOUNT_ID);
		args = new Object[] { accountId };
		argTypes = new int[] { Types.INTEGER };

		try {
			List<Dashboard> dashboards = jdbc.query(preStatement, args, argTypes, new DashboardMapper());
			
			return dashboards; 
			
		} catch (DataAccessException e) {
			//TODO: Standardize error handling for the database.
			e.printStackTrace();
			return null;
		}
	}
	
}
