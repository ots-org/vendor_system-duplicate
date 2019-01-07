package com.fuso.enterprise.ots.srv.api.service.functional;

import java.util.List;

import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;

public interface OTSUserService {
	
	List<UserDetails> getUserIDUsers(String userId);

}
