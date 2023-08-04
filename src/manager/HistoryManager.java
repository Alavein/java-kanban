package manager;

import tasks.Task;

import java.util.List;

public interface HistoryManager {

    void addToHistoryTask(Task task);
    void remove(int id);
    List<Task> getHistory();
}
