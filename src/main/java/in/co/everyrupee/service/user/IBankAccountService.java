package in.co.everyrupee.service.user;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.MultiValueMap;

import in.co.everyrupee.pojo.user.BankAccount;

public interface IBankAccountService {
	/**
	 * Fetch all bank accounts
	 * 
	 * @param financialPortfolioId
	 * @return
	 */
	public List<BankAccount> getAllBankAccounts(String financialPortfolioId);

	/**
	 * Add a new bank account
	 * 
	 * @param formData
	 * @return
	 */
	public BankAccount addNewBankAccount(MultiValueMap<String, String> formData);

	/**
	 * Preview bank accounts (first three + default)
	 * 
	 * @param financialPortfolioId
	 * @return
	 */
	public List<BankAccount> previewBankAccounts(String financialPortfolioId);

	/**
	 * Select Account
	 * 
	 * @param pFinancialPortfolioId
	 * @param formData
	 * @return
	 */
	public void selectAccount(MultiValueMap<String, String> formData);

	/**
	 * Categorize Bank Account
	 * 
	 * @param pFinancialPortfolioId
	 * @return
	 */
	public Map<String, Set<BankAccount>> categorizeBankAccount(String pFinancialPortfolioId);
}
