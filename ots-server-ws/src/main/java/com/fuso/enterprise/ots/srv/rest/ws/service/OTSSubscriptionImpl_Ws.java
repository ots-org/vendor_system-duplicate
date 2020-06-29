package com.fuso.enterprise.ots.srv.rest.ws.service;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fuso.enterprise.ots.srv.api.service.functional.OTSSubscriptionService;
import com.fuso.enterprise.ots.srv.api.service.request.AddSubscriptionBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddUserDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetSubscriptionDetailsRequest;
import com.fuso.enterprise.ots.srv.server.util.ResponseWrapper;

public class OTSSubscriptionImpl_Ws implements OTSSubscription_Ws{

	private final Logger logger = LoggerFactory.getLogger(getClass());
	ResponseWrapper responseWrapper ;
	
	@Inject
	private OTSSubscriptionService oTSSubscriptionService;
	
	public Response buildResponse(Object data,String description) {
		ResponseWrapper wrapper = new ResponseWrapper(200,description, data);
		return Response.ok(wrapper).build();
	}
	
	public Response buildResponse(int code,String description) {
		ResponseWrapper wrapper = new ResponseWrapper(code,description);
		return Response.ok(wrapper).build();
	}
	

	@Override
	public Response addNewSubscriptionDetails(AddSubscriptionBORequest addSubscriptionBORequest) {
		Response response =null;
		response = buildResponse(oTSSubscriptionService.addUserSubscription(addSubscriptionBORequest),"successful");
		return response;
	}

	@Override
	public Response getSubscriptionDetails(GetSubscriptionDetailsRequest subscriptionDetailsRequest) {
		Response response =null;
		response = buildResponse(oTSSubscriptionService.getSubscriptionDetails(subscriptionDetailsRequest),"successful");
		return response;
	}

	@Override
	public Response getCurrentSubscriptionDetails(GetSubscriptionDetailsRequest subscriptionDetailsRequest) {
		Response response =null;
		response = buildResponse(oTSSubscriptionService.getCurrentSubscriptionDetails(subscriptionDetailsRequest),"successful");
		return response;
	}


}
