package com.fuso.enterprise.ots.srv.api.service.functional;

import com.fuso.enterprise.ots.srv.api.service.response.UserDataBOResponse;

public interface OTSUserService {
	
	UserDataBOResponse getUserIDUsers(String userId);

}
