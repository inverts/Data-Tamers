package io.analytics.repository;


import io.analytics.domain.GoogleAccount;
import io.analytics.repository.interfaces.IGoogleAccountRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;


/**
 * A repository for querying and updating GoogleAccounts in the database.
 * 
 * @author Dave Wong
 *
 */
@Repository
public class GoogleAccountRepository implements IGoogleAccountRepository {

	private final JdbcTemplate jdbc;
	private final SimpleJdbcInsert jdbcInsert;
	private final SimpleJdbcInsert jdbcInsertRelationship;
	
	@Autowired
	public GoogleAccountRepository(DataSource dataSource) {
		this.jdbc = new JdbcTemplate(dataSource);
		//Must specify .usingGeneratedKeyColumns in order to use .executeAndReturnKey.
		jdbcInsert = new SimpleJdbcInsert(this.jdbc).withTableName(GOOGLEACCOUNTS_TABLE).usingGeneratedKeyColumns(GoogleAccountsTable.ID);
		jdbcInsertRelationship = new SimpleJdbcInsert(this.jdbc).withTableName(GOOGLEACCOUNTSHASACCOUNTS_TABLE);
	}

	public static final String GOOGLEACCOUNTS_TABLE = "GoogleAccounts";
	public static final String GOOGLEACCOUNTSHASACCOUNTS_TABLE = "GoogleAccounts_has_Accounts";
	
	private static final class GoogleAccountsTable {
		public static final String ID = "idGoogleAccounts";
		public static final String REFRESH_TOKEN = "refreshToken";
		public static final String OWNER_ACCOUNT_ID = "ownerAccountId";
	}
	private static final class GoogleAccountsHasAccountsTable {
		public static final String GOOGLEACCOUNTS_ID = "GoogleAccounts_idGoogleAccounts";
		public static final String ACCOUNT_ID = "Accounts_idAccounts";
	}
	
	private static final class GoogleAccountMapper implements RowMapper<GoogleAccount> {
		@Override
		public GoogleAccount mapRow(ResultSet rs, int row) throws SQLException {
			GoogleAccount googleAccount = new GoogleAccount(rs.getInt(GoogleAccountsTable.ID));
			googleAccount.setActiveRefreshToken(rs.getString(GoogleAccountsTable.REFRESH_TOKEN));
			googleAccount.setOwnerAccountId(rs.getInt(GoogleAccountsTable.OWNER_ACCOUNT_ID));
			return googleAccount;
		}	
	}
	
	/**
	 * Adds a new Google Account into the database. The GoogleAccount must have a refresh token 
	 * and an owner id set in order to add it to the database.
	 * 
	 * @param account The GoogleAccount to add to the database.
	 * @return Returns true on success, false otherwise.
	 */
	public GoogleAccount addGoogleAccount(GoogleAccount account) {
		if(account.getActiveRefreshToken() == null || account.getOwnerAccountId() < 0)
			return null;
		
        Map<String, Object> insertParams = new HashMap<String, Object>();
        insertParams.put(GoogleAccountsTable.REFRESH_TOKEN, account.getActiveRefreshToken());
        insertParams.put(GoogleAccountsTable.OWNER_ACCOUNT_ID, account.getOwnerAccountId());
        Number newGoogleAccountId;
        try {
        	newGoogleAccountId = jdbcInsert.executeAndReturnKey(insertParams);
        } catch (Exception e) {
        	//Not sure what exceptions can be thrown, the documentation simply says:
        	//"This method will always return a key or throw an exception if a key was not returned."
        	//I would imagine we'll see SQLExceptions.
        	e.printStackTrace();
        	return null;
        }

        GoogleAccount newGoogleAccount = new GoogleAccount(newGoogleAccountId.intValue()); 
        newGoogleAccount.setActiveRefreshToken(account.getActiveRefreshToken());
        newGoogleAccount.setOwnerAccountId(account.getOwnerAccountId());
		return newGoogleAccount;
		
	}
	

