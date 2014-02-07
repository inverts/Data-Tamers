package io.analytics.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Service;

import io.analytics.domain.Account;
import io.analytics.repository.AccountRepository;
import io.analytics.repository.GoogleAccountRepository;
import io.analytics.repository.interfaces.IAccountRepository;
import io.analytics.repository.interfaces.IGoogleAccountRepository;
import io.analytics.service.interfaces.IAccountService;

@Service
public class AccountService implements IAccountService {

	//TODO: Autowire.
	private IAccountRepository accountRepository = new AccountRepository();
	private IGoogleAccountRepository googleAccountRepository = new GoogleAccountRepository();
	
	/**
	 * Gets an Account object given its id.
	 * @param id
	 * @return
	 */
	public Account getAccountById(int id) {
		//TODO: Implement.
		return null;
	}
	
	/**
	 * Attempts to save an account to the database and returns a boolean indicating success.
	 * @param account The Account to save.
	 * @return
	 */
	public boolean saveAccount(Account account) {
		//TODO: Implement.
		return false;
	}
	
	/**
	 * Gets a list of accounts 
	 * @param userId
	 * @return
	 */
	public List<Account> getAccountsOwnedByUser(int userId) {
		//TODO: Implement.
		return null;
	}
	
	/**
	 * Creates a new Account. In order for an Account to exist, it must have an owner,
	 * and it must have a default filter.
	 * The remaining details (id and creation date) are filled in by the method.
	 * WARNING: Currently does not return an Account with a valid Account ID.
	 * 
	 * @param ownerId
	 * @param filterId
	 * @return The new Account that was created and saved to the database. <code>null</code>
	 * if it could not be saved.
	 */
	public Account createAndSaveAccount(int ownerId, int filterId) {
		Account acc = new Account(-1);
		acc.setOwnerId(ownerId);
		acc.setDefaultFilterId(filterId);
		acc.setCreationDate(Calendar.getInstance());
		
		if (accountRepository.addNewAccount(acc))
			return acc;
		else
			return null;
		
	}

	@Override
	public boolean addAccountToGoogleAccount(int accountId, int googleAccountId) {
		return googleAccountRepository.addRelationshipToAccount(googleAccountId, accountId);
	}
}
