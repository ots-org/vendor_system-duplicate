package com.fuso.enterprise.ipt.srv.rest.ws.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.fuso.enterprise.ipt.srv.api.model.domain.BuyersGroupsDetails;
import com.fuso.enterprise.ipt.srv.api.model.domain.InputAddBuyerstoGroup;
import com.fuso.enterprise.ipt.srv.api.model.domain.InputForCancelPartOrder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Validated
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "BOM Related Services", description = "This service provides the operations for IPT Buyers")
@Path("buyers")
@CrossOrigin
public interface IPTBuyersV18_1Ws {
	
	@GET
    @Path("/get-BuyersList")
	@ApiOperation(value = "get-BuyersList", notes = "This operation will give the list of Buyers based on buyersgroup", response = BuyerDetails.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	List<BuyerDetails> getBuyersList(@ApiParam(value = "buyersGroup", required = true) @NotNull @Valid @QueryParam("buyersGroup") String buyersGroup);


	@GET
    @Path("/get-BuyerGroupsList")
	@ApiOperation(value = "get-BuyerGroupsList", notes = "This operation will give the list of BuyerGroups", response = BuyersGroupsDetails.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	List<BuyersGroupsDetails> getBuyerGroupsList();

	
	@POST
    @Path("/add_buyersto_group")
    @ApiOperation(value = "add_buyersto_group", notes = "This operation will d○	if BuyerGroup does not exist, it will create. users will be added to BuyerGroup", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response addBuyerstoGroup(@ApiParam(value = "addBuyerstoGroup", required = true) @NotNull @Valid InputAddBuyerstoGroup inputAddBuyerstoGroup);
	
	
	@GET
    @Path("/get_all_BuyersList")
	@ApiOperation(value = "get_all_BuyersList", notes = "This operation will give the list of All Buyers", response = BuyerDetails.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	List<BuyerDetails> getAllBuyersList();
	
	
	@POST
    @Path("/cancel_part_order")
    @ApiOperation(value = "cancel_part_order", notes = "This operation will cancel by proc team ", response = Response.class)
    @ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response cancelPartOrder(@ApiParam(value = "cancelPartOrder", required = true) @NotNull @Valid InputForCancelPartOrder inputForCancelPartOrder);
	
	
}
