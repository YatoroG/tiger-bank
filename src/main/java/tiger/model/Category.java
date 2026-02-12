package tiger.model;

public class Category {
    private final int categoryId;
    private OperationType operationType;
    private String name;

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

    public void setType(OperationType newType) {
        this.operationType = newType;
    }
}
