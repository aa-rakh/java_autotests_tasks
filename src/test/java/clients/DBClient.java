package clients;

import containers.ContainersLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.conf.ParamType;
import org.jooq.impl.DSL;
import org.jooq.Result;
import org.postgresql.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBClient {
    private static final Logger logger = LogManager.getLogger(DBClient.class);
    private final String user = "test_user";
    private final String password = "qwerty";
    private Connection connection;
    public static Connection dbConnection;

    public Connection openDBConnection() {
        try {
            DriverManager.registerDriver(new Driver());
            connection = DriverManager.getConnection(ContainersLoader.getPostgreSQLConnectionList(), user, password);
            logger.info("Подключение к PostgreSQL создано");
        } catch (SQLException e) {
            logger.error("Ошибка подключения: " + e.getMessage());
        }
        return connection;
    }

    public void closeDBConnection() {
        try {
            connection.close();
        } catch (Exception exception) {
            logger.info("При запуске тестов не выполнялось подключение к БД");
            logger.info(exception);
        }

        logger.info("Соединение с PostgreSQL успешно закрыто");
    }

    public static Result<Record> insertIntoTable(Table table, Record record) {
        DSLContext create = DSL.using(dbConnection, SQLDialect.POSTGRES);

        String sql = create
                .insertInto(table)
                .set(record)
                .getSQL(ParamType.INLINED);

        logger.info("Запрос: " + sql);
        Result<Record> result = create.fetch(sql);
        logger.info("\n" + result);

        return result;
    }
}
