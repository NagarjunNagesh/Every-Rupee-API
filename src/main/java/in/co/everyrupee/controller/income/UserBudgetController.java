package in.co.everyrupee.controller.income;

import in.co.everyrupee.constants.GenericConstants;
import in.co.everyrupee.constants.income.DashboardConstants;
import in.co.everyrupee.pojo.income.UserBudget;
import in.co.everyrupee.service.income.IUserBudgetService;
import in.co.everyrupee.utils.GenericResponse;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Manage Budget For Users
 *
 * @author Nagarjun
 */
@RestController
@RequestMapping("/api/budget")
@Validated
public class UserBudgetController {

  @Autowired private IUserBudgetService userBudgetService;

  // Get All User Budgets
  @RequestMapping(value = "/{financialPortfolioId}", method = RequestMethod.GET)
  public List<UserBudget> getUserBudgetByFinancialPortfolioId(
      @PathVariable String financialPortfolioId,
      @RequestParam(DashboardConstants.Budget.DATE_MEANT_FOR) @Size(min = 0, max = 10)
          String dateMeantFor) {

    return getUserBudgetService().fetchAllUserBudget(financialPortfolioId, dateMeantFor);
  }

  // Save User Budgets
  @RequestMapping(
      value = "/save/{financialPortfolioId}",
      method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public UserBudget updateAutoGeneratedUserBudget(
      @PathVariable @Size(min = 0, max = GenericConstants.MAX_ALLOWED_LENGTH_FINANCIAL_PORTFOLIO)
          String financialPortfolioId,
      @RequestBody MultiValueMap<String, String> formData,
      Principal userPrincipal) {

    UserBudget userBudgetResponse =
        getUserBudgetService().saveAutoGeneratedUserBudget(formData, financialPortfolioId);
    return userBudgetResponse;
  }

  // Delete User Budgets
  @RequestMapping(value = "/{financialPortfolioId}/{categoryIds}", method = RequestMethod.DELETE)
  public GenericResponse deleteAutoGeneratedUserBudgetById(
      @PathVariable @Size(min = 0, max = GenericConstants.MAX_ALLOWED_LENGTH_FINANCIAL_PORTFOLIO)
          String financialPortfolioId,
      @PathVariable String categoryIds,
      Principal userPrincipal,
      @RequestParam(DashboardConstants.Budget.DATE_MEANT_FOR) @Size(min = 0, max = 10)
          String dateMeantFor,
      @RequestParam(DashboardConstants.Budget.DELETE_ONLY_AUTO_GENERATED_BUDGET_PARAM)
          Boolean deleteOnlyAutoGenerated) {

    if (deleteOnlyAutoGenerated) {
      getUserBudgetService()
          .deleteAutoGeneratedUserBudgets(categoryIds, financialPortfolioId, dateMeantFor);
    } else {
      getUserBudgetService().deleteUserBudgets(categoryIds, financialPortfolioId, dateMeantFor);
    }

    return new GenericResponse("success");
  }

  // Delete All User Budgets
  @RequestMapping(value = "/{financialPortfolioId}", method = RequestMethod.DELETE)
  public GenericResponse deleteAllUserBudgetById(
      @PathVariable @Size(min = 0, max = GenericConstants.MAX_ALLOWED_LENGTH_FINANCIAL_PORTFOLIO)
          String financialPortfolioId,
      @RequestParam(DashboardConstants.Budget.DATE_MEANT_FOR) @Size(min = 0, max = 10)
          String dateMeantFor,
      @RequestParam(DashboardConstants.Budget.AUTO_GENERATED_BUDGET_PARAM) Boolean autoGenerated) {

    getUserBudgetService().deleteAllUserBudgets(financialPortfolioId, dateMeantFor, autoGenerated);

    return new GenericResponse("success");
  }

  // Update budget in user budget
  @RequestMapping(
      value = "/{financialPortfolioId}/update/{formFieldName}/{dateMeantFor}",
      method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public List<UserBudget> updateAutoGeneratedUserBudgetById(
      @PathVariable @Size(min = 0, max = GenericConstants.MAX_ALLOWED_LENGTH_FINANCIAL_PORTFOLIO)
          String financialPortfolioId,
      @PathVariable @Size(min = 0, max = GenericConstants.MAX_ALLOWED_LENGTH_FINANCIAL_PORTFOLIO)
          String formFieldName,
      @PathVariable @Size(min = 0, max = 10) String dateMeantFor,
      @RequestBody MultiValueMap<String, String> formData) {

    List<UserBudget> userBudgetSaved =
        getUserBudgetService()
            .updateAutoGeneratedBudget(formData, formFieldName, financialPortfolioId, dateMeantFor);

    return userBudgetSaved;
  }

  // Copy all previous budgeted month to the current month
  @RequestMapping(
      value = "/copyPreviousBudget/{financialPortfolioId}",
      method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public List<UserBudget> copyPreviousBudgetById(
      @PathVariable @Size(min = 0, max = GenericConstants.MAX_ALLOWED_LENGTH_FINANCIAL_PORTFOLIO)
          String financialPortfolioId,
      Principal userPrincipal,
      @RequestBody MultiValueMap<String, String> formData) {

    return getUserBudgetService().copyPreviousBudgetToSelectedMonth(financialPortfolioId, formData);
  }

  // Fetch all the dates with the user budget data for the user
  @RequestMapping(
      value = "/fetchAllDatesWithData/{financialPortfolioId}",
      method = RequestMethod.GET)
  public Set<Integer> fetchAllDatesWithUserBudgetById(
      @PathVariable @Size(min = 0, max = GenericConstants.MAX_ALLOWED_LENGTH_FINANCIAL_PORTFOLIO)
          String financialPortfolioId,
      Principal userPrincipal) {

    return getUserBudgetService().fetchAllDatesWithUserBudget(financialPortfolioId);
  }

  // Fetch all the dates with the user budget data for the user
  @RequestMapping(
      value = "/changeCategory/{financialPortfolioId}",
      method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public UserBudget changeCategoryWithUserBudgetById(
      @PathVariable @Size(min = 0, max = GenericConstants.MAX_ALLOWED_LENGTH_FINANCIAL_PORTFOLIO)
          String financialPortfolioId,
      Principal userPrincipal,
      @RequestBody MultiValueMap<String, String> formData) {

    UserBudget userBudgetSaved =
        getUserBudgetService().changeCategoryWithUserBudget(financialPortfolioId, formData);

    return userBudgetSaved;
  }

  /**
   * Delete All User Budget
   *
   * @param financialPortfolioId
   * @param dateMeantFor
   * @param autoGenerated
   * @return
   */
  @RequestMapping(value = "/", method = RequestMethod.DELETE)
  public GenericResponse deleteAllUserBudget(
      @RequestParam @Size(min = 0, max = GenericConstants.MAX_ALLOWED_LENGTH_FINANCIAL_PORTFOLIO)
          String financialPortfolioId) {

    getUserBudgetService().deleteAllUserBudgets(financialPortfolioId);

    return new GenericResponse("success");
  }

  public IUserBudgetService getUserBudgetService() {
    return userBudgetService;
  }
}
