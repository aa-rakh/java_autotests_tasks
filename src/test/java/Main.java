import clients.DBClient;
import containers.PostgreSQLTestContainer;
import models.db.test_schema.tables.records.StudentsRecord;

import static models.db.test_schema.Tables.STUDENTS;
import static clients.DBClient.dbConnection;

public class Main {
    public static void main(String[] args) {
        DBClient dbClient = new DBClient();
        PostgreSQLTestContainer.runPostgreSQLContainer();
        dbConnection = dbClient.openDBConnection();
        DBClient.insertIntoTable(STUDENTS, new StudentsRecord(Integer.parseInt("1"), "Ivanov Petr", "IRT"));
        dbClient.closeDBConnection();
    }
}