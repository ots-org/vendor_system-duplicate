package com.fuso.enterprise.ots.srv.rest.ws.service;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;

import com.fuso.enterprise.ots.srv.api.model.domain.LoginAuthenticationRequest;
import com.fuso.enterprise.ots.srv.api.service.functional.OTSUserService;
import com.fuso.enterprise.ots.srv.api.service.request.AddUserDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.MapUsersDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.RequestBOUserBySearch;
import com.fuso.enterprise.ots.srv.api.service.response.LoginAuthenticationBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.MapUsersDataBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.UserDataBOResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.server.util.ResponseWrapper;
@Validated
@Service
public class OTSUsersV18_1WsImpl implements OTSUsersV18_1Ws{
	ResponseWrapper responseWrapper;
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Inject
	private OTSUserService otsUserService;

	@Override
	public Response getUserIDUsers(String userId) {
		Response response =null;
		logger.info("Inside Event=1007,Class:OTSUsersV18_1WsImpl, Method:getUserIDUsers, UserId:"+userId);
		UserDataBOResponse UserDataBOResponse = new UserDataBOResponse();
		try {
			UserDataBOResponse = otsUserService.getUserIDUsers(userId);
			if(UserDataBOResponse!=null) {
				logger.info("Inside Event=1,Class:OTSUsersV18_1WsImpl,Method:getUserIDUsers, "
						+ "UserList Size:" +UserDataBOResponse.getUserDetails().size());
			}
			response = buildResponse(UserDataBOResponse,"Successfull");
			
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
	public Response getUserDetails(RequestBOUserBySearch requestBOUserBySearch) {
		
	if(!requestBOUserBySearch.getRequestData().getSearchKey().isEmpty()|| !requestBOUserBySearch.getRequestData().getSearchKey().isEmpty())
		{ 
			Response response =null;
			logger.info("Inside Event=1008,Class:OTSUsersV18_1WsImpl, Method:getUserDetails, RequestBOUserBySearch:"+requestBOUserBySearch);
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
	public Response LoginAuthentication(LoginAuthenticationRequest loginAuthenticationRequest) {
		Response response =null;
		LoginAuthenticationBOResponse loginAuthenticationBOResponse =   otsUserService.LoginAuthentication(loginAuthenticationRequest);
		 response = buildResponse(loginAuthenticationBOResponse,"Successfull");
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




}
