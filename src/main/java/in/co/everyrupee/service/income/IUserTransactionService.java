package in.co.everyrupee.service.income;

import in.co.everyrupee.pojo.TransactionType;
import in.co.everyrupee.pojo.income.UserTransaction;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.util.MultiValueMap;

public interface IUserTransactionService {

  Object fetchUserTransaction(String financialPortfolioId, String dateMeantFor);

  UserTransaction saveUserTransaction(
      MultiValueMap<String, String> formData, String financialPortfolioId);

  void deleteUserTransactions(
      String transactionalIds, String financialPortfolioId, String dateMeantFor);

  UserTransaction updateTransactions(
      MultiValueMap<String, String> formData, String formFieldName, String financialPortfolioId);

  Map<Integer, Double> fetchCategoryTotalAndUpdateUserBudget(
      String financialPortfolioId, String dateMeantFor, boolean updateBudget);

  List<UserTransaction> fetchUserTransactionByCreationDate(
      String financialPortfolioId, String dateMeantFor);

  Object fetchLifetimeCalculations(
      TransactionType type, boolean fetchAverage, String pFinancialPortfolioId);

  Double fetchUserTransactionCategoryTotal(
      String financialPortfolioId, Integer categoryId, Date dateMeantFor);

  void deleteUserTransactions(String pFinancialPortfolioId);

  void deleteTransactionsByBankAccount(int bankAccountById);

  void copyFromPreviousMonth();
}
