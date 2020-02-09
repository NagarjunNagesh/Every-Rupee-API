package in.co.everyrupee.service.user;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.validation.constraints.Size;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import in.co.everyrupee.constants.user.BankAccountConstants;
import in.co.everyrupee.exception.InvalidAttributeValueException;
import in.co.everyrupee.pojo.user.AccountType;
import in.co.everyrupee.pojo.user.BankAccount;
import in.co.everyrupee.repository.user.BankAccountRepository;
import in.co.everyrupee.utils.ERStringUtils;

@Transactional
@Service
@CacheConfig(cacheNames = { BankAccountConstants.BANK_ACCOUNT_CACHE })
public class BankAccountService implements IBankAccountService {

	@Autowired
	private BankAccountRepository bankAccountRepository;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Override
	@Cacheable(key = "#pFinancialPortfolioId")
	public List<BankAccount> getAllBankAccounts(String pFinancialPortfolioId) {

		return bankAccountRepository.findByFinancialPortfolioId(pFinancialPortfolioId);
	}

	@Override
	@CacheEvict(key = "#formData.getFirst(\"financialPortfolioId\")")
	public BankAccount addNewBankAccount(MultiValueMap<String, String> formData) {

		if (ERStringUtils.isBlank(formData.getFirst(BankAccountConstants.LINKED_ACCOUNT))) {
			throw new InvalidAttributeValueException("addNewBankAccount", BankAccountConstants.LINKED_ACCOUNT, null);
		}

		BankAccount newAccount = new BankAccount();
		newAccount.setLinked(Boolean.parseBoolean(formData.getFirst(BankAccountConstants.LINKED_ACCOUNT_PARAM)));
		newAccount.setFinancialPortfolioId(formData.getFirst(BankAccountConstants.FINANCIAL_PORTFOLIO_ID_PARAM));
		newAccount.setBankAccountName(formData.getFirst(BankAccountConstants.BANK_ACCOUNT_NAME_PARAM));
		newAccount.setAccountBalance(Double.parseDouble(formData.getFirst(BankAccountConstants.ACCOUNT_BALANCE_PARAM)));
		// Replace all space in the text to without space
		newAccount.setAccountType(
				AccountType.valueOf(formData.getFirst(BankAccountConstants.ACCOUNT_TYPE_PARAM).replaceAll("\\s+", "")));
		return bankAccountRepository.save(newAccount);
	}

	@Override
	public List<BankAccount> previewBankAccounts(String financialPortfolioId) {
		List<BankAccount> linkedBA = getAllBankAccounts(financialPortfolioId);
		List<BankAccount> selectedBA = new ArrayList<BankAccount>();

		// Fetch the first selected account
		for (BankAccount bankAccount : linkedBA) {
			if (bankAccount.isSelectedAccount()) {
				selectedBA.add(bankAccount);
				break;
			}
		}

		// Sort the list of bank accounts by number of times selected
		linkedBA.sort(Comparator.comparing(BankAccount::getNumberOfTimesSelected).reversed());

		int count = 0;
		for (BankAccount bankAccount : linkedBA) {
			// Fetches the first four accounts for preview
			if (count >= 3) {
				break;
			}

			// If there is none selected then set the first one as selected
			if (CollectionUtils.isEmpty(selectedBA)) {
				bankAccount.setSelectedAccount(true);
				// Saves the bank account as selected and stores the result in the selectedBA
				selectedBA.add(bankAccountRepository.save(bankAccount));
				continue;
			} else if (selectedBA.get(0).getId() == bankAccount.getId()) {
				// If the bank account is already present in the object (selectedBA)
				continue;
			}

			selectedBA.add(bankAccount);
			count++;
		}

		return selectedBA;
	}

	@Override
	public void selectAccount(MultiValueMap<String, String> formData) {
		String bankAccountId = formData.getFirst(BankAccountConstants.BANK_ACCOUNT_ID);
		String selectedAccount = formData.getFirst(BankAccountConstants.SELECTED_ACCOUNT_PARAM);

		List<BankAccount> bankAccountList = bankAccountRepository.findAll();

		// Convert bank account to selected
		for (BankAccount bankAccount : bankAccountList) {
			if (bankAccount.getId() == Integer.parseInt(bankAccountId)) {
				bankAccount.setSelectedAccount(Boolean.parseBoolean(selectedAccount));
				bankAccount.setNumberOfTimesSelected(bankAccount.getNumberOfTimesSelected() + 1);
				bankAccountRepository.save(bankAccount);
			} else if (bankAccount.isSelectedAccount()) {
				bankAccount.setSelectedAccount(false);
				bankAccountRepository.save(bankAccount);
			}
		}
	}

