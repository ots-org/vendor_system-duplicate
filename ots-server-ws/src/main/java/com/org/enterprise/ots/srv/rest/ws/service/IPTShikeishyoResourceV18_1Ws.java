
package com.org.enterprise.ots.srv.rest.ws.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.validation.annotation.Validated;

import com.org.enterprise.ots.srv.api.model.domain.ShikeishyoDetails;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Validated
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "IPTShikeishyoResourceV18_1Ws", description = "This service provides the operations of shikeishyo")
@Path("shikeishyo")
public interface IPTShikeishyoResourceV18_1Ws {

    @POST
    @Path("/createOrUpdateShikeishyo")
    @ApiOperation(value = "createOrUpdateShikeishyo", notes = "This operation will create or update shikeishyo details", response = String.class)
    @ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
    Response createOrUpdateShikeishyo(@ApiParam(value = "shikeishyoDetails", required = true) @NotNull @Valid ShikeishyoDetails shikeishyoDetails);

    @GET
    @Path("/get-all-shikeishyos")
    @ApiOperation(value = "get-all-shikeishyos", notes = "This operation will give the list of shikeishyo for user", response = ShikeishyoDetails.class)
    @ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
    List<ShikeishyoDetails> getAllShikeishyoForUser(@ApiParam(value = "userId", required = true) @NotNull @Valid @QueryParam("userId") String userId,
            @ApiParam(value = "userRole", required = true) @NotNull @Valid @QueryParam("userRole") String userRole);
    
    
    @GET
    @Path("/test-vendor-system")
    @ApiOperation(value = "test-vendor-system", notes = "This operation will test the system", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
    Response testSystem(@ApiParam(value = "userId", required = true) @NotNull @Valid @QueryParam("userId") String userId);
    
    

}
