package rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String name;
    private String id;

    public enum Type {
        CUSTOMER,
        MERCHANT,
        MANAGER,
    }
}
