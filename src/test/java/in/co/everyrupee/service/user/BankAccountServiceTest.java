/**
 * 
 */
package in.co.everyrupee.service.user;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import in.co.everyrupee.constants.income.DashboardConstants;
import in.co.everyrupee.pojo.income.UserBudget;
import in.co.everyrupee.pojo.user.BankAccount;
import in.co.everyrupee.repository.user.BankAccountRepository;
import in.co.everyrupee.service.income.CategoryService;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author arjun
 *
 */
@RunWith(SpringRunner.class)
@WithMockUser
public class BankAccountServiceTest {
	@Autowired
	private BankAccountService bankAccountService;

	@MockBean
	private BankAccountRepository bankAccountRepository;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String FINANCIAL_PORTFOLIO_ID = "193000000";

	@TestConfiguration
	static class BankAccountServiceImplTestContextConfiguration {

		@Bean
		public BankAccountService bankAccountService() {
			return new BankAccountService();
		}

	}

	@Before
	public void setUp() {
		BankAccount newAccount = new BankAccount();
		newAccount.setFinancialPortfolioId(FINANCIAL_PORTFOLIO_ID);
		newAccount.setSelectedAccount(true);
		Mockito.when(bankAccountRepository.save(Mockito.any())).thenReturn(newAccount);
	}

	/**
	 * TEST: to return user budget with fetchAllUserBudget
	 */
	@Test
	public void userBudgetRetrieveMock() {
		BankAccount newBankAccount = getBankAccountService().fetchSelectedAccount(FINANCIAL_PORTFOLIO_ID);

		assertThat(newBankAccount, is(notNullValue()));
		assertThat(newBankAccount.getFinancialPortfolioId(), is(FINANCIAL_PORTFOLIO_ID));
		assertThat(newBankAccount.isSelectedAccount(), is(true));
	}

	private BankAccountService getBankAccountService() {
		return bankAccountService;
	}

}
