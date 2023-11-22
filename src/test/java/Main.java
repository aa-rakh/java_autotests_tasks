import clients.DBClient;
import containers.PostgreSQLTestContainer;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        PostgreSQLTestContainer.runPostgreSQLContainer();
        DBClient dbClient = new DBClient();
//        Падает при попытке подключиться - java.lang.ExceptionInInitializerError
        Connection connection = dbClient.openDBConnection();
//        dbClient.insertIntoTable();
        dbClient.closeDBConnection();
    }
}