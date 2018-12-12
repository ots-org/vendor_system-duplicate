
package com.org.enterprise.ots.srv.server.dao;

import com.org.enterprise.ots.srv.api.model.domain.ShikeishyoDetails;
import com.org.enterprise.ots.srv.server.model.entity.TmpShikeisho;

public interface TmpShikeishyoDao {

    void createOrUpdateShikeishyo(ShikeishyoDetails shikeishyoDetails, boolean isNewShikeisho);

    TmpShikeisho readShikeishyo(String shikeishyoNo);

    void invokeStoredProcedure();

}
