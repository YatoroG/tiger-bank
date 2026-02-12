package tiger.model;

public class Category {
    private static int nextCategoryId = 1;
    private final int categoryId;
    private OperationType operationType;
    private String name;

    public Category(String name, OperationType operationType) {
        this.categoryId = nextCategoryId;
        this.name = name;
        this.operationType = operationType;
        nextCategoryId++;
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

    public void setType(OperationType newType) {
        this.operationType = newType;
    }
}
