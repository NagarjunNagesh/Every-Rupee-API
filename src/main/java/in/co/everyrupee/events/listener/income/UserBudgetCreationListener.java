package in.co.everyrupee.events.listener.income;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;

import in.co.everyrupee.constants.income.DashboardConstants;
import in.co.everyrupee.events.income.OnSaveTransactionCompleteEvent;
import in.co.everyrupee.service.income.IUserBudgetService;
import in.co.everyrupee.utils.ERStringUtils;

/**
 * Asynchronously creates a budget for the user, removing itself from the
 * transaction of the caller method.
 * 
 * @author Nagarjun
 *
 */
@Async
@Component
public class UserBudgetCreationListener implements IUserBudgetCreationListener {

	@Autowired
	private IUserBudgetService userBudgetService;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Annotations defined below are required to not propogate the transactions and
	 * to create a new transaction for the listener.
	 */
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public void onApplicationEvent(final OnSaveTransactionCompleteEvent event) {
		this.saveAutoGeneratedUserBudget(event);
	}

	/**
	 * Save Auto generated budget
	 */
	@Override
	public void saveAutoGeneratedUserBudget(final OnSaveTransactionCompleteEvent event) {

		try {
			MultiValueMap<String, String> formData = event.getFormData();
			logger.debug("Creating user budget for the financial portfolio - " + event.getFinancialPortfolioId());

			if (CollectionUtils.isEmpty(formData.get(DashboardConstants.Transactions.CATEGORY_OPTIONS))
					|| ERStringUtils.isBlank(formData.getFirst(DashboardConstants.Transactions.CATEGORY_OPTIONS))
					|| CollectionUtils.isEmpty(formData.get(DashboardConstants.Transactions.AMOUNT))
					|| ERStringUtils.isBlank(formData.getFirst(DashboardConstants.Transactions.AMOUNT))
					|| ERStringUtils.equalsIgnoreCase(formData.getFirst(DashboardConstants.Transactions.AMOUNT),
							DashboardConstants.DEFAULT_ADD_ROW_QUANTITY)) {
				logger.error("Invalid event data for financial portfolioid - " + event.getFinancialPortfolioId()
						+ " Amount is " + formData.getFirst(DashboardConstants.Transactions.AMOUNT) + " category is "
						+ formData.getFirst(DashboardConstants.Transactions.CATEGORY_OPTIONS));
				return;
			}

			formData.put(DashboardConstants.Budget.CATEGORY_ID,
					formData.get(DashboardConstants.Transactions.CATEGORY_OPTIONS));
			formData.put(DashboardConstants.Budget.PLANNED, formData.get(DashboardConstants.Transactions.AMOUNT));

			userBudgetService.saveAutoGeneratedUserBudget(formData, event.getFinancialPortfolioId());
		} catch (Exception e) {
			logger.error("Unable to create a budget for the category " + e);
		}
	}

}
