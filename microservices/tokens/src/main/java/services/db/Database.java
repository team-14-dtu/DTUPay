package services.db;


import services.exceptions.CanNotGenerateTokensException;
import services.exceptions.CustomerNotFoundException;

import java.util.List;
import java.util.UUID;
// @author : Naja
public interface Database {

    List<UUID> getTokens(UUID cid);

    void addTokens(UUID cid, List<UUID> tokens);

    List<UUID> generateNewTokens(UUID cid, int numberOfTokens) throws CanNotGenerateTokensException;

    UUID findCustomerFromTokenId(UUID tokenId) throws CustomerNotFoundException;

    void invalidateToken(UUID tokenId, UUID cid);
}
