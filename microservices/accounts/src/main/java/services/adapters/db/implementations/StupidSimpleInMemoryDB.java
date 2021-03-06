package services.adapters.db.implementations;
// @author : Petr
import services.adapters.db.Database;
import services.entities.User;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class StupidSimpleInMemoryDB implements Database {

    // Static to simulate single database
    private static final HashMap<UUID, User> users = new HashMap<>();

    @Override
    public User save(String name, String cpr, String bankAccountId) throws DatabaseError {
        System.out.println("saving to db");

        if (name == null || cpr == null || bankAccountId == null) {
            throw new DatabaseError("All arguments must be non-null");
        }

        if (users.values().stream().anyMatch(user ->
                user.bankAccountId.equals(bankAccountId) ||
                        user.cpr.equals(cpr))
        ) {
            throw new DatabaseError("CPR and bankAccountId has to be unique");
        }

        // This is unnecessary, UUID conflicts are like winning a lottery
        UUID newId;
        do {
            newId = UUID.randomUUID();
        } while (users.containsKey(newId));

        final var user = new User(newId, bankAccountId, name, cpr);
        users.put(newId, user);

        return user;
    }

    @Override
    public boolean removeByCpr(String cpr) {
        final var toRemove = users
                .values()
                .stream()
                .filter(user -> user.cpr.equals(cpr))
                .collect(Collectors.toList());

        boolean removedSomeone = false;
        for (User user : toRemove) {
            users.remove(user.id);
            removedSomeone = true;
        }

        return removedSomeone;
    }

    @Override
    public User findByCPR(String cpr) {
        List<User> foundUsers = users.values().stream().filter(u -> u.cpr.equals(cpr)).collect(Collectors.toList());

        User foundUser = null;

        if (foundUsers.size() != 0) {
            foundUser = foundUsers.get(0);
        }

        return foundUser;
    }

    @Override
    public User findById(UUID id) {
        return users.getOrDefault(id, null);
    }

    @Override
    public void clear() {
        users.clear();
    }
}
