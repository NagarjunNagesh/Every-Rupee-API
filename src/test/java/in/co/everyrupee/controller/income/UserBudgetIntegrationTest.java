package in.co.everyrupee.controller.income;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import in.co.everyrupee.constants.income.DashboardConstants;
import in.co.everyrupee.pojo.income.UserBudget;
import in.co.everyrupee.repository.income.UserBudgetRepository;
import in.co.everyrupee.service.income.IUserTransactionService;

/**
 * User Budget Controller Test (Cache, Controller. Service)
 * 
 * @author Nagarjun
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserBudgetIntegrationTest {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	@MockBean
	private UserBudgetRepository userBudgetRepository;

	@MockBean
	private IUserTransactionService userTransactionService;

	@Autowired
	CacheManager cacheManager;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	private Date dateMeantFor;

	private List<Integer> categoryIdList;

	private List<UserBudget> userBudgetList;

	private List<String> cacheObjectKey;

	private static final String FINANCIAL_PORTFOLIO_ID = "20102019165756359";

	private static final String DATE_MEANT_FOR = "01062019";

	@Before
	public void setUp() {
		setUserBudgetList(new ArrayList<UserBudget>());
		UserBudget userBudget = new UserBudget();

		setMvc(MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build());
		DateFormat format = new SimpleDateFormat(DashboardConstants.DATE_FORMAT, Locale.ENGLISH);

		try {
			setDateMeantFor(format.parse(DATE_MEANT_FOR));
		} catch (ParseException e) {
			logger.error(e + " Unable to add date to the user budget");
		}

		// Set Category Id List
		setCategoryIdList(new ArrayList<Integer>());
		getCategoryIdList().add(3);
		getCategoryIdList().add(4);
		getCategoryIdList().add(5);
		getCategoryIdList().add(6);

		// sets a user budget for user budget list
		userBudget.setFinancialPortfolioId(FINANCIAL_PORTFOLIO_ID);
		userBudget.setCategoryId(3);
		userBudget.setPlanned(300);
		userBudget.setAutoGeneratedBudget(true);

		// Appends the above created user budget to the list
		getUserBudgetList().add(userBudget);

		setCacheObjectKey(new ArrayList<String>());
		getCacheObjectKey().add(FINANCIAL_PORTFOLIO_ID);
		getCacheObjectKey().add(DATE_MEANT_FOR);

		// Testing the Cache Layer
		when(getUserBudgetRepository().fetchAllUserBudget(FINANCIAL_PORTFOLIO_ID, getDateMeantFor()))
				.thenReturn(getUserBudgetList());

		when(userTransactionService.fetchUserTransactionCategoryTotal(FINANCIAL_PORTFOLIO_ID, 3, getDateMeantFor()))
				.thenReturn(300d);

	}

	/**
	 * TEST: Get user Budget by financial portfolio Id
	 * 
	 * @throws Exception
	 */
	@WithMockUser(value = "spring")
	@Test
	public void getUserBudgetByFinancialPortfolioId() throws Exception {

		getMvc().perform(
				get("/api/budget/20102019165756359?dateMeantFor=01062019").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.*").isNotEmpty())
				.andExpect(jsonPath("$[0].financialPortfolioId", is(FINANCIAL_PORTFOLIO_ID)));

		// Testing the Cache Layer
		getMvc().perform(
				get("/api/budget/20102019165756359?dateMeantFor=01062019").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.*").isNotEmpty())
				.andExpect(jsonPath("$[0].financialPortfolioId", is(FINANCIAL_PORTFOLIO_ID)));

		// Making Sure the Cache was used
		verify(getUserBudgetRepository(), times(1)).fetchAllUserBudget(FINANCIAL_PORTFOLIO_ID, getDateMeantFor());
		// Ensuring that the cache contains the said values
		assertThat(getCacheManager().getCache(DashboardConstants.Budget.BUDGET_CACHE_NAME).get(getCacheObjectKey()),
				is(notNullValue()));

	}

	/**
	 * TEST: Delete user Budget by category Id
	 * 
	 * @throws Exception
	 */
	@WithMockUser(value = "spring")
	@Test
	public void deleteAutoGeneratedUserBudgetById() throws Exception {

		// Making sure that the cache evict is functioning as expected by calling the
		// fetch before and after
		getMvc().perform(
				get("/api/budget/20102019165756359?dateMeantFor=01062019").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.*").isNotEmpty())
				.andExpect(jsonPath("$[0].financialPortfolioId", is(FINANCIAL_PORTFOLIO_ID)));

		// Ensuring that the cache contains the said values
		assertThat(getCacheManager().getCache(DashboardConstants.Budget.BUDGET_CACHE_NAME).get(getCacheObjectKey()),
				is(notNullValue()));

		getMvc().perform(
				delete("/api/budget/20102019165756359/3,4,5,6?dateMeantFor=01062019&deleteOnlyAutoGenerated=true")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.*").isNotEmpty())
				.andExpect(jsonPath("$.message", is("success")));

		verify(getUserBudgetRepository(), times(1)).deleteAutoGeneratedUserBudgetWithCategoryIds(getCategoryIdList(),
				FINANCIAL_PORTFOLIO_ID, getDateMeantFor());

		// Ensuring that the cache is evicted
		assertNull(getCacheManager().getCache(DashboardConstants.Budget.BUDGET_CACHE_NAME).get(getCacheObjectKey()));

		// Making Sure the Cache was evicted
		verify(getUserBudgetRepository(), times(1)).fetchAllUserBudget(FINANCIAL_PORTFOLIO_ID, getDateMeantFor());

		// Making sure that the cache evict is functioning as expected by calling the
		// fetch before and after
		getMvc().perform(
				get("/api/budget/20102019165756359?dateMeantFor=01062019").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.*").isNotEmpty())
				.andExpect(jsonPath("$[0].financialPortfolioId", is(FINANCIAL_PORTFOLIO_ID)));

		// Ensuring that the cache contains the said values
		assertThat(getCacheManager().getCache(DashboardConstants.Budget.BUDGET_CACHE_NAME).get(getCacheObjectKey()),
				is(notNullValue()));

		getMvc().perform(
				delete("/api/budget/20102019165756359/3,4,5,6?dateMeantFor=01062019&deleteOnlyAutoGenerated=false")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.*").isNotEmpty())
				.andExpect(jsonPath("$.message", is("success")));

		verify(getUserBudgetRepository(), times(1)).deleteUserBudgetWithCategoryIds(getCategoryIdList(),
				FINANCIAL_PORTFOLIO_ID, getDateMeantFor());

		// Ensuring that the cache is evicted
		assertNull(getCacheManager().getCache(DashboardConstants.Budget.BUDGET_CACHE_NAME).get(getCacheObjectKey()));

	}

	/**
	 * TEST: Delete all entries from user Budget with financial portfolio id as
	 * ${id}
	 * 
	 * @throws Exception
	 */
	@WithMockUser(value = "spring")
	@Test
	public void deleteAllUserBudgetById() throws Exception {

		// Making sure that the cache evict is functioning as expected by calling the
		// fetch before and after
		getMvc().perform(
				get("/api/budget/20102019165756359?dateMeantFor=01062019").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.*").isNotEmpty())
				.andExpect(jsonPath("$[0].financialPortfolioId", is(FINANCIAL_PORTFOLIO_ID)));

		// Ensuring that the cache contains the said values
		assertThat(getCacheManager().getCache(DashboardConstants.Budget.BUDGET_CACHE_NAME).get(getCacheObjectKey()),
				is(notNullValue()));

		// Autogenerated true
		getMvc().perform(delete("/api/budget/20102019165756359?dateMeantFor=01062019&autoGenerated=true")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.*").isNotEmpty()).andExpect(jsonPath("$.message", is("success")));

		verify(getUserBudgetRepository(), times(1)).deleteAllUserBudget(FINANCIAL_PORTFOLIO_ID, getDateMeantFor(),
				true);

		// Ensuring that the cache is evicted
		assertNull(getCacheManager().getCache(DashboardConstants.Budget.BUDGET_CACHE_NAME).get(getCacheObjectKey()));

		// Making sure that the cache evict is functioning as expected by calling the
		// fetch before and after
		getMvc().perform(
				get("/api/budget/20102019165756359?dateMeantFor=01062019").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.*").isNotEmpty())
				.andExpect(jsonPath("$[0].financialPortfolioId", is(FINANCIAL_PORTFOLIO_ID)));

		// Ensuring that the cache contains the said values
		assertThat(getCacheManager().getCache(DashboardConstants.Budget.BUDGET_CACHE_NAME).get(getCacheObjectKey()),
				is(notNullValue()));

		// Making Sure the Cache was used
		verify(getUserBudgetRepository(), times(1)).fetchAllUserBudget(FINANCIAL_PORTFOLIO_ID, getDateMeantFor());

		// Autogenerated false
		getMvc().perform(delete("/api/budget/20102019165756359?dateMeantFor=01062019&autoGenerated=false")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.*").isNotEmpty()).andExpect(jsonPath("$.message", is("success")));

		verify(getUserBudgetRepository(), times(1)).deleteAllUserBudget(FINANCIAL_PORTFOLIO_ID, getDateMeantFor(),
				false);

		// Ensuring that the cache is evicted
		assertNull(getCacheManager().getCache(DashboardConstants.Budget.BUDGET_CACHE_NAME).get(getCacheObjectKey()));

		// Making Sure the Cache was used
		verify(getUserBudgetRepository(), times(1)).fetchAllUserBudget(FINANCIAL_PORTFOLIO_ID, getDateMeantFor());

		// Autogenerated null
		getMvc().perform(delete("/api/budget/20102019165756359?dateMeantFor=01062019&autoGenerated=")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.*").isNotEmpty()).andExpect(jsonPath("$.message", is("success")));

		verify(getUserBudgetRepository(), times(1)).deleteAllUserBudget(FINANCIAL_PORTFOLIO_ID, getDateMeantFor());

	}

	/**
	 * TEST: update user Budget by category Id without form data
	 * 
	 * @throws Exception
	 */
	@WithMockUser(value = "spring")
	@Test
	public void updateAutoGeneratedUserBudgetById() throws Exception {

		RequestBuilder request = MockMvcRequestBuilders
				.post("/api/budget/20102019165756359/update/autoGenerated/01062019").param("2", "300").param("3", "400")
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);

		getMvc().perform(request).andExpect(status().isOk());

		verify(getUserTransactionService(), times(1)).fetchCategoryTotalAndUpdateUserBudget("20102019165756359",
				"01062019", false);

		List<Integer> categoryIds = new ArrayList<Integer>();
		categoryIds.add(2);
		categoryIds.add(3);
		verify(getUserBudgetRepository(), times(1)).fetchAutoGeneratedUserBudgetWithCategoryIds(categoryIds,
				"20102019165756359", getDateMeantFor());

	}

	/**
	 * TEST: save user Budget by category Id
	 * 
	 * @throws Exception
	 */
	@WithMockUser(value = "spring")
	@Test
	public void update() throws Exception {

		// Making sure that the cache evict is functioning as expected by calling the
		// fetch before and after
		getMvc().perform(
				get("/api/budget/20102019165756359?dateMeantFor=01062019").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.*").isNotEmpty())
				.andExpect(jsonPath("$[0].financialPortfolioId", is(FINANCIAL_PORTFOLIO_ID)));

		// Ensuring that the cache contains the said values
		assertThat(getCacheManager().getCache(DashboardConstants.Budget.BUDGET_CACHE_NAME).get(getCacheObjectKey()),
				is(notNullValue()));

		List<Integer> categoryList = new ArrayList<Integer>();
		categoryList.add(3);

		RequestBuilder request = MockMvcRequestBuilders.post("/api/budget/save/20102019165756359")
				.accept(MediaType.APPLICATION_JSON).param(DashboardConstants.Budget.PLANNED, "300")
				.param(DashboardConstants.Budget.CATEGORY_ID, "3")
				.param(DashboardConstants.Budget.FINANCIAL_PORTFOLIO_ID, FINANCIAL_PORTFOLIO_ID)
				.param(DashboardConstants.Budget.DATE_MEANT_FOR, "01062019")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);

		getMvc().perform(request).andExpect(status().isOk());

		verify(getUserBudgetRepository(), times(1)).findById(new BigInteger("20102019166818381"));

		// Ensuring that the cache is evicted
		assertNull(getCacheManager().getCache(DashboardConstants.Budget.BUDGET_CACHE_NAME).get(getCacheObjectKey()));

		// Making sure that the cache evict is functioning as expected by calling the
		// fetch before and after
		getMvc().perform(
				get("/api/budget/20102019165756359?dateMeantFor=01062019").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.*").isNotEmpty())
				.andExpect(jsonPath("$[0].financialPortfolioId", is(FINANCIAL_PORTFOLIO_ID)));

		// Ensuring that the cache contains the said values
		assertThat(getCacheManager().getCache(DashboardConstants.Budget.BUDGET_CACHE_NAME).get(getCacheObjectKey()),
				is(notNullValue()));

		// Making Sure the Cache was used
		verify(getUserBudgetRepository(), times(2)).fetchAllUserBudget(FINANCIAL_PORTFOLIO_ID, getDateMeantFor());

	}

	/**
	 * TEST: fetch all dates with user budget by id
	 * 
	 * @throws Exception
	 */
	@WithMockUser(value = "spring")
	@Test
	public void fetchAllDatesWithUserBudgetById() throws Exception {

		List<Date> newDates = new ArrayList<Date>();
		newDates.add(new Date());
		when(getUserBudgetRepository().findAllDatesByFPId(FINANCIAL_PORTFOLIO_ID)).thenReturn(newDates);

		getMvc().perform(
				get("/api/budget/fetchAllDatesWithData/20102019165756359").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.*").isNotEmpty());

		verify(getUserBudgetRepository(), times(1)).findAllDatesByFPId(FINANCIAL_PORTFOLIO_ID);

	}

	/**
	 * TEST: copy previous budgets by id
	 * 
	 * @throws Exception
	 */
	@WithMockUser(value = "spring")
	@Test
	public void copyPreviousBudgetById() throws Exception {

		// Forbidden HTTP request
		RequestBuilder request = MockMvcRequestBuilders.post("/api/budget/copyPreviousBudget/20102019165756359")
				.accept(MediaType.APPLICATION_JSON).param(DashboardConstants.Budget.DATE_MEANT_FOR, DATE_MEANT_FOR)
				.param(DashboardConstants.Budget.DATE_TO_COPY, "01042019")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);

		getMvc().perform(request).andExpect(status().isForbidden());

		// Not Acceptable Http request
		RequestBuilder requestSameDate = MockMvcRequestBuilders.post("/api/budget/copyPreviousBudget/20102019165756359")
				.accept(MediaType.APPLICATION_JSON).param(DashboardConstants.Budget.DATE_MEANT_FOR, DATE_MEANT_FOR)
				.param(DashboardConstants.Budget.DATE_TO_COPY, DATE_MEANT_FOR)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);

		getMvc().perform(requestSameDate).andExpect(status().isNotAcceptable());
	}

	/**
	 * TEST: change Category with user budget id
	 * 
	 * @throws Exception
	 */
	@WithMockUser(value = "spring")
	@Test
	public void changeCategoryWithUserBudgetById() throws Exception {
		// Not Acceptable Http request
		RequestBuilder requestSameDate = MockMvcRequestBuilders.post("/api/budget/changeCategory/20102019165756359")
				.accept(MediaType.APPLICATION_JSON).param(DashboardConstants.Budget.DATE_MEANT_FOR, DATE_MEANT_FOR)
				.param(DashboardConstants.Budget.CATEGORY_ID, "3").param(DashboardConstants.Budget.NEW_CATEGORY_ID, "4")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);

		getMvc().perform(requestSameDate).andExpect(status().isOk());

	}

	/**
	 * TEST: Delete user Budget by financial portfolio Id
	 * 
	 * @throws Exception
	 */
	@WithMockUser(value = "spring")
	@Test
	public void deleteUserBudget() throws Exception {
		getMvc().perform(delete("/api/budget/")
				.param(DashboardConstants.Overview.FINANCIAL_PORTFOLIO_ID, FINANCIAL_PORTFOLIO_ID.toString())
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$").isNotEmpty());

		verify(getUserBudgetRepository(), times(1)).deleteAllUserBudgets(Mockito.anyString());
	}

	private UserBudgetRepository getUserBudgetRepository() {
		return userBudgetRepository;
	}

	private Date getDateMeantFor() {
		return dateMeantFor;
	}

	private void setDateMeantFor(Date dateMeantFor) {
		this.dateMeantFor = dateMeantFor;
	}

	private List<Integer> getCategoryIdList() {
		return categoryIdList;
	}

	private void setCategoryIdList(List<Integer> categoryIdList) {
		this.categoryIdList = categoryIdList;
	}

	private List<UserBudget> getUserBudgetList() {
		return userBudgetList;
	}

	private void setUserBudgetList(List<UserBudget> userBudgetList) {
		this.userBudgetList = userBudgetList;
	}

	private MockMvc getMvc() {
		return mvc;
	}

	private void setMvc(MockMvc mvc) {
		this.mvc = mvc;
	}

	private CacheManager getCacheManager() {
		return cacheManager;
	}

	private List<String> getCacheObjectKey() {
		return cacheObjectKey;
	}

	private void setCacheObjectKey(List<String> cacheObjectKey) {
		this.cacheObjectKey = cacheObjectKey;
	}

	public IUserTransactionService getUserTransactionService() {
		return userTransactionService;
	}

}
