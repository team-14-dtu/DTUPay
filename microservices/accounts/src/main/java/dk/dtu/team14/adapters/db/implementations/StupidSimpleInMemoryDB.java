package dk.dtu.team14.adapters.db.implementations;

import dk.dtu.team14.adapters.db.Database;
import dk.dtu.team14.entities.User;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@ApplicationScoped
public class StupidSimpleInMemoryDB implements Database {

    // Static to simulate single database
    private static final HashMap<String, User> users = new HashMap<>();

    @Override
    public User save(String name, String cpr, String bankAccountId) {
        if (name == null || cpr == null || bankAccountId == null) {
            throw new IllegalArgumentException("All arguments must be non-null");
        }

        /*if (users.values().stream().anyMatch(user ->
                user.bankAccountId.equals(bankAccountId) ||
                        user.cpr.equals(cpr))
        ) {
            throw new IllegalArgumentException("CPR and bankAccountId has to be unique");
        }*/ //TODO: implement duplicate check again...


        // This is unnecessary, UUID conflicts are like winning a lottery
        String newId;
        do {
            newId = UUID.randomUUID().toString();
        } while (users.containsKey(newId));

        final var user = new User(newId, bankAccountId, name, cpr);
        users.put(newId, user);

        return user;
    }

    @Override
    public boolean retire(String userId) {
        var removedUser = users.remove(userId);
        return removedUser != null;
    }

    @Override
    public User findByCPR(String cpr) {
        List<User> foundUsers = users.values().stream().filter(u -> u.cpr.equals(cpr)).collect(Collectors.toList());

        User foundUser = null;

        if (foundUsers.size()!=0) {
            foundUser = foundUsers.get(0);
        }

        return foundUser;
    }
}
