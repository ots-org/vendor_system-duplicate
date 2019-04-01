package com.fuso.enterprise.ots.srv.rest.ws.service;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fuso.enterprise.ots.srv.api.model.domain.AssgineEmployeeModel;
import com.fuso.enterprise.ots.srv.api.model.domain.CloseOrderModelRequest;
import com.fuso.enterprise.ots.srv.api.service.functional.OTSOrderService;
import com.fuso.enterprise.ots.srv.api.service.request.AddOrUpdateOnlyOrderProductRequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddOrUpdateOrderProductBOrequest;
import com.fuso.enterprise.ots.srv.api.service.request.CloseOrderBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetAssginedOrderBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetCustomerOrderByStatusBOrequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetEmployeeOrder;
import com.fuso.enterprise.ots.srv.api.service.request.GetListOfOrderByDateBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetOrderBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetOrderByStatusRequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateForAssgineBOrequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateOrderDetailsRequest;
import com.fuso.enterprise.ots.srv.api.service.response.GetListOfOrderByDateBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.OrderDetailsBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.OrderProductBOResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.common.exception.ErrorEnumeration;
import com.fuso.enterprise.ots.srv.server.util.ResponseWrapper;

public class OTSOrder_WsImpl implements OTSOrder_Ws{
	
	
	@Inject
	OTSOrderService oTSOrderService;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	ResponseWrapper responseWrapper ;
	
	
	public Response buildResponse(Object data,String description) {
		ResponseWrapper wrapper = new ResponseWrapper(200,description, data);
		return Response.ok(wrapper).build();
	}
	
