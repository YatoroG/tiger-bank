package tiger.service.facade;

import java.util.Collection;
import org.springframework.stereotype.Component;
import tiger.model.dto.CategoryFields;
import tiger.model.requests.category.*;
import tiger.service.CategoryService;

@Component
public class CategoryFacade {
    private final CategoryService categoryService;

    public CategoryFacade(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public void create(CreateCategoryRequest request) {
        categoryService.createCategory(request.name(), request.type());
    }

    public void updateName(UpdateCategoryNameRequest request) {
        categoryService.updateCategoryName(request.id(), request.newName());
    }

    public void updateType(UpdateCategoryTypeRequest request) {
        categoryService.updateCategoryType(request.id(), request.newType());
    }

    public void delete(DeleteCategoryRequest request) {
        categoryService.deleteCategory(request.id());
    }

    public Collection<CategoryFields> getAll() {
        return categoryService.getAllCategories();
    }

    public CategoryFields getCategory(GetCategoryRequest request) {
        return categoryService.getCategory(request.id());
    }
}
