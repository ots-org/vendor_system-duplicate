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

import com.fuso.enterprise.ots.srv.api.service.request.AddProductStockBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddorUpdateProductBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetProductStockList;
import com.fuso.enterprise.ots.srv.api.service.request.ProductDetailsBORequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Validated
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "OTSProduct_Ws", description = "This service provides the operations for OTS Product")
@Path("product")
@CrossOrigin
public interface OTSProduct_Ws {
	
	@POST
    @Path("/getProductList")
	@ApiOperation(value = "getProductList", notes = "Getting getProductList Request Based On Product Name and Id", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response getProductList(@ApiParam(value = "request", required = true) @NotNull  @Valid ProductDetailsBORequest  productDetailsBORequest);
	
	@POST
    @Path("/addOrUpdateProduct")
	@ApiOperation(value = "addOrUpdateProduct", notes = "adding or updating ProductDetails", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response addOrUpdateProduct(@ApiParam(value = "request", required = true) @NotNull  @Valid AddorUpdateProductBORequest  addorUpdateProductBORequest);

	@POST
	@Path("/addProductStock")
	@ApiOperation(value = "addOrUpdateProduct", notes = "This operation will add New product stock ", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response addProductStock(@ApiParam(value = "request", required = true) @NotNull @Valid AddProductStockBORequest addStockProductBORequest);

	@POST
	@Path("/getProductStockList")
	@ApiOperation(value = "getProductStock", notes = "This operation will fetch details about product stock ", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response getProductStockList(@ApiParam(value = "request", required = true) @NotNull @Valid GetProductStockList getProductStockList);
}
