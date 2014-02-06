package io.analytics.repository;

import io.analytics.domain.Filter;
import io.analytics.repository.interfaces.IFilterRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@Repository
@Component
public class FilterRepository implements IFilterRepository {
	
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
	
	//TODO: Use a connection properties file.
	private static DriverManagerDataSource DATASOURCE = 
				new DriverManagerDataSource("jdbc:mysql://davidkainoa.com:3306/davidkai_analytics", 
						"davidkai_data", "PNjO_#a40@wZPmh-Q");
	
	private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	
	public Filter addNewFilter(Filter f) {
		JdbcTemplate jdbc = new JdbcTemplate(DATASOURCE);
        
		String preStatement;
		Object[] args;
		int[] argTypes;
		
		String startDate, endDate;
		if (f.getStartDate() == null)
			startDate = null;
		else
			startDate = formatter.format(f.getStartDate().getTime());
		
		if (f.getEndDate() == null)
			endDate = null;
		else
			endDate = formatter.format(f.getEndDate().getTime());
		

		if (f.getParentAccountId() <= 0) {
			preStatement = String.format("INSERT INTO `%s` (`%s`, `%s`, `%s`, `%s`) VALUES (?, ?, ?, ?);", 
					FILTERS_TABLE, FilterTable.START_DATE, FilterTable.END_DATE, 
					FilterTable.INTEREST_METRIC, FilterTable.GOOGLE_PROFILE_ID);
			args = new Object[]{ startDate, endDate, f.getInterestMetric(), f.getGoogleProfileId() };
			argTypes = new int[]{ Types.DATE, Types.DATE, Types.VARCHAR, Types.VARCHAR };
		} else {
			preStatement = String.format("INSERT INTO `%s` (`%s`, `%s`, `%s`, `%s`, `%s`) VALUES (?, ?, ?, ?, ?);", 
					FILTERS_TABLE, FilterTable.PARENT_ACCOUNT_ID, FilterTable.START_DATE, FilterTable.END_DATE, 
					FilterTable.INTEREST_METRIC, FilterTable.GOOGLE_PROFILE_ID);
			args = new Object[]{ f.getParentAccountId(), startDate, endDate, f.getInterestMetric(), f.getGoogleProfileId() };
			argTypes = new int[]{ Types.INTEGER, Types.DATE, Types.DATE, Types.VARCHAR, Types.VARCHAR };
		}
		
		int affectedRows;
		try {
			//These will not work unless we're in a transaction.
			//int lastRowFirst = jdbc.queryForInt("SELECT LAST_INSERT_ID();");
			affectedRows = jdbc.update(preStatement, args, argTypes);
			//int lastRow = jdbc.queryForInt("SELECT LAST_INSERT_ID();");

			if (affectedRows == 1) { //&& lastRowFirst != lastRow) {
				Filter newFilter = new Filter(0); //TODO: Replace with correct ID. This is wrong.
				newFilter.setStartDate(f.getStartDate());
				newFilter.setEndDate(f.getEndDate());
				newFilter.setGoogleProfileId(f.getGoogleProfileId());
				newFilter.setInterestMetric(f.getInterestMetric());
				newFilter.setParentAccountId(f.getParentAccountId());
				return newFilter;
			} else {
				return null;
			}
		} catch (DataAccessException e) {
			//TODO: Standardize error handling for the database.
			//e.printStackTrace();
			return null;
		}
	}
}
