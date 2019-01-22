package com.fuso.enterprise.ots.srv.rest.ws.service;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fuso.enterprise.ots.srv.api.service.functional.OTSUserService;
import com.fuso.enterprise.ots.srv.api.service.request.AddUserDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.MapUsersDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.MapUsersDataBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.UserDataBOResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.server.util.ResponseWrapper;

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
						+ "UserList Size:" +UserDataBOResponse.getUserdetails().size());
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
						+ UserDataBOResponse.getUserdetails().size());
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
