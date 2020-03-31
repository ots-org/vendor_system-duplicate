package com.fuso.enterprise.ots.srv.rest.ws.service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.fuso.enterprise.ots.srv.api.service.request.AddSubscriptionBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddUserDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetSubscriptionDetailsRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Validated
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "OTSSubscription_Ws", description = "This service provides the operations for OTS users subscription")
@Path("users_subscription")
@CrossOrigin
public interface OTSSubscription_Ws {
		
	@POST
	@Path("/addSubscriptionDetails")
	@ApiOperation(value = "addSubscriptionDetails", notes = "This operation will addSubscriptionDetails ", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response addNewSubscriptionDetails(@ApiParam(value = "request", required = true) @NotNull @Valid AddSubscriptionBORequest  addSubscriptionBORequest);
	
	@POST
	@Path("/getSubscriptionDetails")
	@ApiOperation(value = "addSubscriptionDetails", notes = "This operation will addSubscriptionDetails ", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response getSubscriptionDetails(@ApiParam(value = "request", required = true) @NotNull @Valid GetSubscriptionDetailsRequest  subscriptionDetailsRequest);
	
}
