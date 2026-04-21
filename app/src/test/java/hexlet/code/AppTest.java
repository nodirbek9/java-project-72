package hexlet.code;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {
    private Javalin app;

    @BeforeEach
    void setUp() {
        app = App.getApp();
    }

    @Test
    void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Анализатор страниц");
        });
    }

    @Test
    void testUrlsPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    void testCreateUrl() throws Exception {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=https://www.example.com";

            assertThat(client.post("/urls", requestBody).code()).isEqualTo(200);

            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://www.example.com");
        });
    }

    @Test
    void testCreateDuplicateUrl() throws Exception {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=https://www.example.com";

            assertThat(client.post("/urls", requestBody).code()).isEqualTo(200);
            assertThat(client.post("/urls", requestBody).code()).isEqualTo(200);
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://www.example.com");
        });
    }

    @Test
    void testCreateInvalidUrl() throws Exception {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=invalid-url";
            var response = client.post("/urls", requestBody);

            assertThat(response.code()).isEqualTo(422);
            assertThat(response.body().string()).contains("Некорректный URL");
        });
    }

    @Test
    void testShowUrl() throws Exception {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=https://www.example.com";
            client.post("/urls", requestBody);

            var response = client.get("/urls/1");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://www.example.com");
        });
    }

    @Test
    void testShowNonExistentUrl() throws Exception {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/999999");
            assertThat(response.code()).isEqualTo(404);
        });
    }
}
