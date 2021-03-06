package services.adapters.db;
// @author : Emmanuel
import services.entities.User;

import java.util.UUID;

public interface Database {
    User save(String name, String cpr, String bankAccountId) throws DatabaseError;

    boolean removeByCpr(String cpr);

    User findByCPR(String cpr);

    User findById(UUID id);

    void clear();

    class DatabaseError extends Exception {
        public DatabaseError(String message) {
            super(message);
        }
    }
}
