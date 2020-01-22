package in.co.everyrupee.constants.income;

public class DashboardConstants {

	public static final String ID_SEQ = "id_seq";
	public static final String DATE_FORMAT = "ddMMyyyy";
	public static final String CREATION_DATE = "create_date";
	public static final String MODIFICATION_DATE = "modify_date";
	public static final String DEFAULT_ADD_ROW_QUANTITY = "0";
	public static final String BOOLEAN_DEFAULT_TRUE = "BOOLEAN DEFAULT TRUE";
	public static final String BOOLEAN_FALSE = "false";
	public static final String RECURRENCE_COLUMN_DEFINITION = "varchar(32) default 'NEVER'";

	public class Budget {
		public static final String BUDGET_ID = "budget_id";
		public static final String FINANCIAL_PORTFOLIO_ID = "financial_portfolio_id";
		public static final String CATEGORY_ID = "category_id";
		public static final String PLANNED = "planned";
		public static final String AUTO_GENERATED_BUDGET = "auto_generated_budget";
		public static final String USER_BUDGET_TABLE = "user_budget";
		public static final String BUDGET_CACHE_NAME = "userBudget";
		public static final String AUTO_GENERATED_JSON = "autoGenerated";
		public static final String BUDGET_ID_JSON = "budgetId";
		public static final String DATE_MEANT_FOR = "dateMeantFor";
		public static final String AUTO_GENERATED_BUDGET_PARAM = "autoGenerated";
		public static final String CATEGORY_ID_JSON = "categoryId";
		public static final String RECURRENCE = "recurrence";
		public static final String DELETE_ONLY_AUTO_GENERATED_BUDGET_PARAM = "deleteOnlyAutoGenerated";
		public static final String DATE_TO_COPY = "dateToCopy";
		public static final String NEW_CATEGORY_ID = "newCategoryId";
	}

	public class Category {
		public static final String CATEGORY_TABLE = "category";
		public static final String CATEGORY_ID = "category_id";
		public static final String CATEGORY_NAME = "category_name";
		public static final String PARENT_CATEGORY = "parent_category";
		public static final String CATEGORY_CACHE_NAME = "categories";
		public static final String CATEGORY_ID_JSON = "categoryId";
		public static final String CATEGORY_INCOME_OR_NOT = "categoryIncomePrediction";
		public static final String INCOME_CATEGORY_ID = "2";
		public static final String EXPENSE_CATEGORY_ID = "1";
	}

	public class Transactions {
		public static final String TRANSACTIONS_TABLE = "user_transactions";
		public static final String TRANSACTIONS_ID = "transaction_id";
		public static final String DESCRIPTION = "description";
		public static final String CATEGORY_ID = "category_id";
		public static final String FINANCIAL_PORTFOLIO_ID = "financial_portfolio_id";
		public static final String AMOUNT = "amount";
		public static final String TRANSACTIONS__ID_JSON = "transactionId";
		public static final String CATEGORY_FORM_FIELD_NAME = "category";
		public static final String CATEGORY_ID_JSON = "categoryId";
		public static final String AMOUNT_FIELD_NAME = "transaction";
		public static final String TRANSACTIONS_CACHE_NAME = "userTransaction";
		public static final String CATEGORY_OPTIONS = "categoryOptions";
		public static final String TRANSACTIONS_AMOUNT = "amount";
		public static final String DATE_MEANT_FOR = "dateMeantFor";
		public static final String RECURRENCE = "recurrence";
		public static final String UPDATE_BUDGET_PARAM = "updateBudget";
		public static final String ACCOUNT_ID = "accountId";
	}

	public class Overview {
		public static final String PARENT_CATEGORY_ID_PARAM = "parentCategoryId";
		public static final String TYPE_PARAM = "type";
		public static final String AVERAGE_PARAM = "average";
		public static final String DATE_MEANT_FOR = "dateMeantFor";
		public static final String FINANCIAL_PORTFOLIO_ID = "financialPortfolioId";
	}
}
