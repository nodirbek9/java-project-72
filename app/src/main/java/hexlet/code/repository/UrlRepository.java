package hexlet.code.repository;

import hexlet.code.model.Url;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class UrlRepository extends BaseRepository {
    public UrlRepository(DataSource dataSource) {
        super(dataSource);
    }

    public void save(Url url) throws SQLException {
        String sql = "INSERT INTO urls (name, created_at) VALUES (?, ?)";

        try (var conn = getDataSource().getConnection();
             var stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            var timestamp = Timestamp.valueOf(LocalDateTime.now());
            stmt.setString(1, url.getName());
            stmt.setTimestamp(2, timestamp);
            stmt.executeUpdate();

            var generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                url.setId(generatedKeys.getLong(1));
                url.setCreatedAt(timestamp.toLocalDateTime());
            }
        }
    }

    public Optional<Url> find(Long id) throws SQLException {
        String sql = "SELECT * FROM urls WHERE id = ?";

        try (var conn = getDataSource().getConnection();
             var stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            var rs = stmt.executeQuery();

            if (rs.next()) {
                var url = new Url(rs.getString("name"));
                url.setId(rs.getLong("id"));
                url.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return Optional.of(url);
            }

            return Optional.empty();
        }
    }

    public Optional<Url> findByName(String name) throws SQLException {
        String sql = "SELECT * FROM urls WHERE name = ?";

        try (var conn = getDataSource().getConnection();
             var stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            var rs = stmt.executeQuery();

            if (rs.next()) {
                var url = new Url(rs.getString("name"));
                url.setId(rs.getLong("id"));
                url.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return Optional.of(url);
            }

            return Optional.empty();
        }
    }

    public List<Url> findAll() throws SQLException {
        String sql = "SELECT * FROM urls ORDER BY created_at DESC";

        try (var conn = getDataSource().getConnection();
             var stmt = conn.createStatement()) {

            var rs = stmt.executeQuery(sql);
            var urls = new ArrayList<Url>();

            while (rs.next()) {
                var url = new Url(rs.getString("name"));
                url.setId(rs.getLong("id"));
                url.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                urls.add(url);
            }

            return urls;
        }
    }

}