	public List<GoogleAccount> getGoogleAccountsForOwnerAccount(int accountId) {
		//TODO: Method stub, implement.
		return null;
	}

	
	public List<GoogleAccount> getGoogleAccountsForAccount(int accountId) {

		if (accountId < 0)
			return null;
		
		String preStatement;
		Object[] args;
		int[] argTypes;
		
		String projection = String. format("%1$s.%2$s, %1$s.%3$s, %1$s.%4$s", 
				this.GOOGLEACCOUNTS_TABLE, GoogleAccountsTable.ID, GoogleAccountsTable.REFRESH_TOKEN, GoogleAccountsTable.OWNER_ACCOUNT_ID);
		
		String joinedTables = String.format("%s, %s, %s", 
				this.GOOGLEACCOUNTS_TABLE, this.GOOGLEACCOUNTSHASACCOUNTS_TABLE, AccountRepository.ACCOUNTS_TABLE);
		
		String googleAccountsCondition = String.format("%s.%s = %s.%s", 
				this.GOOGLEACCOUNTS_TABLE, GoogleAccountsTable.ID, 
				this.GOOGLEACCOUNTSHASACCOUNTS_TABLE, GoogleAccountsHasAccountsTable.GOOGLEACCOUNTS_ID);

		String accountsCondition = String.format("%s.%s = %s.%s", 
				AccountRepository.ACCOUNTS_TABLE, AccountRepository.AccountTable.ACCOUNT_ID, 
				this.GOOGLEACCOUNTSHASACCOUNTS_TABLE, GoogleAccountsHasAccountsTable.ACCOUNT_ID);
		
		String accountCondition = String.format("%s.%s = ?", AccountRepository.ACCOUNTS_TABLE, AccountRepository.AccountTable.ACCOUNT_ID);
		
		
		preStatement = String.format("select %s FROM %s WHERE %s AND %s AND %s;", projection, joinedTables,
				googleAccountsCondition, accountsCondition, accountCondition);
		
		args = new Object[] { accountId };
		argTypes = new int[] { Types.INTEGER };

		try {
			List<GoogleAccount> googleAccounts = jdbc.query(preStatement, args, argTypes, new GoogleAccountMapper());
			
			return googleAccounts; 
			
		} catch (DataAccessException e) {
			//TODO: Standardize error handling for the database.
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public boolean addRelationshipToAccount(int googleAccountId, int accountId) {
        Map<String, Object> insertParams = new HashMap<String, Object>();
        insertParams.put(GoogleAccountsHasAccountsTable.GOOGLEACCOUNTS_ID, googleAccountId);
        insertParams.put(GoogleAccountsHasAccountsTable.ACCOUNT_ID, accountId);
        int rowsAffected;
        try {
        	rowsAffected = jdbcInsertRelationship.execute(insertParams);
        } catch (Exception e) {
        	//Not sure what exceptions can be thrown, the documentation simply says:
        	//"This method will always return a key or throw an exception if a key was not returned."
        	//I would imagine we'll see SQLExceptions.
        	e.printStackTrace();
        	return false;
        } 

        return (rowsAffected == 1);
	}
	
	public boolean updateGoogleAccount(GoogleAccount ga) {

		String preStatement;
		Object[] args;
		int[] argTypes;

		preStatement = String.format("UPDATE `%s` SET `%s`=?, `%s`=? WHERE `%s`=?", GOOGLEACCOUNTS_TABLE, GoogleAccountsTable.REFRESH_TOKEN, GoogleAccountsTable.OWNER_ACCOUNT_ID, GoogleAccountsTable.ID);
		args = new Object[] { ga.getActiveRefreshToken(), ga.getOwnerAccountId(), ga.getId() };
		argTypes = new int[] { Types.VARCHAR, Types.INTEGER, Types.INTEGER};
		int rowsAffected = 0;
		try {
			rowsAffected = jdbc.update(preStatement, args, argTypes);
		} catch (DataAccessException e) {
			//TODO: Throw the exception upwards
			e.printStackTrace();
		}
		if (rowsAffected !=1)
			return false;
		return true;
	}

}
