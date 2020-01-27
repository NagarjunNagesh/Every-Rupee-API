/**
 * 
 */
package in.co.everyrupee.events.listener.user;

import org.springframework.context.ApplicationListener;

import in.co.everyrupee.events.user.OnAffectBankAccountBalanceEvent;

/**
 * Event to trigger when the account balance is changed
 * 
 * @author Nagarjun
 *
 */
public interface IBankAccountBalanceUpdateListener extends ApplicationListener<OnAffectBankAccountBalanceEvent> {

	public void updateBankAccountBalance(final OnAffectBankAccountBalanceEvent event);

}
