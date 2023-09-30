package manager;

import server.HttpTaskManager;
import server.KVServer;
import server.LocalDateTimeAdapter;
import tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;



public class Managers {
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting().serializeNulls().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        return gsonBuilder.create();
    }

    public static FileBackedTasksManager getDefaultFileBacked(String file) {
        return new FileBackedTasksManager(file);
    }

    public static HttpTaskManager getDefaultHttpTask(){
        return new HttpTaskManager("http://localhost:", KVServer.PORT);
    }
}