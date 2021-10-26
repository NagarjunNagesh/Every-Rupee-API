package in.co.everyrupee.events.listener.income;

import in.co.everyrupee.events.user.OnDeleteBankAccountCompleteEvent;
import in.co.everyrupee.service.income.IUserTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Asynchronously delete transactions by account id
 *
 * @author Nagarjun
 */
@Component
public class DeleteTransactionsByAccountIdListener {

  @Autowired private IUserTransactionService userTransactionsService;

  Logger logger = LoggerFactory.getLogger(this.getClass());

  /** Save Auto generated budget */
  @Async
  @EventListener
  public void deleteTransactionsByAccountId(final OnDeleteBankAccountCompleteEvent event) {
    userTransactionsService.deleteTransactionsByBankAccount(event.getBankAccountById());
  }
}
