package com.fuso.enterprise.ots.srv.rest.ws.service;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fuso.enterprise.ots.srv.api.service.functional.OTSUserService;
import com.fuso.enterprise.ots.srv.api.service.response.UserDataBOResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.server.util.ResponseWrapper;

public class OTSUsersV18_1WsImpl implements OTSUsersV18_1Ws{
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
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
			response = buildResponse(UserDataBOResponse);
			
		}catch(BusinessException e) {
			throw new BusinessException(e.getMessage(), e);
		}catch(Throwable e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return response;
	}

	public Response buildResponse(Object data) {
		ResponseWrapper wrapper = new ResponseWrapper(200, data);
		return Response.ok(wrapper).build();
	}
	
}
