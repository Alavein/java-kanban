package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.HashMap;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private final HttpServer server;
    private final HttpTaskManager manager;
    private final Gson gson;
    private final Gson gsonNoAdapter;

    public HttpTaskServer() throws IOException {
        this.server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::handle);
        this.manager = Managers.getDefaultHttpTask();
        this.gson = Managers.getGson();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting().serializeNulls();
        this.gsonNoAdapter = gsonBuilder.create();
    }

    public void start() {
        System.out.println("Сервер запущен в работу. Порт - " + PORT);
        server.start();
    }

    public void stop(int delay) {
        server.stop(delay);
        System.out.println("Сервер остановлен.");
    }

    private void writeResponse200(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.sendResponseHeaders(200, 0);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes(Charset.defaultCharset()));
        os.close();
    }

    private void writeResponse404(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.sendResponseHeaders(404, 0);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes(Charset.defaultCharset()));
        os.close();
    }

    private void writeResponse400(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.sendResponseHeaders(400, 0);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes(Charset.defaultCharset()));
        os.close();
    }

    private String getJson(Object object) {
        try {
            return gson.toJson(object);
        } catch (NullPointerException e) {
            return gsonNoAdapter.toJson(object);
        }
    }

    private String getBody(HttpExchange httpExchange) throws IOException {
        InputStream inputStream = httpExchange.getRequestBody();
        return new String(inputStream.readAllBytes(), Charset.defaultCharset());
    }

    private void handle(HttpExchange httpExchange) throws IOException {
        try {
            String uri = httpExchange.getRequestURI().toString();
            String path = httpExchange.getRequestURI().getPath().replace("/tasks", "");
            String method = httpExchange.getRequestMethod();
            System.out.println("Началась обработка " + method + " запроса: " + uri);

            switch (method) {
                case "GET": {
                    switch (path) {
                        case "": {
                            String response = getJson(manager.getPrioritizedTasks());
                            writeResponse200(httpExchange, response);
                            break;
                        }
                        case "/task": {
                            String response = getJson(manager.getTaskList());
                            writeResponse200(httpExchange, response);
                            break;
                        }
                        case "/task/": {
                            try {
                                int id = Integer.parseInt(uri.replace("/tasks/task/?id=", ""));
                                Task task = manager.getTaskById(id);
                                String response;
                                if (task != null) {
                                    response = getJson(task);
                                    writeResponse200(httpExchange, response);
                                } else {
                                    response = "Задачи типа Task с таким id не существует.";
                                    writeResponse404(httpExchange, response);
                                }
                            } catch (Exception exception) {
                                String response = "Ошибка. Неверно сформирован запрос.";
                                writeResponse400(httpExchange, response);
                            }
                            break;
                        }
                        case "/subtask": {
                            String response = getJson(manager.getSubtaskList());
                            writeResponse200(httpExchange, response);
                            break;
                        }
                        case "/subtask/": {
                            try {
                                int id = Integer.parseInt(uri.replace("/tasks/subtask/?id=", ""));
                                SubTask subTask = manager.getSubTaskById(id);
                                String response;
                                if (subTask != null) {
                                    response = getJson(subTask);
                                    writeResponse200(httpExchange, response);
                                } else {
                                    response = "Задачи типа Subtask с таким id не существует.";
                                    writeResponse404(httpExchange, response);
                                }
                            } catch (Exception exception) {
                                String response = "Ошибка. Неверно сформирован запрос.";
                                writeResponse400(httpExchange, response);
                            }
                            break;
                        }
                        case "/epic": {
                            String response = getJson(manager.getEpicList());
                            writeResponse200(httpExchange, response);
                            break;
                        }
                        case "/epic/": {
                            try {
                                int id = Integer.parseInt(uri.replace("/tasks/epic/?id=", ""));
                                Epic epic = manager.getEpicById(id);
                                String response;
                                if (epic != null) {
                                    response = getJson(epic);
                                    writeResponse200(httpExchange, response);
                                } else {
                                    response = "Задачи типа Epic с таким id не существует.";
                                    writeResponse404(httpExchange, response);
                                }
                            } catch (Exception exception) {
                                String response = "Ошибка. Неверно сформирован запрос.";
                                writeResponse400(httpExchange, response);
                            }
                            break;
                        }
                        case "/subtask/epic/": {
                            try {
                                int id = Integer.parseInt(uri.replace("/tasks/subtask/epic/?id=", ""));
                                String response = getJson(manager.getSubtaskByEpicId(id));
                                if (!response.equals("null")) {
                                    writeResponse200(httpExchange, response);
                                } else {
                                    response = "Задачи типа Epic с таким id не существует.";
                                    writeResponse404(httpExchange, response);
                                }
                            } catch (Exception exception) {
                                String response = "Ошибка. Неверно сформирован запрос.";
                                writeResponse400(httpExchange, response);
                            }
                            break;
                        }
                        case "/history": {
                            String response = getJson(manager.getHistory());
                            writeResponse200(httpExchange, response);
                            break;
                        }
                        default:
                            String response = "Ошибка. Такого эндпойнта не существует.";
                            writeResponse404(httpExchange, response);
                            break;
                    }
                    break;
                }
                case "POST": {
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
                                writeResponse200(httpExchange, response);
                            } catch (Exception exception) {
                                String response = "Ошибка. Неверно сформирован запрос или тело.";
                                writeResponse400(httpExchange, response);
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
                                    writeResponse200(httpExchange, response);
                                } else {
                                    response = "Задачи типа Task с таким id не существует.";
                                    writeResponse404(httpExchange, response);
                                }
                            } catch (Exception exception) {
                                String response = "Ошибка. Неверно сформирован запрос или тело.";
                                writeResponse400(httpExchange, response);
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
                                    writeResponse200(httpExchange, response);
                                } else {
                                    String response = "Ошибка. В теле неверный id задачи типа Epic.";
                                    writeResponse400(httpExchange, response);
                                }
                            } catch (Exception exception) {
                                String response = "Ошибка. Неверно сформирован запрос или тело.";
                                writeResponse400(httpExchange, response);
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
                                    writeResponse200(httpExchange, response);
                                } else {
                                    response = "Задачи типа Subtask с таким id не существует.";
                                    writeResponse404(httpExchange, response);
                                }
                            } catch (Exception exception) {
                                String response = "Ошибка. Неверно сформирован запрос или тело.";
                                writeResponse400(httpExchange, response);
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
                                    writeResponse200(httpExchange, response);
                                } else {
                                    String response = "Ошибка расчета времени.";
                                    writeResponse400(httpExchange, response);
                                }
                            } catch (Exception exception) {
                                String response = "Ошибка. Неверно сформирован запрос или тело.";
                                writeResponse400(httpExchange, response);
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
                                    writeResponse200(httpExchange, response);
                                } else {
                                    response = "Задачи типа Epic с таким id не существует.";
                                    writeResponse404(httpExchange, response);
                                }
                            } catch (Exception exception) {
                                String response = "Ошибка. Неверно сформирован запрос или тело.";
                                writeResponse400(httpExchange, response);
                            }
                            break;
                        }
                    }
                    break;
                }
                case "DELETE": {
                    switch (path) {
                        case "/task": {
                            try {
                                manager.deleteAllTasks();
                                writeResponse200(httpExchange, "Все задачи типа Task успешно удалены.");
                            } catch (Exception exception) {
                                writeResponse400(httpExchange, exception.getMessage());
                            }
                            break;
                        }
                        case "/task/": {
                            try {
                                int id = Integer.parseInt(uri.replace("/tasks/task/?id=", ""));
                                manager.deleteTaskById(id);
                                writeResponse200(httpExchange, "Задача типа Task успешно удалена.");
                            } catch (Exception exception) {
                                writeResponse400(httpExchange, exception.getMessage());
                            }
                            break;
                        }
                        case "/subtask": {
                            try {
                                manager.deleteAllSubTasks();
                                writeResponse200(httpExchange, "Все задачи типа Subtask успешно удалены.");
                            } catch (Exception exception) {
                                writeResponse400(httpExchange, exception.getMessage());
                            }
                            break;
                        }
                        case "/subtask/": {
                            try {
                                int id = Integer.parseInt(uri.replace("/tasks/subtask/?id=", ""));
                                manager.deleteSubTasksById(id);
                                writeResponse200(httpExchange, "Задача типа Subtask успешно удалена.");
                            } catch (Exception exception) {
                                writeResponse400(httpExchange, exception.getMessage());
                            }
                            break;
                        }
                        case "/epic": {
                            try {
                                manager.deleteAllEpics();
                                writeResponse200(httpExchange, "Все задачи типа Epic успешно удалены.");
                            } catch (Exception exception) {
                                writeResponse400(httpExchange, exception.getMessage());
                            }
                            break;
                        }
                        case "/epic/": {
                            try {
                                int id = Integer.parseInt(uri.replace("/tasks/epic/?id=", ""));
                                manager.deleteEpicById(id);
                                writeResponse200(httpExchange, "Задача типа Epic успешно удалена.");
                            } catch (Exception exception) {
                                writeResponse400(httpExchange, exception.getMessage());
                            }
                            break;
                        }
                    }
                    break;
                }
                default:
                    String response = "Ошибка. Метод не подходит.";
                    writeResponse400(httpExchange, response);
            }
        } catch (Throwable throwable) {
            writeResponse400(httpExchange, throwable.getMessage());
        } finally {
            httpExchange.close();
        }
    }
}
