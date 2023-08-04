package manager;

import tasks.Task;

import java.util.*;


public class InMemoryHistoryManager implements HistoryManager {

    private static class Node<T> {
        Node<T> next;
        Node<T> prev;
        T item;

        public Node(Node<T> next, Node<T> prev, T item) {
            this.next = next;
            this.prev = prev;
            this.item = item;
        }
    }

    private final CustomLinkedList customLinkedList = new CustomLinkedList();

    private static class CustomLinkedList {

        Map<Integer, Node<Task>> viewsHistory = new HashMap<>();
        Node<Task> head;
        Node<Task> tail;

        void linkLast(Task task) {
            Node<Task> newNode = new Node<>(null, tail, task);
            removeNode(viewsHistory.get(task.getId()));
            if (tail == null) {
                head = newNode;
            } else {
                tail.next = newNode;
            }
            tail = newNode;
            viewsHistory.put(task.getId(), newNode);
        }

        private void removeNode(Node<Task> node) {
            if (node != null) {
                final Node<Task> prev = node.prev;
                final Node<Task> next = node.next;
                viewsHistory.remove(node.item.getId());
                if (prev == null && next == null) {
                    head = null;
                    tail = null;
                } else if (next == null) {
                    tail = prev;
                    tail.next = next;
                } else if (prev == null) {
                    head = next;
                    head.prev = null;
                } else {
                    prev.next = next;
                    next.prev = prev;
                }
            }
        }

        List<Task> getTasks() {
            List<Task> tasks = new ArrayList<>(viewsHistory.size());
            Node<Task> node = head;
            while (node != null) {
                tasks.add(node.item);
                node = node.next;
            }
            return tasks;
        }

        Node<Task> getNode(int id) {
            return viewsHistory.get(id);
        }
    }

    @Override
    public void addToHistoryTask(Task task) {
        if (task != null) {
            customLinkedList.linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
        customLinkedList.removeNode(customLinkedList.getNode(id));
    }

    @Override
    public List<Task> getHistory() {
        return customLinkedList.getTasks();
    }
}
