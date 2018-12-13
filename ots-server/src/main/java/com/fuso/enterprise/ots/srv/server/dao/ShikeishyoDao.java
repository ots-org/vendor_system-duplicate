
package com.fuso.enterprise.ots.srv.server.dao;

import java.util.List;

import com.fuso.enterprise.ots.srv.api.model.domain.ShikeishyoDetails;

public interface ShikeishyoDao {

    List<ShikeishyoDetails> getAllShikeishyoForUser(String userId, String userRole);

}
