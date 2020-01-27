/**
 * 
 */
package in.co.everyrupee.events.listener.user;

import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import in.co.everyrupee.events.user.OnDeleteUserTransactionCompleteEvent;
import in.co.everyrupee.pojo.income.UserTransaction;
import in.co.everyrupee.service.income.CategoryService;
import in.co.everyrupee.service.user.IBankAccountService;

/**
 * Update the bank account from list of user transactions
 * 
 * @author developer
 *
 */
public class BankAccountBulkUpdateListener implements IBankAccountBulkUpdateListener {

	@Autowired
	private IBankAccountService bankAccountService;

	@Autowired
	private CategoryService categoryService;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Annotations defined below are required to not propogate the transactions and
	 * to create a new transaction for the listener.
	 */
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public void onApplicationEvent(OnDeleteUserTransactionCompleteEvent event) {
		this.bulkUpdateBankAccountBalance(event);
	}

	@Override
	public void bulkUpdateBankAccountBalance(OnDeleteUserTransactionCompleteEvent event) {
		Map<Integer, Double> bankAccountAndAmount = new HashedMap<Integer, Double>();
		for (UserTransaction userTransaction : event.getUserTransactionList()) {
			Double amountToModify = bankAccountAndAmount.get(userTransaction.getAccountId());

			// Calculate if the amount is to be deleted or added.
			boolean categoryIncome = categoryService.categoryIncome(userTransaction.getCategoryId());
			double amount = userTransaction.getAmount();
			if (!categoryIncome) {
				amount *= -1;
			}

			if (amountToModify == null) {
				bankAccountAndAmount.put(userTransaction.getAccountId(), amount);
			}
		}
	}

	public IBankAccountService getBankAccountService() {
		return bankAccountService;
	}

	public void setBankAccountService(IBankAccountService bankAccountService) {
		this.bankAccountService = bankAccountService;
	}

}
