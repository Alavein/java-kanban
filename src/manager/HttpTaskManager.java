package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import server.KVTaskClient;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {
    private KVTaskClient client;
    private Gson gson;
    private static final String TASKS_KEY = "tasks";
    private static final String SUBTASKS_KEY = "subtasks";
    private static final String EPICS_KEY = "epics";
    private static final String HISTORY_KEY = "history";

    public HttpTaskManager(String host, int port) {
        super(null);
        this.client = new KVTaskClient(host, port);
        this.gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    }

    @Override
    public void save() {
        String jsonTasks = gson.toJson(getTaskList());
        client.put(TASKS_KEY, jsonTasks);
        String jsonSubTasks = gson.toJson(getSubtaskList());
        client.put(SUBTASKS_KEY, jsonSubTasks);
        String jsonEpics = gson.toJson(getEpicList());
        client.put(EPICS_KEY, jsonEpics);
        List<Integer> historyIds = getHistory().stream()
                .map(Task::getId)
                .collect(Collectors.toList());
        String jsonHistory = gson.toJson(historyIds);
        client.put(HISTORY_KEY, jsonHistory);
    }

    public void load() {
        String jsonTasks = client.load(TASKS_KEY);
        Type taskType = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> tasks1 = gson.fromJson(jsonTasks, taskType);
        for (Task task : tasks1) {
            tasks.put(task.getId(), task);
        }

        String jsonSubTasks = client.load(SUBTASKS_KEY);
        Type subTaskType = new TypeToken<List<SubTask>>() {
        }.getType();
        List<SubTask> subTasks1 = gson.fromJson(jsonSubTasks, subTaskType);
        for (SubTask subTask : subTasks1) {
            subTasks.put(subTask.getId(), subTask);
        }

        String jsonEpics = client.load(EPICS_KEY);
        Type epicType = new TypeToken<List<Epic>>() {
        }.getType();
        List<Epic> epics1 = gson.fromJson(jsonEpics, epicType);
        for (Epic epic : epics1) {
            epics.put(epic.getId(), epic);
        }

        String jsonHistoryIds = client.load(HISTORY_KEY);
        Type historyIdsType = new TypeToken<List<Integer>>() {
        }.getType();
        List<Integer> historyIds = gson.fromJson(jsonHistoryIds, historyIdsType);
        for (Integer id : historyIds) {
            Managers.getDefaultHistory().addToHistoryTask(findTask(id));
        }
    }

    protected Task findTask(Integer id) {
        final Task task = tasks.get(id);
        if (Objects.nonNull(task)) {
            return task;
        }

        final SubTask subTask = subTasks.get(id);
        if (Objects.nonNull(subTask)) {
            return subTask;
        }
        return epics.get(id);
    }
}
