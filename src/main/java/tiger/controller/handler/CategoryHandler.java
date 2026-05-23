package tiger.controller.handler;

import java.util.Scanner;
import org.springframework.stereotype.Component;
import tiger.model.OperationType;
import tiger.service.CategoryService;

@Component
public class CategoryHandler {
    private final CategoryService categoryService;
    private final Scanner scanner = new Scanner(System.in);

    public CategoryHandler(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public void handleCategories() {
        System.out.println("1. Создать категорию");
        System.out.println("2. Список категорий");
        int num = Integer.parseInt(scanner.nextLine());

        if (num == 1) {
            System.out.print("Тип категории (1 - Доход, 2 - Расход): ");
            int type = Integer.parseInt(scanner.nextLine());
            System.out.print("Название категории: ");
            String name = scanner.nextLine();

            categoryService.createCategory(name, OperationType.fromInt(type - 1));
            System.out.println("Категория добавлена");
        } else if (num == 2) {
            categoryService.getAllCategories().forEach(c ->
                    System.out.println(c.getCategoryId() + ": [" + c.getCategoryType() + "] " + c.getName()));
        }
    }
}
