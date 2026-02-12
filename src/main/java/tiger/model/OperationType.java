package tiger.model;

public enum OperationType {
    INCOME("Доход"),
    EXPENSE("Расход");

    private final String title;

    OperationType(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
