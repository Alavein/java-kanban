package tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.time.Duration;
import java.time.LocalDateTime;

public class Epic extends Task {

    private ArrayList<Integer> subTaskIds = new ArrayList<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();

    public Epic(String title, String content, Status status) {
        super(title, content, status);
    }

    public ArrayList<Integer> getSubTaskId() {
        return subTaskIds;
    }

    public void setSubTaskId(ArrayList<Integer> subTaskId) {
        this.subTaskIds = subTaskId;
    }

    @Override
    public Type getType() {
        return Type.EPIC;
    }

    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public void putSubTask(int id, SubTask subTask) {
        this.subTasks.put(id, subTask);
    }

    public void countTime() {
        long AllDuration = 0;
        for (SubTask subTask : getSubTasks()) {
            AllDuration += subTask.getDuration();
            if (this.startTime == null || this.startTime.isAfter(subTask.startTime)) {
                this.startTime = LocalDateTime.parse(subTask.getStartTime(), FORMATTER);
            }
        }
        this.duration = Duration.ofMinutes(AllDuration);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", status='" + status + '\'' +
                ", startTime=" + getStartTime() + '\'' +
                ", duration=" + getDuration() + '\'' +
                ", endTime=" + getEndTime() + '\'' +
                '}';
    }
}


