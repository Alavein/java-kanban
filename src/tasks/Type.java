package tasks;
public enum Type {
    TASK, EPIC, SUBTASK;

    private String type;

    private void getTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}