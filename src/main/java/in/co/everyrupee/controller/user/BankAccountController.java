package in.co.everyrupee.controller.user;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.constraints.Size;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.co.everyrupee.constants.GenericConstants;
import in.co.everyrupee.constants.income.DashboardConstants;
import in.co.everyrupee.pojo.user.BankAccount;
import in.co.everyrupee.service.user.IBankAccountService;
import in.co.everyrupee.utils.GenericResponse;

/**
 *
 * Manage Bank Account with API
 * 
 * @author Nagarjun
 * 
 **/
@RestController
@RequestMapping("/api/bankaccount")
@Validated
public class BankAccountController {

	@Autowired
	private IBankAccountService bankAccountService;

	/**
	 * Get All Bank Accounts
	 * 
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public List<BankAccount> getAllBankAccounts(
			@RequestParam(DashboardConstants.Overview.FINANCIAL_PORTFOLIO_ID) @Size(min = 0, max = GenericConstants.MAX_ALLOWED_LENGTH_FINANCIAL_PORTFOLIO) String pFinancialPortfolioId) {
		return getBankAccountService().getAllBankAccounts(pFinancialPortfolioId);
	}

	/**
	 * Post Add New account
	 * 
	 * @param formData
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public BankAccount addNewBankAccount(@RequestBody MultiValueMap<String, String> formData) {
		return getBankAccountService().addNewBankAccount(formData);
	}

	/**
	 * Get All User Budgets
	 * 
	 * @return
	 */
	@RequestMapping(value = "/preview", method = RequestMethod.GET)
	public List<BankAccount> previewBankAccounts(
			@RequestParam(DashboardConstants.Overview.FINANCIAL_PORTFOLIO_ID) @Size(min = 0, max = GenericConstants.MAX_ALLOWED_LENGTH_FINANCIAL_PORTFOLIO) String pFinancialPortfolioId) {
		return getBankAccountService().previewBankAccounts(pFinancialPortfolioId);
	}

	/**
	 * Post convert selected account
	 * 
	 * @param userPrincipal
	 * @param formData
	 * @return
	 */
	@RequestMapping(value = "/select", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public GenericResponse selectAccount(@RequestBody MultiValueMap<String, String> formData) {
		getBankAccountService().selectAccount(formData);

		return new GenericResponse("success");
	}

	/**
	 * Categorize the bank account
	 * 
	 * @return
	 */
	@RequestMapping(value = "/categorize", method = RequestMethod.GET)
	public Map<String, Set<BankAccount>> categorizeBankAccount(
			@RequestParam(DashboardConstants.Overview.FINANCIAL_PORTFOLIO_ID) @Size(min = 0, max = GenericConstants.MAX_ALLOWED_LENGTH_FINANCIAL_PORTFOLIO) String pFinancialPortfolioId) {
		return getBankAccountService().categorizeBankAccount(pFinancialPortfolioId);
	}

	/**
	 * Categorize the bank account
	 * 
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.DELETE)
	public GenericResponse deleteAllBankAccounts(
			@RequestParam(DashboardConstants.Overview.FINANCIAL_PORTFOLIO_ID) @Size(min = 0, max = GenericConstants.MAX_ALLOWED_LENGTH_FINANCIAL_PORTFOLIO) String pFinancialPortfolioId) {
		getBankAccountService().deleteAllBankAccounts(pFinancialPortfolioId);

		return new GenericResponse("success");
	}

	public IBankAccountService getBankAccountService() {
		return bankAccountService;
	}

}
