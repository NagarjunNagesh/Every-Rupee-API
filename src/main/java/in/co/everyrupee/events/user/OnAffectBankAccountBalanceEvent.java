/**
 * 
 */
package in.co.everyrupee.events.user;

import org.springframework.context.ApplicationEvent;
import org.springframework.security.core.context.SecurityContextHolder;

import in.co.everyrupee.pojo.user.BankAccount;

/**
 * Event which is triggered when the account balance is changed
 * 
 * @author Nagarjun
 *
 */
public class OnAffectBankAccountBalanceEvent extends ApplicationEvent {

	private static final long serialVersionUID = 4007654671841535138L;
	private BankAccount bankAccount;
	private Double amounToModify;
	private Integer accountId;

	public OnAffectBankAccountBalanceEvent(final BankAccount bankAccount, final Double amountToModify,
			final Integer accountId) {
		super(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		this.bankAccount = bankAccount;
		this.amounToModify = amountToModify;
		this.accountId = accountId;
	}

	public BankAccount getBankAccount() {
		return bankAccount;
	}

	public Double getAmounToModify() {
		return amounToModify;
	}

	public Integer getAccountId() {
		return accountId;
	}

}
