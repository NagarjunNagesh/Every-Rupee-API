/**
 * 
 */
package in.co.everyrupee.events.listener.user;

import org.springframework.context.ApplicationListener;

import in.co.everyrupee.events.user.OnDeleteUserTransactionCompleteEvent;

/**
 * @author developer
 *
 */
public interface IBankAccountBulkUpdateListener extends ApplicationListener<OnDeleteUserTransactionCompleteEvent> {

	public void bulkUpdateBankAccountBalance(final OnDeleteUserTransactionCompleteEvent event);
}
