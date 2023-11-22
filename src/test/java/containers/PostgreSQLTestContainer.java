package containers;

import org.testcontainers.containers.ContainerLaunchException;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PostgreSQLTestContainer extends PostgreSQLContainer<PostgreSQLTestContainer> {
    private static final Map<String, GenericContainer<?>> INITIALIZED = new ConcurrentHashMap<>();

    public static final String SERVICE_NAME = "POSTGRESQL_CONTAINER";
    public final int postgresPort = 5432;

    @Override
    public void start() {
        super.addFixedExposedPort(postgresPort, postgresPort);
        super.start();
    }

    public static void runPostgreSQLContainer() {
        bootPostgresDBPortfolioSchema();
    }

    public static PostgreSQLContainer bootPostgresDBPortfolioSchema() {
        return LazyPostgresDBPortfolioSchemaLoader.INSTANCE;
    }

    private static class LazyPostgresDBPortfolioSchemaLoader {
        private static final PostgreSQLTestContainer INSTANCE;

        static {
            INSTANCE = new PostgreSQLTestContainer(
                    Network.newNetwork(),
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

    public PostgreSQLTestContainer(Network network, String postgresUser, String postgresPassword, String postgresDb, String scriptName) {
        super(DockerImageName.parse(System.getProperty("postgresImage") == null ? "postgres:13.3" : System.getProperty("postgresImage")));

        this.withReuse(true);
        this.withNetwork(network);
        this.withCreateContainerCmdModifier(cmd -> cmd.withName(SERVICE_NAME));
        this.withCopyFileToContainer(MountableFile.forClasspathResource(scriptName), "/docker-entrypoint-initdb.d/init_postgresql.sql");
        this.withEnv("POSTGRES_USER", postgresUser);
        this.withEnv("POSTGRES_PASSWORD", postgresPassword);
        this.withEnv("POSTGRES_DB", postgresDb);
        this.waitingFor(
                Wait.forLogMessage(".*database system is ready to accept connections.*", 2)
        );
    }
}
