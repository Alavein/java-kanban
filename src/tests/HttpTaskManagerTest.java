package tests;

import manager.Managers;
import manager.HttpTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import server.KVServer;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.Status;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    private KVServer server;
    private HttpTaskServer taskServer;

    @BeforeEach
    void setUp() throws IOException {
        server = new KVServer();
        server.start();
        taskServer = new HttpTaskServer();
        taskServer.start();
        taskManager = Managers.getDefaultHttpTask();
    }

    @AfterEach
    void afterEach() {
        server.stop(0);
        taskServer.stop(0);
    }

    @Test
    void shouldLoadFromServer() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        taskManager.makeNewTask(task);
        Epic epic = new Epic("Epic title", "Epic content", Status.NEW);
        taskManager.makeNewEpic(epic);
        SubTask subTask = new SubTask("SubTask title", "SubTask content", Status.NEW, 2);
        taskManager.makeNewSubTask(subTask);

        HttpTaskManager manager1 = Managers.getDefaultHttpTask();
        assertEquals(0, manager1.getEpicList().size(), "Список задач типа Epic должен быть пуст.");
        manager1.load();
        assertNotNull(manager1.getSubtaskList(),
                "Список задач типа Subtask не должен быть пустым.");
        assertEquals(taskManager.getTaskById(task.getId()), manager1.getTaskById(task.getId()),
                "Задачи типа Task должны совпадать.");
        assertEquals(taskManager.getSubTaskById(subTask.getId()), manager1.getSubTaskById(subTask.getId()),
                "Задачи типа Subtask должны совпадать.");
        assertEquals(taskManager.getEpicById(epic.getId()), manager1.getEpicById(epic.getId()),
                "Задачи типа Epic должны совпадать.");
    }
}

