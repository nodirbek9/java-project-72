package hexlet.code.service;

import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.util.HtmlParser;
import kong.unirest.Unirest;

import java.sql.SQLException;
import java.util.List;

public final class UrlCheckService {

    private final UrlCheckRepository urlCheckRepository;

    public UrlCheckService(UrlCheckRepository urlCheckRepository) {
        this.urlCheckRepository = urlCheckRepository;
    }

    public UrlCheck performCheck(String url, Long urlId) throws Exception {
        var response = Unirest.get(url).asString();
        var statusCode = response.getStatus();

        // Throw exception for 4xx and 5xx status codes
        if (statusCode >= 400) {
            throw new Exception("HTTP error: " + statusCode);
        }

        var body = response.getBody();

        var parsed = HtmlParser.parse(body);

        var urlCheck = new UrlCheck(
                statusCode,
                parsed.title(),
                parsed.h1(),
                parsed.description(),
                urlId
        );

        urlCheckRepository.save(urlCheck);
        return urlCheck;
    }

    public List<UrlCheck> findByUrlId(Long urlId) throws SQLException {
        return urlCheckRepository.findByUrlId(urlId);
    }

    public UrlCheck findLastCheckByUrlId(Long urlId) throws SQLException {
        return urlCheckRepository.findLastCheckByUrlId(urlId);
    }
}
