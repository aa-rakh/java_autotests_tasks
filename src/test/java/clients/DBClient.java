package clients;

import containers.ContainersLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBClient {
    private static final Logger logger = LogManager.getLogger(DBClient.class);
    private final String dbName = "test_db";
    private final String user = "test_user";
    private final String password = "qwerty";
    private String url = null;
    private Connection connection;

    public Connection openDBConnection() throws SQLException {
        url = String.format("jdbc:postgresql://%s:5432/%s", ContainersLoader.bootPostgresDB().getHost(), dbName);
        connection = DriverManager.getConnection(url, user, password);
        logger.info("Подключение к PostgreSQL создано");

        return connection;
    }

    public void closeDBConnection() {
        try {
            connection.close();
        } catch (Exception exception) {
            System.out.println("При запуске тестов не выполнялось подключение к БД");
            System.out.println(exception);
        }

        System.out.println("Соединение с PostgreSQL успешно закрыто");
    }


}
