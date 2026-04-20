package hexlet.code.model;

import lombok.*;

import java.sql.Timestamp;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Url {
    private Long id;
    private String name;
    private Timestamp createdAt;
}
