package com.fuso.enterprise.ots.srv.api.service.response;

import java.util.List;
import com.fuso.enterprise.ots.srv.api.model.domain.CompleteOrderDetails;

public class GetListOfOrderByDateBOResponse {

	private List<CompleteOrderDetails> completeOrderDetails;

	public List<CompleteOrderDetails> getCompleteOrderDetails() {
		return completeOrderDetails;
	}

	public void setCompleteOrderDetails(List<CompleteOrderDetails> completeOrderDetails) {
		this.completeOrderDetails = completeOrderDetails;
	}

}
