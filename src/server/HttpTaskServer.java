package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import manager.HttpTaskManager;
import manager.Managers;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.HashMap;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private final HttpServer server;
    private final HttpTaskManager manager;
    private final Gson gson;
    private final Gson gsonNoAdapter;

    private static final String HTTP_METHOD_GET = "GET";
    private static final String HTTP_METHOD_POST = "POST";
    private static final String HTTP_METHOD_DELETE = "DELETE";

    public HttpTaskServer() throws IOException {
        this.server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::handleGetMethod);
        server.createContext("/tasks", this::handlePostMethod);
        server.createContext("/tasks", this::handleDeleteMethod);
        this.manager = Managers.getDefaultHttpTask();
        this.gson = getGson();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting().serializeNulls();
        this.gsonNoAdapter = gsonBuilder.create();
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting().serializeNulls().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        return gsonBuilder.create();
    }

    public void start() {
        System.out.println("Сервер запущен в работу. Порт - " + PORT);
        server.start();
    }

    public void stop(int delay) {
        server.stop(delay);
        System.out.println("Сервер остановлен.");
    }

    private void writeResponseSuccess(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.sendResponseHeaders(200, 0);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes(Charset.defaultCharset()));
        os.close();
    }

    private void writeResponseNotFoundError(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.sendResponseHeaders(404, 0);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes(Charset.defaultCharset()));
        os.close();
    }

    private void writeResponseBadRequestError(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.sendResponseHeaders(400, 0);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes(Charset.defaultCharset()));
        os.close();
    }

    private String getJson(Object object) {
        return gson.toJson(object);
    }

    private String getBody(HttpExchange httpExchange) throws IOException {
        InputStream inputStream = httpExchange.getRequestBody();
        return new String(inputStream.readAllBytes(), Charset.defaultCharset());
    }

    private void handleGetMethod(HttpExchange httpExchange) throws IOException {
        String uri = httpExchange.getRequestURI().toString();
        String path = httpExchange.getRequestURI().getPath().replace("/tasks", "");
        String method = httpExchange.getRequestMethod();
        if (method.equals(HTTP_METHOD_GET)) {
            System.out.println("Началась обработка " + HTTP_METHOD_GET + " запроса: " + uri);

            switch (path) {
                case "": {
                    String response = getJson(manager.getPrioritizedTasks());
                    writeResponseSuccess(httpExchange, response);
                    break;
                }
                case "/task": {
                    String response = getJson(manager.getTaskList());
                    writeResponseSuccess(httpExchange, response);
                    break;
                }
                case "/task/": {
                    try {
                        int id = Integer.parseInt(uri.replace("/tasks/task/?id=", ""));
                        Task task = manager.getTaskById(id);
                        String response;
                        if (task != null) {
                            response = getJson(task);
                            writeResponseSuccess(httpExchange, response);
                        } else {
                            response = "Задачи типа Task с таким id не существует.";
                            writeResponseNotFoundError(httpExchange, response);
                        }
                    } catch (Exception exception) {
                        String response = "Ошибка. Неверно сформирован запрос.";
                        writeResponseBadRequestError(httpExchange, response);
                    }
                    break;
                }
                case "/subtask": {
                    String response = getJson(manager.getSubtaskList());
                    writeResponseSuccess(httpExchange, response);
                    break;
                }
                case "/subtask/": {
                    try {
                        int id = Integer.parseInt(uri.replace("/tasks/subtask/?id=", ""));
                        SubTask subTask = manager.getSubTaskById(id);
                        String response;
                        if (subTask != null) {
                            response = getJson(subTask);
                            writeResponseSuccess(httpExchange, response);
                        } else {
                            response = "Задачи типа Subtask с таким id не существует.";
                            writeResponseNotFoundError(httpExchange, response);
                        }
                    } catch (Exception exception) {
                        String response = "Ошибка. Неверно сформирован запрос.";
                        writeResponseBadRequestError(httpExchange, response);
                    }
                    break;
                }
                case "/epic": {
                    String response = getJson(manager.getEpicList());
                    writeResponseSuccess(httpExchange, response);
                    break;
                }
                case "/epic/": {
                    try {
                        int id = Integer.parseInt(uri.replace("/tasks/epic/?id=", ""));
                        Epic epic = manager.getEpicById(id);
                        String response;
                        if (epic != null) {
                            response = getJson(epic);
                            writeResponseSuccess(httpExchange, response);
                        } else {
                            response = "Задачи типа Epic с таким id не существует.";
                            writeResponseNotFoundError(httpExchange, response);
                        }
                    } catch (Exception exception) {
                        String response = "Ошибка. Неверно сформирован запрос.";
                        writeResponseBadRequestError(httpExchange, response);
                    }
                    break;
                }
                case "/subtask/epic/": {
                    try {
                        int id = Integer.parseInt(uri.replace("/tasks/subtask/epic/?id=", ""));
                        String response = getJson(manager.getSubtaskByEpicId(id));
                        if (!response.equals("null")) {
                            writeResponseSuccess(httpExchange, response);
                        } else {
                            response = "Задачи типа Epic с таким id не существует.";
                            writeResponseNotFoundError(httpExchange, response);
                        }
                    } catch (Exception exception) {
                        String response = "Ошибка. Неверно сформирован запрос.";
                        writeResponseBadRequestError(httpExchange, response);
                    }
                    break;
                }
                case "/history": {
                    String response = getJson(manager.getHistory());
                    writeResponseSuccess(httpExchange, response);
                    break;
                }
                default:
                    String response = "Ошибка. Такого эндпойнта не существует.";
                    writeResponseNotFoundError(httpExchange, response);
                    break;
            }
        }
    }

    private void handlePostMethod(HttpExchange httpExchange) throws IOException {
        String uri = httpExchange.getRequestURI().toString();
        String path = httpExchange.getRequestURI().getPath().replace("/tasks", "");
        String method = httpExchange.getRequestMethod();
        if (method.equals(HTTP_METHOD_POST)) {
            System.out.println("Началась обработка " + HTTP_METHOD_POST + " запроса: " + uri);

            switch (path) {
                case "/task": {
                    try {
                        Task task = gson.fromJson(getBody(httpExchange), Task.class);
                        if (task.getTitle() == null) {
                            task.setTitle("");
                        }
                        if (task.getContent() == null) {
                            task.setContent("");
                        }
                        manager.makeNewTask(task);
                        String response = "Задача типа Task успешно добавлена.";
                        writeResponseSuccess(httpExchange, response);
                    } catch (Exception exception) {
                        String response = "Ошибка. Неверно сформирован запрос или тело.";
                        writeResponseBadRequestError(httpExchange, response);
                    }
                    break;
                }
                case "/task/": {
                    try {
                        int id = Integer.parseInt(uri.replace("/tasks/task/?id=", ""));
                        Task task = manager.getTaskById(id);
                        String response;
                        if (task != null) {
                            Task task1 = gson.fromJson(getBody(httpExchange), Task.class);
                            task1.setId(id);
                            if (task1.getStatus() == null) {
                                task1.setStatus(task.getStatus());
                            }
                            if (task1.getTitle() == null) {
                                task1.setTitle(task.getTitle());
                            }
                            if (task1.getContent() == null) {
                                task1.setContent(task.getContent());
                            }
                            manager.updateTask(task1);
                            response = "Задача типа Task успешно обновлена.";
                            writeResponseSuccess(httpExchange, response);
                        } else {
                            response = "Задачи типа Task с таким id не существует.";
                            writeResponseNotFoundError(httpExchange, response);
                        }
                    } catch (Exception exception) {
                        String response = "Ошибка. Неверно сформирован запрос или тело.";
                        writeResponseBadRequestError(httpExchange, response);
                    }
                    break;
                }
                case "/subtask": {
                    try {
                        SubTask subTask = gson.fromJson(getBody(httpExchange), SubTask.class);
                        Epic epic = manager.getEpicById(subTask.getEpicId());
                        if (subTask.getEpicId() != 0 && epic != null) {
                            if (subTask.getTitle() == null) {
                                subTask.setTitle("");
                            }
                            if (subTask.getContent() == null) {
                                subTask.setContent("");
                            }
                            manager.makeNewSubTask(subTask);
                            epic.putSubTask(subTask.getId(), subTask);
                            epic.countTime();
                            String response = "Задача типа Subtask успешно добавлена.";
                            writeResponseSuccess(httpExchange, response);
                        } else {
                            String response = "Ошибка. В теле неверный id задачи типа Epic.";
                            writeResponseBadRequestError(httpExchange, response);
                        }
                    } catch (Exception exception) {
                        String response = "Ошибка. Неверно сформирован запрос или тело.";
                        writeResponseBadRequestError(httpExchange, response);
                    }
                    break;
                }
                case "/subtask/": {
                    try {
                        int id = Integer.parseInt(uri.replace("/tasks/subtask/?id=", ""));
                        SubTask subTask = manager.getSubTaskById(id);
                        String response;
                        if (subTask != null) {
                            SubTask subTask1 = gson.fromJson(getBody(httpExchange), SubTask.class);
                            subTask1.setId(id);
                            subTask1.setEpicId(subTask.getEpicId());
                            if (subTask1.getStatus() == null) {
                                subTask1.setStatus(subTask.getStatus());
                            }
                            if (subTask1.getTitle() == null) {
                                subTask1.setTitle(subTask.getTitle());
                            }
                            if (subTask1.getContent() == null) {
                                subTask1.setContent(subTask.getContent());
                            }
                            manager.updateSubTask(id, subTask1, subTask1.getStatus());
                            response = "Задача типа Subtask успешно обновлена.";
                            writeResponseSuccess(httpExchange, response);
                        } else {
                            response = "Задачи типа Subtask с таким id не существует.";
                            writeResponseNotFoundError(httpExchange, response);
                        }
                    } catch (Exception exception) {
                        String response = "Ошибка. Неверно сформирован запрос или тело.";
                        writeResponseBadRequestError(httpExchange, response);
                    }
                    break;
                }
                case "/epic": {
                    try {
                        Epic epic = gson.fromJson(getBody(httpExchange), Epic.class);
                        if (epic.getStartTime() == null && epic.getDuration() == 0) {
                            if (epic.getSubTasks() == null) {
                                HashMap<Integer, SubTask> subTaskMap = new HashMap<>();
                                epic.setSubTasks(subTaskMap);
                            }
                            if (epic.getTitle() == null) {
                                epic.setTitle("");
                            }
                            if (epic.getContent() == null) {
                                epic.setContent("");
                            }
                            manager.makeNewEpic(epic);
                            String response = "Задача типа Epic успешно добавлена.";
                            writeResponseSuccess(httpExchange, response);
                        } else {
                            String response = "Ошибка расчета времени.";
                            writeResponseBadRequestError(httpExchange, response);
                        }
                    } catch (Exception exception) {
                        String response = "Ошибка. Неверно сформирован запрос или тело.";
                        writeResponseBadRequestError(httpExchange, response);
                    }
                    break;
                }
                case "/epic/": {
                    try {
                        int id = Integer.parseInt(uri.replace("/tasks/epic/?id=", ""));
                        Epic epic = manager.getEpicById(id);
                        String response;
                        if (epic != null) {
                            Epic epic1 = gson.fromJson(getBody(httpExchange), Epic.class);
                            epic1.setId(id);
                            epic1.setStatus(epic.getStatus());
                            if (epic1.getTitle() == null) {
                                epic1.setTitle(epic.getTitle());
                            }
                            if (epic1.getContent() == null) {
                                epic1.setContent(epic.getContent());
                            }
                            manager.updateEpic(id, epic1);
                            response = "Задача типа Epic успешно успешно обновлена.";
                            writeResponseSuccess(httpExchange, response);
                        } else {
                            response = "Задачи типа Epic с таким id не существует.";
                            writeResponseNotFoundError(httpExchange, response);
                        }
                    } catch (Exception exception) {
                        String response = "Ошибка. Неверно сформирован запрос или тело.";
                        writeResponseBadRequestError(httpExchange, response);
                    }
                    break;
                }
            }
        }
    }

    private void handleDeleteMethod(HttpExchange httpExchange) throws IOException {
        String uri = httpExchange.getRequestURI().toString();
        String path = httpExchange.getRequestURI().getPath().replace("/tasks", "");
        String method = httpExchange.getRequestMethod();

        if (method.equals(HTTP_METHOD_DELETE)) {
            System.out.println("Началась обработка " + HTTP_METHOD_DELETE + " запроса: " + uri);

            switch (path) {
                case "/task": {
                    try {
                        manager.deleteAllTasks();
                        writeResponseSuccess(httpExchange, "Все задачи типа Task успешно удалены.");
                    } catch (Exception exception) {
                        writeResponseBadRequestError(httpExchange, exception.getMessage());
                    }
                    break;
                }
                case "/task/": {
                    try {
                        int id = Integer.parseInt(uri.replace("/tasks/task/?id=", ""));
                        manager.deleteTaskById(id);
                        writeResponseSuccess(httpExchange, "Задача типа Task успешно удалена.");
                    } catch (Exception exception) {
                        writeResponseBadRequestError(httpExchange, exception.getMessage());
                    }
                    break;
                }
                case "/subtask": {
                    try {
                        manager.deleteAllSubTasks();
                        writeResponseSuccess(httpExchange, "Все задачи типа Subtask успешно удалены.");
                    } catch (Exception exception) {
                        writeResponseBadRequestError(httpExchange, exception.getMessage());
                    }
                    break;
                }
                case "/subtask/": {
                    try {
                        int id = Integer.parseInt(uri.replace("/tasks/subtask/?id=", ""));
                        manager.deleteSubTasksById(id);
                        writeResponseSuccess(httpExchange, "Задача типа Subtask успешно удалена.");
                    } catch (Exception exception) {
                        writeResponseBadRequestError(httpExchange, exception.getMessage());
                    }
                    break;
                }
                case "/epic": {
                    try {
                        manager.deleteAllEpics();
                        writeResponseSuccess(httpExchange, "Все задачи типа Epic успешно удалены.");
                    } catch (Exception exception) {
                        writeResponseBadRequestError(httpExchange, exception.getMessage());
                    }
                    break;
                }
                case "/epic/": {
                    try {
                        int id = Integer.parseInt(uri.replace("/tasks/epic/?id=", ""));
                        manager.deleteEpicById(id);
                        writeResponseSuccess(httpExchange, "Задача типа Epic успешно удалена.");
                    } catch (Exception exception) {
                        writeResponseBadRequestError(httpExchange, exception.getMessage());
                    }
                    break;
                }
            }
        }
    }
}
