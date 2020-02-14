/**
 * 
 */
package in.co.everyrupee.repository.income;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import in.co.everyrupee.pojo.income.UserTransaction;

/**
 * Reference user transactions
 * 
 * @author Nagarjun
 *
 */
@Repository
public interface UserTransactionsRepository extends JpaRepository<UserTransaction, Integer> {
	/**
	 * 
	 * @param financialPortfolioId
	 * @return
	 */
	List<UserTransaction> findByFinancialPortfolioId(String financialPortfolioId);

	/**
	 * Find all the transactions for the month specified (Orders by creation date
	 * desc)
	 * 
	 * @param financialPortfolioId
	 * @param dateMeantFor
	 * @return
	 */
	@Query("select u from UserTransaction u where u.financialPortfolioId in ?1 and u.dateMeantFor in ?2 order by date(createDate) desc ")
	List<UserTransaction> findByFinancialPortfolioIdAndDate(String financialPortfolioId, Date dateMeantFor);

	/**
	 * Delete all user with ids specified in {@code ids} parameter
	 * 
	 * @param ids List of user ids
	 */
	@Modifying
	@Query("delete from UserTransaction u where u.id in ?1 and u.financialPortfolioId in ?2")
	void deleteUsersWithIds(List<Integer> ids, String financialPortfolioId);

	/**
	 * 
	 * @param financialPortfolioId
	 * @return
	 */
	@Query("select u from UserTransaction u where u.financialPortfolioId in ?1 and u.categoryId in ?2 order by date(createDate) asc ")
	List<UserTransaction> findByFinancialPortfolioIdAndCategories(String financialPortfolioId,
			List<Integer> categoryIds);

	/**
	 * Fetches all user with category ids specified in {@code ids} parameter
	 * 
	 * @param ids List of category ids
	 */
	@Query("SELECT u FROM UserTransaction u where u.categoryId in ?1 and u.financialPortfolioId in ?2 and u.dateMeantFor in ?3")
	List<UserTransaction> fetchUserTransactionWithCategoryId(Integer categoryId, String financialPortfolioId,
			Date dateMeantFor);

	/**
	 * Delete all user transactions with financial portfolio id
	 * 
	 * @param financialPortfolioId
	 */
	@Modifying
	@Query("delete from UserTransaction u where u.financialPortfolioId in ?1")
	void deleteAllUserTransactions(String financialPortfolioId);

	/**
	 * Fetch all user dates by financial portfolio id
	 * 
	 * @param financialPortfolioId
	 * @return
	 */
	@Query("SELECT u.dateMeantFor FROM UserTransaction u where u.financialPortfolioId in ?1")
	List<Date> findAllDatesByFPId(String financialPortfolioId);

	/**
	 * Delete by bank account id
	 * 
	 * @param pBankAccountId
	 */
	@Modifying
	@Query("delete from UserTransaction u where u.accountId in ?1")
	void deleteByBankAccount(int pBankAccountId);

}
