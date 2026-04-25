package hexlet.code;

import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlsPage;
import hexlet.code.dto.UrlWithLastCheck;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlRepository;
import hexlet.code.repository.UrlCheckRepository;
import io.javalin.Javalin;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import kong.unirest.Unirest;
import org.jsoup.Jsoup;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import io.javalin.rendering.template.JavalinJte;

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
            throw new RuntimeException("Failed to run migrations", e);
        }
        var urlRepository = new UrlRepository(dataSource);
        var urlCheckRepository = new UrlCheckRepository(dataSource);

        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        });

        app.get("/", ctx -> {
            ctx.render("index.jte", Collections.singletonMap("ctx", ctx));
        });

        app.post("/urls", ctx -> {
            String inputUrl = ctx.formParam("url");
            System.out.println("Received URL: " + inputUrl);  // ← LOG

            try {
                // URL ni parse qilish
                var uri = new URI(inputUrl);
                var url = uri.toURL();

                // Faqat protocol + host + port
                String normalizedUrl = url.getProtocol() + "://" + url.getHost();
                if (url.getPort() != -1) {
                    normalizedUrl += ":" + url.getPort();
                }
                System.out.println("Normalized URL: " + normalizedUrl);  // ← LOG

                // Mavjudligini tekshirish
                var existingUrl = urlRepository.findByName(normalizedUrl);

                if (existingUrl.isPresent()) {
                    System.out.println("URL already exists!");
                    ctx.sessionAttribute("flash", "Страница уже существует");
                    ctx.sessionAttribute("flashType", "info");
                    ctx.redirect("/urls/" + existingUrl.get().getId());
                } else {
                    // Yangi
                    System.out.println("Saving new URL...");
                    var newUrl = new Url(normalizedUrl);
                    urlRepository.save(newUrl);
                    System.out.println("Saved with ID: " + newUrl.getId());
                    ctx.sessionAttribute("flash", "Страница успешно добавлена");
                    ctx.sessionAttribute("flashType", "success");
                    ctx.redirect("/urls/" + newUrl.getId());
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());  // ← LOG
                e.printStackTrace();  // ← FULL ERROR
                ctx.status(422);
                ctx.sessionAttribute("flash", "Некорректный URL");
                ctx.sessionAttribute("flashType", "danger");
                ctx.render("index.jte", Collections.singletonMap("ctx", ctx));
            }
        });

        app.get("/urls", ctx -> {
            var urls = urlRepository.findAll();
            var urlsWithChecks = new ArrayList<UrlWithLastCheck>();

            for (var url : urls) {
                var lastCheck = urlCheckRepository.findLastCheckByUrlId(url.getId());
                urlsWithChecks.add(new UrlWithLastCheck(url, lastCheck));
            }

            var page = new UrlsPage(urlsWithChecks);
            var model = new HashMap<String, Object>();
            model.put("page", page);
            model.put("ctx", ctx);
            ctx.render("urls/index.jte", model);
        });


        app.get("/urls/{id}", ctx -> {
            Long id = ctx.pathParamAsClass("id", Long.class).get();
            var url = urlRepository.find(id);

            if (url.isPresent()) {
                var checks = urlCheckRepository.findByUrlId(id);
                var page = new UrlPage(url.get(), checks);
                var model = new HashMap<String, Object>();
                model.put("page", page);
                model.put("ctx", ctx);
                ctx.render("urls/show.jte", model);
            } else {
                ctx.status(404);
                ctx.result("URL not found");
            }
        });

        app.post("/urls/{id}/checks", ctx -> {
            Long id = ctx.pathParamAsClass("id", Long.class).get();
            var url = urlRepository.find(id);

            if (url.isEmpty()) {
                ctx.status(404);
                ctx.result("URL not found");
                return;
            }

            try {
                var response = Unirest.get(url.get().getName()).asString();
                var statusCode = response.getStatus();
                var body = response.getBody();

                var doc = Jsoup.parse(body);
                var title = doc.title();
                var h1Element = doc.selectFirst("h1");
                var h1 = h1Element != null ? h1Element.text() : "";
                var descriptionElement = doc.selectFirst("meta[name=description]");
                var description = descriptionElement != null ? descriptionElement.attr("content") : "";

                // Truncate to 200 characters
                if (title.length() > 200) {
                    title = title.substring(0, 200) + "...";
                }
                if (h1.length() > 200) {
                    h1 = h1.substring(0, 200) + "...";
                }
                if (description.length() > 200) {
                    description = description.substring(0, 200) + "...";
                }

                var urlCheck = new UrlCheck(statusCode, title, h1, description, id);
                urlCheckRepository.save(urlCheck);

                ctx.sessionAttribute("flash", "Страница успешно проверена");
                ctx.sessionAttribute("flashType", "success");
            } catch (Exception e) {
                ctx.sessionAttribute("flash", "Произошла ошибка при проверке");
                ctx.sessionAttribute("flashType", "danger");
            }

            ctx.redirect("/urls/" + id);
        });

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

    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        TemplateEngine templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);
        return templateEngine;
    }
}
