package hexlet.code;

import hexlet.code.config.DatabaseConfig;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
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
    private static UrlCheckRepository urlCheckRepository;
    private static UrlRepository urlRepository;

    @BeforeAll
    static void beforeAll() throws Exception {
        mockServer = new MockWebServer();
        mockServer.start();

        var dataSource = DatabaseConfig.getDataSource();
        urlCheckRepository = new UrlCheckRepository(dataSource);
        urlRepository = new UrlRepository(dataSource);
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
            assertThat(response.code()).isEqualTo(HttpStatus.OK.getCode());
            assertThat(response.body().string()).contains("Анализатор страниц");
        });
    }

    @Test
    void testUrlsPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(HttpStatus.OK.getCode());
        });
    }

    @Test
    void testCreateUrl() throws Exception {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=https://www.example.com";

            var postResponse = client.post("/urls", requestBody);
            assertThat(postResponse.code()).isIn(HttpStatus.OK.getCode(), HttpStatus.FOUND.getCode());

            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(HttpStatus.OK.getCode());
            assertThat(response.body().string()).contains("https://www.example.com");
        });
    }

    @Test
    void testCreateDuplicateUrl() throws Exception {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=https://www.example.com";

            var firstPost = client.post("/urls", requestBody);
            assertThat(firstPost.code()).isIn(HttpStatus.OK.getCode(), HttpStatus.FOUND.getCode());

            var secondPost = client.post("/urls", requestBody);
            assertThat(secondPost.code()).isIn(HttpStatus.OK.getCode(), HttpStatus.FOUND.getCode());

            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(HttpStatus.OK.getCode());
            assertThat(response.body().string()).contains("https://www.example.com");
        });
    }

    @Test
    void testCreateInvalidUrl() throws Exception {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=invalid-url";
            var response = client.post("/urls", requestBody);

            assertThat(response.code()).isEqualTo(HttpStatus.UNPROCESSABLE_CONTENT.getCode());
            assertThat(response.body().string()).contains("Некорректный URL");
        });
    }

    @Test
    void testShowUrl() throws Exception {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=https://www.example.com";
            client.post("/urls", requestBody);

            // Get the URL from database
            var url = urlRepository.findByName("https://www.example.com");
            assertThat(url).isPresent();
            Long urlId = url.get().getId();

            var response = client.get("/urls/" + urlId);
            assertThat(response.code()).isEqualTo(HttpStatus.OK.getCode());
            assertThat(response.body().string()).contains("https://www.example.com");
        });
    }

    @Test
    void testShowNonExistentUrl() throws Exception {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/999999");
            assertThat(response.code()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
        });
    }

    @Test
    void testCheckUrl() throws Exception {
        String html = "<html><head><title>Test Title</title>"
                + "<meta name=\"description\" content=\"Test Description\"></head>"
                + "<body><h1>Test H1</h1></body></html>";

        mockServer.enqueue(new MockResponse()
                .setBody(html)
                .setResponseCode(HttpStatus.OK.getCode()));

        String mockUrl = mockServer.url("/").toString();
        // Normalize URL to match what's stored in database
        String normalizedUrl = mockUrl.replaceAll("/$", "");

        JavalinTest.test(app, (server, client) -> {
            client.post("/urls", "url=" + mockUrl);

            // Get URL ID from database using normalized URL
            var url = urlRepository.findByName(normalizedUrl);
            assertThat(url).isPresent();
            Long urlId = url.get().getId();

            var checkResponse = client.post("/urls/" + urlId + "/checks");
            assertThat(checkResponse.code()).isIn(HttpStatus.OK.getCode(), HttpStatus.FOUND.getCode());

            var showResponse = client.get("/urls/" + urlId);
            assertThat(showResponse.code()).isEqualTo(HttpStatus.OK.getCode());
            var body = showResponse.body().string();
            assertThat(body).contains("Test Title");
            assertThat(body).contains("Test H1");
            assertThat(body).contains("Test Description");
            assertThat(body).contains("200");

            // Verify data in database
            var checks = urlCheckRepository.findByUrlId(urlId);
            assertThat(checks).isNotEmpty();
            var check = checks.get(0);
            assertThat(check.getTitle()).isEqualTo("Test Title");
            assertThat(check.getH1()).isEqualTo("Test H1");
            assertThat(check.getDescription()).isEqualTo("Test Description");
            assertThat(check.getStatusCode()).isEqualTo(HttpStatus.OK.getCode());
        });
    }

    @Test
    void testCheckUrlWithError() throws Exception {
        mockServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.NOT_FOUND.getCode()));

        String mockUrl = mockServer.url("/").toString();
        String normalizedUrl = mockUrl.replaceAll("/$", "");

        JavalinTest.test(app, (server, client) -> {
            client.post("/urls", "url=" + mockUrl);

            // Get URL ID from database using normalized URL
            var url = urlRepository.findByName(normalizedUrl);
            assertThat(url).isPresent();
            Long urlId = url.get().getId();

            var checkResponse = client.post("/urls/" + urlId + "/checks");
            assertThat(checkResponse.code()).isIn(HttpStatus.OK.getCode(), HttpStatus.FOUND.getCode());

            var showResponse = client.get("/urls/" + urlId);
            assertThat(showResponse.code()).isEqualTo(HttpStatus.OK.getCode());
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
                .setResponseCode(HttpStatus.OK.getCode()));

        String mockUrl = mockServer.url("/").toString();
        String normalizedUrl = mockUrl.replaceAll("/$", "");

        JavalinTest.test(app, (server, client) -> {
            client.post("/urls", "url=" + mockUrl);

            // Get URL ID from database using normalized URL
            var url = urlRepository.findByName(normalizedUrl);
            assertThat(url).isPresent();
            Long urlId = url.get().getId();

            client.post("/urls/" + urlId + "/checks");

            var showResponse = client.get("/urls/" + urlId);
            var body = showResponse.body().string();
            assertThat(body).contains("...");
        });
    }
}
