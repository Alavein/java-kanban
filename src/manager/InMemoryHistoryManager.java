package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class InMemoryHistoryManager implements HistoryManager {
    private static final int HISTORY_SIZE = 10;
    private List<Task> viewsHistory = new LinkedList<>();

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(viewsHistory);
    }

    @Override
    public void addToHistoryTask(Task task) {
        if (task == null) {
            return;
        }
        if (viewsHistory.size() == HISTORY_SIZE) {
            viewsHistory.remove(0);
        }
        viewsHistory.add(task);
    }
}
