package containers;

import org.testcontainers.containers.ContainerLaunchException;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ContainersLoader {

    private static final Map<String, GenericContainer<?>> INITIALIZED = new ConcurrentHashMap<>();
    private static final Logger logger = LogManager.getLogger(ContainersLoader.class);

    public static PostgreSQLTestContainer bootPostgresDB() {
        return LazyPostgresDBLoader.INSTANCE;
    }

    private static class LazyPostgresDBLoader {
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
                logger.error(INSTANCE.getLogs());
                throw new ContainerLaunchException(exception.getCause().toString());
            }

            INITIALIZED.put(INSTANCE.getContainerName(), INSTANCE);
        }
    }

    public static Network getNetwork() {
        return LazyNetworkLoader.INSTANCE;
    }

    private static class LazyNetworkLoader {
        private static final Network INSTANCE = Network.newNetwork();
    }


}
