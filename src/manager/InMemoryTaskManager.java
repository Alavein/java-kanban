package manager;

import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private int nextId = 1;
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();

    private HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public int makeNewTask(Task task) {
        task.setId(nextId);
        nextId++;
        tasks.put(task.getId(), task);
        return (task.getId());
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public List<Task> getTaskList() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        historyManager.addToHistoryTask(task);
        return tasks.get(id);
    }

    @Override
    public int makeNewEpic(Epic epic) {
        epic.setId(nextId);
        nextId++;
        epics.put(epic.getId(), epic);
        return (epic.getId());
    }

    @Override
    public List<Epic> getEpicList() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.remove(id);
        for (Integer subTaskId : epic.getSubTaskId()) {
            subTasks.remove(subTaskId);
        }
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        ArrayList<Status> epicsStatuses = new ArrayList<>();

        for (Integer subTaskId : epic.getSubTaskId()) {
            SubTask subTask = subTasks.get(subTaskId);

            if (subTask.getStatus() == Status.NEW) {
                epic.setStatus(Status.NEW);
            } else if (subTask.getStatus() == Status.IN_PROGRESS) {
                epic.setStatus(Status.IN_PROGRESS);
                epicsStatuses.add(subTask.getStatus());
            } else if (subTask.getStatus() == Status.DONE) {
                epicsStatuses.add(subTask.getStatus());
            }
        }
        for (Status status : epicsStatuses) {
            int count = 0;
            int size = epicsStatuses.size();

            if (status == Status.DONE) {
                count++;
            }
            if (count == size) {
                epic.setStatus(Status.DONE);
            }
        }
    }

    @Override
    public Epic getEpicById(int id) {
        Task task = (Task) epics.get(id);
        historyManager.addToHistoryTask(task);
        return epics.get(id);
    }

    @Override
    public Integer makeNewSubTask(SubTask subTask) {
        subTask.setId(nextId);
        nextId++;
        subTasks.put(subTask.getId(), subTask);

        Epic epic = epics.get(subTask.getEpicId());
        if (epic == null) {
            return null;
        }
        epic.getSubTaskId().add(subTask.getId());
        updateEpicStatus(epic);
        return (subTask.getId());
    }

    @Override
    public List<SubTask> getSubtaskList() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public List<SubTask> getSubtaskByEpicId(int id) {
        ArrayList<SubTask> subtaskByEpicId = new ArrayList<>();
        Epic epic = epics.get(id);

        for (Integer subTaskId : epic.getSubTaskId()) {
            SubTask subTask = subTasks.get(subTaskId);
            subtaskByEpicId.add(subTask);
        }
        return subtaskByEpicId;
    }

    @Override
    public void deleteAllSubTasks() {
        for (Epic epic : epics.values()) {
            epic.getSubTaskId().clear();
            updateEpicStatus(epic);
        }
        subTasks.clear();
    }

    @Override
    public SubTask getSubTaskById(int id) {
        Task task = (Task) subTasks.get(id);
        historyManager.addToHistoryTask(task);
        return subTasks.get(id);
    }

    @Override
    public void deleteSubTasksById(int id) {
        for (Epic epic : epics.values()) {
            if (epic.getSubTaskId().contains(id)) {
                epic.getSubTaskId().remove(id);
                updateEpicStatus(epic);
            }
            subTasks.remove(id);
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);

        int epicId = subTask.getEpicId();
        Epic epic = epics.get(epicId);
        subTask.setStatus(epic.getStatus());
        updateEpicStatus(epic);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
