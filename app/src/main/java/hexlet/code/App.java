package hexlet.code;

import io.javalin.Javalin;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) {
        Javalin app = getApp();
        app.start(getPort());
    }

    public static Javalin getApp() {
        var dataSource = getDataSource();

        try {
            runMigrations(dataSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
        });

        app.get("/", ctx -> ctx.result("Welcome to Hexlet!"));
        return app;
    }

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "8080");
        return Integer.valueOf(port);
    }

    private static HikariDataSource getDataSource() {
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

    private static void runMigrations(DataSource dataSource) throws SQLException {
        // schema.sql ni o'qish
        var inputStream = App.class.getClassLoader()
                .getResourceAsStream("schema.sql");

        var sql = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        // SQL ni bajarish
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }
}
