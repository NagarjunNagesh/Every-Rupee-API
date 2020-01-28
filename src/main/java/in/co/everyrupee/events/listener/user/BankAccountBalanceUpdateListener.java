/**
 * 
 */
package in.co.everyrupee.events.listener.user;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import in.co.everyrupee.events.user.OnAffectBankAccountBalanceEvent;
import in.co.everyrupee.pojo.user.BankAccount;
import in.co.everyrupee.service.user.IBankAccountService;

/**
 * Update the bank account balance asynchronously
 * 
 * @author Nagarjun
 *
 */
@Async
@Component
public class BankAccountBalanceUpdateListener implements IBankAccountBalanceUpdateListener {

	@Autowired
	private IBankAccountService bankAccountService;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Annotations defined below are required to not propogate the transactions and
	 * to create a new transaction for the listener.
	 */
	@Override
	public void onApplicationEvent(OnAffectBankAccountBalanceEvent event) {
		this.updateBankAccountBalance(event);

	}

	@Override
	public void updateBankAccountBalance(OnAffectBankAccountBalanceEvent event) {
		BankAccount modifyBankAccount = event.getBankAccount();
		if (modifyBankAccount == null) {
			Integer accountId = event.getAccountId();
			Optional<BankAccount> bankAccount = getBankAccountService().fetchBankAccountById(accountId);
			if (bankAccount.isPresent()) {
				modifyBankAccount = bankAccount.get();
			}
		}
		// Update the bank balance
		getBankAccountService().updateBankBalance(modifyBankAccount, event.getAmounToModify());
	}

	public IBankAccountService getBankAccountService() {
		return bankAccountService;
	}

}
