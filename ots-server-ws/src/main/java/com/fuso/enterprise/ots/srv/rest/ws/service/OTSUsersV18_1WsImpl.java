package com.fuso.enterprise.ots.srv.rest.ws.service;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;

import com.fuso.enterprise.ots.srv.api.model.domain.LoginAuthenticationModel;
import com.fuso.enterprise.ots.srv.api.service.functional.OTSUserService;
import com.fuso.enterprise.ots.srv.api.service.request.AddUserDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.LoginAuthenticationBOrequest;
import com.fuso.enterprise.ots.srv.api.service.request.MapUsersDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.LoginUserResponse;
import com.fuso.enterprise.ots.srv.api.service.response.MapUsersDataBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.UserDataBOResponse;
import com.fuso.enterprise.ots.srv.api.service.request.AddNewBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.MappedToBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.GetNewRegistrationResponse;

import com.fuso.enterprise.ots.srv.api.service.response.UserRegistrationResponce;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.server.util.ResponseWrapper;
import com.fuso.enterprise.ots.srv.api.service.request.RequestBOUserBySearch;

public class OTSUsersV18_1WsImpl implements OTSUsersV18_1Ws{
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	ResponseWrapper responseWrapper ;
	@Inject
	private OTSUserService otsUserService;

	@Override
	public Response getUserIDUsers(String userId) {
		Response response =null;
		logger.info("Inside Event=1,Class:OTSUsersV18_1WsImpl, Method:getUserIDUsers, UserId:"+userId);
		UserDataBOResponse UserDataBOResponse = new UserDataBOResponse();
		try {
			UserDataBOResponse = otsUserService.getUserIDUsers(userId);
			if(UserDataBOResponse!=null) {
				logger.info("Inside Event=1,Class:OTSUsersV18_1WsImpl,Method:getUserIDUsers, "
						+ "UserList Size:" +UserDataBOResponse.getUserDetails().size());
			}
			response = responseWrapper.buildResponse(UserDataBOResponse);
			
		}catch(BusinessException e) {
			throw new BusinessException(e.getMessage(), e);
		}catch(Throwable e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return response;
	}

	@Override
	public Response addNewUser(AddUserDataBORequest addUserDataBORequest) {
		Response response = null;
		logger.info("Inside Event=1004,Class:OTSUsersV18_1WsImpl, Method:addNewUser, UserDataBORequest:"
				+ addUserDataBORequest.getRequestData().getFirstName());
		UserDataBOResponse UserDataBOResponse = new UserDataBOResponse();
		try {
			UserDataBOResponse = otsUserService.addNewUser(addUserDataBORequest);
			if (UserDataBOResponse != null) {
				logger.info("Inside Event=1004,Class:OTSUsersV18_1WsImpl,Method:addNewUser, " + "UserList Size:"
						+ UserDataBOResponse.getUserDetails().size());
			}
			response = responseWrapper.buildResponse(UserDataBOResponse);
			} catch (BusinessException e) {
			throw new BusinessException(e.getMessage(), e);
		    } catch (Throwable e) {
			throw new BusinessException(e.getMessage(), e);
		    }
		      return response;
	}

	@Override
	public Response mappUser(MapUsersDataBORequest mapUsersDataBORequest) {
		Response response;
		MapUsersDataBOResponse userMappingBOResponse = new MapUsersDataBOResponse();
		logger.info("Inside Event=1005,Class:OTSUsersV18_1WsImpl, Method:mappUser, UserId:" + mapUsersDataBORequest.getRequestData().getUserId());
	  try {
			userMappingBOResponse = otsUserService.mappUser(mapUsersDataBORequest);
			if (userMappingBOResponse != null) {
				logger.info("Inside Event=1005,Class:OTSUsersV18_1WsImpl,Method:mappUser, " + "UserList Size:"
						+ userMappingBOResponse.getMappedMessage());
			}
			response = responseWrapper.buildResponse(userMappingBOResponse);
		} catch (BusinessException e) {
			throw new BusinessException(e.getMessage(), e);
		} catch (Throwable e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return response;
	}

	@Override
	public Response getUserDetails(RequestBOUserBySearch requestBOUserBySearch) {		
	if(!requestBOUserBySearch.getRequestData().getSearchKey().isEmpty()|| !requestBOUserBySearch.getRequestData().getSearchKey().isEmpty())
		{ 
			Response response =null;
			logger.info("Inside Event=VM1-T18 and VM1-T13,Class:OTSUsersV18_1WsImpl, Method:getUserDetails, RequestBOUserBySearch:"+requestBOUserBySearch);
			UserDataBOResponse UserDataBOResponse = new UserDataBOResponse();
			try {
				UserDataBOResponse = otsUserService.getUserDetails(requestBOUserBySearch);
				if(UserDataBOResponse!=null) {
					logger.info("Inside Event=1008,Class:OTSUsersV18_1WsImpl,Method:getUserDetails, "
							+ "UserList Size:" +UserDataBOResponse.getUserDetails().size());
				}
				if(UserDataBOResponse.getUserDetails().size() == 0) {
					response = buildResponse("input is not present in DB");
				}else{
					response = buildResponse(UserDataBOResponse,"Successfull");
				}
				
			}catch(BusinessException e) {
				throw new BusinessException(e.getMessage(), e);
			}catch(Throwable e) {
				throw new BusinessException(e.getMessage(), e);
			}
		
			return response;
		}else
		{
			Response response = buildResponse("Check Input");
			return response;
		}
	}

	@Override
	public Response addUserRegistration( AddNewBORequest addNewBORequest) {
		Response response;
		logger.info("Inside Event=1001,Class:OTSUsersV18_1WsImpl, addUserRegistration, "
				+ "addNewBORequest:"+addNewBORequest.getRequestData().getEmailId()+",FirstName:"+addNewBORequest.getRequestData().getFirstName()+",LastName:"+addNewBORequest.getRequestData().getLastName());
		UserRegistrationResponce userRegistrationResponce = new UserRegistrationResponce();
		try {
			userRegistrationResponce.setEmailId(otsUserService.addUserRegistration(addNewBORequest));  
			if(!userRegistrationResponce.getEmailId().isEmpty()) {
				logger.info("Inside Event=1001,Class:OTSUsersV18_1WsImpl,Method:addUserRegistration, "
						+ "UserEmail:" +userRegistrationResponce.getEmailId());
			}
			response = responseWrapper.buildResponse(userRegistrationResponce.getEmailId(),"User Successfully Added for Registration");
		}catch(BusinessException e) {
			throw new BusinessException(e.getMessage(), e);
		}catch(Throwable e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return response;
	}

	@Override
	public Response getNewRegistration(MappedToBORequest mappedToBORequest) {
		Response response = null;
		String strMessage = "There Are No Mapped Users";
		logger.info("Inside Event=1002,Class:OTSUsersV18_1WsImpl, addUserRegistration, "
				+ "addNewBORequest, getMappedUser for UserId:"+mappedToBORequest.getRequestData().getMappedTo());
		GetNewRegistrationResponse getNewRegistrationResponse = new GetNewRegistrationResponse();
		try {
			getNewRegistrationResponse = otsUserService.getNewRegistration(mappedToBORequest.getRequestData().getMappedTo());
			if(!getNewRegistrationResponse.getRegistorToUserDetails().isEmpty()) {
				logger.info("Inside Event=1002,Class:OTSUsersV18_1WsImpl,Method:getNewRegistration, "
						+ "UserListSize:" + getNewRegistrationResponse.getRegistorToUserDetails().size());
				strMessage ="User Retrieved Succesfully";
			}
			response = responseWrapper.buildResponse(getNewRegistrationResponse.getRegistorToUserDetails(),strMessage);
		}catch(BusinessException e) {
			throw new BusinessException(e.getMessage(), e);
		}catch(Throwable e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return response;
	}

	public Response buildResponse(Object data,String description) {
		ResponseWrapper wrapper = new ResponseWrapper(200,description, data);
		return Response.ok(wrapper).build();
	}
	
	public Response buildResponse(String description) {
		ResponseWrapper wrapper = new ResponseWrapper(600,description);
		return Response.ok(wrapper).build();
	}

	@Override
	public Response otsLoginAuthentication(LoginAuthenticationBOrequest loginAuthenticationBOrequest) {
		
		System.out.println("loginUserRequest++++++++++++++++++1"+loginAuthenticationBOrequest.getRequestData().getPassword());
		
		
		if(!loginAuthenticationBOrequest.getRequestData().getEmailId().isEmpty()|| !loginAuthenticationBOrequest.getRequestData().getPassword().isEmpty())
		{ 
			Response response =null;
			logger.info("Inside Event=VM1-T28,Class:OTSUsersV18_1WsImpl, Method:otsLoginAuthentication, loginAuthenticationBOrequest:"+loginAuthenticationBOrequest);
			LoginUserResponse loginUserResponse = new LoginUserResponse();
			try {
				loginUserResponse = otsUserService.otsLoginAuthentication(loginAuthenticationBOrequest);
				if(loginUserResponse!=null) {
					logger.info("Inside Event=1008,Class:OTSUsersV18_1WsImpl,Method:getUserDetails, "
							+ "UserData" +loginUserResponse.getUserDetails());
					System.out.println("loginUserResponse++++++++++++++++++2"+loginUserResponse.getUserDetails().getUsrPassword());
				}
				LoginAuthenticationModel loginAuth = new LoginAuthenticationModel();
				if(loginUserResponse.getUserDetails().getEmailId().isEmpty()||!loginAuthenticationBOrequest.getRequestData().getPassword().equals(loginUserResponse.getUserDetails().getUsrPassword())) {
					response = buildResponse("Password is Incorrect");
				}else{
					response = buildResponse(loginUserResponse,"Successfull");
				}
				
			}catch(BusinessException e) {
				throw new BusinessException(e.getMessage(), e);
			}catch(Throwable e) {
				throw new BusinessException(e.getMessage(), e);
			}
		
			return response;
		}else
		{
			Response response = buildResponse("Check Input");
			return response;
		}
	}
}
