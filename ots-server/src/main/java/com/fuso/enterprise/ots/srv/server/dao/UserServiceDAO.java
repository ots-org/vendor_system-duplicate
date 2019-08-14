package com.fuso.enterprise.ots.srv.server.dao;

import java.util.List;

import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;
import com.fuso.enterprise.ots.srv.api.service.request.AddNewBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddUserDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.ChangePasswordRequest;
import com.fuso.enterprise.ots.srv.api.service.request.LoginAuthenticationBOrequest;
import com.fuso.enterprise.ots.srv.api.service.response.LoginUserResponse;
import com.fuso.enterprise.ots.srv.api.service.response.UserDataBOResponse;

public interface UserServiceDAO {
	
	List<UserDetails> getUserIdUsers(String userId);

	UserDataBOResponse addNewUser(AddUserDataBORequest addUserDataBORequest);
	
	LoginUserResponse otsLoginAuthentication(LoginAuthenticationBOrequest  loginAuthenticationBOrequest);
	
	Integer CheckForExists(AddNewBORequest addNewBORequest);
	
	UserDetails getUserDetails(Integer userId);

	UserDetails checkForOTP(String mobilenumber);

	String changePassword(ChangePasswordRequest changePasswordRequest);

	UserDetails getUserDetailsForEmployee(Integer userId);

	UserDetails getUserDetailsForCustomer(Integer userId);
}
