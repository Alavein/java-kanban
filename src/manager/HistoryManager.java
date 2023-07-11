package manager;

import tasks.Task;

import java.util.List;

public interface HistoryManager {

    void addToHistoryTask(Task task);

    List<Task> getHistory();
}
