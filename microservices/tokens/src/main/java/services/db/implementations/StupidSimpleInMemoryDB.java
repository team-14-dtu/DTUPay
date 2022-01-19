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
    public List<UUID> addTokens(UUID cid, List<UUID> tokens) {
        if (tokenDB.get(cid) == null) {
            tokenDB.put(cid,new ArrayList<>());
        }
        tokenDB.get(cid).addAll(tokens);
        return tokenDB.get(cid);
    }

    @Override
    public void invalidateToken(UUID tokenId, UUID cid) {
        tokenDB.get(cid).remove(tokenId);
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

    @Override
    public List<UUID> generateNewTokens(UUID cid, int numberOfTokens) throws CanNotGenerateTokensException {

        if (!tokenDB.containsKey(cid)) {
            tokenDB.put(cid, new ArrayList<>());
        }

        List<UUID> currentTokensOfCustomer = tokenDB.get(cid);

        if (currentTokensOfCustomer.size() <= 1) {

            //Create tokens
            System.out.println("Generating "+numberOfTokens+" new tokens");
            for (int i=0; i<numberOfTokens; i++ ) {
                UUID newToken = UUID.randomUUID();
                tokenDB.get(cid).add(newToken);
                System.out.println("Generated: "+cid+" Token: "+newToken);
            }
        } else {
            String errorMessage = "Customer has "+currentTokensOfCustomer.size()+" already and can therefore not request tokens";
            throw new CanNotGenerateTokensException(errorMessage);
        }
        return tokenDB.get(cid);
    }
    @Override
    public HashMap<UUID,List<UUID>> pr() {
        return tokenDB;
    }

    @Override
    public void clear() {
        tokenDB.clear();
    }
}
