/**
 * 
 */
package in.co.everyrupee.event.listener.user;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import in.co.everyrupee.events.user.OnAffectBankAccountBalanceEvent;
import in.co.everyrupee.pojo.user.BankAccount;
import in.co.everyrupee.repository.user.BankAccountRepository;

/**
 * Bank Account Balance Update Listener Test (Listener)
 * 
 * @author Nagarjun
 *
 */
@RunWith(SpringRunner.class)
public class BankAccountBalanceUpdateListenerTest {

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@MockBean
	private BankAccountRepository bankAccountRepository;

	private static final int ACCOUNT_ID = 1930;

	@Before
	public void setup() {
		BankAccount bankAccount = new BankAccount();
		bankAccount.setAccountBalance(12d);
		bankAccount.setId(ACCOUNT_ID);

		when(getBankAccountRepository().findById(ACCOUNT_ID)).thenReturn(Optional.of(bankAccount));
	}

	@Test
	@WithMockUser(value = "spring")
	public void updateBankAccountBalance() {
		eventPublisher.publishEvent(new OnAffectBankAccountBalanceEvent(null, 11d, ACCOUNT_ID));

		// Check if ASYNC is invoked properly
		verify(getBankAccountRepository(), times(0)).findById(ACCOUNT_ID);
	}

	private BankAccountRepository getBankAccountRepository() {
		return bankAccountRepository;
	}

}
