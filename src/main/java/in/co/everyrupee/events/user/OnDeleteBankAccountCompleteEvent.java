/**
 * 
 */
package in.co.everyrupee.events.user;

import org.springframework.context.ApplicationEvent;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 
 * On deleting the user transaction event
 * 
 * @author Nagarjun
 *
 */
public class OnDeleteBankAccountCompleteEvent extends ApplicationEvent {

	private static final long serialVersionUID = -2233313482197986444L;
	private int bankAccountById;

	public OnDeleteBankAccountCompleteEvent(int bankAccountById) {
		super(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		this.bankAccountById = bankAccountById;
	}

	public int getBankAccountById() {
		return bankAccountById;
	}
}
