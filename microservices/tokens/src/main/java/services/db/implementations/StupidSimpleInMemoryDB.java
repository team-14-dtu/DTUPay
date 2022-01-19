package services.db.implementations;

import services.db.Database;
import services.exceptions.CustomerNotFoundException;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class StupidSimpleInMemoryDB implements Database {

    // Static to simulate single database
    private static final HashMap<UUID, List<UUID>> tokenDB = new HashMap<>();

    @Override
    public void invalidateToken(UUID tokenId, UUID cid) {
        List<UUID> tokenIds = tokenDB.get(cid);
        tokenIds.remove(tokenId);

        tokenDB.remove(cid);
        tokenDB.put(cid,tokenIds);
    }
    @Override
    public UUID findCustomerFromTokenId(UUID tokenId) throws CustomerNotFoundException {

        System.out.println("Looking for token: "+tokenId);

        for (UUID cid : tokenDB.keySet()) {
            if (tokenDB.get(cid).contains(tokenId)) {
                invalidateToken(tokenId, cid);
                System.out.println("Customer: " + cid.toString());
                return cid;
            }
        }
        String error = "Customer is not found";
        throw new CustomerNotFoundException(error);
    }

    /*@Override
    public User save(String name, String cpr, String bankAccountId) {
        System.out.println("saving to db");

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
        return users.get(id);
    }*/

    @Override
    public void clear() {
        tokenDB.clear();
    }
}
