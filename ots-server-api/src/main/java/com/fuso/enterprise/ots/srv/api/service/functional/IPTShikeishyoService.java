
package com.fuso.enterprise.ots.srv.api.service.functional;

import java.util.List;

import com.fuso.enterprise.ots.srv.api.model.domain.ShikeishyoDetails;

public interface IPTShikeishyoService {

    void createOrUpdateShikeishyo(ShikeishyoDetails shikeishyoDetails);

    List<ShikeishyoDetails> getAllShikeishyoForUser(String userId, String userRole);

}
