package io.analytics.repository;
import io.analytics.domain.Account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;


@Repository
public class AccountRepository {

	private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final String ACCOUNTS_TABLE = "Accounts";
	private static final class AccountTable {
		public static final String ACCOUNT_ID = "idAccounts";
		public static final String FILTER_ID = "defaultFilterId";
		public static final String OWNER_USER_ID = "ownerUserId";
		public static final String CREATION_DATE = "creationDate";
	}
	private static final class AccountMapper implements RowMapper<Account> {

		@Override
		public Account mapRow(ResultSet rs, int row) throws SQLException {
			Account acct = new Account(rs.getInt(AccountTable.ACCOUNT_ID));
			acct.setDefaultFilterId(rs.getInt(AccountTable.FILTER_ID));
			acct.setOwnerId(rs.getInt(AccountTable.OWNER_USER_ID));
			
			//TODO: Careful with time zones.
			Calendar cal = Calendar.getInstance();
			cal.setTime(rs.getDate(AccountTable.CREATION_DATE));
			acct.setCreationDate(cal);
			
			return acct;
		}
		
	}
	
	//TODO: Use a connection properties file.
	private static DriverManagerDataSource DATASOURCE = 
				new DriverManagerDataSource("jdbc:mysql://davidkainoa.com:3306/davidkai_analytics", 
						"davidkai_data", "PNjO_#a40@wZPmh-Q");
	
	/**
	 * Gets a list of accounts that this user owns.
	 * 
	 * @param userId
	 * @return
	 */
	public static List<Account> getUserOwnedAccounts(int ownerUserId) {
		JdbcTemplate jdbc = new JdbcTemplate(DATASOURCE);
		List<Account> results;
		try {
			results = jdbc.query("SELECT * FROM ? WHERE ?=?;", new AccountMapper(), 
					ACCOUNTS_TABLE, 
					AccountTable.OWNER_USER_ID, 
					ownerUserId);
		} catch (DataAccessException e) {
			//TODO: Standardize error handling for the database.
			e.printStackTrace();
			return null;
		}
		
		return results;
	}
	
	/**
	 * Adds a new account to the database.
	 * 
	 * @param a
	 * @return
	 */
	public static boolean addNewAccount(Account a) {
		JdbcTemplate jdbc = new JdbcTemplate(DATASOURCE);
		int affectedRows;
		try {
			affectedRows = jdbc.update("INSERT INTO ? (?, ?, ?) VALUES (?, ?, ?);", 
					ACCOUNTS_TABLE, 
					AccountTable.FILTER_ID, AccountTable.OWNER_USER_ID, AccountTable.CREATION_DATE,
					a.getDefaultFilterId(), a.getOwnerId(), formatter.format(a.getCreationDate().getTime()));
		} catch (DataAccessException e) {
			//TODO: Standardize error handling for the database.
			e.printStackTrace();
			return false;
		}
		
		//Return true if the statement successfully affected one row.
		return affectedRows == 1;
		
	}
}
