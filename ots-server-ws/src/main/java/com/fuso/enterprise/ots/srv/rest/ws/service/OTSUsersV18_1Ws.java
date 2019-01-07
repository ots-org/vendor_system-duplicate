package com.fuso.enterprise.ots.srv.rest.ws.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;

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
	@ApiOperation(value = "get-userID-users", notes = "This operation will give the list of user based on userID", response = UserDetails.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	List<UserDetails> getUserIDUsers(@ApiParam(value = "userId", required = true) @NotNull @Valid @QueryParam("userId") String userId);
		
}
