package manager;

import server.KVServer;

public class Managers {
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static FileBackedTasksManager getDefaultFileBacked(String file) {
        return new FileBackedTasksManager(file);
    }

    public static HttpTaskManager getDefaultHttpTask(){
        return new HttpTaskManager("http://localhost:", KVServer.PORT);
    }
}