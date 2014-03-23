package io.analytics.repository;
import io.analytics.domain.Account;
import io.analytics.repository.interfaces.IAccountRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;


/**
 * A repository for gathering Account data from the database.
 * 
 * @author Dave Wong
 *
 */
@Repository
public class AccountRepository implements IAccountRepository {

	private final JdbcTemplate jdbc;
	private final SimpleJdbcInsert jdbcInsert;
	
	@Autowired
	public AccountRepository(DataSource dataSource) {
		this.jdbc = new JdbcTemplate(dataSource);
		//Must specify .usingGeneratedKeyColumns in order to use .executeAndReturnKey.
		jdbcInsert = new SimpleJdbcInsert(this.jdbc).withTableName(ACCOUNTS_TABLE).usingGeneratedKeyColumns(AccountTable.ACCOUNT_ID);
	}
	
	
	public static final String ACCOUNTS_TABLE = "Accounts";
	public static final class AccountTable {
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

	private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	
	/**
	 * Adds a new account to the database.
	 * 
	 * @param a The new Account to add.
	 * @return <code>true</code> if the Account was added successfully. <code>false</code> otherwise.
	 */
	public Account addNewAccount(Account a) {
		
		String creationDate;
		if (a.getOwnerId() <= 0 || a.getDefaultFilterId() <= 0)
			return null;
		if (a.getCreationDate() == null)
			creationDate = formatter.format(Calendar.getInstance().getTime());
		else
			creationDate = formatter.format(a.getCreationDate().getTime());
		
        Map<String, Object> insertParams = new HashMap<String, Object>();
        insertParams.put(AccountTable.FILTER_ID, a.getDefaultFilterId());
        insertParams.put(AccountTable.OWNER_USER_ID, a.getOwnerId());
        insertParams.put(AccountTable.CREATION_DATE, creationDate);
        
        Number newAccountId;
        try {
        	newAccountId = jdbcInsert.executeAndReturnKey(insertParams);
        } catch (Exception e) {
        	//Not sure what exceptions can be thrown, the documentation simply says:
        	//"This method will always return a key or throw an exception if a key was not returned."
        	//I would imagine we'll see SQLExceptions.
        	System.out.println("Database error.");
        	return null;
        }

        Account newAccount = new Account(newAccountId.intValue()); 
        newAccount.setCreationDate(a.getCreationDate());
        newAccount.setDefaultFilterId(a.getDefaultFilterId());
        newAccount.setOwnerId(a.getOwnerId());
		return newAccount;
		
	}
	
	
	/**
	 * Gets a list of accounts that this user owns.
	 * 
	 * @param userId
	 * @return A (possibly empty) list of Accounts. Returns <code>null</code> if there was a problem.
	 */
	public List<Account> getUserOwnedAccounts(int ownerUserId) {
		if (ownerUserId < 0)
			throw new IllegalArgumentException();
		
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
        	System.out.println("Database error.");
			return null;
		}
		
	}
	
}

