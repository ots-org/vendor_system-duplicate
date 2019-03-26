package com.fuso.enterprise.ots.srv.functional.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.List;
import java.sql.Date;
import javax.inject.Inject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fuso.enterprise.ots.srv.api.model.domain.AddProductStock;
import com.fuso.enterprise.ots.srv.api.model.domain.AssgineEmployeeModel;
import com.fuso.enterprise.ots.srv.api.model.domain.CloseOrderModelRequest;
import com.fuso.enterprise.ots.srv.api.model.domain.CompleteOrderDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderDetailsAndProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderedProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.UpdateOrderDetailsModelRequest;
import com.fuso.enterprise.ots.srv.api.service.functional.OTSOrderService;
import com.fuso.enterprise.ots.srv.api.service.request.AddOrUpdateOnlyOrderProductRequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddOrUpdateOrderProductBOrequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddProductStockBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.CloseOrderBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetOrderBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetOrderByStatusRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetAssginedOrderBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetCustomerOrderByStatusBOrequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetListOfOrderByDateBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateForAssgineBOrequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateOrderDetailsRequest;
import com.fuso.enterprise.ots.srv.api.service.response.GetListOfOrderByDateBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.OrderDetailsBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.OrderProductBOResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.common.exception.ErrorEnumeration;
import com.fuso.enterprise.ots.srv.server.dao.OrderProductDAO;
import com.fuso.enterprise.ots.srv.server.dao.OrderServiceDAO;
import com.fuso.enterprise.ots.srv.server.dao.ProductStockDao;
import com.fuso.enterprise.ots.srv.server.dao.ProductStockHistoryDao;
import com.fuso.enterprise.ots.srv.server.dao.impl.UserServiceDAOImpl;

@Service
@Transactional
public class OTSOrderServiceImpl implements OTSOrderService {
	private static final java.sql.Date Date = null;
	private OrderServiceDAO orderServiceDAO; 
	private OrderProductDAO orderProductDao;
	private ProductStockHistoryDao productStockHistoryDao;
	private ProductStockDao productStockDao;
	private OrderDetails orderDetails;
	private UserServiceDAOImpl userServiceDAOImpl;

