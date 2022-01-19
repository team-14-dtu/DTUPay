package services.db;


import services.exceptions.CanNotGenerateTokensException;
import services.exceptions.CustomerNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public interface Database {


   /* User save(String name, String cpr, String bankAccountId);
    boolean removeByCpr(String cpr);

    User findByCPR(String cpr);
    User findById(UUID id);*/

    void invalidateToken(UUID tokenId, UUID cid);

    UUID findCustomerFromTokenId(UUID tokenId) throws CustomerNotFoundException;

    List<UUID> generateNewTokens(UUID cid, int numberOfTokens) throws CanNotGenerateTokensException;

    List<UUID> getTokens(UUID cid);
    List<UUID> addTokens(UUID cid, List<UUID> tokens);

    HashMap<UUID,List<UUID>> pr();

    void clear();
}
