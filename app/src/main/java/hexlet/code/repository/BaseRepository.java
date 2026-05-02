package hexlet.code.repository;

import javax.sql.DataSource;

public class BaseRepository {
    private static DataSource dataSource;

    public BaseRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static final DataSource getDataSource() {
        return dataSource;
    }
}
