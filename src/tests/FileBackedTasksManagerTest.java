package tests;

import manager.FileBackedTasksManager;
import manager.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static manager.FileBackedTasksManager.loadFromFile;
import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    protected String path = "/Users/canta/dev/java-kanban/src/resources/tests.txt";
    protected File file = new File(path);

    @BeforeEach
    void setUp() {
        taskManager = new FileBackedTasksManager(path);
    }

    @AfterEach
    public void afterEach() throws IOException {
            Files.delete(Path.of(path));
    }

    @Test
    void shouldSaveAndLoad() {
        FileBackedTasksManager fileManager = new FileBackedTasksManager(path);
        taskManager.save();
        FileBackedTasksManager fileManager2 = loadFromFile(file);

        assertEquals(Collections.EMPTY_LIST, fileManager.getTaskList());
        assertEquals(Collections.EMPTY_LIST, fileManager.getEpicList());
        assertEquals(Collections.EMPTY_LIST, fileManager.getSubtaskList());
    }

    @Test
    public void shouldCorrectlySaveAndLoad() {
        FileBackedTasksManager fileManager = new FileBackedTasksManager(path);
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        taskManager.makeNewTask(task);
        Epic epic = new Epic("Epic title", "Epic content", Status.NEW);
        taskManager.makeNewEpic(epic);
        fileManager.save();
        FileBackedTasksManager fileManager2 = loadFromFile(file);

        assertEquals(List.of(task), taskManager.getTaskList());
        assertEquals(List.of(epic), taskManager.getEpicList());
    }

}
