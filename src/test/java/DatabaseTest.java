import org.junit.jupiter.api.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Проверка подключения к БД world и отправки запросов")
public class DatabaseTest extends BeforeAfter {

    @Test
    @Order(1)
    @DisplayName("Проверка создания таблицы в БД")
    public void testCreateTable() {
        String query = "CREATE TABLE continents ("
                + "ID int(6) NOT NULL,"
                + "NAME_MAINLAND VARCHAR(45) NOT NULL,"
                + "SQUARE FLOAT(2) NOT NULL,"
                + "PERCENT VARCHAR(45) NOT NULL,"
                + "PRIMARY KEY (id))";
        ConnectionDatabase.createTable(query);
    }

    @Test
    @Order(7)
    @DisplayName("Проверка удаления таблицы из БД")
    public void testDeleteTable(){
        String query = "DROP TABLE continents";
        ConnectionDatabase.deleteTable(query);
    }

    @Test
    @Order(2)
    @DisplayName("Добовление данных в таблицу")
    public void testInsertRequest() {
        String query = "INSERT INTO continents (ID, NAME_MAINLAND, SQUARE, PERCENT) VALUES (1, 'Europe', 11.5, '8%'),"
                + "(2, 'Asia', 43.4, '29%')";
        ConnectionDatabase.insertIntoTable(query);

        String selectQuery = "SELECT * FROM continents";
        ResultSet rs = ConnectionDatabase.selectFromTable(selectQuery);
        assertAll("Проверка введенных данных первой строки",
                () -> assertEquals("1", rs.getString("ID")),
                () -> assertEquals("Europe", rs.getString("NAME_MAINLAND")),
                () -> assertEquals("11.5", rs.getString("SQUARE")),
                () -> assertEquals("8%", rs.getString("PERCENT")));
    }

    @Test
    @Order(3)
    @DisplayName("Изменение данных в таблице")
    public void testUpdateRequest() throws SQLException {
        String query = "UPDATE continents SET SQUARE = 44.7 WHERE ID='1'";
        ConnectionDatabase.updateInTable(query);
        String selectQuery = "SELECT SQUARE FROM continents WHERE ID=1";
        ResultSet rs = ConnectionDatabase.selectFromTable(selectQuery);
        String expectedSquare = "44.7";
        String actualSquare = rs.getString("SQUARE");
        assertEquals(expectedSquare, actualSquare, "Фактическая площадь '" + actualSquare + "'. Ожидаемоя - '" + expectedSquare + "'.");

    }

    @Test
    @Order(4)
    @DisplayName("Отправка простого SELECT запроса")
    public void testSelectRequest() throws SQLException {
        String query = "SELECT * FROM continents";
        ResultSet rs = ConnectionDatabase.selectFromTable(query);
        String expectedMainland = "Europe";
        String actualMainland = rs.getString("NAME_MAINLAND");
        assertEquals(expectedMainland, actualMainland, "Фактический континент '" + actualMainland + "'. Ожидаемый - '" + expectedMainland + "'.");
    }

    @Test
    @Order(5)
    @DisplayName("Отправка SELECT запроса с условием where")
    public void testSelectWhereRequest() throws SQLException {
        String query = "SELECT * FROM continents WHERE id=2";
        ResultSet rs = ConnectionDatabase.selectFromTable(query);
        String expectedMainland = "Asia";
        String actualMainland = rs.getString("NAME_MAINLAND");
        assertEquals(expectedMainland, actualMainland, "Фактический континент '" + actualMainland + "'. Ожидаемый - '" + expectedMainland + "'.");
    }

    @Test
    @Order(6)
    @DisplayName("Удаление данных из таблицы")
    public void testDeleteRequest() {
        String query = "DELETE FROM continents WHERE ID=1";
        ConnectionDatabase.deleteFromTable(query);
    }


}




