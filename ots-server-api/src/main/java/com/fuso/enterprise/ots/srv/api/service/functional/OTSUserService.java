package com.fuso.enterprise.ots.srv.api.service.functional;
import com.fuso.enterprise.ots.srv.api.service.request.AddUserDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.MapUsersDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.MapUsersDataBOResponse;
import com.fuso.enterprise.ots.srv.api.model.domain.LoginAuthenticationRequest;
import com.fuso.enterprise.ots.srv.api.service.request.RequestBOUserBySearch;
import com.fuso.enterprise.ots.srv.api.service.response.LoginAuthenticationBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.UserDataBOResponse;


public interface OTSUserService {
	
	UserDataBOResponse getUserIDUsers(String userId);
	UserDataBOResponse getUserDetails(RequestBOUserBySearch requestBOUserBySearch);
	LoginAuthenticationBOResponse LoginAuthentication(LoginAuthenticationRequest loginAuthenticationRequest);
	UserDataBOResponse addNewUser(AddUserDataBORequest addUserDataBORequest);
	MapUsersDataBOResponse mappUser(MapUsersDataBORequest mapUsersDataBORequest);
}