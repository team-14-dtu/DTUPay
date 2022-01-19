package services.db;


import services.exceptions.CustomerNotFoundException;

import java.util.UUID;

public interface Database {


   /* User save(String name, String cpr, String bankAccountId);
    boolean removeByCpr(String cpr);

    User findByCPR(String cpr);
    User findById(UUID id);*/

    void invalidateToken(UUID tokenId, UUID cid);

    UUID findCustomerFromTokenId(UUID tokenId) throws CustomerNotFoundException;

    void clear();
}
