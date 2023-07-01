package Tasks;

public class SubTask extends Task {
    protected int epicId;

    public SubTask(String title, String content, int epicId) {
        super(title, content);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicId +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
