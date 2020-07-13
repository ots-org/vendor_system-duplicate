package com.fuso.enterprise.ots.srv.server.dao;

import com.fuso.enterprise.ots.srv.api.model.domain.DonationBoResponse;
import com.fuso.enterprise.ots.srv.api.model.domain.DonationResponseByStatus;
import com.fuso.enterprise.ots.srv.api.service.request.GetDonationByStatusRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetDonationReportByDateRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetUserDetailsForRequest;
import com.fuso.enterprise.ots.srv.api.service.response.GetDonationReportByDateResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetUserDetailsForResponse;

public interface DonationRequestMappingDAO {

	String addNewDonation(DonationBoResponse donationBoResponse);
	 GetDonationReportByDateResponse getDonationReportByDate(
				GetDonationReportByDateRequest donationReportByDateRequest);
	 GetDonationReportByDateResponse  getRequestByDonationId(GetDonationReportByDateResponse getDonationReportByDateResponse );
}