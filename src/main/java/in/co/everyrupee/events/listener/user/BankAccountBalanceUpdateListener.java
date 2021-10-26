/** */
package in.co.everyrupee.events.listener.user;

import in.co.everyrupee.events.user.OnAffectBankAccountBalanceEvent;
import in.co.everyrupee.pojo.user.BankAccount;
import in.co.everyrupee.service.user.IBankAccountService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Update the bank account balance asynchronously
 *
 * @author Nagarjun
 */
@Component
public class BankAccountBalanceUpdateListener {

  @Autowired private IBankAccountService bankAccountService;

  Logger logger = LoggerFactory.getLogger(this.getClass());

  /**
   * Update the bank balance
   *
   * @param event
   */
  @Async
  @EventListener
  public void updateBankAccountBalance(OnAffectBankAccountBalanceEvent event) {
    BankAccount modifyBankAccount = event.getBankAccount();
    if (modifyBankAccount == null) {
      Integer accountId = event.getAccountId();
      Optional<BankAccount> bankAccount = getBankAccountService().fetchBankAccountById(accountId);
      if (bankAccount.isPresent()) {
        modifyBankAccount = bankAccount.get();
      }
    }
    // Update the bank balance
    getBankAccountService().updateBankBalance(modifyBankAccount, event.getAmounToModify());
  }

  public IBankAccountService getBankAccountService() {
    return bankAccountService;
  }
}
