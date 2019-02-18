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


import com.fuso.enterprise.ots.srv.api.service.request.BillDetailsBORequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Validated
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "OTSBill_Ws", description = "This service provides the operations for OTS Bill")
@Path("bill")
@CrossOrigin
public interface OTSBill_Ws {

	@POST
    @Path("/addOrUpdateBill")
	@ApiOperation(value = "addOrUpdateBill", notes = "adding or updating BillDetails", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response addOrUpdateBill(@ApiParam(value = "request", required = true) @NotNull  @Valid BillDetailsBORequest  billDetailsBORequest);
}
