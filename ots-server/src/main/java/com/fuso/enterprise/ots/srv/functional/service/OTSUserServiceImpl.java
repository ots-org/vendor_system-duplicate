package com.fuso.enterprise.ots.srv.functional.service;

import java.util.List;

import javax.inject.Inject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fuso.enterprise.ots.srv.api.model.domain.LoginAuthenticationRequest;
import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;
import com.fuso.enterprise.ots.srv.api.service.functional.OTSUserService;
import com.fuso.enterprise.ots.srv.api.service.request.RequestBOUserBySearch;
import com.fuso.enterprise.ots.srv.api.service.response.LoginAuthenticationBOResponse;
import com.fuso.enterprise.ots.srv.api.service.request.AddUserDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.MapUsersDataBORequest;import com.fuso.enterprise.ots.srv.api.service.response.UserDataBOResponse;
import com.fuso.enterprise.ots.srv.server.dao.UserMapDAO;
import com.fuso.enterprise.ots.srv.server.dao.UserServiceDAO;import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.api.service.response.MapUsersDataBOResponse;import com.fuso.enterprise.ots.srv.server.dao.UserServiceDAO;
import com.fuso.enterprise.ots.srv.server.dao.UserServiceUtilityDAO;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;

@Service
@Transactional
public class OTSUserServiceImpl implements  OTSUserService{
	
	private UserMapDAO userMapDAO;	
	private UserServiceDAO userServiceDAO;
	private UserServiceUtilityDAO userServiceUtilityDAO;
	@Inject
	public OTSUserServiceImpl(UserServiceDAO userServiceDAO,UserServiceUtilityDAO userServiceUtilityDAO, UserMapDAO userMapDAO) {
		this.userServiceDAO=userServiceDAO;
		this.userServiceUtilityDAO = userServiceUtilityDAO;
		this.userMapDAO = userMapDAO;
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
	
	@Override
	public UserDataBOResponse getUserDetails(RequestBOUserBySearch requestBOUserBySearch) {
		UserDataBOResponse userDataBOResponse = new UserDataBOResponse();
		try {
			List<UserDetails> userDetailList= userServiceUtilityDAO.getUserDetails(requestBOUserBySearch);
			userDataBOResponse.setUserdetails(userDetailList);
		}catch(Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return userDataBOResponse;
	}

	@Override
	public LoginAuthenticationBOResponse LoginAuthentication(LoginAuthenticationRequest loginAuthenticationRequest) {
		LoginAuthenticationBOResponse loginAuthenticationBOResponse = new LoginAuthenticationBOResponse();
		UserDetails userDetails = userServiceUtilityDAO.LoginAuthentication(loginAuthenticationRequest);
		 loginAuthenticationBOResponse.setUserDetails(userDetails);
		 return loginAuthenticationBOResponse;
	}	
	@Override
	public UserDataBOResponse addNewUser(AddUserDataBORequest addUserDataBORequest) {
		UserDataBOResponse userDataBOResponse = new UserDataBOResponse();
		try {
			userDataBOResponse = userServiceDAO.addNewUser(addUserDataBORequest);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return userDataBOResponse;
	}

	@Override
	public MapUsersDataBOResponse mappUser(MapUsersDataBORequest mapUsersDataBORequest) {
		MapUsersDataBOResponse userMappingBOResponse = new MapUsersDataBOResponse();
		try {
			userMappingBOResponse = userMapDAO.mappUser(mapUsersDataBORequest);
            } catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return userMappingBOResponse;
	}
	

}
