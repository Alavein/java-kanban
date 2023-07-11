package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> viewsHistory = new LinkedList<>();

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(viewsHistory);
    }

    @Override
    public void addToHistoryTask(Task task) {
        if (viewsHistory.size() == 10) {
            viewsHistory.remove(0);
        }
        viewsHistory.add(task);
    }
}
