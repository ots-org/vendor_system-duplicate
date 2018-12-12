
package com.org.enterprise.ots.srv.server.dao;

import java.util.List;

import com.org.enterprise.ots.srv.api.model.domain.ShikeishyoDetails;

public interface ShikeishyoDao {

    List<ShikeishyoDetails> getAllShikeishyoForUser(String userId, String userRole);
    
   // void testSystem(String userId);

}
