package manager;

import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TaskManager {

    private int nextId = 1;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    public int makeNewTask(Task task) {
        task.setId(nextId);
        nextId++;
        tasks.put(task.getId(), task);
        return (task.getId());
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public ArrayList<Task> getTaskList() {
        return new ArrayList<>(tasks.values());
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public int makeNewEpic(Epic epic) {
        epic.setId(nextId);
        nextId++;
        epics.put(epic.getId(), epic);
        return (epic.getId());
    }

    public ArrayList<Epic> getEpicList() {
        return new ArrayList<>(epics.values());
    }

    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    public void deleteEpicById(int id) {
        Epic epic = epics.remove(id);
        for (Integer subTaskId : epic.getSubTaskId()) {
            subTasks.remove(subTaskId);
        }
    }

    private void updateEpicStatus(Epic epic) {
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

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

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

    public ArrayList<SubTask> getSubtaskList() {
        return new ArrayList<>(subTasks.values());
    }

    public ArrayList<SubTask> getSubtaskByEpicId(int id) {
        ArrayList<SubTask> subtaskByEpicId = new ArrayList<>();
        Epic epic = epics.get(id);

        for (Integer subTaskId : epic.getSubTaskId()) {
            SubTask subTask = subTasks.get(subTaskId);
            subtaskByEpicId.add(subTask);
        }
        return subtaskByEpicId;
    }

    public void deleteAllSubTasks() {
        for (Epic epic : epics.values()) {
            epic.getSubTaskId().clear();
            updateEpicStatus(epic);
        }
        subTasks.clear();
    }

    public SubTask getSubTaskById(int id) {
        return subTasks.get(id);
    }

    public void deleteSubTasksById(int id) {
        for (Epic epic : epics.values()) {
            if (epic.getSubTaskId().contains(id)) {
                epic.getSubTaskId().remove(id);
                updateEpicStatus(epic);
            }
            subTasks.remove(id);
        }
    }

    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);

        int epicId = subTask.getEpicId();
        Epic epic = epics.get(epicId);
        subTask.setStatus(epic.getStatus());
        updateEpicStatus(epic);
    }
}
