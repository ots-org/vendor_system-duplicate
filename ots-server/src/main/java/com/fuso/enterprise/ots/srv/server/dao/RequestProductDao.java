package com.fuso.enterprise.ots.srv.server.dao;

import com.fuso.enterprise.ots.srv.api.model.domain.DonationBoResponse;
import com.fuso.enterprise.ots.srv.api.model.domain.DonationResponseByStatus;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderDetailsAndProductDetails;
import com.fuso.enterprise.ots.srv.api.service.request.AddDonationtoRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetDonationByStatusRequest;

public interface RequestProductDao {
	OrderDetailsAndProductDetails addOrUpdateRequest(OrderDetailsAndProductDetails orderDetailsAndProductDetails);
	DonationResponseByStatus getDonationListBystatus(GetDonationByStatusRequest donationByStatusRequest);
	DonationBoResponse addNewDonation(AddDonationtoRequest addDonationtoRequest);
}
