package io.analytics.repository;
import io.analytics.domain.Account;
import io.analytics.domain.User;
import io.analytics.repository.interfaces.IAccountRepository;
import io.analytics.security.Role;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;


import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;


@Repository
public class AccountRepository implements IAccountRepository {

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
	 * @return A (possibly empty) list of Accounts. Returns <code>null</code> if there was a problem.
	 */
	public List<Account> getUserOwnedAccounts(int ownerUserId) {
		if (ownerUserId < 0)
			return null;
		
		JdbcTemplate jdbc = new JdbcTemplate(DATASOURCE);
		String preStatement;
		Object[] args;
		int[] argTypes;

		preStatement = String.format("SELECT * FROM `%s` WHERE `%s`=?", ACCOUNTS_TABLE, AccountTable.OWNER_USER_ID);
		args = new Object[] { ownerUserId };
		argTypes = new int[] { Types.INTEGER };

		try {
			List<Account> accounts = jdbc.query(preStatement, args, argTypes, new AccountMapper());
			
			return accounts; 
			
		} catch (DataAccessException e) {
			//TODO: Standardize error handling for the database.
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	/**
	 * Adds a new account to the database.
	 * 
	 * @param a The new Account to add.
	 * @return <code>true</code> if the Account was added successfully. <code>false</code> otherwise.
	 */
	public boolean addNewAccount(Account a) {
		JdbcTemplate jdbc = new JdbcTemplate(DATASOURCE);
		String preStatement;
		Object[] args;
		int[] argTypes;
		
		preStatement = String.format("INSERT INTO `%s` (`%s`, `%s`, `%s`) VALUES (?, ?, ?);", 
				ACCOUNTS_TABLE, AccountTable.FILTER_ID, AccountTable.OWNER_USER_ID, AccountTable.CREATION_DATE);
		
		args = new Object[] { a.getDefaultFilterId(), a.getOwnerId(), formatter.format(a.getCreationDate().getTime()) };
		
		argTypes = new int[] { Types.INTEGER, Types.INTEGER, Types.DATE };
		
		int affectedRows;
		try {
			//Don't delete commented lines below - these should be implemented on the next build.
			//int lastRowFirst = jdbc.queryForInt("SELECT LAST_INSERT_ID();");
			affectedRows = jdbc.update(preStatement, args, argTypes);
			//int lastRow = jdbc.queryForInt("SELECT LAST_INSERT_ID();");
		} catch (DataAccessException e) {
			//TODO: Standardize error handling for the database.
			e.printStackTrace();
			return false;
		}
		
		//Return true if the statement successfully affected one row.
		return affectedRows == 1;
		
	}
}

