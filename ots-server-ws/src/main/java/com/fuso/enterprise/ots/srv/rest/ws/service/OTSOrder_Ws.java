package com.fuso.enterprise.ots.srv.rest.ws.service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import com.fuso.enterprise.ots.srv.api.service.request.GetOrderBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetOrderByStatusRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetSchedulerRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetUserDetailsForRequest;
import com.fuso.enterprise.ots.srv.api.service.request.SaleVocherBoRequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateDonationRequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateForAssgineBOrequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateOrderDetailsRequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateOrderStatusRequest;
import com.fuso.enterprise.ots.srv.api.model.domain.AddScheduler;
import com.fuso.enterprise.ots.srv.api.model.domain.AssgineEmployeeModel;
import com.fuso.enterprise.ots.srv.api.model.domain.CloseOrderModelRequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddDonationtoRequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddOrUpdateOnlyOrderProductRequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddOrUpdateOrderProductBOrequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddSchedulerRequest;
import com.fuso.enterprise.ots.srv.api.service.request.CloseOrderBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.DirectSalesVoucherRequest;
import com.fuso.enterprise.ots.srv.api.service.request.EmployeeOrderTransferRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetAssginedOrderBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetCustomerOrderByStatusBOrequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetDonationByStatusRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetDonationReportByDateRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetEmployeeOrder;
import com.fuso.enterprise.ots.srv.api.service.request.GetListOfOrderByDateBORequest;

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
	
	@POST
    @Path("/getListOfOrderByDate")
	@ApiOperation(value = "getOrder", notes = "get Order For Customer By status", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response getListOfOrderByDateRequest(@ApiParam(value = "request", required = true) @NotNull  @Valid GetListOfOrderByDateBORequest  getListOfOrderByDateBORequest);

	@POST
    @Path("/salesVocher")
	@ApiOperation(value = "getOrder", notes = "Inserting the data when closeing order or derliverd the product to customer", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response saleVocher(@ApiParam(value = "request", required = true) @NotNull  @Valid SaleVocherBoRequest  saleVocherBoRequest);
	
	@POST
    @Path("/orderReportByDate")
	@ApiOperation(value = "getOrder", notes = "Inserting the data when closeing order or derliverd the product to customer", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response orderReportByDate(@ApiParam(value = "request", required = true) @NotNull  @Valid GetOrderBORequest  getOrderBORequest);
	
	@POST
    @Path("/insertScheduler")
	@ApiOperation(value = "getOrder", notes = "Inserting the data to scheduler", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response InsertScheduler(@ApiParam(value = "request", required = true) @NotNull  @Valid AddSchedulerRequest  addScheduler);
	
	
	@POST
    @Path("/getSchedulerByStatus")
	@ApiOperation(value = "getSchedulerByStatus", notes = "getting the data from scheduler", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response getScheduler(@ApiParam(value = "request", required = true) @NotNull  @Valid GetSchedulerRequest  getSchedulerRequest);
	
	@GET
    @Path("/checkSchedulerCronJob")
	@ApiOperation(value = "get-userID-users", notes = "This operation will give the list of user based on userID", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response CheckSchedulerCronJob();
	
	@POST
    @Path("/employeeTransferOrder")
	@ApiOperation(value = "employeeTransferOrder", notes = "change Order one employee to another", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response employeeTransferOrder(@ApiParam(value = "request", required = true) @NotNull  @Valid EmployeeOrderTransferRequest  employeeOrderTransferRequest);	

	@POST
    @Path("/updateOrderStatus")
	@ApiOperation(value = "getOrderByStatusAndDistributor", notes = "Getting getorder By Status and DistubutorId", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response getOrderByStatus(@ApiParam(value = "request", required = true) @NotNull  @Valid UpdateOrderStatusRequest updateOrderStatusRequest);	
	
	@POST
    @Path("/directSalesVoucherr")
	@ApiOperation(value = "getOrder", notes = "Inserting the data when closeing order or derliverd the product to customer", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response directSalesVoucher(@ApiParam(value = "request", required = true) @NotNull  @Valid DirectSalesVoucherRequest  DirectSalesVoucherRequest);
	
	@POST
    @Path("/getDonationListBystatus")
	@ApiOperation(value = "getOrder", notes = "Get List of donation by status", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response getDonationListBystatus(@ApiParam(value = "request", required = true) @NotNull  @Valid GetDonationByStatusRequest  donationByStatusRequest);
	
	@POST
    @Path("/addNewDonation")
	@ApiOperation(value = "getOrder", notes = "add List of donation by status", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response addNewDonation(@ApiParam(value = "request", required = true) @NotNull  @Valid AddDonationtoRequest  addDonationtoRequest);
	
	@POST
    @Path("/getDonationReportByDate")
	@ApiOperation(value = "getOrder", notes = "to Get Donation Report By Date", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response getDonationReportByDate(@ApiParam(value = "request", required = true) @NotNull  @Valid GetDonationReportByDateRequest  donationReportByDateRequest);

	@POST
    @Path("/getListOfOrderDetailsForRequest")
	@ApiOperation(value = "getOrder", notes = "to Get Donation Report By Date", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response getListOfOrderDetailsForRequest(@ApiParam(value = "request", required = true) @NotNull  @Valid GetUserDetailsForRequest  getUserDetailsForRequest);

	@POST
    @Path("/getDonationForUpdateStatus")
	@ApiOperation(value = "getOrder", notes = "to Get Donation Report By Date", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response getDonationForUpdateStatus(GetDonationByStatusRequest donationByStatusRequest);

	@POST
    @Path("/updateDonation")
	@ApiOperation(value = "getOrder", notes = "to Get Donation Report By Date", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response updateDonation(UpdateDonationRequest UpdateDonationRequest);
	
	@POST
    @Path("/donateDonation")
	@ApiOperation(value = "getOrder", notes = "to Get Donation Report By Date", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response donateDonation(SaleVocherBoRequest  saleVocherBoRequest);
	
	@POST
    @Path("/getRazorPayOrder")
	@ApiOperation(value = "getOrder", notes = "to Get Donation Report By Date", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response getRazorPayOrder(UpdateOrderDetailsRequest  updateOrderDetailsRequest);
}