	public Response buildResponse(int code,String description) {
		ResponseWrapper wrapper = new ResponseWrapper(code,description);
		return Response.ok(wrapper).build();
	}
	
	
	@Override
	public Response getOrderList(GetOrderBORequest getOrderBORequest) {
		OrderDetailsBOResponse orderDetailsBOResponse = new OrderDetailsBOResponse();
		Response response = null;
		logger.info("Inside Event=1011,Class:OTSOrder_WsImpl, Method:getOrderList, addorUpdateProductBORequest:"
				+ getOrderBORequest);
		try {
			if(!getOrderBORequest.getRequest().getDistributorsId().equals(null)&&
					!getOrderBORequest.getRequest().getFromTime().equals(null)&&
					!getOrderBORequest.getRequest().getToTime().equals(null)){	
			    orderDetailsBOResponse = oTSOrderService.getOrderBydate(getOrderBORequest);
				if (!oTSOrderService.getOrderBydate(getOrderBORequest).getOrderDetails().get(0).equals(null)) {
					logger.info("Inside Event=1011,Class:OTSProduct_WsImpl,Method:getOrderList, " + "Successfull");
					response = buildResponse(orderDetailsBOResponse,"Successfull");
				}else{
					response = buildResponse(600,"No order from"+getOrderBORequest.getRequest().getFromTime()+"To"+getOrderBORequest.getRequest().getToTime());
				}
			}else{
				response = buildResponse(1001,"Check input");
			}
		} catch (BusinessException e) {
			throw new BusinessException(e, ErrorEnumeration.FAILURE_ORDER_GET);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.FAILURE_ORDER_GET);
		}
		return response;
	}

	@Override
	public Response getOrderByStatusAndDistributor(GetOrderByStatusRequest  getOrderByStatusRequest) {	
		OrderProductBOResponse orderProductBOResponse = new OrderProductBOResponse();
		Response response = null;
		
		response = buildResponse(orderProductBOResponse,"Successfull");
		
		logger.info("Inside Event=1020,Class:OTSProduct_WsImpl, Method:getOrderByStatusAndDistributor, getOrderByStatusRequest:"
				+ getOrderByStatusRequest);
		try {
			if(!getOrderByStatusRequest.getRequest().getDistrubitorId().equals(null))  {	
				orderProductBOResponse = oTSOrderService.getOrderByStatusAndDistributor(getOrderByStatusRequest);
				System.out.println(orderProductBOResponse.getOrderList().get(0).getOrderDate()+"in WS");
				
				if (!orderProductBOResponse.getOrderList().isEmpty()) {
					logger.info("Inside Event=1011,Class:OTSProduct_WsImpl,Method:getOrderList, " + "Successfull");
					response = buildResponse(orderProductBOResponse,"Successfull");
				}else{
					response = buildResponse(600,"No Order For You");
				}
			}
				else {
					response = buildResponse(1001,"Check input");
				}
				
			} catch (BusinessException e) {
				throw new BusinessException(e, ErrorEnumeration.FAILURE_ORDER_GET);
			} catch (Throwable e) {
				throw new BusinessException(e, ErrorEnumeration.FAILURE_ORDER_GET);
			}
		return response;
	}

	@Override
	public Response insertOrderAndProduct(AddOrUpdateOrderProductBOrequest addOrUpdateOrderProductBOrequest) {
		Response response = null;
		logger.info("Inside Event=1020,Class:OTSOrder_WsImpl,Method:insertOrderAndProduct,addOrUpdateOrderProductBOrequest " + addOrUpdateOrderProductBOrequest);
		try {	
			String ResponseValue = oTSOrderService.insertOrderAndProduct(addOrUpdateOrderProductBOrequest);
			response = buildResponse(200,ResponseValue);
		}catch(Exception e){
			throw new BusinessException(e, ErrorEnumeration.INPUT_PARAMETER_INCORRECT);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.INPUT_PARAMETER_INCORRECT);
		}
		 return response;
	}

	@Override
	public Response addOrderAndProduct(AddOrUpdateOnlyOrderProductRequest addOrUpdateOnlyOrderProductRequest) {
		Response response = null;
		logger.info("Inside Event=1019,Class:OTSOrder_WsImpl,Method:addOrderAndProduct,addOrUpdateOrderProductBOrequest " + addOrUpdateOnlyOrderProductRequest);
		try {	
			String ResponseValue = oTSOrderService.addOrUpdateOrderProduct(addOrUpdateOnlyOrderProductRequest);
			response = buildResponse(200,ResponseValue);
		}catch(Exception e)
		{
			throw new BusinessException(e, ErrorEnumeration.INPUT_PARAMETER_INCORRECT);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.INPUT_PARAMETER_INCORRECT);
		}
		 return response;
	}

	@Override
	public Response UpdateOrder(UpdateOrderDetailsRequest updateOrderDetailsRequest) {
		Response response = null;
		logger.info("Inside Event=1018,Class:OTSOrder_WsImpl,Method:UpdateOrder,addOrUpdateOrderProductBOrequest " + updateOrderDetailsRequest);
		
		try {	
			String ResponseValue = oTSOrderService.UpdateOrder(updateOrderDetailsRequest);
			response = buildResponse(200,ResponseValue);
		}catch(Exception e)
		{
			throw new BusinessException(e, ErrorEnumeration.INPUT_PARAMETER_INCORRECT);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.INPUT_PARAMETER_INCORRECT);
		}
		 return response;
	}

	@Override
	public Response updateAssginedOrder(UpdateForAssgineBOrequest  updateForAssgineBOrequest) {
		Response response = null;
		logger.info("Inside Event=1018,Class:OTSOrder_WsImpl,Method:UpdateOrder,addOrUpdateOrderProductBOrequest " + updateForAssgineBOrequest);
		
		try {	
			String ResponseValue = oTSOrderService.updateAssginedOrder(updateForAssgineBOrequest);;
			response = buildResponse(200,ResponseValue);
		}catch(Exception e){
			e.printStackTrace();
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_ORDER_INSERTION);
		} catch (Throwable e) {
			e.printStackTrace();
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_ORDER_INSERTION);
		}
		 return response;
	}

	@Override
	public Response getAssginedOrder(GetAssginedOrderBORequest getAssginedOrderBORequest) {
		Response response = null;
		logger.info("Inside Event=1018,Class:OTSOrder_WsImpl,Method:UpdateOrder,addOrUpdateOrderProductBOrequest " + getAssginedOrderBORequest);
		OrderProductBOResponse orderDetailsBOResponse = new OrderProductBOResponse();
		try {	
			orderDetailsBOResponse = oTSOrderService.getAssginedOrder(getAssginedOrderBORequest);
			if(!orderDetailsBOResponse.getOrderList().isEmpty()) {
				response = buildResponse(orderDetailsBOResponse,"Assgined Order For Employee");}
			else {
				response = buildResponse(400,"No Order For You");
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new BusinessException(e,ErrorEnumeration.FAILURE_ORDER_GET);
		} catch (Throwable e) {
			e.printStackTrace();
			throw new BusinessException(e,ErrorEnumeration.FAILURE_ORDER_GET);
		}
		 return response;
	}

	@Override
	public Response closeOrder(CloseOrderBORequest closeOrderBORequest) {
		Response response = null;
		try {	
			String ResponseDate = oTSOrderService.closeOrder(closeOrderBORequest);
			response = buildResponse(200,ResponseDate);
		}catch(Exception e){
			e.printStackTrace();
			throw new BusinessException(e, ErrorEnumeration.ORDER_CLOSE);
		} catch (Throwable e) {
			e.printStackTrace();
			throw new BusinessException(e, ErrorEnumeration.ORDER_CLOSE);
		}
		
		 return response;
	}


	@Override
	public Response getCustomerOrderStatus(GetCustomerOrderByStatusBOrequest getCustomerOrderByStatusBOrequest) {
		logger.info("Inside Event=1030,Class:OTSOrder_WsImpl,Method:getCustomerOrderStatus,getCustomerOrderByStatusBOrequest " + getCustomerOrderByStatusBOrequest);
		OrderProductBOResponse orderProductBOResponse = new OrderProductBOResponse();
		Response response = null;	
		try {
			orderProductBOResponse = oTSOrderService.getCustomerOrderStatus(getCustomerOrderByStatusBOrequest);
			if(orderProductBOResponse.getOrderList().size() == 0) {
				response = buildResponse(400,"No Order For You");
			}else {
				response = buildResponse(orderProductBOResponse,"Successfull");
			}
		}catch(Exception e) {
			e.printStackTrace();
			throw new BusinessException(e,ErrorEnumeration.FAILURE_ORDER_GET);
		}
		return response;
	}

	@Override
	public Response getOrderDetailsByDate(GetOrderBORequest getOrderBORequest) {
		OrderProductBOResponse orderProductBOResponse = new OrderProductBOResponse();
		Response response = null;
		logger.info("Inside Event=1011,Class:OTSOrder_WsImpl, Method:getOrderList, addorUpdateProductBORequest:"
				+ getOrderBORequest);
		try {
			if(!getOrderBORequest.getRequest().getDistributorsId().equals(null)&&
					!getOrderBORequest.getRequest().getFromTime().equals(null)&&
					!getOrderBORequest.getRequest().getToTime().equals(null)){	
				orderProductBOResponse = oTSOrderService.getOrderDetailsByDate(getOrderBORequest);
				if (!oTSOrderService.getOrderBydate(getOrderBORequest).getOrderDetails().get(0).equals(null)) {
					logger.info("Inside Event=1011,Class:OTSProduct_WsImpl,Method:getOrderList, " + "Successfull");
					response = buildResponse(orderProductBOResponse,"Successfull");
				}else{
					response = buildResponse(600,"No order from"+getOrderBORequest.getRequest().getFromTime()+"To"+getOrderBORequest.getRequest().getToTime());
				}
			}else{
				response = buildResponse(1001,"Check input");
			}
		} catch (BusinessException e) {
			throw new BusinessException(e, ErrorEnumeration.FAILURE_ORDER_GET);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.FAILURE_ORDER_GET);
		}
		return response;
	}

	@Override
	public Response getListOfOrderByDateRequest(GetListOfOrderByDateBORequest getListOfOrderByDateBORequest) {
		Response response;
		GetListOfOrderByDateBOResponse getListOfOrderByDateBOResponse = new GetListOfOrderByDateBOResponse();
		try {
			getListOfOrderByDateBOResponse=oTSOrderService.getListOfOrderByDate(getListOfOrderByDateBORequest);
			response = buildResponse(getListOfOrderByDateBOResponse,"Successfull");
			return response;
		}catch (BusinessException e) {
			throw new BusinessException(e, ErrorEnumeration.FAILURE_ORDER_GET);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.FAILURE_ORDER_GET);
		}
	}
		
}
