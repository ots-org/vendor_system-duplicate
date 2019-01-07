package com.fuso.enterprise.ots.srv.rest.ws.service;

import java.util.List;

import javax.inject.Inject;

import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;
import com.fuso.enterprise.ots.srv.api.service.functional.OTSUserService;

public class OTSUsersV18_1WsImpl implements OTSUsersV18_1Ws{
	
	@Inject
	private OTSUserService otsUserService;

	@Override
	public List<UserDetails> getUserIDUsers(String userId) {
		List<UserDetails> listofUsers = otsUserService.getUserIDUsers(userId);
		return listofUsers;
	}

}
