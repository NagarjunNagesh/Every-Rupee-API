package in.co.everyrupee.controller.income;

import java.security.Principal;
import java.util.Map;

import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.co.everyrupee.constants.GenericConstants;
import in.co.everyrupee.constants.income.DashboardConstants;
import in.co.everyrupee.pojo.income.UserTransaction;
import in.co.everyrupee.service.income.IUserTransactionService;
import in.co.everyrupee.utils.GenericResponse;

/**
 * Manage API User Transactions
 * 
 * @author Nagarjun Nagesh
 *
 */
@RestController
@RequestMapping("/api/transactions")
@Validated
public class UserTransactionsController {

	@Autowired
	private IUserTransactionService userTransactionService;

	/**
	 * Get a Single User Transaction
	 * 
	 * @param pFinancialPortfolioId
	 * @param userPrincipal
	 * @return
	 */
	@RequestMapping(value = "/{pFinancialPortfolioId}", method = RequestMethod.GET)
	public Object getUserTransactionByFinancialPortfolioId(
			@PathVariable @Size(min = 0, max = GenericConstants.MAX_ALLOWED_LENGTH_FINANCIAL_PORTFOLIO) String pFinancialPortfolioId,
			Principal userPrincipal,
			@RequestParam(DashboardConstants.Transactions.DATE_MEANT_FOR) @Size(min = 0, max = 10) String dateMeantFor) {

		return userTransactionService.fetchUserTransaction(pFinancialPortfolioId, dateMeantFor);
	}

	/**
	 * Fetch category total and update user budget
	 * 
	 * @param pFinancialPortfolioId
	 * @param userPrincipal
	 * @return
	 */
	@RequestMapping(value = "/categoryTotal/{pFinancialPortfolioId}", method = RequestMethod.GET)
	public Map<Integer, Double> getCategoryTotalByFinancialPortfolioId(
			@PathVariable @Size(min = 0, max = GenericConstants.MAX_ALLOWED_LENGTH_FINANCIAL_PORTFOLIO) String pFinancialPortfolioId,
			Principal userPrincipal,
			@RequestParam(DashboardConstants.Transactions.DATE_MEANT_FOR) @Size(min = 0, max = 10) String dateMeantFor,
			@RequestParam(DashboardConstants.Transactions.UPDATE_BUDGET_PARAM) boolean updateBudget) {

		return userTransactionService.fetchCategoryTotalAndUpdateUserBudget(pFinancialPortfolioId, dateMeantFor,
				updateBudget);
	}

	/**
	 * Saves a UserTransaction
	 * 
	 * @param pFinancialPortfolioId
	 * @param formData
	 * @param userPrincipal
	 * @return
	 */
	@RequestMapping(value = "/save/{pFinancialPortfolioId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public UserTransaction save(
			@PathVariable @Size(min = 0, max = GenericConstants.MAX_ALLOWED_LENGTH_FINANCIAL_PORTFOLIO) String pFinancialPortfolioId,
			@RequestBody MultiValueMap<String, String> formData, Principal userPrincipal) {

		UserTransaction userTransactionResponse = userTransactionService.saveUserTransaction(formData,
				pFinancialPortfolioId);

		return userTransactionResponse;
	}

	/**
	 * Delete a User Transaction
	 * 
	 * @param pFinancialPortfolioId
	 * @param transactionIds
	 * @param userPrincipal
	 * @return
	 */
	@RequestMapping(value = "/{pFinancialPortfolioId}/{transactionIds}", method = RequestMethod.DELETE)
	public GenericResponse deleteUserTransactionById(
			@PathVariable @Size(min = 0, max = GenericConstants.MAX_ALLOWED_LENGTH_FINANCIAL_PORTFOLIO) String pFinancialPortfolioId,
			@PathVariable String transactionIds, Principal userPrincipal,
			@RequestParam(DashboardConstants.Transactions.DATE_MEANT_FOR) @Size(min = 0, max = 10) String dateMeantFor) {

		userTransactionService.deleteUserTransactions(transactionIds, pFinancialPortfolioId, dateMeantFor);

		return new GenericResponse("success");
	}

	/**
	 * 
	 * Update description, transaction & category in user transactions
	 * 
	 * @param pFinancialPortfolioId
	 * @param formFieldName
	 * @param formData
	 * @param userPrincipal
	 * @return
	 */
	@RequestMapping(value = "/{pFinancialPortfolioId}/update/{formFieldName}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public UserTransaction updateDescriptionByUserTransactionById(
			@PathVariable @Size(min = 0, max = GenericConstants.MAX_ALLOWED_LENGTH_FINANCIAL_PORTFOLIO) String pFinancialPortfolioId,
			@PathVariable @Size(min = 0, max = GenericConstants.MAX_ALLOWED_LENGTH_FINANCIAL_PORTFOLIO) String formFieldName,
			@RequestBody MultiValueMap<String, String> formData, Principal userPrincipal) {

		UserTransaction userTransactionSaved = userTransactionService.updateTransactions(formData, formFieldName,
				pFinancialPortfolioId);

		return userTransactionSaved;
	}

	/**
	 * Delete all  User Transactions
	 * 
	 * @param pFinancialPortfolioId
	 * @return
	 */
	@RequestMapping(value = "/{pFinancialPortfolioId}", method = RequestMethod.DELETE)
	public GenericResponse deleteAllUserTransactions(
			@PathVariable @Size(min = 0, max = GenericConstants.MAX_ALLOWED_LENGTH_FINANCIAL_PORTFOLIO) String pFinancialPortfolioId) {

		userTransactionService.deleteUserTransactions(pFinancialPortfolioId);

		return new GenericResponse("success");
	}

}