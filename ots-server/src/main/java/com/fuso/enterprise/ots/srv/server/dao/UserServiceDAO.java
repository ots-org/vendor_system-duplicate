package com.fuso.enterprise.ots.srv.server.dao;

import java.util.List;
import java.util.Map;

import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;
import com.fuso.enterprise.ots.srv.api.service.request.AddNewBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddUserDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.ChangePasswordRequest;
import com.fuso.enterprise.ots.srv.api.service.request.CustomerDetailsForLoginRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetSellerByLatAndLonRequest;
import com.fuso.enterprise.ots.srv.api.service.request.LoginAuthenticationBOrequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdatePassword;
import com.fuso.enterprise.ots.srv.api.service.request.UpdatePasswordRequest;
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

	UserDataBOResponse updateUser(AddUserDataBORequest addUserDataBORequest);

	String updatePassword(UpdatePasswordRequest updatePasswordRequest);
	
	UserDetails getCustomerDetailsForLogin(CustomerDetailsForLoginRequest customerDetailsForLoginRequest);
	
	List<Map<String, Object>> getSellerDetails(GetSellerByLatAndLonRequest getSellerByLatAndLonRequest);
}
