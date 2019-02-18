package com.fuso.enterprise.ots.srv.functional.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderDetailsAndProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.UpdateOrderDetailsModelRequest;
import com.fuso.enterprise.ots.srv.api.service.functional.OTSOrderService;
import com.fuso.enterprise.ots.srv.api.service.request.AddOrUpdateOnlyOrderProductRequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddOrUpdateOrderProductBOrequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetOrderBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetOrderByStatusRequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateOrderDetailsRequest;
import com.fuso.enterprise.ots.srv.api.service.response.OrderDetailsBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.OrderProductBOResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.common.exception.ErrorEnumeration;
import com.fuso.enterprise.ots.srv.server.dao.OrderProductDAO;
import com.fuso.enterprise.ots.srv.server.dao.OrderServiceDAO;

@Service
@Transactional
public class OTSOrderServiceImpl implements OTSOrderService {
	private OrderServiceDAO orderServiceDAO; 
	private OrderProductDAO orderProductDao;

	@Inject
	public OTSOrderServiceImpl(OrderServiceDAO orderServiceDAO , OrderProductDAO orderProductDao)
	{
		this.orderServiceDAO = orderServiceDAO ;
		this.orderProductDao = orderProductDao;
	}

	
	@Override
	public OrderDetailsBOResponse getOrderBydate(GetOrderBORequest getOrderBORequest) {
		try {
			OrderDetailsBOResponse orderDetailsBOResponse = new OrderDetailsBOResponse();
			orderDetailsBOResponse.setOrderDetails(orderServiceDAO.getOrderBydate(getOrderBORequest));
			return orderDetailsBOResponse;
		}catch(Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
	}
	@Override
	public OrderProductBOResponse getOrderByStatusAndDistributor(GetOrderByStatusRequest getOrderByStatusRequest) {	
		try {
			OrderProductBOResponse orderProductBOResponse = new OrderProductBOResponse();
			List<OrderDetails> OrderDetailsList = orderServiceDAO.getOrderIdByDistributorId(getOrderByStatusRequest);
			List<OrderDetailsAndProductDetails> GetOrderDetailsAndProductDetails = new ArrayList<OrderDetailsAndProductDetails>();
			for (int i = 0; i <OrderDetailsList.size() ; i++)
			{
			List<OrderProductDetails> OrderProductDetailsList = orderProductDao.getUserByStatuesAndDistributorId(OrderDetailsList.get(i));
			GetOrderDetailsAndProductDetails.add(AddProductAndOrderDetailsIntoResponse(OrderDetailsList.get(i),OrderProductDetailsList));
			}
			orderProductBOResponse.setOrderList(GetOrderDetailsAndProductDetails);
			return orderProductBOResponse;
		}catch(Exception e){
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_ORDER_INSERTION);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_ORDER_INSERTION);
		}
	}
	
	private OrderDetailsAndProductDetails AddProductAndOrderDetailsIntoResponse(OrderDetails orderDetails,List<OrderProductDetails> OrderProductDetails)
	{
		OrderDetailsAndProductDetails orderDetailsAndProductDetails = new OrderDetailsAndProductDetails();
		orderDetailsAndProductDetails.setOrderId(orderDetails.getOrderId());
		orderDetailsAndProductDetails.setDistributorId(orderDetails.getDistributorId());
		orderDetailsAndProductDetails.setCustomerId(orderDetails.getCustomerId());
		orderDetailsAndProductDetails.setOrderNumber(orderDetails.getOrderCost());
		orderDetailsAndProductDetails.setAssignedId(orderDetails.getAssignedId());
		orderDetailsAndProductDetails.setOrderCost(orderDetails.getOrderCost());
		orderDetailsAndProductDetails.setOrderStatus(orderDetails.getStatus());
		orderDetailsAndProductDetails.setOrderdProducts(OrderProductDetails);
		return orderDetailsAndProductDetails;
	}


	@Override
	public String insertOrderAndProduct(AddOrUpdateOrderProductBOrequest addOrUpdateOrderProductBOrequest) {
		String Response;
		try {
			Integer orderId = orderServiceDAO.insertOrderAndGetOrderId(addOrUpdateOrderProductBOrequest);
			try {
				for(int i=0 ; i <addOrUpdateOrderProductBOrequest.getRequest().getProductList().size() ; i++)
				{
					orderProductDao.insertOrdrerProductByOrderId(orderId, addOrUpdateOrderProductBOrequest.getRequest().getProductList().get(i));	
				}
				Response = "Order Placed and OrderId Is"+orderId;
				return Response;
			}catch(Exception e){
				throw new BusinessException(e, ErrorEnumeration.INPUT_PARAMETER_INCORRECT);
			} catch (Throwable e) {
				throw new BusinessException(e, ErrorEnumeration.INPUT_PARAMETER_INCORRECT);
			}
		}catch(Exception e){
			throw new BusinessException(e, ErrorEnumeration.INPUT_PARAMETER_INCORRECT);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.INPUT_PARAMETER_INCORRECT);
		}
			
	}


	@Override
	public String addOrUpdateOrderProduct(AddOrUpdateOnlyOrderProductRequest addOrUpdateOnlyOrderProductRequest) {
		String Response;
		try {
			for(int i=0 ; i < addOrUpdateOnlyOrderProductRequest.getProductList().size() ;i++)
			{
				 orderProductDao.addOrUpdateOrderProduct(addOrUpdateOnlyOrderProductRequest.getProductList().get(i));
			}
			Response = "Updated For OrderId"+addOrUpdateOnlyOrderProductRequest.getProductList().get(0).getOrderdId();
		}catch(Exception e){
			throw new BusinessException(e, ErrorEnumeration.INPUT_PARAMETER_INCORRECT);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.INPUT_PARAMETER_INCORRECT);
		}
		return Response;
	}


	@Override
	public String UpdateOrder(UpdateOrderDetailsRequest updateOrderDetailsRequest) {
		String Response;
		try {
			orderServiceDAO.UpdateOrder(updateOrderDetailsRequest);
			Response = "Updated For OrderId"+updateOrderDetailsRequest.getRequest().getOrderId();
		}catch(Exception e){
			throw new BusinessException(e, ErrorEnumeration.INPUT_PARAMETER_INCORRECT);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.INPUT_PARAMETER_INCORRECT);
		}
		return Response;
	}


}
