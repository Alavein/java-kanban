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
}