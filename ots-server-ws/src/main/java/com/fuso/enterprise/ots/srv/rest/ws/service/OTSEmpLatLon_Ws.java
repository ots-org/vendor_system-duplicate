package com.fuso.enterprise.ots.srv.rest.ws.service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.fuso.enterprise.ots.srv.api.service.request.EmpLatLongBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetEmpLatLongBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.GetEmpLatLongBOResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Validated
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "OTSEmpLatLon_Ws", description = "This service provides the operations for OTS Employee Tracking")
@Path("track")
@CrossOrigin
public interface OTSEmpLatLon_Ws {
	
	@POST
    @Path("/updateEmpLatLong")
	@ApiOperation(value = "updateEmpLatLong", notes = "adding Latitude and longitude values", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response updateEmpLatLong(@ApiParam(value = "request", required = true) @NotNull  @Valid EmpLatLongBORequest  empLatLongBORequest);
	
	@POST
    @Path("/getEmpLatLong")
	@ApiOperation(value = "getEmpLatLong", notes = "adding Latitude and longitude values", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	GetEmpLatLongBOResponse getEmpLatLong(@ApiParam(value = "request", required = true) @NotNull  @Valid GetEmpLatLongBORequest  getEmpLatLongBORequest);

}
