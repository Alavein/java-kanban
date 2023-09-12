package tasks;

public class SubTask extends Task {
    private int epicId;
    private Type type = Type.SUBTASK;

    public SubTask(String title, String content, Status status, int epicId) {
        super(title, content, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicId +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", status='" + status + '\'' +
                ", startTime=" + getStartTime() + '\'' +
                ", duration=" + getDuration() + '\'' +
                ", endTime=" + getEndTime() + '\'' +
                '}';
    }
}
