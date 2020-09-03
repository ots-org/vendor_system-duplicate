package com.fuso.enterprise.ots.srv.server.dao;

import com.fuso.enterprise.ots.srv.api.model.domain.DonationResponseByStatus;
import com.fuso.enterprise.ots.srv.api.service.request.AddDonationtoRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetDonationByStatusRequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateDonationRequest;
import com.fuso.enterprise.ots.srv.api.service.response.GetDonationReportByDateResponse;
public interface DonationServiceDAO {
	String addNewDonation(AddDonationtoRequest addDonationtoRequest);
	GetDonationReportByDateResponse getDonationForUpdateStatus(GetDonationByStatusRequest donationByStatusRequest);
	String updateDonation(UpdateDonationRequest updateDonationRequest);
}
