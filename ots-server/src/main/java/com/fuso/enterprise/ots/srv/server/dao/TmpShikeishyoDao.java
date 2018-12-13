
package com.fuso.enterprise.ots.srv.server.dao;

import com.fuso.enterprise.ots.srv.api.model.domain.ShikeishyoDetails;
import com.fuso.enterprise.ots.srv.server.model.entity.TmpShikeisho;

public interface TmpShikeishyoDao {

    void createOrUpdateShikeishyo(ShikeishyoDetails shikeishyoDetails, boolean isNewShikeisho);

    TmpShikeisho readShikeishyo(String shikeishyoNo);

    void invokeStoredProcedure();

}
