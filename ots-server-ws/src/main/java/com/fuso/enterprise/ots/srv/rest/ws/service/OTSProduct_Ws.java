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

import com.fuso.enterprise.ots.srv.api.model.domain.AddProductCategoryAndProductModelRequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddProductCategoryAndProductRequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddProductStockBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddorUpdateProductBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.AirTableRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetProductDetailsForBillRequst;
import com.fuso.enterprise.ots.srv.api.service.request.GetProductStockListRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetProductStockRequest;
import com.fuso.enterprise.ots.srv.api.service.request.ProductBulkUploadRequest;
import com.fuso.enterprise.ots.srv.api.service.request.ProductDetailsBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateProductStatusRequest;
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
	Response getProductList(@ApiParam(value = "request") ProductDetailsBORequest  productDetailsBORequest);
	
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
	Response getProductStockList(@ApiParam(value = "request", required = true) @NotNull @Valid GetProductStockListRequest getProductStockListRequest);

	@POST
	@Path("/getProductStock")
	@ApiOperation(value = "getProductStock", notes = "This operation will fetch details about productstock", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response getProductStock(@ApiParam(value = "request", required = true) @NotNull @Valid GetProductStockRequest getProductStockRequest);

	@POST
	@Path("/getProductDetailsForBill")
	@ApiOperation(value = "getProductStock", notes = "get ProductList For bill By orderId", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response getProductDetailsForBill(@ApiParam(value = "request", required = true) @NotNull @Valid GetProductDetailsForBillRequst getProductDetailsForBillRequst);
	
	@POST
	@Path("/updateProductStatus")
	@ApiOperation(value = "getProductStock", notes = "get ProductList For bill By orderId", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response UpdateProductStatus(@ApiParam(value = "request", required = true) @NotNull @Valid UpdateProductStatusRequest updateProductStatusRequestModel);

	@POST
	@Path("/productBulkUpload")
	@ApiOperation(value = "import_Product_excel", notes = "This operation will import product from excel and return a json of product list", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response productBulkUpload(
	@ApiParam(value = "PartsBase64Excel", required = true) @NotNull @Valid ProductBulkUploadRequest partsBase64Excel);

	@POST
	@Path("/addProductAndCategory")
	@ApiOperation(value = "addProductAndCategory", notes = "This operation will add category , sub category and product", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response addProductAndCategory(
	@ApiParam(value = "addProductAndCategory", required = true) @NotNull @Valid AddProductCategoryAndProductRequest addProductAndCategoryRequest);

	@POST
	@Path("/searchProduct")
	@ApiOperation(value = "searchProduct", notes = "This operation will search Product", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response searchProduct(
	@ApiParam(value = "addProductAndCategory", required = true) @NotNull @Valid ProductDetailsBORequest ProductDetailsBORequest);

	@POST
	@Path("/addAirTabelData")
	@ApiOperation(value = "getProductStock", notes = "get ProductList For bill By orderId", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response addAirTabelData(@ApiParam(value = "request", required = true) @NotNull @Valid AirTableRequest airTableRequest);
	
	@POST
    @Path("/airTabelCaluclation")
	@ApiOperation(value = "get-userID-users", notes = "this will do internal caluclation of air tabel", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response airTabelCaluclation(GetProductStockListRequest airTableRequest);

	
}
