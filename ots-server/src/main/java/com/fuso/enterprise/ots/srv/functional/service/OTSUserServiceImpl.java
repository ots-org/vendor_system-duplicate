package com.fuso.enterprise.ots.srv.functional.service;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;
import com.fuso.enterprise.ots.srv.api.service.functional.OTSUserService;
import com.fuso.enterprise.ots.srv.server.dao.UserServiceDAO;

@Service
@Transactional
public class OTSUserServiceImpl implements  OTSUserService{
	
private Logger logger = LoggerFactory.getLogger(OTSUserServiceImpl.class);
	
	private UserServiceDAO userServiceDAO;
	
	@Inject
	public OTSUserServiceImpl(UserServiceDAO userServiceDAO) {
		this.userServiceDAO=userServiceDAO;
	}

	@Override
	public List<UserDetails> getUserIDUsers(String userId) {
		return userServiceDAO.getUserIdUsers(userId);
	}

}
