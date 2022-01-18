package dk.dtu.team14.adapters.db.implementations;

import dk.dtu.team14.adapters.db.Database;
import dk.dtu.team14.entities.User;

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
    public User save(String name, String cpr, String bankAccountId) {
        if (name == null || cpr == null || bankAccountId == null) {
            throw new IllegalArgumentException("All arguments must be non-null");
        }

        if (users.values().stream().anyMatch(user ->
                user.bankAccountId.equals(bankAccountId) ||
                        user.cpr.equals(cpr))
        ) {
            throw new IllegalArgumentException("CPR and bankAccountId has to be unique");
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
    public boolean retire(String cpr) {
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
    public User findById(String id)
    {
        return users.get(id);
    }
}
