package in.co.everyrupee.event.listener.income;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import in.co.everyrupee.events.user.OnDeleteBankAccountCompleteEvent;

/**
 * Bank Account Bulk Delete Listener Test (Listener)
 * 
 * @author Nagarjun
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class OnDeleteTransactionsByBankAccountTest {

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Test
	@WithMockUser(value = "spring")
	public void deleteTransactionsByAccountId() {
		eventPublisher.publishEvent(new OnDeleteBankAccountCompleteEvent(123456));
	}

}
