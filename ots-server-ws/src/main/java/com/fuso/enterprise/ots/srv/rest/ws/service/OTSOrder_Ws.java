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
import com.fuso.enterprise.ots.srv.api.service.request.GetOrderBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetOrderByStatusRequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateForAssgineBOrequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateOrderDetailsRequest;
import com.fuso.enterprise.ots.srv.api.model.domain.AssgineEmployeeModel;
import com.fuso.enterprise.ots.srv.api.model.domain.CloseOrderModelRequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddOrUpdateOnlyOrderProductRequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddOrUpdateOrderProductBOrequest;
import com.fuso.enterprise.ots.srv.api.service.request.CloseOrderBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetAssginedOrderBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetCustomerOrderByStatusBOrequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetEmployeeOrder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Validated
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "OTSOrder_Ws", description = "This service provides the operations for OTS Order")
@Path("order")
@CrossOrigin
public interface OTSOrder_Ws {

	@POST
    @Path("/getOrder")
	@ApiOperation(value = "getOrder", notes = "Getting getorder Request Based On userid and interval of time", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response getOrderList(@ApiParam(value = "request", required = true) @NotNull  @Valid GetOrderBORequest  getOrderBORequest);
	
	@POST
    @Path("/getOrderByStatusAndDistributor")
	@ApiOperation(value = "getOrderByStatusAndDistributor", notes = "Getting getorder By Status and DistubutorId", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response getOrderByStatusAndDistributor(@ApiParam(value = "request", required = true) @NotNull  @Valid GetOrderByStatusRequest  getOrderByStatusRequest);	

	@POST
    @Path("/insertOrderAndProduct")
	@ApiOperation(value = "addOrderAndProduct", notes = "inserting Order And Product as a order", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response insertOrderAndProduct(@ApiParam(value = "request", required = true) @NotNull  @Valid AddOrUpdateOrderProductBOrequest  addOrUpdateOrderProductBOrequest);	

	@POST
    @Path("/addUpdateOrderAndProduct")
	@ApiOperation(value = "addOrderAndProduct", notes = "Update OrderProduct for order", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response addOrderAndProduct(@ApiParam(value = "request", required = true) @NotNull  @Valid AddOrUpdateOnlyOrderProductRequest addOrUpdateOnlyOrderProductRequest);	

	@POST
    @Path("/UpdateOrder")
	@ApiOperation(value = "addOrderAndProduct", notes = "Update OrderProduct for order", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response UpdateOrder(@ApiParam(value = "request", required = true) @NotNull  @Valid UpdateOrderDetailsRequest updateOrderDetailsRequest);	

	@POST
    @Path("/updateOrderAssgin")
	@ApiOperation(value = "getOrder", notes = "update order for employee", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response updateAssginedOrder(@ApiParam(value = "request", required = true) @NotNull  @Valid UpdateForAssgineBOrequest  updateForAssgineBOrequest);
	
	@POST
    @Path("/getAssginedOrder")
	@ApiOperation(value = "getOrder", notes = "get Assgined order for employee", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response getAssginedOrder(@ApiParam(value = "request", required = true) @NotNull  @Valid GetAssginedOrderBORequest  getAssginedOrderBORequest);
	
	@POST
    @Path("/closeOrder")
	@ApiOperation(value = "getOrder", notes = "Closeing Order", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response closeOrder(@ApiParam(value = "request", required = true) @NotNull  @Valid CloseOrderBORequest closeOrderBORequest);
	
	@POST
    @Path("/getCustomerOrderStatus")
	@ApiOperation(value = "getOrder", notes = "get Order For Customer By status", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response getCustomerOrderStatus(@ApiParam(value = "request", required = true) @NotNull  @Valid GetCustomerOrderByStatusBOrequest getCustomerOrderByStatusBOrequest);
	
	@POST
    @Path("/getOrderDetailsByDate")
	@ApiOperation(value = "getOrder", notes = "get Order For Customer By status", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response getOrderDetailsByDate(@ApiParam(value = "request", required = true) @NotNull  @Valid GetOrderBORequest  getOrderBORequest);
	
}
