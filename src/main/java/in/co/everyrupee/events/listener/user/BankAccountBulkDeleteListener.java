/**
 * 
 */
package in.co.everyrupee.events.listener.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import in.co.everyrupee.events.user.OnDeleteUserTransactionCompleteEvent;
import in.co.everyrupee.pojo.income.UserTransaction;
import in.co.everyrupee.pojo.user.BankAccount;
import in.co.everyrupee.service.income.CategoryService;
import in.co.everyrupee.service.user.IBankAccountService;

/**
 * Update the bank account from list of user transactions
 * 
 * @author Nagarjun
 *
 */
@Component
public class BankAccountBulkDeleteListener {

	@Autowired
	private IBankAccountService bankAccountService;

	@Autowired
	private CategoryService categoryService;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Bulk Update Bank Balance
	 */
	@Async
	@EventListener
	public void bulkUpdateBankAccountBalance(OnDeleteUserTransactionCompleteEvent event) {
		Map<Integer, Double> bankAccountAndAmount = new HashedMap<Integer, Double>();
		for (UserTransaction userTransaction : event.getUserTransactionList()) {
			Double amountToModify = bankAccountAndAmount.get(userTransaction.getAccountId());

			// Calculate if the amount is to be deleted or added.
			boolean categoryIncome = categoryService.categoryIncome(userTransaction.getCategoryId());
			double amount = userTransaction.getAmount();
			if (categoryIncome) {
				amount *= -1;
			}

			if (amountToModify == null) {
				bankAccountAndAmount.put(userTransaction.getAccountId(), amount);
			} else {
				Double amountUpdated = bankAccountAndAmount.get(userTransaction.getAccountId());
				bankAccountAndAmount.put(userTransaction.getAccountId(), amountUpdated + amount);
			}
		}

		// Iterate over bank account and Amount
		List<BankAccount> bankAccountList = bankAccountService.fetchAllBankAccount(bankAccountAndAmount.keySet());
		List<BankAccount> temp = new ArrayList<BankAccount>();
		for (BankAccount recBankAccount : bankAccountList) {
			Double updatedAmount = bankAccountAndAmount.get(recBankAccount.getId());
			// Set Account Balance
			recBankAccount.setAccountBalance(recBankAccount.getAccountBalance() + updatedAmount);
			// Add to temporary list
			temp.add(recBankAccount);
		}
		// Clear temp and add to bank account list
		bankAccountList.clear();
		bankAccountList.addAll(temp);
		// Save all bank account list
		bankAccountService.saveAll(bankAccountList);

	}

	public IBankAccountService getBankAccountService() {
		return bankAccountService;
	}

	public void setBankAccountService(IBankAccountService bankAccountService) {
		this.bankAccountService = bankAccountService;
	}

}
