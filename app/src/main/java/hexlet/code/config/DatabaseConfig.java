package hexlet.code.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class DatabaseConfig {

    public static HikariDataSource getDataSource() {
        HikariConfig config = new HikariConfig();

        String jdbcUrl = System.getenv("JDBC_DATABASE_URL");

        if (jdbcUrl == null || jdbcUrl.isEmpty()) {
            config.setJdbcUrl("jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;");
            config.setUsername("");
            config.setPassword("");
        } else {
            config.setJdbcUrl(jdbcUrl);
        }

        return new HikariDataSource(config);
    }

    public static void runMigrations(DataSource dataSource) throws SQLException {
        var inputStream = DatabaseConfig.class.getClassLoader()
                .getResourceAsStream("schema.sql");

        var sql = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }
}
