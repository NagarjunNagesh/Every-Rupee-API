/** */
package in.co.everyrupee.events.user;

import in.co.everyrupee.pojo.income.UserTransaction;
import java.util.List;
import org.springframework.context.ApplicationEvent;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * On deleting the user transaction event
 *
 * @author Nagarjun
 */
public class OnDeleteUserTransactionCompleteEvent extends ApplicationEvent {

  private static final long serialVersionUID = -2233313482197986444L;
  private List<UserTransaction> userTransactionList;

  public OnDeleteUserTransactionCompleteEvent(List<UserTransaction> userTransactionList) {
    super(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    this.userTransactionList = userTransactionList;
  }

  public List<UserTransaction> getUserTransactionList() {
    return userTransactionList;
  }
}