	@Inject
	public OTSOrderServiceImpl(OrderServiceDAO orderServiceDAO , OrderProductDAO orderProductDao,ProductStockHistoryDao productStockHistoryDao,ProductStockDao productStockDao,UserServiceDAOImpl userServiceDAOImpl)
	{
		this.orderServiceDAO = orderServiceDAO ;
		this.orderProductDao = orderProductDao;
		this.productStockHistoryDao=productStockHistoryDao;
		this.productStockDao=productStockDao;
		this.userServiceDAOImpl = userServiceDAOImpl;
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
					orderProductDao.insertOrdrerProductByOrderId(orderId, addOrUpdateOrderProductBOrequest.getRequest().getProductList().get(i));}
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
		String distributorId;
		AddProductStockBORequest addProductStockBORequest=new AddProductStockBORequest();
		AddProductStock addProductStock=new AddProductStock();
		try {
			for(int i=0 ; i < addOrUpdateOnlyOrderProductRequest.getProductList().size() ;i++){
				distributorId =orderProductDao.addOrUpdateOrderProduct(addOrUpdateOnlyOrderProductRequest.getProductList().get(i));
			    /*
			     * fetching current date
			     */
				Date date = new Date(0);
				long d = System.currentTimeMillis();
				date.setTime(d);
			    addProductStock.setUsersId(distributorId);
				addProductStock.setProductId(addOrUpdateOnlyOrderProductRequest.getProductList().get(i).getProductId());
				addProductStock.setProductStockQty(addOrUpdateOnlyOrderProductRequest.getProductList().get(i).getOrderedQty());
				addProductStock.setOrderId(addOrUpdateOnlyOrderProductRequest.getProductList().get(i).getOrderdId());
				addProductStock.setProductStockAddDate(date);
				addProductStockBORequest.setRequestData(addProductStock);
				productStockHistoryDao.addProductStockHistory(addProductStockBORequest);	
				productStockDao.updateProductStockQuantity(addProductStockBORequest);
			}
			Response = "Updated For OrderId"+""+addOrUpdateOnlyOrderProductRequest.getProductList().get(0).getOrderdId();
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


	@Override
	public String updateAssginedOrder(UpdateForAssgineBOrequest  updateForAssgineBOrequest) {
		String Response;
		try {
			Response = orderServiceDAO.updateAssginedOrder(updateForAssgineBOrequest);;
		}catch(Exception e){
			e.printStackTrace();
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_ORDER_INSERTION);
		} catch (Throwable e) {
			e.printStackTrace();
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_ORDER_INSERTION);
		}
		return Response;
	}


	@Override
	public OrderProductBOResponse getAssginedOrder(GetAssginedOrderBORequest getAssginedOrderBORequest) {
	try {
		OrderProductBOResponse orderProductBOResponse = new OrderProductBOResponse();
		List<OrderDetails> OrderDetailsList = orderServiceDAO.getAssginedOrder(getAssginedOrderBORequest);
		List<OrderDetailsAndProductDetails> GetOrderDetailsAndProductDetails = new ArrayList<OrderDetailsAndProductDetails>();
		for (int i = 0; i <OrderDetailsList.size() ; i++)
		{
			List<OrderProductDetails> OrderProductDetailsList = orderProductDao.getUserByStatuesAndDistributorId(OrderDetailsList.get(i));
			GetOrderDetailsAndProductDetails.add(AddProductAndOrderDetailsIntoResponse(OrderDetailsList.get(i),OrderProductDetailsList));
		}
		orderProductBOResponse.setOrderList(GetOrderDetailsAndProductDetails);
		return orderProductBOResponse;}
	catch(Exception e){
			throw new BusinessException(e,ErrorEnumeration.FAILURE_ORDER_GET);}
	catch (Throwable e) {
			throw new BusinessException(e,ErrorEnumeration.FAILURE_ORDER_GET);
		}
	}


	@Override
	public String closeOrder(CloseOrderBORequest closeOrderBORequest) {
		orderDetails = new OrderDetails();
		/*Update the Status As close in Order Table*/
		orderDetails = orderServiceDAO.closeOrder(closeOrderBORequest);
		/*Get All the Product Details For Particular Order*/
		try {
			List<OrderProductDetails> ProductList = orderProductDao.getProductListByOrderId(closeOrderBORequest.getRequest().getOrderId());
			
			AddOrUpdateOnlyOrderProductRequest addOrUpdateOnlyOrderProductRequest = new AddOrUpdateOnlyOrderProductRequest();
			
			List<OrderedProductDetails> orderedProductDetails = new ArrayList<OrderedProductDetails>();
			
			for(int i=0;i<ProductList.size() ; i++) {
				OrderedProductDetails orderedProductDetailstemp = new OrderedProductDetails();
				
				orderedProductDetailstemp.setOrderdId(ProductList.get(i).getOtsOrderId());
				orderedProductDetailstemp.setDeliveredQty(ProductList.get(i).getOtsDeliveredQty());
				orderedProductDetailstemp.setProductId(ProductList.get(i).getOtsProductId());
				orderedProductDetailstemp.setOrderProductId(ProductList.get(i).getOtsOrderProductId());
				orderedProductDetailstemp.setOrderedQty(ProductList.get(i).getOtsOrderedQty());
				orderedProductDetailstemp.setProductCost(ProductList.get(i).getOtsOrderProductCost());
				orderedProductDetailstemp.setProductStatus("close");
				
				orderedProductDetails.add(i,orderedProductDetailstemp);
				
			}
			addOrUpdateOnlyOrderProductRequest.setProductList(orderedProductDetails);
			addOrUpdateOrderProduct(addOrUpdateOnlyOrderProductRequest);
			String Response = "Order Has been closed for OrderId "+closeOrderBORequest.getRequest().getOrderId();
			return Response;}
		catch(Exception e){
			e.printStackTrace();
			throw new BusinessException(e, ErrorEnumeration.ORDER_CLOSE);}
		catch (Throwable e) {
			e.printStackTrace();
			throw new BusinessException(e, ErrorEnumeration.ORDER_CLOSE);}	
		}
	
	@Override
	public OrderProductBOResponse getCustomerOrderStatus(GetCustomerOrderByStatusBOrequest getCustomerOrderByStatusBOrequest) {
		OrderProductBOResponse orderProductBOResponse = new OrderProductBOResponse();
		List<OrderDetailsAndProductDetails> GetOrderDetailsAndProductDetails = new ArrayList<OrderDetailsAndProductDetails>();
		try {
			List<OrderDetails> OrderDetailsList = orderServiceDAO.getCustomerOrderStatus(getCustomerOrderByStatusBOrequest);
			for (int i = 0; i <OrderDetailsList.size() ; i++)
			{
				List<OrderProductDetails> OrderProductDetailsList = orderProductDao.getUserByStatuesAndDistributorId(OrderDetailsList.get(i));
				GetOrderDetailsAndProductDetails.add(AddProductAndOrderDetailsIntoResponse(OrderDetailsList.get(i),OrderProductDetailsList));
			}
			orderProductBOResponse.setOrderList(GetOrderDetailsAndProductDetails);
			return orderProductBOResponse;
		}catch(Exception e){
			throw new BusinessException(e, ErrorEnumeration.FAILURE_ORDER_GET);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.FAILURE_ORDER_GET);
		}
	}


	@Override
	public OrderProductBOResponse getOrderDetailsByDate(GetOrderBORequest getOrderBORequest) {
		OrderProductBOResponse orderProductBOResponse = new OrderProductBOResponse();
		List<OrderDetailsAndProductDetails> GetOrderDetailsAndProductDetails = new ArrayList<OrderDetailsAndProductDetails>();
		try {
			List<OrderDetails> OrderDetailsList = orderServiceDAO.getOrderBydate(getOrderBORequest);
			for (int i = 0; i <OrderDetailsList.size() ; i++)
			{
				List<OrderProductDetails> OrderProductDetailsList = orderProductDao.getUserByStatuesAndDistributorId(OrderDetailsList.get(i));
				GetOrderDetailsAndProductDetails.add(AddProductAndOrderDetailsIntoResponse(OrderDetailsList.get(i),OrderProductDetailsList));
			}
			orderProductBOResponse.setOrderList(GetOrderDetailsAndProductDetails);
			return orderProductBOResponse;
		}catch(Exception e){
			throw new BusinessException(e, ErrorEnumeration.FAILURE_ORDER_GET);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.FAILURE_ORDER_GET);
		}
	}


	@Override
	public GetListOfOrderByDateBOResponse getListOfOrderByDate(
			GetListOfOrderByDateBORequest getListOfOrderByDateBORequest) {
		try{
			GetListOfOrderByDateBOResponse getListOfOrderByDateBOResponse = new GetListOfOrderByDateBOResponse();
			List<CompleteOrderDetails> orderDetails = orderServiceDAO.getListOfOrderByDate(getListOfOrderByDateBORequest);
			for(int i = 0 ; i<orderDetails.size() ; i++) {
				orderDetails.get(i).setCustomerDetails(userServiceDAOImpl.getUserDetails(Integer.valueOf(orderDetails.get(i).getCustomerId())));
				orderDetails.get(i).setDistributorDetails(userServiceDAOImpl.getUserDetails(Integer.valueOf(orderDetails.get(i).getDistributorId())));
				orderDetails.get(i).setEmployeeDetails(userServiceDAOImpl.getUserDetails(Integer.valueOf(orderDetails.get(i).getDistributorId())));
			}
			getListOfOrderByDateBOResponse.setCompleteOrderDetails(orderDetails);
			return getListOfOrderByDateBOResponse;
		}catch(Exception e){
			throw new BusinessException(e, ErrorEnumeration.FAILURE_ORDER_GET);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.FAILURE_ORDER_GET);
		}
	}
	
}
