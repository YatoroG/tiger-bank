package tiger.model;

public enum OperationType {
    INCOME("Доход"),
    EXPENSE("Расход");

    private String title;

    OperationType() {
    }

    OperationType(String title) {
        this.title = title;
    }

    public static OperationType fromInt(int index) {
        return OperationType.values()[index];
    }

    public static OperationType fromStr(String title) {
        for (OperationType type : values()) {
            if (type.title.equalsIgnoreCase(title.trim())) {
                return type;
            }
        }

        return OperationType.valueOf(title);
    }

    @Override
    public String toString() {
        return title;
    }
}
