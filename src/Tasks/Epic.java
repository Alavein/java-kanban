package Tasks;

import Tasks.Task;

import java.util.ArrayList;

public class Epic extends Task {

    protected ArrayList<Integer> subTaskId = new ArrayList<>();

    public Epic(String title, String content) {
        super(title, content);
    }

    public ArrayList<Integer> getSubTaskId() {
        return subTaskId;
    }

    public void setSubTaskId(ArrayList<Integer> subTaskId) {
        this.subTaskId = subTaskId;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
