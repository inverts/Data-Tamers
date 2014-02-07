package io.analytics.repository;

import io.analytics.domain.GoogleAccount;
import io.analytics.repository.interfaces.IGoogleAccountRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class GoogleAccountRepository implements IGoogleAccountRepository {

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
	public boolean addGoogleAccount(GoogleAccount account) {
		if(account.getActiveRefreshToken() == null || account.getOwnerAccountId() < 0)
			return false;
		
		JdbcTemplate jdbc = new JdbcTemplate(DATASOURCE);
		String preStatement;
		Object[] args;
		int[] argTypes;
		
		preStatement = String.format("INSERT INTO `%s` (`%s`, `%s`) VALUES (?, ?);", 
				GOOGLEACCOUNTS_TABLE, GoogleAccountsTable.REFRESH_TOKEN, GoogleAccountsTable.OWNER_ACCOUNT_ID);

		args = new Object[] { account.getActiveRefreshToken(), account.getOwnerAccountId() };
		
		argTypes = new int[] { Types.VARCHAR, Types.INTEGER };
		
		int affectedRows;
		try {
			affectedRows = jdbc.update(preStatement, args, argTypes);
		} catch (DataAccessException e) {
			//TODO: Standardize error handling for the database.
			e.printStackTrace();
			return false;
		}
		
		//Return true if the statement successfully affected one row.
		return affectedRows == 1;
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
