package utils;


import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.*;

public class JooqUtils {

    public static void main(String[] args) throws Exception{
        generateSchema();
    }

    public static void generateSchema() throws Exception {
        Configuration configuration = new Configuration()
                .withJdbc(new Jdbc()
                        .withDriver("org.postgresql.Driver")
                        .withUrl("jdbc:postgresql://localhost:5432/test_db")
                        .withUser("test_user")
                        .withPassword("qwerty"))
                .withGenerator(new Generator()
                        .withDatabase(new Database()
                                .withName("org.jooq.meta.postgres.PostgresDatabase")
                                .withIncludes(".*")
                                .withExcludes("")
                                .withInputSchema("test_schema"))
                        .withTarget(new Target()
                                .withPackageName("models.db.test_schema")
                                .withDirectory("src/test/java")));

        GenerationTool.generate(configuration);
    }
}