package manager;

import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    public int makeNewTask(Task task);

    public void updateTask(Task task);

    public List<Task> getTaskList();

    public void deleteAllTasks();

    public Task getTaskById(int id);

    public int makeNewEpic(Epic epic);

    public ArrayList<Epic> getEpicList();

    public void deleteAllEpics();

    public void deleteEpicById(int id);

    public void updateEpicStatus(Epic epic);

    public Epic getEpicById(int id);

    public Integer makeNewSubTask(SubTask subTask);

    public ArrayList<SubTask> getSubtaskList();

    public ArrayList<SubTask> getSubtaskByEpicId(int id);

    public void deleteAllSubTasks();

    public SubTask getSubTaskById(int id);

    public void deleteSubTasksById(int id);

    public void updateSubTask(SubTask subTask);

    public List<Task> getHistory();
}