	@Override
	public Map<String, Set<BankAccount>> categorizeBankAccount(String pFinancialPortfolioId) {
		List<BankAccount> bankAccountList = getAllBankAccounts(pFinancialPortfolioId);
		Map<String, Set<BankAccount>> categorizeBankAccount = new HashMap<String, Set<BankAccount>>();

		for (BankAccount bankAccount : bankAccountList) {
			Set<BankAccount> bankAccountSet = new HashSet<BankAccount>();

			if (categorizeBankAccount.keySet().contains(bankAccount.getAccountType().getType())) {
				bankAccountSet = categorizeBankAccount.get(bankAccount.getAccountType().getType());
			}

			bankAccountSet.add(bankAccount);
			categorizeBankAccount.put(bankAccount.getAccountType().getType(), bankAccountSet);
		}

		return categorizeBankAccount;
	}

	@Override
	@CacheEvict(key = "#pFinancialPortfolioId")
	public void deleteAllBankAccounts(String pFinancialPortfolioId) {
		bankAccountRepository.deleteAllBankAccounts(pFinancialPortfolioId);
	}

	@Override
	public BankAccount fetchSelectedAccount(String pFinancialPortfolioId) {
		BankAccount bankAccount = new BankAccount();
		List<BankAccount> bankAccountList = bankAccountRepository
				.findSelectedAccountsByFinancialPortfolioId(pFinancialPortfolioId);

		if (CollectionUtils.isNotEmpty(bankAccountList)) {
			bankAccount = bankAccountList.get(0);
		} else {
			// Create a cash account and
			BankAccount newAccount = new BankAccount();
			newAccount.setLinked(false);
			newAccount.setSelectedAccount(true);
			newAccount.setFinancialPortfolioId(pFinancialPortfolioId);
			newAccount.setBankAccountName(AccountType.CASH.toString());
			newAccount.setAccountBalance(0d);
			newAccount.setAccountType(AccountType.CASH);
			bankAccount = bankAccountRepository.save(newAccount);
		}

		return bankAccount;
	}

	@Override
	@CacheEvict(key = "#bankAccount.getFinancialPortfolioId()")
	public void updateBankBalance(BankAccount bankAccount, Double amountModified) {

		// If amountModified is null then return
		if (amountModified == null || amountModified.isNaN() || amountModified.isInfinite() || bankAccount == null) {
			LOGGER.warn(
					"Unable to update the account balance as the amount modified is {0} and the bank account is {1}",
					amountModified, bankAccount);
			return;
		}

		// Update the new bank balance
		bankAccount.setAccountBalance(bankAccount.getAccountBalance() + amountModified);
		bankAccountRepository.save(bankAccount);
	}

	@Override
	@CacheEvict(key = "#bankAccountOld.getFinancialPortfolioId()")
	public BankAccount updateBankAccount(String bankAccountId, BankAccount bankAccountOld) {
		Optional<BankAccount> bankAccount = bankAccountRepository.findById(Integer.parseInt(bankAccountId));

		if (bankAccount.isPresent()) {
			bankAccount.get().setAccountBalance(bankAccountOld.getAccountBalance());
			return bankAccountRepository.save(bankAccount.get());
		}
		LOGGER.warn("Bank account with Id {0} was not found for the financial portfolio id {1}", bankAccountId,
				bankAccountOld.getFinancialPortfolioId());
		return null;
	}

	@Override
	@CacheEvict(key = "#pFinancialPortfolioId")
	public void deleteBankAccount(@Size(min = 0, max = 60) String pBankAccountId,
			@Size(min = 0, max = 60) String pFinancialPortfolioId) {
		bankAccountRepository.deleteById(Integer.parseInt(pBankAccountId));

		// Delete all transactions with account ID
		eventPublisher.publishEvent(new DeleteTransactionByAccountId(bankAccount, transactionAmount, null));
	}

	@Override
	public Optional<BankAccount> fetchBankAccountById(Integer accountId) {
		return bankAccountRepository.findById(accountId);
	}

	@Override
	public List<BankAccount> fetchAllBankAccount(Set<Integer> accountIds) {
		return bankAccountRepository.findAllById(accountIds);
	}

	@Override
	public void saveAll(List<BankAccount> bankAccountList) {
		bankAccountRepository.saveAll(bankAccountList);
	}

}
