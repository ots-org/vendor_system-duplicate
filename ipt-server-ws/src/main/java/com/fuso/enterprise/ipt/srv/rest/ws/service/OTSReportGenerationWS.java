package com.fuso.enterprise.ipt.srv.rest.ws.service;

import java.util.List;

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

import com.fuso.enterprise.ipt.srv.api.model.domain.BuyerDetails;
import com.fuso.enterprise.ipt.srv.api.model.domain.InputCustomerDetails;
import com.fuso.enterprise.ipt.srv.api.model.domain.InputForEmployeeOrders;
import com.fuso.enterprise.ipt.srv.server.model.entity.CustomerDetails;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Validated
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "Order Related Services", description = "This service provides the operations for generating and tracking the orders of Customer")
@Path("reports")
@CrossOrigin
public interface OTSReportGenerationWS {
	
	@POST
    @Path("/getEmployeeOrders")
	@ApiOperation(value = "getEmployeeOrders", notes = "This operation will fetch customer orders for an employee", response = InputCustomerDetails.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	InputCustomerDetails getEmployeeOrders(@ApiParam(value = "getEmployeeOrders", required = true) @NotNull @Valid InputForEmployeeOrders inputForEmployeeOrders);



}
