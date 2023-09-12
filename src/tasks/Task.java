package tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.time.Duration;

public class Task {
    protected int id;
    protected String title;
    protected String content;
    protected Status status;
    private Type type = Type.TASK;
    protected LocalDateTime startTime;
    protected Duration duration;
    protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public Task(String title, String content, Status status) {
        this.title = title;
        this.content = content;
        this.status = status;
    }

    public Task() {
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Type getType() {
        return type;
    }

    public String getStartTime() {
        return startTime.format(formatter);
    }

    public long getDuration() {
        return duration.toMinutes();
    }

    public String getEndTime() {
        return startTime.plus(duration).format(formatter);
    }

    public DateTimeFormatter getFormatter() {
        return formatter;
    }

    public void setStartTime(String startTime) {
        this.startTime = LocalDateTime.parse(startTime, formatter);
    }

    public void setDuration(long duration) {
        this.duration = Duration.ofMinutes(duration);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", status='" + status + '\'' +
                ", startTime=" + getStartTime() + '\'' +
                ", duration=" + getDuration() + '\'' +
                ", endTime=" + getEndTime() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(title, task.title) && Objects.equals(content, task.content)
                && Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, status);
    }
}
