
package com.org.enterprise.ots.srv.rest.ws.service;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.org.enterprise.ots.srv.api.model.domain.ShikeishyoDetails;
import com.org.enterprise.ots.srv.api.service.functional.IPTShikeishyoService;

@Validated
@Service
public class IPTShikeishyoResourceV18_1WsImpl implements IPTShikeishyoResourceV18_1Ws {

    @Inject
    private IPTShikeishyoService iptShikeishyoService;

    public Response createOrUpdateShikeishyo(ShikeishyoDetails shikeishyoDetails) {
        iptShikeishyoService.createOrUpdateShikeishyo(shikeishyoDetails);
        return Response.ok().build();
    }

    @Override
    public List<ShikeishyoDetails> getAllShikeishyoForUser(String userId, String userRole) {
        return iptShikeishyoService.getAllShikeishyoForUser(userId, userRole);
    }

	@Override
	public Response testSystem(String userId) {
		// TODO Auto-generated method stub
		iptShikeishyoService.testSystem(userId);
		return Response.ok().build();
	}

}
