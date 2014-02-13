package io.analytics.repository;


import io.analytics.domain.GoogleAccount;
import io.analytics.repository.interfaces.IGoogleAccountRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

@Repository
public class GoogleAccountRepository implements IGoogleAccountRepository {

	private final JdbcTemplate jdbc;
	private final SimpleJdbcInsert jdbcInsert;
	
	@Autowired
	public GoogleAccountRepository(DataSource dataSource) {
		this.jdbc = new JdbcTemplate(dataSource);
		//Must specify .usingGeneratedKeyColumns in order to use .executeAndReturnKey.
		jdbcInsert = new SimpleJdbcInsert(this.jdbc).withTableName(GOOGLEACCOUNTS_TABLE).usingGeneratedKeyColumns(GoogleAccountsTable.GOOGLEACCOUNTS_ID);
	}
	
	public static final String GOOGLEACCOUNTS_TABLE = "GoogleAccounts";
	private static final class GoogleAccountsTable {
		public static final String GOOGLEACCOUNTS_ID = "idGoogleAccounts";
		public static final String REFRESH_TOKEN = "refreshToken";
		public static final String OWNER_ACCOUNT_ID = "ownerAccountId";
	}
	
	private static final class GoogleAccountMapper implements RowMapper<GoogleAccount> {
		@Override
		public GoogleAccount mapRow(ResultSet rs, int row) throws SQLException {
			GoogleAccount googleAccount = new GoogleAccount(rs.getInt(GoogleAccountsTable.GOOGLEACCOUNTS_ID));
			googleAccount.setActiveRefreshToken(rs.getString(GoogleAccountsTable.REFRESH_TOKEN));
			googleAccount.setOwnerAccountId(rs.getInt(GoogleAccountsTable.OWNER_ACCOUNT_ID));
			
			return googleAccount;
		}	
	}
	
	//TODO: Use a connection properties file.
	private static DriverManagerDataSource DATASOURCE = 
				new DriverManagerDataSource("jdbc:mysql://davidkainoa.com:3306/davidkai_analytics", 
						"davidkai_data", "PNjO_#a40@wZPmh-Q");
	
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

        GoogleAccount newGoogleAccount = new GoogleAccount(newGoogleAccountId.intValue()); //TODO: Replace with correct ID. This is wrong.
        newGoogleAccount.setActiveRefreshToken(account.getActiveRefreshToken());
        newGoogleAccount.setOwnerAccountId(account.getOwnerAccountId());
		return newGoogleAccount;
		
	}
	

	/**
	 * Retrieves a list of Google accounts owned by one particular user.
	 * Note that this is different from a list of Google Accounts associated with
	 * one particular Account.
	 * @param ownerId The User id that you are trying to find Google Accounts for.
	 * @return A List of GoogleAccounts, which may be empty if none are found.
	 */
	public List<GoogleAccount> getGoogleAccountsForOwner(int ownerId) {
		//TODO: Method stub, implement.
		return null;
	}


	@Override
	public boolean addRelationshipToAccount(int googleAccountId, int accountId) {
		// TODO Auto-generated method stub
		return false;
	}

}
