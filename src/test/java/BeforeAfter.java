import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import utils.Log;

public class BeforeAfter {

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        Log.info("------- Начало теста: " + testInfo.getDisplayName() + " -------");
        Assertions.assertNotNull(ConnectionDatabase.connectToDB());
    }

    @AfterEach
    public void tearDown(TestInfo testInfo) {
        ConnectionDatabase.closeConnection();
        Log.info("------- Тест завершен: " + testInfo.getDisplayName() + " -------");
    }
}
