package tasks;

import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    private TaskManager taskManager;
    private Epic epic;
    private SubTask subTask;
    private SubTask subTask2;


    @BeforeEach
    void makeTestEpicAndSubTasks() {
        taskManager = new InMemoryTaskManager();
        epic = new Epic("Epic title", "Epic content", Status.NEW);
        subTask = new SubTask("SubTask title", "SubTask content", Status.NEW, 2);
        subTask.setStartTime("01.09.2022 09:00");
        subTask.setDuration(60);
        taskManager.makeNewSubTask(subTask);
        subTask2 = new SubTask("SubTask2 title", "SubTask2 content", Status.NEW, 3);
        subTask2.setStartTime("01.09.2022 11:00");
        subTask2.setDuration(60);
        taskManager.makeNewSubTask(subTask2);
    }

    private List<Status> getStatuses() {
        return epic.getSubTasks().stream()
                .map(SubTask::getStatus)
                .collect(Collectors.toList());
    }

    @Test
    void shouldBeEverySubTasksWithNewStatus() {
        List<Status> subTaskListStatus = getStatuses();
        for (Status status : subTaskListStatus) {
            assertEquals(Status.NEW, status, "Задача типа SubTask должна иметь статус: «NEW».");
        }
        Status status = epic.getStatus();
        assertEquals(Status.NEW, status, "Задача типа Epic должна иметь статус: «NEW».");
    }

    @Test
    void shouldBeEverySubTasksWithDoneStatus() {
        subTask.setStatus(Status.DONE);
        subTask2.setStatus(Status.DONE);
        epic.setStatus(Status.DONE);
        List<Status> subTaskListStatus = getStatuses();
        for (Status status : subTaskListStatus) {
            assertEquals(Status.DONE, status, "Задача типа SubTask должна иметь статус: «DONE».");
        }
        Status status = epic.getStatus();
        assertEquals(Status.DONE, status, "Задача типа Epic должна иметь статус: «DONE».");
    }

    @Test
    void shouldBeAllSubtasksWithInProgressStatus() {
        subTask.setStatus(Status.IN_PROGRESS);
        subTask2.setStatus(Status.IN_PROGRESS);
        epic.setStatus(Status.IN_PROGRESS);
        List<Status> subTaskListStatus = getStatuses();
        for (Status status : subTaskListStatus) {
            assertEquals(Status.IN_PROGRESS, status, "Задача типа SubTask должна иметь статус: «IN_PROGRESS».");
        }
        Status status = epic.getStatus();
        assertEquals(Status.IN_PROGRESS, status, "Задача типа Epic должна иметь статус: «IN_PROGRESS».");
    }

    @Test
    void shouldBeSubTasksWithNewAndDoneStatus() {
        subTask2.setStatus(Status.DONE);
        epic.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.NEW, subTask.getStatus(), "Задача типа SubTask должна иметь статус: «NEW».");
        assertEquals(Status.DONE, subTask2.getStatus(), "Вторая задача типа SubTask должна иметь статус: «DONE».");
        Status status = epic.getStatus();
        assertEquals(Status.IN_PROGRESS, status, "Задача типа Epic должна иметь статус: «IN_PROGRESS».");
    }

    @Test
    void shouldBeDeleteAllSubTasks() {
        taskManager.deleteAllSubTasks();
        assertEquals(0, epic.getSubTasks().size(),"Список задач типа SubTask должен быть пуст.");
        Status status = epic.getStatus();
        assertEquals(Status.NEW, status, "Задача типа Epic должна иметь статус: «NEW».");
    }
}