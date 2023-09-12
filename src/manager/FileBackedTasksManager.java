package manager;

import tasks.*;

import javax.imageio.IIOException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private File file;
    private static final String HEADER = "id, type, name, description, status, epic";

    public FileBackedTasksManager(String path) {
        super();
        this.file = new File(path);
    }

    public void save() {
        try (FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8)) {
            fileWriter.write(HEADER + "\n");
            for (Task task : super.getTaskList()) {
                fileWriter.write(toString(task) + "\n");
            }
            for (Epic epic : super.getEpicList()) {
                fileWriter.write(toString(epic) + "\n");
            }
            for (SubTask subTask : super.getSubtaskList()) {
                fileWriter.write(toString(subTask) + "\n");
            }
            fileWriter.write("\n");
            fileWriter.write(historyToString(super.historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка: Файл не удалось сохранить." + e.getMessage());
        }
    }

    public static FileBackedTasksManager loadFromFile(File FILE) {
        List<String> stringsWithTasks = new ArrayList<>();
        FileBackedTasksManager manager = new FileBackedTasksManager("/Users/canta/dev/java-kanban/src/resources/tasks.txt");

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE, StandardCharsets.UTF_8))) {
            while (bufferedReader.ready()) {
                stringsWithTasks.add(bufferedReader.readLine());
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка: Файл не был считан.");
        }

        if (stringsWithTasks.isEmpty()) {
            throw new ManagerSaveException("Ошибка: Файл пустой");
        }

        List<Integer> historyList = historyFromString(stringsWithTasks.get(stringsWithTasks.size() - 1));
        stringsWithTasks.remove(stringsWithTasks.size() - 1);
        stringsWithTasks.remove(0);

        int generateId = 0;

        for (String str : stringsWithTasks) {
            if (!str.isEmpty() || !str.isBlank()) {
                Task task = manager.fromString(str);
                if (task != null) {
                    if (task.getId() > generateId) {
                        generateId = task.getId();
                    }

                    switch (task.getType()) {
                        case EPIC:
                            manager.epics.put(task.getId(), (Epic) task);
                            break;
                        case SUBTASK:
                            manager.subTasks.put(task.getId(), (SubTask) task);
                            break;
                        case TASK:
                            manager.tasks.put(task.getId(), task);
                            break;
                        default:
                            break;
                    }
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
        Task result = null;

            switch (Type.valueOf(newTask[1])) {
                case TASK:
                    result = new Task(newTask[2], newTask[3], Status.getEnum(newTask[4]));
                    result.setId(Integer.parseInt(newTask[0]));
                    break;
                case SUBTASK:
                    result = new SubTask(newTask[2], newTask[3], Status.getEnum(newTask[4]), Integer.parseInt(newTask[5]));
                    result.setId(Integer.parseInt(newTask[0]));
                    break;
                case EPIC:
                    result = new Epic(newTask[2], newTask[3], Status.getEnum(newTask[4]));
                    result.setId(Integer.parseInt(newTask[0]));
                    break;
                default:
                    throw new ManagerSaveException("Задачи такого типа не предусмотрены.");
            }
        return result;
    }

    private static String historyToString(HistoryManager manager) {
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

    private static List<Integer> historyFromString(String value) {
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
}
