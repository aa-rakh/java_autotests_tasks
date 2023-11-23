package containers;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.time.Duration;

public class PostgreSQLTestContainer extends GenericContainer<PostgreSQLTestContainer> {

    public static final String SERVICE_NAME = "POSTGRESQL_CONTAINER";
    public final int postgresPort = 5432;

    @Override
    public void start() {
        super.addFixedExposedPort(postgresPort, postgresPort);
        super.start();
    }

    public static void runPostgreSQLContainer() {
        ContainersLoader.bootPostgresDBPortfolioSchema();
    }

    public String getConnectionList() {
        return this.getHost() + ":" + postgresPort;

    }

    public PostgreSQLTestContainer(Network network, String postgresUser, String postgresPassword, String postgresDb, String scriptName) {
        super(DockerImageName.parse(System.getProperty("postgresImage") == null ? "postgres:13.3" : System.getProperty("postgresImage")));

        this.withReuse(true);
        this.withNetwork(network);
        this.withCreateContainerCmdModifier(cmd -> cmd.withName(SERVICE_NAME));
        this.withCopyFileToContainer(MountableFile.forClasspathResource(scriptName), "/docker-entrypoint-initdb.d/init_postgresql.sql");
        this.withEnv("POSTGRES_PASSWORD", postgresPassword);
        this.waitingFor(
                Wait.forLogMessage(".*database system is ready to accept connections.*", 2).withStartupTimeout(Duration.ofMinutes(1))
        );
    }
}
