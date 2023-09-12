package tests;

import manager.HistoryManager;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;
    protected Task task;
    protected Epic epic;
    protected SubTask subTask;
    protected HistoryManager historyManager;

    @Test
    void shouldMakeNewTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        final int taskId = taskManager.makeNewTask(task);

        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTaskList();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void shouldUpdateTask() {
        Task task = new Task("Task title", "Task content", Status.NEW);
        final Map<Integer, Task> tasks = new HashMap<>();
        tasks.put(task.getId(), task);

        assertEquals(1, tasks.size(), "Неверное количество задач.");


    }

    @Test
    void shouldGetTaskList() {
        Task task = new Task("Task title", "Task content", Status.NEW);
        taskManager.makeNewTask(task);
        final ArrayList<Task> tasksList = new ArrayList<>();
        tasksList.add(task);

        assertEquals(1, tasksList.size(), "Неверное количество задач.");
    }

    @Test
    void shouldDeleteAllTasks() {
        Task task = new Task("Task title", "Task content", Status.NEW);
        taskManager.makeNewTask(task);
        final ArrayList<Task> tasksList = new ArrayList<>();
        tasksList.add(task);
        tasksList.clear();

        assertEquals(0, tasksList.size(), "Задачи не были удалены.");
    }

    @Test
    void shouldGetTaskById() {
        Task task = new Task("Task title", "Task content", Status.NEW);
        taskManager.makeNewTask(task);
        final int id = taskManager.makeNewTask(task);
        final HashMap<Integer, Task> tasksList = new HashMap<>();
        tasksList.put(id, task);

        assertNotNull(tasksList.get(id), "Задачи на возвращаются.");
    }

    @Test
    void shouldMakeNewEpic() {
        Epic epic = new Epic("Epic title", "Epic content", Status.NEW);
        taskManager.makeNewEpic(epic);
        final Epic savedEpic = taskManager.getEpicById(epic.getId());

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        final List<Epic> epics = taskManager.getEpicList();

        assertNotNull(epics, "Задачи на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
    }

    @Test
    void shouldGetEpicList() {
        Epic epic = new Epic("Epic title", "Epic content", Status.NEW);
        taskManager.makeNewEpic(epic);
        final ArrayList<Epic> epicsList = new ArrayList<>();
        epicsList.add(epic);

        assertEquals(1, epicsList.size(), "Неверное количество задач.");
    }

    @Test
    void shouldDeleteAllEpics() {
        Epic epic = new Epic("Epic title", "Epic content", Status.NEW);
        taskManager.makeNewEpic(epic);
        final ArrayList<Epic> epicsList = new ArrayList<>();
        epicsList.add(epic);
        epicsList.clear();

        assertEquals(0, epicsList.size(), "Задачи не были удалены.");
    }

    @Test
    void shouldDeleteEpicById() {
        Epic epic = new Epic("Epic title", "Epic content", Status.NEW);
        taskManager.makeNewEpic(epic);
        final int id = taskManager.makeNewEpic(epic);
        final HashMap<Integer, Epic> epicsList = new HashMap<>();
        epicsList.put(id, epic);
        epicsList.remove(id);

        assertEquals(0, epicsList.size(), "Задачи не были удалены.");
    }

    @Test
    void shouldUpdateEpicStatus() {
        Epic epic = new Epic("Epic title", "Epic content", Status.NEW);
        taskManager.makeNewEpic(epic);
        epic.setStatus(Status.DONE);

        assertEquals(Status.DONE, epic.getStatus(), "Статус не изменился.");

    }

    @Test
    void shouldGetEpicById() {
        Epic epic = new Epic("Epic title", "Epic content", Status.NEW);
        taskManager.makeNewEpic(epic);
        final int id = taskManager.makeNewEpic(epic);
        final HashMap<Integer, Epic> epicsList = new HashMap<>();
        epicsList.put(id, epic);

        assertNotNull(epicsList.get(id), "Задачи на возвращаются.");
    }

    @Test
    void shouldMakeNewSubTask() {
        SubTask subTask = new SubTask("SubTask title", "SubTask content", Status.NEW, 1);
        taskManager.makeNewSubTask(subTask);
        final SubTask savedSubTask = taskManager.getSubTaskById(subTask.getId());

        assertNotNull(savedSubTask, "Задача не найдена.");
        assertEquals(subTask, savedSubTask, "Задачи не совпадают.");

        final List<SubTask> subTasks = taskManager.getSubtaskList();

        assertNotNull(subTasks, "Задачи на возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество задач.");
    }

    @Test
    void shouldGetSubtaskList() {
        SubTask subTask = new SubTask("SubTask title", "SubTask content", Status.NEW, 1);
        SubTask subTask1 = new SubTask("SubTask title", "SubTask content", Status.NEW, 2);
        taskManager.makeNewSubTask(subTask);
        taskManager.makeNewSubTask(subTask1);
        final ArrayList<SubTask> subTaskList = new ArrayList<>();
        subTaskList.add(subTask);
        subTaskList.add(subTask1);

        assertEquals(2, subTaskList.size(), "Неверное количество задач.");
    }

    @Test
    void shouldGetSubtaskByEpicId() {
        Epic epic = new Epic("Epic title", "Epic content", Status.NEW);
        taskManager.makeNewEpic(epic);
        final int id = taskManager.makeNewEpic(epic);
        SubTask subTask = new SubTask("SubTask title", "SubTask content", Status.NEW, id);
        taskManager.makeNewSubTask(subTask);
        final HashMap<Integer, SubTask> subTaskList = new HashMap<>();
        subTaskList.put(id, subTask);

        assertNotNull(subTaskList.get(id), "Задача на возвращается.");
        assertEquals(subTaskList.get(id), subTask, "Задачи не совпадают.");

    }

    @Test
    void shouldDeleteAllSubTasks() {
        SubTask subTask = new SubTask("SubTask title", "SubTask content", Status.NEW, 1);
        SubTask subTask1 = new SubTask("SubTask title", "SubTask content", Status.NEW, 2);
        taskManager.makeNewSubTask(subTask);
        taskManager.makeNewSubTask(subTask1);
        final ArrayList<SubTask> subTaskList = new ArrayList<>();
        subTaskList.add(subTask);
        subTaskList.add(subTask1);
        subTaskList.clear();

        assertEquals(0, subTaskList.size(), "Задачи не были удалены.");
    }

    @Test
    void shouldGetSubTaskById() {
        SubTask subTask = new SubTask("SubTask title", "SubTask content", Status.NEW, 1);
        taskManager.makeNewSubTask(subTask);
        final HashMap<Integer, SubTask> subTaskList = new HashMap<>();
        subTaskList.put(subTask.getId(), subTask);

        assertNotNull(subTaskList.get(subTask.getId()), "Задача на возвращается.");
    }

    @Test
    void shouldDeleteSubTasksById() {
        SubTask subTask = new SubTask("SubTask title", "SubTask content", Status.NEW, 1);
        SubTask subTask1 = new SubTask("SubTask title", "SubTask content", Status.NEW, 2);
        taskManager.makeNewSubTask(subTask);
        taskManager.makeNewSubTask(subTask1);
        final HashMap<Integer, SubTask> subTaskList = new HashMap<>();
        subTaskList.put(subTask.getId(), subTask);
        subTaskList.put(subTask1.getId(), subTask1);
        subTaskList.remove(subTask1.getId());

        assertEquals(1, subTaskList.size(), "Задача не была удалена.");


    }

    @Test
    void shouldUpdateSubTask() {
        SubTask subTask = new SubTask("SubTask title", "SubTask content", Status.NEW, 1);
        final Map<Integer, SubTask> subTasks = new HashMap<>();
        subTasks.put(subTask.getId(), subTask);

        assertEquals(1, subTasks.size(), "Неверное количество задач.");

    }

    @Test
    void shouldAddToHistory() {
        Task task = new Task("Task title", "Task content", Status.NEW);
        taskManager.makeNewTask(task);
        historyManager.addToHistoryTask(task);

        final List<Task> history = historyManager.getHistory();

        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

}
