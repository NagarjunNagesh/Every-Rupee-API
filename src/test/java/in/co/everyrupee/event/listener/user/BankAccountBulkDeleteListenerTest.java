/**
 * 
 */
package in.co.everyrupee.event.listener.user;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import in.co.everyrupee.events.user.OnDeleteUserTransactionCompleteEvent;
import in.co.everyrupee.pojo.income.UserTransaction;

/**
 * Bank Account Bulk Delete Listener Test (Listener)
 * 
 * @author Nagarjun
 *
 */
@RunWith(SpringRunner.class)
public class BankAccountBulkDeleteListenerTest {

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Test
	@WithMockUser(value = "spring")
	public void updateBankAccountBalance() {
		List<UserTransaction> userTransactionLst = new ArrayList<UserTransaction>();
		eventPublisher.publishEvent(new OnDeleteUserTransactionCompleteEvent(userTransactionLst));
	}

}
