package hexlet.code.service;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.UrlNormalizer;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UrlService {

    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public String normalizeUrl(String inputUrl) throws Exception {
        return UrlNormalizer.normalize(inputUrl);
    }

    public Optional<Url> findByName(String name) throws SQLException {
        return urlRepository.findByName(name);
    }

    public Url save(String normalizedUrl) throws SQLException {
        var url = new Url(normalizedUrl);
        urlRepository.save(url);
        return url;
    }

    public Optional<Url> findById(Long id) throws SQLException {
        return urlRepository.find(id);
    }

    public List<Url> findAll() throws SQLException {
        return urlRepository.findAll();
    }
}
