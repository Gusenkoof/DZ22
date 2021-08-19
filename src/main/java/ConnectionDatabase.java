import org.jooq.impl.DSL;
import utils.Log;

import java.sql.*;

public class ConnectionDatabase {
    private static final String host = "localhost";
    private static final String DBName = "world";
    private static final String url = "jdbc:mysql://" + host + ":3306/" + DBName + "?serverTimezone=Europe/Moscow";
    private static final String user = "root";
    private static final String password = "defender198605";

    private static Connection con = null;
    private static Statement stmt = null;
    private static ResultSet rs = null;


    // подключение к базе данных
    public static Connection connectToDB(){
        Log.info("Подключаемся к базе данных " + DBName);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, password);
            Log.info("Подключение к базе данных прошло успешно");
        } catch (ClassNotFoundException e){
            Log.error(e.getMessage());
        } catch (SQLException sqlEx){
            Log.error("Подключение к базе данных проволилось\n" + sqlEx.getMessage());
        }
        return con;
    }

    //отключение от базы данных
    public static void closeConnection(){
        if (con != null) {
            try {
                con.close();
                Log.info("Соединение к базе данных закрыто");
            } catch (SQLException se){
                Log.error("Соединени к базе данных не закрыто \n" + se.getMessage());
            }
        }
        if (rs != null) {
            try {
                rs.close();
                Log.info("Набор результатов успешно закрыт");
            } catch (SQLException se) {
                Log.error("Набор результатов не закрылся\n" + se.getMessage());
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
                Log.info("Заявление успешно закрыто");
            } catch (SQLException se) {
                Log.error("Заявленеи не закрыто \n" + se.getMessage());
            }
        }
    }

    // Создание таблицы
    public static void createTable(String query) {
        try {
            stmt = connectToDB().prepareStatement(query);
            Log.info("Отправление запроса к БД");
            stmt.executeUpdate(query);
            Log.info("Таблица создана успешно");
        } catch (SQLException se) {
            Log.error("Таблица не создана \n" + se.getMessage());
        }
    }

    //Удаление таблицы
    public static void deleteTable(String query) {
        try {
            stmt = connectToDB().prepareStatement(query);
            Log.info("Отправление запроса к БД на удаление таблицы");
            stmt.executeUpdate(query);
            Log.info("Таблица удалена");
        } catch (SQLException se) {
            Log.error("Таблица не удалось удалить \n" + se.getMessage());
        }
    }

    //удаление из таблицы
    public static void deleteFromTable(String query) {
        try {
            Log.info("Отправка запроса" + query);
            stmt = connectToDB().createStatement();
            stmt.executeUpdate(query);
            Log.info("Удаление из таблицы прошло успешно");
        } catch (SQLException se) {
            Log.error("Данные из таблицы небыли удалены\n" + se.getMessage());
        }
    }

    //Добовленеи данных в таблицу
    public static void insertIntoTable(String query) {
        try {
            stmt = connectToDB().createStatement();
            Log.info("Отправка запроса в БД " + query);
            stmt.executeUpdate(query);
            Log.info("Новые данные были добавлены в таблицу");
        } catch (SQLException se) {
            Log.error("Новые данные не были добавлены в таблицу\n" + se.getMessage());
        }
    }

    //проверка
    public static ResultSet selectFromTable(String query) {
        try {
            stmt = connectToDB().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            Log.info("Отправка запроса в БД: " + query);
            rs = stmt.executeQuery(query);
            rs.next();
            //логирование результатов запроса
            addSQLRequestResultsToLog(query);
        } catch (SQLException se) {
            Log.error(se.getMessage());
        }
        return rs;
    }
    //логирование
    private static void addSQLRequestResultsToLog(String query) {
        StringBuilder builder = new StringBuilder();
        DSL.using(connectToDB()).fetchStream(query)
                .forEach(r -> builder.append(r.format()));
        Log.info("Результаты запроса\n" + builder);
    }

    //Изменение данных в таблице
    public static void updateInTable(String query) {
        try {
            stmt = connectToDB().createStatement();
            Log.info("Отправка запроса в БД " + query);
            stmt.executeUpdate(query);
            Log.info("Данные были обнавлены");
        } catch (SQLException se) {
            Log.error("Данные не обнавились:\n" + se.getMessage());
        }
    }

}
