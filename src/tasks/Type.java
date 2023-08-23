package tasks;
public enum Type {
    TASK, EPIC, SUBTASK;

    private String type;

    private void GetType(String type) {
        this.type = type;
    }

    public String GetType() {
        return type;
    }

    public static Type getEnumType(String type) {

        switch (type) {
            case "TASK":
                return TASK;
            case "EPIC":
                return EPIC;
            case "SUBTASK":
                return SUBTASK;
            default:
                return null;
        }
    }
}