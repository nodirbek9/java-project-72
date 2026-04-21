package hexlet.code.repository;

import javax.sql.DataSource;

public class BaseRepository {
    private DataSource dataSource;

    public BaseRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
