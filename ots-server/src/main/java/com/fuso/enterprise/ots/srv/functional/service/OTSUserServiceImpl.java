package com.fuso.enterprise.ots.srv.functional.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;
import com.fuso.enterprise.ots.srv.api.service.functional.OTSUserService;
import com.fuso.enterprise.ots.srv.api.service.response.UserDataBOResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.server.dao.UserServiceDAO;

@Service
@Transactional
public class OTSUserServiceImpl implements  OTSUserService{
	

	private UserServiceDAO userServiceDAO;
	
	@Inject
	public OTSUserServiceImpl(UserServiceDAO userServiceDAO) {
		this.userServiceDAO=userServiceDAO;
	}

	@Override
	public UserDataBOResponse getUserIDUsers(String userId) {
		UserDataBOResponse userDataBOResponse = new UserDataBOResponse();
		try {
			List<UserDetails> userDetailList= userServiceDAO.getUserIdUsers(userId);
			userDataBOResponse.setUserdetails(userDetailList);
		}catch(Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return userDataBOResponse;
	}

}
