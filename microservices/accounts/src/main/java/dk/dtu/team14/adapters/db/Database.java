package dk.dtu.team14.adapters.db;

import dk.dtu.team14.entities.User;

import java.util.UUID;

public interface Database {
    User save(String name, String cpr, String bankAccountId);
    boolean removeByCpr(String cpr);

    User findByCPR(String cpr);
    User findById(UUID id);

    void clear();
}
