package com.fuso.enterprise.ots.srv.api.service.response;

import java.util.List;
import com.fuso.enterprise.ots.srv.api.model.domain.DonationBoResponse;
import com.fuso.enterprise.ots.srv.api.model.domain.DonationModel;;

public class GetDonationReportByDateResponse {

	List<DonationModel> donationList;

	public List<DonationModel> getDonationList() {
		return donationList;
	}

	public void setDonationList(List<DonationModel> donationList) {
		this.donationList = donationList;
	}
	
}
