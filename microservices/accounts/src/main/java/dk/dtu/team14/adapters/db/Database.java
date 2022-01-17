package dk.dtu.team14.adapters.db;

import dk.dtu.team14.entities.User;

public interface Database {
    User save(String name, String cpr, String bankAccountId);
    boolean retire(String cpr);

    User findByCPR(String cpr);
}
