package tasks;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subTaskIds = new ArrayList<>();
    private Type type = Type.EPIC;

    public Epic(String title, String content, Status status) {
        super(title, content, status);
    }

    public ArrayList<Integer> getSubTaskId() {
        return subTaskIds;
    }

    public void setSubTaskId(ArrayList<Integer> subTaskId) {
        this.subTaskIds = subTaskId;
    }

    public Type getType() {
        return type;
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


