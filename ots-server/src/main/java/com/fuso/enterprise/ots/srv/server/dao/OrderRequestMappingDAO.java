package com.fuso.enterprise.ots.srv.server.dao;

import com.fuso.enterprise.ots.srv.api.model.domain.DonationResponseByStatus;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderDetailsAndProductDetails;
import com.fuso.enterprise.ots.srv.api.service.request.GetDonationByStatusRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetUserDetailsForRequest;
import com.fuso.enterprise.ots.srv.api.service.response.GetUserDetailsForResponse;

public interface OrderRequestMappingDAO {
	OrderDetailsAndProductDetails MapOrderAndRequest(OrderDetailsAndProductDetails orderDetailsAndProductDetails);
	GetUserDetailsForResponse getListOfOrderDetailsForRequest(GetUserDetailsForRequest getUserDetailsForRequest);
}
