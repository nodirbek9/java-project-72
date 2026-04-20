package hexlet.code.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.sql.DataSource;

@AllArgsConstructor
@Getter
public class BaseRepository {
    private DataSource dataSource;


}
