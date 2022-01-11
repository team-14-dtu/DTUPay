package rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Automatic getter and setters and equals etc
@NoArgsConstructor // Needed for JSON deserialization and XML serialization and deserialization
@AllArgsConstructor
public class User {
    private String name;
    private String id;
}
