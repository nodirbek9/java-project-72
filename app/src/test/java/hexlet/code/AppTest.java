package hexlet.code;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {
    private Javalin app;
    private static MockWebServer mockServer;

    @BeforeAll
    static void beforeAll() throws Exception {
        mockServer = new MockWebServer();
        mockServer.start();
    }

    @AfterAll
    static void afterAll() throws Exception {
        mockServer.shutdown();
    }

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

            var postResponse = client.post("/urls", requestBody);
            assertThat(postResponse.code()).isIn(200, 302); // Accept both redirect and success

            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://www.example.com");
        });
    }

    @Test
    void testCreateDuplicateUrl() throws Exception {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=https://www.example.com";

            var firstPost = client.post("/urls", requestBody);
            assertThat(firstPost.code()).isIn(200, 302);

            var secondPost = client.post("/urls", requestBody);
            assertThat(secondPost.code()).isIn(200, 302);

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

    @Test
    void testCheckUrl() throws Exception {
        String html = "<html><head><title>Test Title</title>"
                + "<meta name=\"description\" content=\"Test Description\"></head>"
                + "<body><h1>Test H1</h1></body></html>";

        mockServer.enqueue(new MockResponse()
                .setBody(html)
                .setResponseCode(200));

        String mockUrl = mockServer.url("/").toString();

        JavalinTest.test(app, (server, client) -> {
            client.post("/urls", "url=" + mockUrl);

            var checkResponse = client.post("/urls/1/checks");
            assertThat(checkResponse.code()).isIn(200, 302);

            var showResponse = client.get("/urls/1");
            assertThat(showResponse.code()).isEqualTo(200);
            var body = showResponse.body().string();
            assertThat(body).contains("Test Title");
            assertThat(body).contains("Test H1");
            assertThat(body).contains("Test Description");
            assertThat(body).contains("200");
        });
    }

    @Test
    void testCheckUrlWithError() throws Exception {
        mockServer.enqueue(new MockResponse()
                .setResponseCode(404));

        String mockUrl = mockServer.url("/").toString();

        JavalinTest.test(app, (server, client) -> {
            client.post("/urls", "url=" + mockUrl);

            var checkResponse = client.post("/urls/1/checks");
            assertThat(checkResponse.code()).isIn(200, 302);

            var showResponse = client.get("/urls/1");
            assertThat(showResponse.code()).isEqualTo(200);
            var body = showResponse.body().string();
            // Check table should be empty (no checks created for errors)
            assertThat(body).contains("Проверки");
            assertThat(body).doesNotContain("<td>404</td>");
        });
    }

    @Test
    void testCheckUrlWithLongContent() throws Exception {
        String longTitle = "A".repeat(250);
        String longH1 = "B".repeat(250);
        String longDescription = "C".repeat(250);

        String html = "<html><head><title>" + longTitle + "</title>"
                + "<meta name=\"description\" content=\"" + longDescription + "\"></head>"
                + "<body><h1>" + longH1 + "</h1></body></html>";

        mockServer.enqueue(new MockResponse()
                .setBody(html)
                .setResponseCode(200));

        String mockUrl = mockServer.url("/").toString();

        JavalinTest.test(app, (server, client) -> {
            client.post("/urls", "url=" + mockUrl);
            client.post("/urls/1/checks");

            var showResponse = client.get("/urls/1");
            var body = showResponse.body().string();
            assertThat(body).contains("...");
        });
    }
}
