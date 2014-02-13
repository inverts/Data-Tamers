package io.analytics.repository;

import io.analytics.domain.Filter;
import io.analytics.repository.interfaces.IFilterRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

@Repository
public class FilterRepository implements IFilterRepository {
	
	private final JdbcTemplate jdbc;
	private final SimpleJdbcInsert jdbcInsert;
	
	@Autowired
	public FilterRepository(DataSource dataSource) {
		this.jdbc = new JdbcTemplate(dataSource);
		jdbcInsert = new SimpleJdbcInsert(this.jdbc).withTableName(FILTERS_TABLE).usingGeneratedKeyColumns(FilterTable.FILTER_ID);
	}

	public static final String FILTERS_TABLE = "Filters";
		
	private static final class FilterTable {
		public static final String FILTER_ID = "idFilters";
		public static final String PARENT_ACCOUNT_ID = "parentAccountId";
		public static final String START_DATE = "startDate";
		public static final String END_DATE = "endDate";
		public static final String INTEREST_METRIC = "interestMetric";
		public static final String GOOGLE_PROFILE_ID = "googleProfileId";
	}
	
	private static final class FilterMapper implements RowMapper<Filter> {

		@Override
		public Filter mapRow(ResultSet rs, int row) throws SQLException {
			Filter filter = new Filter(rs.getInt(FilterTable.FILTER_ID));
			filter.setParentAccountId(rs.getInt(FilterTable.PARENT_ACCOUNT_ID));
			
			//TODO: Careful with time zones.
			Calendar start = Calendar.getInstance();
			start.setTime(rs.getDate(FilterTable.START_DATE));
			filter.setStartDate(start);

			//TODO: Careful with time zones.
			Calendar end = Calendar.getInstance();
			end.setTime(rs.getDate(FilterTable.END_DATE));
			filter.setEndDate(end);
			
			filter.setInterestMetric(rs.getString(FilterTable.INTEREST_METRIC));
			filter.setGoogleProfileId(rs.getString(FilterTable.GOOGLE_PROFILE_ID));
			
			return filter;
		}
		
	}
	
	private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	
	
	public Filter addNewFilter(Filter f) {
		String startDate, endDate;
		if (f.getStartDate() == null)
			startDate = null;
		else
			startDate = formatter.format(f.getStartDate().getTime());
		
		if (f.getEndDate() == null)
			endDate = null;
		else
			endDate = formatter.format(f.getEndDate().getTime());
		
        Map<String, Object> insertParams = new HashMap<String, Object>();
        if (f.getParentAccountId() > 0)
        	insertParams.put(FilterTable.PARENT_ACCOUNT_ID, f.getParentAccountId());
        insertParams.put(FilterTable.START_DATE, startDate);
        insertParams.put(FilterTable.END_DATE, endDate);
        insertParams.put(FilterTable.INTEREST_METRIC, f.getInterestMetric());
        insertParams.put(FilterTable.GOOGLE_PROFILE_ID, f.getGoogleProfileId());
        Number newFilterId;
        try {
        	newFilterId = jdbcInsert.executeAndReturnKey(insertParams);
        } catch (Exception e) {
        	//Not sure what exceptions can be thrown, the documentation simply says:
        	//"This method will always return a key or throw an exception if a key was not returned."
        	//I would imagine we'll see SQLExceptions.
        	e.printStackTrace();
        	return null;
        }

		Filter newFilter = new Filter(newFilterId.intValue()); //TODO: Replace with correct ID. This is wrong.
		newFilter.setStartDate(f.getStartDate());
		newFilter.setEndDate(f.getEndDate());
		newFilter.setGoogleProfileId(f.getGoogleProfileId());
		newFilter.setInterestMetric(f.getInterestMetric());
		newFilter.setParentAccountId(f.getParentAccountId());
		return newFilter;
		
	}
}
