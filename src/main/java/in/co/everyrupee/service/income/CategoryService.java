package in.co.everyrupee.service.income;

import in.co.everyrupee.constants.GenericConstants;
import in.co.everyrupee.constants.income.DashboardConstants;
import in.co.everyrupee.exception.ResourceNotFoundException;
import in.co.everyrupee.pojo.income.Category;
import in.co.everyrupee.repository.income.CategoryRepository;
import in.co.everyrupee.utils.ERStringUtils;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class CategoryService implements ICategoryService {

  @Autowired private CategoryRepository categoryRepository;

  Logger LOGGER = LoggerFactory.getLogger(this.getClass());

  @Override
  @Cacheable(value = DashboardConstants.Category.CATEGORY_CACHE_NAME, key = "#root.method.name")
  public List<Category> fetchCategories() {
    List<Category> categoriesList = categoryRepository.fetchAllCategories();

    if (CollectionUtils.isEmpty(categoriesList)) {
      throw new ResourceNotFoundException("Category", "categories", "all");
    }
    return categoriesList;
  }

  @Override
  @Cacheable(value = DashboardConstants.Category.CATEGORY_INCOME_OR_NOT, key = "#categoryId")
  public Boolean categoryIncome(int categoryId) {
    List<Category> categoriesList = categoryRepository.fetchAllCategories();

    if (CollectionUtils.isEmpty(categoriesList)) {
      throw new ResourceNotFoundException("Category", "categories", "all");
    }

    Optional<Category> category =
        categoriesList.stream().filter(x -> categoryId == x.getCategoryId()).findFirst();

    if (category.isPresent()) {
      Category currentCategory = category.get();
      boolean categoryIncome =
          ERStringUtils.equalsIgnoreCase(
              currentCategory.getParentCategory(), GenericConstants.INCOME_CATEGORY);
      LOGGER.debug(
          "Category to search is "
              + categoryId
              + " which is a category income - "
              + categoryIncome);
      if (categoryIncome) {
        return true;
      }
      return false;
    }

    return false;
  }
}
