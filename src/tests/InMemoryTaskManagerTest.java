package tests;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
    }

}
