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
import io.analytics.domain.Widget;
import io.analytics.repository.interfaces.IDashboardRepository;
import io.analytics.repository.interfaces.IWidgetRepository;

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
public class WidgetRepository implements IWidgetRepository {

	private JdbcTemplate jdbc;
	private SimpleJdbcInsert jdbcInsert;
	
	@Autowired
	public WidgetRepository(DataSource dataSource) {
		this.jdbc = new JdbcTemplate(dataSource);
		jdbcInsert = new SimpleJdbcInsert(this.jdbc).withTableName(WIDGET_TABLE).usingGeneratedKeyColumns(WidgetTable.WIDGET_ID);
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

	public static final String WIDGET_TABLE = "Widgets";
	private static final class WidgetTable {
		public static final String WIDGET_ID = "idWidgets";
		public static final String WIDGET_TYPE_ID = "WidgetTypes_idWidgetTypes";
		public static final String DEFAULT_FILTER_ID = "defaultFilterId";
		public static final String DASHBOARD_ID = "Dashboards_idDashboards";
		public static final String PRIORITY = "widgetPriority";
	}
	
	private static final class WidgetMapper implements RowMapper<Widget> {

		@Override
		public Widget mapRow(ResultSet rs, int row) throws SQLException {
			Widget w = new Widget(rs.getInt(WidgetTable.WIDGET_ID));
			w.setWidgetTypeId(rs.getInt(WidgetTable.WIDGET_TYPE_ID));
			w.setDefaultFilterId(rs.getInt(WidgetTable.DEFAULT_FILTER_ID));
			w.setDashboardId(rs.getInt(WidgetTable.DASHBOARD_ID));
			w.setPriority(rs.getInt(WidgetTable.PRIORITY));
			return w;
		}
	}
	
	@Override
	public int addNewWidget(int defaultFilterId, int widgetTypeId, int dashboardId, int priority) {

		if (defaultFilterId < 0 || widgetTypeId < 0 || dashboardId < 0)
			return -1;
		Map<String, Object> insertParams = new HashMap<String, Object>();
        insertParams.put(WidgetTable.WIDGET_TYPE_ID, widgetTypeId);
        insertParams.put(WidgetTable.DEFAULT_FILTER_ID, defaultFilterId);
        insertParams.put(WidgetTable.DASHBOARD_ID, dashboardId);
        insertParams.put(WidgetTable.PRIORITY, priority);
        Number newWidgetId;
        try {
        	newWidgetId = jdbcInsert.executeAndReturnKey(insertParams);
        } catch (Exception e) {
        	//Not sure what exceptions can be thrown, the documentation simply says:
        	//"This method will always return a key or throw an exception if a key was not returned."
        	//I would imagine we'll see SQLExceptions.
        	e.printStackTrace();
        	return -1;
        }
        return newWidgetId.intValue();
	}

	@Override
	public int addNewWidget(Widget w) {
		return addNewWidget(w.getDefaultFilterId(), w.getWidgetTypeId(), w.getDashboardId(), w.getPriority());
	}

	@Override
	public void deleteWidget(int widgetId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Widget> getDashboardWidgets(int dashboardId) {
		if (dashboardId < 0)
			return null;
		String preStatement;
		Object[] args;
		int[] argTypes;

		preStatement = String.format("SELECT * FROM `%s` WHERE `%s`=?", WIDGET_TABLE, WidgetTable.DASHBOARD_ID);
		args = new Object[] { dashboardId };
		argTypes = new int[] { Types.INTEGER };

		try {
			List<Widget> widgets = jdbc.query(preStatement, args, argTypes, new WidgetMapper());
			
			return widgets; 
			
		} catch (DataAccessException e) {
			//TODO: Standardize error handling for the database.
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Widget getDashboard(int dashboardId) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
