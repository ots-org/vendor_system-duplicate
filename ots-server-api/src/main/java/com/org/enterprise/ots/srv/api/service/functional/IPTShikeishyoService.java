
package com.org.enterprise.ots.srv.api.service.functional;

import java.util.List;

import com.org.enterprise.ots.srv.api.model.domain.ShikeishyoDetails;

public interface IPTShikeishyoService {

    void createOrUpdateShikeishyo(ShikeishyoDetails shikeishyoDetails);

    List<ShikeishyoDetails> getAllShikeishyoForUser(String userId, String userRole);

	void testSystem(String userId);

}
