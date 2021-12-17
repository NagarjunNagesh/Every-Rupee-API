package in.co.everyrupee.service.income;

import in.co.everyrupee.pojo.income.Category;
import java.util.List;

public interface ICategoryService {
  List<Category> fetchCategories();

  Boolean categoryIncome(int categoryId);
}
