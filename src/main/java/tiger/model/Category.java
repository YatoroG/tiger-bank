package tiger.model;

import tiger.model.dto.CategoryFields;

public class Category {
    private int categoryId;
    private OperationType operationType;
    private String name;

    public Category() {
    }

    public Category(int categoryId, String name, OperationType operationType) {
        this.categoryId = categoryId;
        this.name = name;
        this.operationType = operationType;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public OperationType getCategoryType() {
        return operationType;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void setCategoryType(OperationType newType) {
        this.operationType = newType;
    }

    public CategoryFields splitCategory() {
        return new CategoryFields(this.categoryId, this.name, this.operationType);
    }
}
