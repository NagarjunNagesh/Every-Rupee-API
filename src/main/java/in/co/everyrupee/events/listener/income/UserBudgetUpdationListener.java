package in.co.everyrupee.events.listener.income;

import in.co.everyrupee.events.income.OnFetchCategoryTotalCompleteEvent;
import in.co.everyrupee.service.income.IUserBudgetService;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Asynchronously updates a budget for the user, removing itself from the transaction of the caller
 * method.
 *
 * @author Nagarjun
 */
@Component
public class UserBudgetUpdationListener {

  @Autowired private IUserBudgetService userBudgetService;

  Logger logger = LoggerFactory.getLogger(this.getClass());

  /** Save Auto generated budget */
  @Async
  @EventListener
  public void updateAutoGeneratedUserBudget(final OnFetchCategoryTotalCompleteEvent event) {

    try {
      Map<Integer, Double> categoryIdAndCategoryTotal = event.getCategoryIdAndTotalAmount();
      String dateMeantFor = event.getDateMeantFor();
      logger.debug(
          "Updating user budget for the financial portfolio - " + event.getFinancialPortfolioId());

      if (categoryIdAndCategoryTotal == null || dateMeantFor == null) {
        return;
      }

      userBudgetService.updateAutoGeneratedUserBudget(
          event.getFinancialPortfolioId(), categoryIdAndCategoryTotal, dateMeantFor);

    } catch (Exception e) {
      logger.error("Unable to create a budget for the category " + e);
    }
  }
}
