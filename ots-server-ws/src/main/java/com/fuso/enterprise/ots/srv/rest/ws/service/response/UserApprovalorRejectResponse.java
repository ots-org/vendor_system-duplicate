package com.fuso.enterprise.ots.srv.rest.ws.service.response;

import java.util.List;

import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;

public class UserApprovalorRejectResponse {
	
	protected List<UserDetails> usersForRejectorApproval;

	public List<UserDetails> getUsersForRejectorApproval() {
		return usersForRejectorApproval;
	}

	public void setUsersForRejectorApproval(List<UserDetails> usersForRejectorApproval) {
		this.usersForRejectorApproval = usersForRejectorApproval;
	}
	
	

}
