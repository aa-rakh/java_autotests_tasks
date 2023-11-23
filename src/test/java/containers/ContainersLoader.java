package containers;

import org.testcontainers.containers.ContainerLaunchException;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ContainersLoader {

    private static final Map<String, GenericContainer<?>> INITIALIZED = new ConcurrentHashMap<>();

    public static PostgreSQLTestContainer bootPostgresDBPortfolioSchema() {
        return LazyPostgresDBPortfolioSchemaLoader.INSTANCE;
    }

    private static class LazyPostgresDBPortfolioSchemaLoader {
        private static final PostgreSQLTestContainer INSTANCE;

        static {
            INSTANCE = new PostgreSQLTestContainer(
                    getNetwork(),
                    "test_user",
                    "qwerty",
                    "test_db",
                    "init_postgresql.sql");
            try {
                INSTANCE.start();
            } catch (Exception exception) {
                System.out.println(INSTANCE.getLogs());
                throw new ContainerLaunchException(exception.getCause().toString());
            }

            INITIALIZED.put(INSTANCE.getContainerName(), INSTANCE);
        }
    }

    public static String getPostgreSQLConnectionList() {
        return "jdbc:postgresql://" + bootPostgresDBPortfolioSchema().getConnectionList() + "/test_db";
    }

    public static Network getNetwork() {
        return LazyNetworkLoader.INSTANCE;
    }

    private static class LazyNetworkLoader {
        private static final Network INSTANCE = Network.newNetwork();
    }
}
