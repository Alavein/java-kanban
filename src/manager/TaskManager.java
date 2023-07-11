package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.List;

public interface TaskManager {

    int makeNewTask(Task task);

    void updateTask(Task task);

    List<Task> getTaskList();

    void deleteAllTasks();

    Task getTaskById(int id);

    int makeNewEpic(Epic epic);

    List<Epic> getEpicList();

    void deleteAllEpics();

    void deleteEpicById(int id);

    void updateEpicStatus(Epic epic);

    Epic getEpicById(int id);

    Integer makeNewSubTask(SubTask subTask);

    List<SubTask> getSubtaskList();

    List<SubTask> getSubtaskByEpicId(int id);

    void deleteAllSubTasks();

    SubTask getSubTaskById(int id);

    void deleteSubTasksById(int id);

    void updateSubTask(SubTask subTask);

    List<Task> getHistory();
}
