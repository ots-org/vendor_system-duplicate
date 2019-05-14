package com.fuso.enterprise.ots.srv.api.service.functional;

import com.fuso.enterprise.ots.srv.api.service.request.AddUserDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.ApproveRegistrationBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.CustomerProductDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.LoginAuthenticationBOrequest;
import com.fuso.enterprise.ots.srv.api.service.request.MapUsersDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.LoginUserResponse;
import com.fuso.enterprise.ots.srv.api.service.response.MapUsersDataBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.OutstandingCustomerResponse;
import com.fuso.enterprise.ots.srv.api.model.domain.RejectUserModel;
import com.fuso.enterprise.ots.srv.api.service.request.AddNewBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.UserRegistrationBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.ApproveRegistrationResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetNewRegistrationResponse;
import com.fuso.enterprise.ots.srv.api.service.response.UserRegistrationResponce;
import com.fuso.enterprise.ots.srv.api.service.response.UserDataBOResponse;
import com.fuso.enterprise.ots.srv.api.service.request.RequestBOUserBySearch;
public interface OTSUserService {
	
	UserDataBOResponse getUserIDUsers(String userId);

	UserDataBOResponse addNewUser(AddUserDataBORequest addUserDataBORequest);

	String mappUser(MapUsersDataBORequest mapUsersDataBORequest);

	UserDataBOResponse getUserDetails(RequestBOUserBySearch requestBOUserBySearch);

	UserRegistrationResponce addUserRegistration(AddNewBORequest addNewBORequest);

	GetNewRegistrationResponse getNewRegistration(String mappedTo);

	LoginUserResponse otsLoginAuthentication(LoginAuthenticationBOrequest  loginAuthenticationBOrequest);

	String mapUserProduct(CustomerProductDataBORequest customerProductDataBORequest);

	ApproveRegistrationResponse approveRegistration(ApproveRegistrationBORequest approveRegistrationBORequest);

	UserDataBOResponse getUserDetailsByMapped(String mappedTo);
	
	UserDataBOResponse checkForUserExistsOrNot(AddUserDataBORequest addUserDataBORequest);

	OutstandingCustomerResponse getOutstandingData(String distributorId);

	String rejectUser(RejectUserModel rejectUserModel);

	
}
