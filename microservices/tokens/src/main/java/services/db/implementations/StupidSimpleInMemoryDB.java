package services.db.implementations;

import services.db.Database;
import services.exceptions.CanNotGenerateTokensException;
import services.exceptions.CustomerNotFoundException;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class StupidSimpleInMemoryDB implements Database {

    // Static to simulate single database
    private static final HashMap<UUID, List<UUID>> tokenDB = new HashMap<>();

    @Override
    public List<UUID> getTokens(UUID cid) {
        return tokenDB.get(cid);
    }

    @Override
    public void addTokens(UUID cid, List<UUID> tokens) {
        tokenDB.computeIfAbsent(cid, k -> new ArrayList<>());
        tokenDB.get(cid).addAll(tokens);
    }

    @Override
    public void invalidateToken(UUID tokenId, UUID cid) {
        tokenDB.get(cid).remove(tokenId);
    }

    @Override
    public UUID findCustomerFromTokenId(UUID tokenId) throws CustomerNotFoundException {
        System.out.println("Looking for token: " + tokenId);

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

    @Override
    public List<UUID> generateNewTokens(UUID cid, int numberOfTokens) throws CanNotGenerateTokensException {
        if (!tokenDB.containsKey(cid)) {
            tokenDB.put(cid, new ArrayList<>());
        }

        if (numberOfTokens > 5) {
            String errorMessage = "Requests of more than 5 tokens are not allowed";
            throw new CanNotGenerateTokensException(errorMessage);
        }

        List<UUID> currentTokensOfCustomer = tokenDB.get(cid);

        if (currentTokensOfCustomer.size() <= 1) {
            //Create tokens
            for (int i = 0; i < numberOfTokens; i++) {
                UUID newToken = UUID.randomUUID();
                tokenDB.get(cid).add(newToken);
            }
        } else {
            String errorMessage = "Customer has " + currentTokensOfCustomer.size() + " tokens and can therefore not request more";
            throw new CanNotGenerateTokensException(errorMessage);
        }
        return tokenDB.get(cid);
    }
}
