package manager;

import tasks.*;

import javax.imageio.IIOException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private File FILE;

    public FileBackedTasksManager(String path) {
        super();
        this.FILE = new File(path);
    }

    public FileBackedTasksManager() {
    }

    public void save() {
        try (FileWriter fileWriter = new FileWriter(FILE, StandardCharsets.UTF_8)) {
            fileWriter.write("id, type, name, description, status, epic" + "\n");
            for (Task task : super.getTaskList()) {
                fileWriter.write(toString(task) + "\n");
            }
            for (Epic epic : super.getEpicList()) {
                fileWriter.write(toString(epic)  + "\n");
            }
            for (SubTask subTask : super.getSubtaskList()) {
                fileWriter.write(toString(subTask)  + "\n");
            }
            fileWriter.write("\n");
            fileWriter.write(historyToString(super.historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка. Файл не удалось сохранить.");
        }
    }

    public static FileBackedTasksManager  loadFromFile(File FILE) {
        List<String> strings = new ArrayList<>();
        FileBackedTasksManager manager = new FileBackedTasksManager();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE, StandardCharsets.UTF_8))) {
            while (bufferedReader.ready()) {
                strings.add(bufferedReader.readLine());
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка. Файл не был считан.");
        }

        if (strings.isEmpty()) {
            throw new ManagerSaveException("Ошибка. Файл пустой");
        }

        List<Integer> historyList = historyFromString(strings.get(strings.size() - 1));
        strings.remove(strings.size() - 1);
        strings.remove(0);

        int generateId = 0;

        for (String str : strings) {
            if (!str.isEmpty() || !str.isBlank()) {
                Task task = manager.fromString(str);

                if (task.getId() > generateId) {
                    generateId = task.getId();
                }

                if (task instanceof Epic) { //task.getType().equals(TaskType.EPICTASK)
                    manager.epics.put(task.getId(), (Epic) task);
                } else if (task instanceof SubTask) { //task.getType().equals(TaskType.SUBTASK)
                    manager.subTasks.put(task.getId(), (SubTask) task);
                } else {
                    manager.tasks.put(task.getId(), task);
                }
            }
        }

        FileBackedTasksManager.setGenerateId(generateId);

        for (int id : historyList) {
            if (manager.tasks.containsKey(id)) {
                manager.historyManager.addToHistoryTask(manager.tasks.get(id));
            } else if (manager.subTasks.containsKey(id)) {
                manager.historyManager.addToHistoryTask(manager.subTasks.get(id));
            } else {
                manager.historyManager.addToHistoryTask(manager.epics.get(id));
            }
        }

        return manager;
    }

    private String getEpicId(Task task) {
        if (task instanceof SubTask) {
            return Integer.toString(((SubTask) task).getEpicId());
        }
        return "";
    }

    private String toString(Task task) {
        String[] strings = {Integer.toString(task.getId()), task.getClass().getSimpleName(), task.getTitle(),
                task.getContent(), task.getStatus().toString(), getEpicId(task)};

        return String.join(", ", strings);
    }

    private Task fromString(String value) {
        String[] newTask = value.split(", ");
        if (newTask[1].equals("Task")) {
            Task task = new Task(newTask[2], newTask[3], Status.getEnum(newTask[4]));
            task.setId(Integer.parseInt(newTask[0]));
            return task;
        } else if (newTask[1].equals("SubTask")) {
            SubTask subTask = new SubTask(newTask[2], newTask[3], Status.getEnum(newTask[4]), Integer.parseInt(newTask[5]));
            subTask.setId(Integer.parseInt(newTask[0]));
            return subTask;
        } else {
            Epic epic = new Epic(newTask[2], newTask[3], Status.getEnum(newTask[4]));
            epic.setId(Integer.parseInt(newTask[0]));
            return epic;
        }
    }

    static String historyToString(HistoryManager manager) {
        List<Task> history = manager.getHistory();
        StringBuilder str = new StringBuilder();
        if (!history.isEmpty()) {
            for (Task task : history) {
                str.append(task.getId()).append(",");
            }
            if (str.length() != 0) {
                str.deleteCharAt(str.length() - 1);
            }
            return str.toString();
        }
        return "";
    }

    static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
        if (!value.isEmpty()) {
            String[] lines = value.split(",");
            for (String line : lines) {
                history.add(Integer.parseInt(line));
            }
        }
        return history;
    }

    @Override
    public int makeNewTask(Task task) {
        super.makeNewTask(task);
        save();
        return (task.getId());
    }

    @Override
    public int makeNewEpic(Epic epic) {
        super.makeNewEpic(epic);
        save();
        return (epic.getId());
    }

    @Override
    public Integer makeNewSubTask(SubTask subTask) {
        super.makeNewSubTask(subTask);
        save();
        return (subTask.getId());
    }

    @Override
    public void deleteSubTasksById(int id) {
        super.deleteSubTasksById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public Task getTaskById(int id) {
        super.getTaskById(id);
        save();
        return super.getTaskById(id);
    }

    @Override
    public Epic getEpicById(int id) {
        super.getEpicById(id);
        save();
        return super.getEpicById(id);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        super.getSubTaskById(id);
        save();
        return super.getSubTaskById(id);
    }

    @Override
    public List<SubTask> getSubtaskByEpicId(int id) {
        super.getSubtaskByEpicId(id);
        save();
        return super.getSubtaskByEpicId(id);
    }


    public static void main(String[] args) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("/Users/canta/dev/java-kanban/tasks.txt");
        Epic epicTask = new Epic("Путешествие", "Поездка в Португалию", Status.NEW);
        fileBackedTasksManager.makeNewEpic(epicTask);
        SubTask subtask1 = new SubTask("Составить список вещей", "Купить все по списку", Status.NEW, 3);
        SubTask subtask2 = new SubTask("Подготовить документы", "Подготовить визу", Status.NEW, 3);


        Epic epicTask2 = new Epic("Купить машину", "Выбрать модель машины", Status.NEW);
        fileBackedTasksManager.makeNewEpic(epicTask2);
        SubTask subtask3 = new SubTask("Найти диллера", "Выбрать машину в нужном магазине", Status.NEW,4);


        fileBackedTasksManager.makeNewSubTask(subtask1);
        fileBackedTasksManager.makeNewSubTask(subtask2);
        fileBackedTasksManager.makeNewSubTask(subtask3);

        fileBackedTasksManager.makeNewTask(new Task("Менять жизнь к лучшему", "Идти на встречу переменам", Status.NEW));


        System.out.println(fileBackedTasksManager.getEpicList());
        System.out.println(fileBackedTasksManager.getSubtaskList());
        System.out.println(fileBackedTasksManager.getTaskList());

        fileBackedTasksManager.getSubTaskById(3);
        fileBackedTasksManager.getSubTaskById(4);
        fileBackedTasksManager.getEpicById(1);
        fileBackedTasksManager.getEpicById(2);
        fileBackedTasksManager.getSubTaskById(3);
        fileBackedTasksManager.getSubTaskById(4);
        fileBackedTasksManager.getEpicById(1);
        fileBackedTasksManager.getEpicById(2);

        fileBackedTasksManager.getHistory();


        FileBackedTasksManager fileBackedTasksManager2 = loadFromFile(new File("/Users/canta/dev/java-kanban/tasks.txt"));
        fileBackedTasksManager2.getHistory();
        System.out.println(fileBackedTasksManager2.getEpicList());
        System.out.println(fileBackedTasksManager2.getSubtaskList());
        System.out.println(fileBackedTasksManager.getTaskList());


    }
}
