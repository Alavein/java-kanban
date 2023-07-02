import Tasks.*;

public class Main {
    public static void main(String[] args) {
        ManagerOfTasks managerOfTasks = new ManagerOfTasks();

        Task task1 = new Task("Отправиться путешествовать", "Поехать в другую страну");
        Task task2 = new Task("Освоить что-то новое", "Научиться водить какой-нибудь транспорт");
        managerOfTasks.makeNewTask(task1);
        managerOfTasks.makeNewTask(task2);

        Epic epic1 = new Epic("Съездить в Японию", "Осмотреть достопримечательности");
        Epic epic2 = new Epic("Научиться водить машину", "Закончить автошколу");
        managerOfTasks.makeNewEpic(epic1);
        managerOfTasks.makeNewEpic(epic2);

        SubTask subTask1 = new SubTask("Получить визу", "Подготовить все документы", 3);
        SubTask subTask2 = new SubTask("Забронировать отель", "Распечатать все документы", 3);
        managerOfTasks.makeNewSubTask(subTask1);
        managerOfTasks.makeNewSubTask(subTask2);

        SubTask subTask3 = new SubTask("Поступить в автошколу",
                "Подготовить все документы для поступления", 4);
        managerOfTasks.makeNewSubTask(subTask3);

        System.out.println(managerOfTasks.getTaskList());
        System.out.println(managerOfTasks.getEpicList());
        System.out.println(managerOfTasks.getSubtaskList());

        task1.setStatus("DONE");
        managerOfTasks.updateTask(task1);
        epic2.setStatus("IN_PROGRESS");
        managerOfTasks.updateSubTask(subTask3);

        System.out.println(managerOfTasks.getTaskList());
        System.out.println(managerOfTasks.getEpicList());
        System.out.println(managerOfTasks.getSubtaskList());

        managerOfTasks.deleteEpicById(3);
        managerOfTasks.getSubtaskByEpicId(4);

        System.out.println(managerOfTasks.getEpicList());
        System.out.println(managerOfTasks.getSubtaskList());
    }
}