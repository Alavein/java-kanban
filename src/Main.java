import manager.InMemoryTaskManager;
import tasks.*;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        Task task1 = new Task("Отправиться путешествовать", "Поехать в другую страну", Status.NEW);
        Task task2 = new Task("Освоить что-то новое", "Научиться водить какой-нибудь транспорт", Status.NEW);
        taskManager.makeNewTask(task1);
        taskManager.makeNewTask(task2);

        Epic epic1 = new Epic("Съездить в Японию", "Осмотреть достопримечательности", Status.NEW);
        Epic epic2 = new Epic("Научиться водить машину", "Закончить автошколу", Status.NEW);
        taskManager.makeNewEpic(epic1);
        taskManager.makeNewEpic(epic2);

        SubTask subTask1 = new SubTask("Получить визу", "Подготовить все документы",  Status.NEW, 3);
        SubTask subTask2 = new SubTask("Забронировать отель", "Распечатать все документы",
                Status.NEW, 3);
        taskManager.makeNewSubTask(subTask1);
        taskManager.makeNewSubTask(subTask2);

        SubTask subTask3 = new SubTask("Поступить в автошколу",
                "Подготовить все документы для поступления",  Status.NEW, 4);
        taskManager.makeNewSubTask(subTask3);

        System.out.println(taskManager.getTaskList());
        System.out.println(taskManager.getEpicList());
        System.out.println(taskManager.getSubtaskList());

        task1.setStatus(Status.DONE);
        taskManager.updateTask(task1);
        epic2.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubTask(subTask3);

        System.out.println(taskManager.getTaskList());
        System.out.println(taskManager.getEpicList());
        System.out.println(taskManager.getSubtaskList());

        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getEpicById(4);
        taskManager.getSubTaskById(5);

        System.out.println("История задач:");
        System.out.println(taskManager.getHistory());

        taskManager.deleteEpicById(3);

        System.out.println("История задач после удаления Эпика:");
        System.out.println(taskManager.getHistory());

        taskManager.getTaskById(2);
        System.out.println("История задач после нового запроса:");
        System.out.println(taskManager.getHistory());

    }
}