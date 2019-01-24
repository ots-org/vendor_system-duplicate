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

import com.fuso.enterprise.ots.srv.api.service.request.AddUserDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.MapUsersDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.RequestBOUserBySearch;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Validated
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "OTSUsersV18_1Ws", description = "This service provides the operations for OTS users")
@Path("users")
@CrossOrigin
public interface OTSUsersV18_1Ws {
	
	@GET
    @Path("/get-userID-users")
	@ApiOperation(value = "get-userID-users", notes = "This operation will give the list of user based on userID", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response getUserIDUsers(@ApiParam(value = "userId", required = true) @NotNull @Valid @QueryParam("userId") String userId);
	
	@POST
	@Path("/addNewUser")
	@ApiOperation(value = "addNewUser", notes = "This operation will add New user ", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response addNewUser(@ApiParam(value = "request", required = true) @NotNull @Valid AddUserDataBORequest addUserDataBORequest);
	
	@POST
	@Path("/mappUser")
	@ApiOperation(value = "mappUser", notes = "This operation will add New user ", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response mappUser(@ApiParam(value = "request", required = true) @NotNull @Valid MapUsersDataBORequest mapUsersDataBORequest);

	@POST
    @Path("/getUserDetails")
	@ApiOperation(value = "getUserDetails", notes = "Getting User Request Based On Name, Email and getUserDetails", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response getUserDetails(@ApiParam(value = "request", required = true) @NotNull  @Valid RequestBOUserBySearch  requestBOUserBySearch);		

		
}
