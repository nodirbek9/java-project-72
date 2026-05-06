package hexlet.code;

import hexlet.code.config.DatabaseConfig;
import hexlet.code.config.TemplateConfig;
import hexlet.code.controller.UrlCheckController;
import hexlet.code.controller.UrlController;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.service.UrlCheckService;
import hexlet.code.service.UrlService;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import io.javalin.rendering.template.JavalinJte;

import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        Javalin app = getApp();
        app.start(getPort());
    }

    public static Javalin getApp() {
        var dataSource = DatabaseConfig.getDataSource();

        try {
            DatabaseConfig.runMigrations(dataSource);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to run migrations", e);
        }

        var urlRepository = new UrlRepository(dataSource);
        var urlCheckRepository = new UrlCheckRepository(dataSource);

        var urlService = new UrlService(urlRepository);
        var urlCheckService = new UrlCheckService(urlCheckRepository);

        var urlController = new UrlController(urlService, urlCheckService);
        var urlCheckController = new UrlCheckController(urlService, urlCheckService);

        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(TemplateConfig.createTemplateEngine()));
        });

        // Global exception handlers
        app.exception(RuntimeException.class, (e, ctx) -> {
            if (e.getMessage() != null && e.getMessage().contains("not found")) {
                ctx.status(HttpStatus.NOT_FOUND);
                ctx.result(e.getMessage());
            } else {
                ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
                ctx.result("Internal server error: " + e.getMessage());
            }
        });

        app.exception(SQLException.class, (e, ctx) -> {
            ctx.status(500);
            ctx.result("Database error: " + e.getMessage());
        });

        app.get("/", urlController::index);
        app.post("/urls", urlController::create);
        app.get("/urls", urlController::list);
        app.get("/urls/{id}", urlController::show);
        app.post("/urls/{id}/checks", urlCheckController::create);

        return app;
    }

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "8080");
        return Integer.valueOf(port);
    }
}
