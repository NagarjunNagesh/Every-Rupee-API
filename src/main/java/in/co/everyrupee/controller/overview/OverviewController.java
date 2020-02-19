package in.co.everyrupee.controller.overview;

import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.co.everyrupee.constants.GenericConstants;
import in.co.everyrupee.constants.income.DashboardConstants;
import in.co.everyrupee.pojo.TransactionType;
import in.co.everyrupee.pojo.user.AccountType;
import in.co.everyrupee.service.income.IUserTransactionService;

/**
 *
 * Manage Overview page in Dashboard
 * 
 * @author Nagarjun
 * 
 **/
@RestController
@RequestMapping("/api/overview")
@Validated
public class OverviewController {

	@Autowired
	private IUserTransactionService userTransactionService;

	/**
	 * Get user transactions sorted by creation date - DESC
	 * 
	 * @param pFinancialPortfolioId
	 * @param userPrincipal
	 * @return
	 */
	@RequestMapping(value = "/recentTransactions", method = RequestMethod.GET)
	public Object getUserTransactionByFinancialPortfolioId(
			@RequestParam(DashboardConstants.Overview.DATE_MEANT_FOR) @Size(min = 0, max = 10) String dateMeantFor,
			@RequestParam(DashboardConstants.Overview.FINANCIAL_PORTFOLIO_ID) @Size(min = 0, max = GenericConstants.MAX_ALLOWED_LENGTH_FINANCIAL_PORTFOLIO) String pFinancialPortfolioId) {

		return getUserTransactionService().fetchUserTransactionByCreationDate(pFinancialPortfolioId, dateMeantFor);
	}

	/**
	 * Fetch the lifetime average income / average expense /
	 * 
	 * @param userPrincipal
	 * @param type
	 * @param fetchAverage
	 * @return
	 */
	@RequestMapping(value = "/lifetime", method = RequestMethod.GET)
	public Object getLifetimeIncomeByFinancialPortfolioId(
			@Valid @RequestParam(DashboardConstants.Overview.TYPE_PARAM) TransactionType type,
			@RequestParam(DashboardConstants.Overview.AVERAGE_PARAM) boolean fetchAverage,
			@RequestParam(DashboardConstants.Overview.FINANCIAL_PORTFOLIO_ID) @Size(min = 0, max = GenericConstants.MAX_ALLOWED_LENGTH_FINANCIAL_PORTFOLIO) String pFinancialPortfolioId,
			@Valid @RequestParam(required = false, name = DashboardConstants.Overview.ACCOUNT_TYPE_PARAM) Optional<AccountType> accountType) {

		// Check if the account type is present
		if (accountType.isPresent()) {
			return null;
		}
		return getUserTransactionService().fetchLifetimeCalculations(type, fetchAverage, pFinancialPortfolioId);
	}

	private IUserTransactionService getUserTransactionService() {
		return userTransactionService;
	}

}
