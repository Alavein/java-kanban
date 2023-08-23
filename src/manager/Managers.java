package manager;

import tasks.Task;

public class Managers {
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault() {
        return new FileBackedTasksManager("/Users/canta/dev/java-kanban/tasks.txt");
    }

}
