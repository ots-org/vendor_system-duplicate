package com.fuso.enterprise.ots.srv.functional.service;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.List;
import java.sql.Date;
import javax.inject.Inject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fuso.enterprise.ots.srv.api.model.domain.AddProductStock;
import com.fuso.enterprise.ots.srv.api.model.domain.AddScheduler;
import com.fuso.enterprise.ots.srv.api.model.domain.AssgineEmployeeModel;
import com.fuso.enterprise.ots.srv.api.model.domain.CloseOrderModelRequest;
import com.fuso.enterprise.ots.srv.api.model.domain.CompleteOrderDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.CustomerOutstandingDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.CustomerProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.GetCustomerOutstandingAmt;
import com.fuso.enterprise.ots.srv.api.model.domain.GetProductRequestModel;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderDetailsAndProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderDetailsRequest;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderProductDetailsSaleVocher;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderProductListRequest;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderedProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.SaleVocherModelRequest;
import com.fuso.enterprise.ots.srv.api.model.domain.SchedulerResponceOrderModel;
import com.fuso.enterprise.ots.srv.api.model.domain.UpdateOrderDetailsModelRequest;
import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;
import com.fuso.enterprise.ots.srv.api.service.functional.OTSOrderService;
import com.fuso.enterprise.ots.srv.api.service.request.AddOrUpdateOnlyOrderProductRequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddOrUpdateOrderProductBOrequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddProductStockBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddSchedulerRequest;
import com.fuso.enterprise.ots.srv.api.service.request.CloseOrderBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.CustomerOutstandingBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.CustomerProductDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.DirectSalesVoucherRequest;
import com.fuso.enterprise.ots.srv.api.service.request.EmployeeOrderTransferRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetAssginedOrderBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetCustomerOrderByStatusBOrequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetCustomerOutstandingAmtBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetListOfOrderByDateBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetOrderBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetOrderByStatusRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetProductStockRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetSchedulerRequest;
import com.fuso.enterprise.ots.srv.api.service.request.SaleVocherBoRequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateForAssgineBOrequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateOrderDetailsRequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateOrderStatusRequest;
import com.fuso.enterprise.ots.srv.api.service.response.GetCustomerOutstandingAmtBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetListOfOrderByDateBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetProductBOStockResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetSchedulerResponse;
import com.fuso.enterprise.ots.srv.api.service.response.OrderDetailsBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.OrderProductBOResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.common.exception.ErrorEnumeration;
import com.fuso.enterprise.ots.srv.server.dao.CustomerOutstandingAmtDAO;
import com.fuso.enterprise.ots.srv.server.dao.MapUserProductDAO;
import com.fuso.enterprise.ots.srv.server.dao.OrderProductDAO;
import com.fuso.enterprise.ots.srv.server.dao.OrderServiceDAO;
import com.fuso.enterprise.ots.srv.server.dao.ProductServiceDAO;
import com.fuso.enterprise.ots.srv.server.dao.ProductStockDao;
import com.fuso.enterprise.ots.srv.server.dao.ProductStockHistoryDao;
import com.fuso.enterprise.ots.srv.server.dao.RequestOrderServiceDao;
import com.fuso.enterprise.ots.srv.server.dao.SchedulerDao;
import com.fuso.enterprise.ots.srv.server.dao.UserMapDAO;
import com.fuso.enterprise.ots.srv.server.dao.UserServiceDAO;
import com.fuso.enterprise.ots.srv.server.dao.impl.CustomerOutstandingAmtDAOImpl;
import com.fuso.enterprise.ots.srv.server.dao.impl.UserServiceDAOImpl;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsRequestOrder;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsScheduler;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;
import com.fuso.enterprise.ots.srv.server.util.FcmPushNotification;

@Service
@Transactional
public class OTSOrderServiceImpl implements OTSOrderService {
	private static final java.sql.Date Date = null;
	private OrderServiceDAO orderServiceDAO;
	private OrderProductDAO orderProductDao;
	private ProductStockHistoryDao productStockHistoryDao;
	private ProductStockDao productStockDao;
	private OrderDetails orderDetails;
	private UserServiceDAO userServiceDAO;
	private MapUserProductDAO mapUserProductDAO;
	private FcmPushNotification fcmPushNotification;
	private CustomerOutstandingAmtDAO customerOutstandingAmtDAO;
	private SchedulerDao schedulerDao;
	private RequestOrderServiceDao requestOrderServiceDao;
	private ProductServiceDAO productServiceDAO;
	private UserMapDAO userMapDAO;
	@Inject
	public OTSOrderServiceImpl(UserMapDAO userMapDAO,OrderServiceDAO orderServiceDAO , OrderProductDAO orderProductDao,ProductStockHistoryDao productStockHistoryDao,ProductStockDao productStockDao,UserServiceDAOImpl userServiceDAO,CustomerOutstandingAmtDAO customerOutstandingAmtDAO,MapUserProductDAO mapUserProductDAO,SchedulerDao schedulerDao,RequestOrderServiceDao requestOrderServiceDao,ProductServiceDAO productServiceDAO)
	{
		this.userMapDAO = userMapDAO;
		this.orderServiceDAO = orderServiceDAO ;
		this.orderProductDao = orderProductDao;
		this.productStockHistoryDao=productStockHistoryDao;
		this.productStockDao=productStockDao;
		this.userServiceDAO = userServiceDAO;
		this.customerOutstandingAmtDAO = customerOutstandingAmtDAO;
		this.mapUserProductDAO = mapUserProductDAO;
		this.schedulerDao =schedulerDao ;
		this.requestOrderServiceDao = requestOrderServiceDao;
		this.productServiceDAO = productServiceDAO;
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
		System.out.println(OrderDetailsList.get(0).getOrderDate()+"service");
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
		orderDetailsAndProductDetails.setDelivaryDate(orderDetails.getOrderDeliveryDate());
		orderDetailsAndProductDetails.setOrderDate(orderDetails.getOrderDate());
		orderDetailsAndProductDetails.setDelivaredDate(orderDetails.getOrderDeliverdDate());
		orderDetailsAndProductDetails.setCustomerDetails(userServiceDAO.getUserDetails(Integer.parseInt(orderDetails.getCustomerId()))); 
		return orderDetailsAndProductDetails;
	}

	private OrderDetailsAndProductDetails GetProductAndOrderDetails(OrderDetails orderDetails,List<OrderProductDetails> OrderProductDetails,String CustomerAmount)
	{
		OrderDetailsAndProductDetails orderDetailsAndProductDetails = new OrderDetailsAndProductDetails();
		orderDetailsAndProductDetails.setOrderId(orderDetails.getOrderId());
		orderDetailsAndProductDetails.setDistributorId(orderDetails.getDistributorId());
		orderDetailsAndProductDetails.setCustomerId(orderDetails.getCustomerId());
		orderDetailsAndProductDetails.setOrderNumber(orderDetails.getOrderNumber());
		orderDetailsAndProductDetails.setAssignedId(orderDetails.getAssignedId());
		orderDetailsAndProductDetails.setOrderCost(orderDetails.getOrderCost());
		orderDetailsAndProductDetails.setOrderStatus(orderDetails.getStatus());
		orderDetailsAndProductDetails.setAmountRecived(orderDetails.getAmountRecived());
		orderDetailsAndProductDetails.setOrderdProducts(OrderProductDetails);
		orderDetailsAndProductDetails.setOrderOutStanding(orderDetails.getOutstandingAmount());
		CustomerProductDetails customerProductDetails = new CustomerProductDetails();
		for(int i=0 ; i<OrderProductDetails.size() ; i++) {
			try {
				customerProductDetails = mapUserProductDAO.getCustomerProductDetailsByUserIdandProductId(OrderProductDetails.get(i).getOtsProductId(),orderDetails.getCustomerId());
				orderDetailsAndProductDetails.getOrderdProducts().get(i).setBalanceCan(customerProductDetails.getCustomerBalanceCan());
			}catch(Exception e) {
				orderDetailsAndProductDetails.getOrderdProducts().get(i).setBalanceCan("0");
			}
		}
		orderDetailsAndProductDetails.setDelivaryDate(orderDetails.getOrderDeliveryDate());
		orderDetailsAndProductDetails.setOrderDate(orderDetails.getOrderDate());
		orderDetailsAndProductDetails.setDelivaredDate(orderDetails.getOrderDeliverdDate());
		orderDetailsAndProductDetails.setOutStandingAmount(CustomerAmount);
		orderDetailsAndProductDetails.setDistributorDetails(userServiceDAO.getUserDetails(Integer.parseInt(orderDetails.getDistributorId())));
		orderDetailsAndProductDetails.setCustomerDetails(userServiceDAO.getUserDetails(Integer.parseInt(orderDetails.getCustomerId())));
		if(orderDetails.getAssignedId()!= null) {
			orderDetailsAndProductDetails.setEmployeeDetails(userServiceDAO.getUserDetails(Integer.parseInt(orderDetails.getAssignedId())));
		}	
		return orderDetailsAndProductDetails;
	}

	@Override
	public OrderProductBOResponse insertOrderAndProduct(AddOrUpdateOrderProductBOrequest addOrUpdateOrderProductBOrequest) {
		OrderDetails otsOrderDetails = new OrderDetails();
		OrderProductBOResponse Response = new OrderProductBOResponse();
		try {
			otsOrderDetails = orderServiceDAO.insertOrderAndGetOrderId(addOrUpdateOrderProductBOrequest);
			try {
				for(int i=0 ; i <addOrUpdateOrderProductBOrequest.getRequest().getProductList().size() ; i++)
				{
					orderProductDao.insertOrdrerProductByOrderId(Integer.parseInt(otsOrderDetails.getOrderId()), addOrUpdateOrderProductBOrequest.getRequest().getProductList().get(i));
				}
				Response = getOrderDiruectSalesVoucher(otsOrderDetails.getOrderId());
				UserDetails user = new UserDetails();
				user = userServiceDAO.getUserDetails(Integer.parseInt(addOrUpdateOrderProductBOrequest.getRequest().getDistributorId()));
				UserDetails Customer = new UserDetails();
				Customer = userServiceDAO.getUserDetails(Integer.parseInt(addOrUpdateOrderProductBOrequest.getRequest().getCustomerId()));
				try {
					String notification = otsOrderDetails.getOrderNumber() + " had been placed by " + Customer.getFirstName()+" "+Customer.getLastName()+" and requested delivery date is "+addOrUpdateOrderProductBOrequest.getRequest().getDelivaryDate()+" please click here to assign the Employee for order";
					fcmPushNotification.sendPushNotification(user.getDeviceId(),"Bislari App" ,notification);
					notification = "Order Placed : Your order "+otsOrderDetails.getOrderNumber()+" had been placed";
					fcmPushNotification.sendPushNotification(Customer.getDeviceId(),"Bislari App" ,notification);
				}catch(Exception e) {
					return Response;
				}
				
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
	public String schedulerOrder(AddOrUpdateOrderProductBOrequest addOrUpdateOrderProductBOrequest) {
		OrderDetails otsOrderDetails = new OrderDetails();
		String Response;
		try {
			otsOrderDetails = orderServiceDAO.insertOrderAndGetOrderId(addOrUpdateOrderProductBOrequest);
			try {
				for(int i=0 ; i <addOrUpdateOrderProductBOrequest.getRequest().getProductList().size() ; i++)
				{
					orderProductDao.insertOrdrerProductByOrderId(Integer.parseInt(otsOrderDetails.getOrderId()), addOrUpdateOrderProductBOrequest.getRequest().getProductList().get(i));
				}
				Response = "Order Placed and OrderId Is "+otsOrderDetails.getOrderId();
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
			try {
				UserDetails User;
				User = userServiceDAO.getUserDetails(Integer.parseInt(updateOrderDetailsRequest.getRequest().getAssignedId()));
				fcmPushNotification.sendPushNotification(User.getDeviceId(),"Bisleri Apps" , "Your registration Succesful,please login to your account");
			}catch(Exception e) {
				return Response;
			}
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
		OrderDetails otsOrderDetails = new OrderDetails();
		try {
			Response = orderServiceDAO.updateAssginedOrder(updateForAssgineBOrequest);
			try {
				otsOrderDetails = orderServiceDAO.GetOrderDetailsByOrderId(updateForAssgineBOrequest.getRequest().getOrderId());
				UserDetails Employee;
				Employee = userServiceDAO.getUserDetails(Integer.parseInt(updateForAssgineBOrequest.getRequest().getAssignedId()));

				String Notification = otsOrderDetails.getOrderNumber()+" have been assigned to you, please click to view the order details ";
				fcmPushNotification.sendPushNotification(Employee.getDeviceId(),"Bislari app" , Notification);

				UserDetails customer = userServiceDAO.getUserDetails(Integer.getInteger(otsOrderDetails.getCustomerId()));
				Notification =" Order Placed : Your order "+ otsOrderDetails.getOrderNumber()+" has been confirmed and will be delivered on or before "+otsOrderDetails.getOrderDeliveryDate();
				fcmPushNotification.sendPushNotification(customer.getDeviceId(),"Bislari app" , Notification);
			}catch(Exception e) {
				return Response;
			}
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
		GetCustomerOutstandingAmt getCustomerOutstandingAmt = new GetCustomerOutstandingAmt();
		GetCustomerOutstandingAmtBORequest getCustomerOutstandingAmtBORequest = new GetCustomerOutstandingAmtBORequest();
		for (int i = 0; i <OrderDetailsList.size(); i++)
		{
			getCustomerOutstandingAmt.setCustomerId(OrderDetailsList.get(i).getCustomerId());
			getCustomerOutstandingAmtBORequest.setRequestData(getCustomerOutstandingAmt);
			String CustomerAmount = customerOutstandingAmtDAO.getCustomerOutstandingAmt(getCustomerOutstandingAmtBORequest).getCustomerOutstandingAmount().get(0).getCustomerOutstandingAmt();

			List<OrderProductDetails> orderProductDetailsList = orderProductDao.getProductListByOrderId(OrderDetailsList.get(i).getOrderId());
			List<OrderProductDetails> orderProductDetailsList2 = new ArrayList<OrderProductDetails>();
			for(int j =0;j < orderProductDetailsList.size() ;j++) {
				GetProductStockRequest getProductStockRequest = new GetProductStockRequest();
				GetProductRequestModel getProductRequestModel = new GetProductRequestModel();
				
				getProductRequestModel.setDistributorId(OrderDetailsList.get(0).getDistributorId());
				getProductRequestModel.setProductId(orderProductDetailsList.get(j).getOtsProductId());
				getProductStockRequest.setRequestData(getProductRequestModel);
				OrderProductDetails orderProductDetails = new OrderProductDetails();
				orderProductDetails.setType(orderProductDetailsList.get(j).getType());
				orderProductDetails = orderProductDetailsList.get(j);
				orderProductDetails.setStock(productStockDao.getProductStockByUidAndPid(getProductStockRequest).getStockQuantity()==null?null:productStockDao.getProductStockByUidAndPid(getProductStockRequest).getStockQuantity());
				orderProductDetailsList2.add(j,orderProductDetails);
			}
			
			GetOrderDetailsAndProductDetails.add(i,GetProductAndOrderDetails(OrderDetailsList.get(i),orderProductDetailsList2,CustomerAmount));
			CustomerAmount = null;
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
				orderDetails.get(i).setCustomerDetails(userServiceDAO.getUserDetails(Integer.valueOf(orderDetails.get(i).getCustomerId())));
				orderDetails.get(i).setDistributorDetails(userServiceDAO.getUserDetails(Integer.valueOf(orderDetails.get(i).getDistributorId())));
				orderDetails.get(i).setEmployeeDetails(userServiceDAO.getUserDetails(orderDetails.get(i).getAssignedId()==null?null:Integer.valueOf(orderDetails.get(i).getAssignedId())));
				orderDetails.get(i).setOrderProductDetails(orderProductDao.getProductListByOrderId(orderDetails.get(i).getOrderId()));
			}
			getListOfOrderByDateBOResponse.setCompleteOrderDetails(orderDetails);
			return getListOfOrderByDateBOResponse;
		}catch(Exception e){
			throw new BusinessException(e, ErrorEnumeration.FAILURE_ORDER_GET);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.FAILURE_ORDER_GET);
		}
	}


	@Override
	public String SalesVocher(SaleVocherBoRequest saleVocherBoRequest) {
		try {

			CustomerOutstandingBORequest customerOutstandingBORequest = new CustomerOutstandingBORequest();
			orderDetails = orderServiceDAO.SalesVocher(saleVocherBoRequest);


			CustomerOutstandingDetails customerOutstandingDetails = new CustomerOutstandingDetails();
			customerOutstandingDetails.setCustomerId(saleVocherBoRequest.getRequest().getCustomerId());
			customerOutstandingDetails.setCustomerOutstandingAmt(saleVocherBoRequest.getRequest().getOutstandingAmount());
			customerOutstandingBORequest.setRequestData(customerOutstandingDetails);

			customerOutstandingAmtDAO.updateCustomerOutstandingAmt(customerOutstandingBORequest);

			CustomerProductDataBORequest customerProductDataBORequest = new CustomerProductDataBORequest();
			OrderProductDetailsSaleVocher orderedProductDetails = new OrderProductDetailsSaleVocher();
			CustomerProductDetails customerProductDetails = new CustomerProductDetails();


			AddProductStockBORequest addProductStockBORequest = new AddProductStockBORequest();
			AddProductStock addProductStock = new AddProductStock();

			for(int i = 0 ; i< saleVocherBoRequest.getRequest().getOrderProductlist().size() ; i++) {
				orderedProductDetails.setProductId(saleVocherBoRequest.getRequest().getOrderProductlist().get(i).getProdcutId());
				orderedProductDetails.setOrderdId(saleVocherBoRequest.getRequest().getOrderId());
				orderedProductDetails.setOrderedQty(saleVocherBoRequest.getRequest().getOrderProductlist().get(i).getOrderQty());
				orderedProductDetails.setDeliveredQty(saleVocherBoRequest.getRequest().getOrderProductlist().get(i).getDeliveredQty());
				orderedProductDetails.setProductCost(saleVocherBoRequest.getRequest().getOrderProductlist().get(i).getProductCost());
				orderedProductDetails.setReceivedQty(saleVocherBoRequest.getRequest().getOrderProductlist().get(i).getEmptyCan());
				orderProductDao.addOrUpdateOrderProductsaleVocher(orderedProductDetails);

				customerProductDetails.setProductId(saleVocherBoRequest.getRequest().getOrderProductlist().get(i).getProdcutId());
				customerProductDetails.setUserId(saleVocherBoRequest.getRequest().getCustomerId());
				customerProductDetails.setCustomerBalanceCan(saleVocherBoRequest.getRequest().getOrderProductlist().get(i).getProductbalanceQty());
				customerProductDetails.setProductPrice(saleVocherBoRequest.getRequest().getOrderProductlist().get(i).getProductCost());
				customerProductDataBORequest.setRequestData(customerProductDetails);
				mapUserProductDAO.UpdateBySaleVocher(customerProductDataBORequest);

				addProductStock.setOrderId(saleVocherBoRequest.getRequest().getOrderId());
				addProductStock.setUsersId(orderDetails.getDistributorId());
				addProductStock.setProductStockQty(saleVocherBoRequest.getRequest().getOrderProductlist().get(i).getDeliveredQty());
				addProductStock.setProductStockStatus("Active");
				addProductStock.setProductId(saleVocherBoRequest.getRequest().getOrderProductlist().get(i).getProdcutId());

				addProductStockBORequest.setRequestData(addProductStock);

				productStockHistoryDao.addProductStockHistory(addProductStockBORequest);
				productStockDao.removeProductStock(addProductStockBORequest);
				try {
					UserDetails distributor;
					distributor = userServiceDAO.getUserDetails(Integer.parseInt(orderDetails.getDistributorId()));
					String notification ="The order "+orderDetails.getOrderNumber()+" has been successfully delivered on "+saleVocherBoRequest.getRequest().getDeliverdDate();
					fcmPushNotification.sendPushNotification(distributor.getDeviceId(),"Bisleri Apps" , notification);

					UserDetails Customer;
					Customer = userServiceDAO.getUserDetails(Integer.parseInt(orderDetails.getCustomerId()));
					notification = "Your order "+orderDetails.getOrderNumber()+" has been successfully delivered on "+ saleVocherBoRequest.getRequest().getDeliverdDate();
					fcmPushNotification.sendPushNotification(Customer.getDeviceId(),"Bisleri Apps" , notification);
				}catch(Exception e) {
					return "Updated";
				}
			}
		}catch (BusinessException e) {
			throw new BusinessException(e, ErrorEnumeration.GET_SALE_VOCHER);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.GET_SALE_VOCHER);
		}
		return "Updated";
	}

	@Override
	public OrderProductBOResponse orderReportByDate(GetOrderBORequest getOrderBORequest) {
		try {
			OrderProductBOResponse orderProductBOResponse = new OrderProductBOResponse();
			List<OrderDetails> OrderDetailsList = orderServiceDAO.getOrderReportByDate(getOrderBORequest);
			List<OrderDetailsAndProductDetails> getOrderDetailsAndProductDetails = new ArrayList<OrderDetailsAndProductDetails>();
			GetCustomerOutstandingAmt getCustomerOutstandingAmt = new GetCustomerOutstandingAmt();
			GetCustomerOutstandingAmtBORequest getCustomerOutstandingAmtBORequest = new GetCustomerOutstandingAmtBORequest();
			for (int i = 0; i <OrderDetailsList.size(); i++)
			{
				getCustomerOutstandingAmt.setCustomerId(OrderDetailsList.get(i).getCustomerId());
				getCustomerOutstandingAmtBORequest.setRequestData(getCustomerOutstandingAmt);
				String CustomerAmount = customerOutstandingAmtDAO.getCustomerOutstandingAmt(getCustomerOutstandingAmtBORequest).getCustomerOutstandingAmount().get(0).getCustomerOutstandingAmt();
				if(getOrderBORequest.getRequest().getProductId()!=null ) {
					List<OrderProductDetails> orderProductDetailsList = orderProductDao.getProductListByOrderId(OrderDetailsList.get(i).getOrderId());
					for (int j = 0; j <orderProductDetailsList.size(); j++){
						if(orderProductDetailsList.get(j).getOtsProductId().equals(getOrderBORequest.getRequest().getProductId())) {
							getOrderDetailsAndProductDetails.add(GetProductAndOrderDetails(OrderDetailsList.get(i),orderProductDetailsList,CustomerAmount));
						}
					}
				}else {
					List<OrderProductDetails> orderProductDetailsList = orderProductDao.getProductListByOrderId(OrderDetailsList.get(i).getOrderId());
					getOrderDetailsAndProductDetails.add(GetProductAndOrderDetails(OrderDetailsList.get(i),orderProductDetailsList,CustomerAmount));
				}
				
				//CustomerAmount = null;
			}
			orderProductBOResponse.setOrderList(getOrderDetailsAndProductDetails);
			return orderProductBOResponse;
		}catch(Exception e){
			throw new BusinessException(e, ErrorEnumeration.FAILURE_ORDER_GET);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.FAILURE_ORDER_GET);
		}
	}


	@Override
	public String InsertScheduler(AddSchedulerRequest addSchedulerRequest) {
		try {
			List<OtsScheduler> SchedulerList = schedulerDao.InsertScheduler(addSchedulerRequest);
			requestOrderServiceDao.insertingOrderForScheduling(SchedulerList);
		}catch(Exception e){
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_SCHEDULER);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_SCHEDULER);
		}
		return "Inserted";
	}


	@Override
	public GetSchedulerResponse getScheduler(GetSchedulerRequest getSchedulerRequest) {
	try {
		GetSchedulerResponse getSchedulerResponse = new GetSchedulerResponse();		
		
		List<SchedulerResponceOrderModel> schedulerResponceOrderModel = new ArrayList<SchedulerResponceOrderModel>();
		schedulerResponceOrderModel = requestOrderServiceDao.getScheduler(getSchedulerRequest);
		for(int i=0;i<schedulerResponceOrderModel.size();i++) {
			schedulerResponceOrderModel.get(i).setUserDetails(userServiceDAO.getUserDetails(Integer.valueOf(schedulerResponceOrderModel.get(i).getCustomerId())));
			schedulerResponceOrderModel.get(i).setProductDetails(productServiceDAO.getProductDetils(schedulerResponceOrderModel.get(i).getProductId()));
		}
		getSchedulerResponse.setResponse(schedulerResponceOrderModel);
		return getSchedulerResponse;
		}catch(Exception e){
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_SCHEDULER);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_SCHEDULER);
		}
			
	}


	@Override
	public String runScheduler12AMTO1AM() {
		List<OtsScheduler> schedulerList = schedulerDao.runScheduler12AMTO1AM();
		List<OtsRequestOrder> requestOrder = requestOrderServiceDao.runSchedulerEveryDay12AMTo1AM(schedulerList);
 		
 		for(int i = 0; i<requestOrder.size() ; i++) {
 			AddOrUpdateOrderProductBOrequest addOrUpdateOrderProductBOrequest =  new AddOrUpdateOrderProductBOrequest();
 			
 			OrderDetailsRequest orderDetailsRequest = new OrderDetailsRequest();
 			orderDetailsRequest.setDistributorId(requestOrder.get(i).getOtsDistributorId().getOtsUsersId().toString());
 			orderDetailsRequest.setCustomerId(requestOrder.get(i).getOtsCustomerId().getOtsUsersId().toString());
 			orderDetailsRequest.setOrderStatus("New");
 			orderDetailsRequest.setDelivaryDate(requestOrder.get(i).getOtsScheduleDt().toString().substring(0, 10));
 			orderDetailsRequest.setOrderDate(requestOrder.get(i).getOtsScheduleDt().toString().substring(0, 10));
 			System.out.print(requestOrder.get(i).getOtsScheduleDt());
 			List<OrderedProductDetails> productList = new ArrayList<OrderedProductDetails>();
 			OrderedProductDetails scheduleOrder = new OrderedProductDetails();
 			if(mapUserProductDAO.getCustomerProductDetailsByUserIdandProductId(requestOrder.get(i).getOtsProductId().getOtsProductId().toString(),requestOrder.get(i).getOtsCustomerId().getOtsUsersId().toString())!=null) {
  				Float cost = Float.valueOf(productServiceDAO.getProductDetils(requestOrder.get(i).getOtsProductId().getOtsProductId().toString()).getProductPrice())*requestOrder.get(i).getOtsRequestQty();
 				Integer IntCost = Math.round(cost);
 				scheduleOrder.setProductId(requestOrder.get(i).getOtsProductId().getOtsProductId().toString());
 				orderDetailsRequest.setOrderCost(IntCost.toString());			
 				scheduleOrder.setProductCost(productServiceDAO.getProductDetils(requestOrder.get(i).getOtsProductId().getOtsProductId().toString()).getProductPrice());
 				//	productList.get(0).setProductCost();
 			}else {
 				Float cost = Float.valueOf(productServiceDAO.getProductDetils(requestOrder.get(i).getOtsProductId().getOtsProductId().toString()).getProductPrice())*Float.valueOf(requestOrder.get(i).getOtsRequestQty());
 				orderDetailsRequest.setOrderCost(cost.toString());
 				productList.get(0).setProductCost(mapUserProductDAO.getCustomerProductDetailsByUserIdandProductId(requestOrder.get(i).getOtsCustomerId().getOtsUsersId().toString(),requestOrder.get(i).getOtsProductId().getOtsProductId().toString()).getProductPrice());
 			}
 			scheduleOrder.setOrderedQty(requestOrder.get(i).getOtsRequestQty().toString());
 			scheduleOrder.setDeliveredQty(requestOrder.get(i).getOtsRequestQty().toString());
 			productList.add(scheduleOrder);
 			orderDetailsRequest.setProductList(productList);
 			addOrUpdateOrderProductBOrequest.setRequest(orderDetailsRequest);
  		//	int ordercost = Math.round(Integer.parseInt(addOrUpdateOrderProductBOrequest.getRequest().getOrderCost()));
 		//	addOrUpdateOrderProductBOrequest.getRequest().setOrderCost(String.valueOf(ordercost));
 			schedulerOrder(addOrUpdateOrderProductBOrequest); 			 			
 		}
 		
		return "Done";
	}


	@Override
	public String employeeTransferOrder(EmployeeOrderTransferRequest employeeOrderTransferRequest) {
		try {
			return orderServiceDAO.employeeTransferOrder(employeeOrderTransferRequest);
		}catch(Exception e){
			e.printStackTrace();
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_ORDER_INSERTION);
		} catch (Throwable e) {
			e.printStackTrace();
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_ORDER_INSERTION);
		}
		
	}


	@Override
	public String UpdateOrderStatus(UpdateOrderStatusRequest updateOrderStatusRequest) {
		try {
			return orderServiceDAO.UpdateOrderStatus(updateOrderStatusRequest);
		}catch(Exception e){
			e.printStackTrace();
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_ORDER_INSERTION);
		} catch (Throwable e) {
			e.printStackTrace();
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_ORDER_INSERTION);
		}
	}

	@Override
	public String directSalesVoucher(DirectSalesVoucherRequest directSalesVoucherRequest) {
		AddOrUpdateOrderProductBOrequest addOrUpdateOrderProductBOrequest = new AddOrUpdateOrderProductBOrequest();
		OrderDetailsRequest orderDetailsRequest = new OrderDetailsRequest();
		
		orderDetailsRequest.setDistributorId(userMapDAO.getMappedDistributor(directSalesVoucherRequest.getRequest().getEmployeeId()));
		orderDetailsRequest.setCustomerId(directSalesVoucherRequest.getRequest().getCustomerId());
		orderDetailsRequest.setAssignedId(directSalesVoucherRequest.getRequest().getEmployeeId());
		orderDetailsRequest.setOrderCost(directSalesVoucherRequest.getRequest().getOrderCost());
		orderDetailsRequest.setOrderDate(directSalesVoucherRequest.getRequest().getDeliveryDate());
		orderDetailsRequest.setDeliverdDate(directSalesVoucherRequest.getRequest().getDeliveryDate());
		orderDetailsRequest.setDelivaryDate(directSalesVoucherRequest.getRequest().getDeliveryDate());
		orderDetailsRequest.setOrderStatus("Generated");
		
		List<OrderedProductDetails> productList = new ArrayList<OrderedProductDetails>();
		
		
		for(int i = 0; i<directSalesVoucherRequest.getRequest().getOrderProductlist().size();i++) {
			OrderedProductDetails orderedProductDetails = new OrderedProductDetails();
			orderedProductDetails.setDeliveredQty(directSalesVoucherRequest.getRequest().getOrderProductlist().get(i).getDeliveredQty());
			orderedProductDetails.setOrderedQty(directSalesVoucherRequest.getRequest().getOrderProductlist().get(i).getOrderQty());
			orderedProductDetails.setOts_delivered_qty(directSalesVoucherRequest.getRequest().getOrderProductlist().get(i).getDeliveredQty());
			orderedProductDetails.setProductCost(directSalesVoucherRequest.getRequest().getOrderProductlist().get(i).getProductCost());
			orderedProductDetails.setProductId(directSalesVoucherRequest.getRequest().getOrderProductlist().get(i).getProdcutId());
			orderedProductDetails.setProductStatus("New");
			productList.add(orderedProductDetails);
		}
		
		orderDetailsRequest.setProductList(productList);
		
		addOrUpdateOrderProductBOrequest.setRequest(orderDetailsRequest);
		
		System.out.print("1");
		insertOrderAndProductFordirectSalesVoucher(addOrUpdateOrderProductBOrequest);
		
		SaleVocherModelRequest SaleVocherModelRequest = new SaleVocherModelRequest();
		SaleVocherModelRequest.setOrderId(orderServiceDAO.getLastOrder().getOrderId());
		
		SaleVocherModelRequest.setAmountReceived(directSalesVoucherRequest.getRequest().getAmountRecived());
		SaleVocherModelRequest.setDeliverdDate(Date.valueOf(directSalesVoucherRequest.getRequest().getDeliveryDate()));
		SaleVocherModelRequest.setOrderCost(directSalesVoucherRequest.getRequest().getOrderCost());
		
		SaleVocherModelRequest.setOrderProductlist(directSalesVoucherRequest.getRequest().getOrderProductlist());
		SaleVocherModelRequest.setOutstandingAmount(directSalesVoucherRequest.getRequest().getOutstandingAmount());
		SaleVocherModelRequest.setCustomerId(directSalesVoucherRequest.getRequest().getCustomerId());
		SaleVocherModelRequest.setOrderProductlist(directSalesVoucherRequest.getRequest().getOrderProductlist());
		
		SaleVocherBoRequest saleVocherBoRequest = new SaleVocherBoRequest();
		saleVocherBoRequest.setRequest(SaleVocherModelRequest);
		SalesVocher(saleVocherBoRequest);
		return "DONE";
		
	}

	@Override
	public String insertOrderAndProductFordirectSalesVoucher(AddOrUpdateOrderProductBOrequest addOrUpdateOrderProductBOrequest) {
		OrderDetails otsOrderDetails = new OrderDetails();
		String Response;
		try {
			otsOrderDetails = orderServiceDAO.insertOrderAndGetOrderId(addOrUpdateOrderProductBOrequest);
			try {
				for(int i=0 ; i <addOrUpdateOrderProductBOrequest.getRequest().getProductList().size() ; i++)
				{
					orderProductDao.insertOrdrerProductByOrderId(Integer.parseInt(otsOrderDetails.getOrderId()), addOrUpdateOrderProductBOrequest.getRequest().getProductList().get(i));
				}
				Response = "Order Placed and OrderId Is "+otsOrderDetails.getOrderId();
				UserDetails user = new UserDetails();
				user = userServiceDAO.getUserDetails(Integer.parseInt(addOrUpdateOrderProductBOrequest.getRequest().getDistributorId()));
				UserDetails Customer = new UserDetails();
				Customer = userServiceDAO.getUserDetails(Integer.parseInt(addOrUpdateOrderProductBOrequest.getRequest().getCustomerId()));
				try {
					String notification = otsOrderDetails.getOrderNumber() + " had been placed by " + Customer.getFirstName()+" "+Customer.getLastName()+" and requested delivery date is "+addOrUpdateOrderProductBOrequest.getRequest().getDelivaryDate()+" please click here to assign the Employee for order";
					fcmPushNotification.sendPushNotification(user.getDeviceId(),"Bislari App" ,notification);
					notification = "Order Placed : Your order "+otsOrderDetails.getOrderNumber()+" had been placed";
					fcmPushNotification.sendPushNotification(Customer.getDeviceId(),"Bislari App" ,notification);
				}catch(Exception e) {
					return Response;
				}
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
	public OrderProductBOResponse getOrderDiruectSalesVoucher(String orderId) {
	try {
		OrderProductBOResponse orderProductBOResponse = new OrderProductBOResponse();
		List<OrderDetails> OrderDetailsList = orderServiceDAO.GetOrderForDrectSalesVoucheri(orderId);
		List<OrderDetailsAndProductDetails> GetOrderDetailsAndProductDetails = new ArrayList<OrderDetailsAndProductDetails>();
		GetCustomerOutstandingAmt getCustomerOutstandingAmt = new GetCustomerOutstandingAmt();
		GetCustomerOutstandingAmtBORequest getCustomerOutstandingAmtBORequest = new GetCustomerOutstandingAmtBORequest();
		for (int i = 0; i <OrderDetailsList.size(); i++)
		{
			getCustomerOutstandingAmt.setCustomerId(OrderDetailsList.get(i).getCustomerId());
			getCustomerOutstandingAmtBORequest.setRequestData(getCustomerOutstandingAmt);
			String CustomerAmount = customerOutstandingAmtDAO.getCustomerOutstandingAmt(getCustomerOutstandingAmtBORequest).getCustomerOutstandingAmount().get(0).getCustomerOutstandingAmt();

			List<OrderProductDetails> orderProductDetailsList = orderProductDao.getProductListByOrderId(OrderDetailsList.get(i).getOrderId());
			List<OrderProductDetails> orderProductDetailsList2 = new ArrayList<OrderProductDetails>();
			for(int j =0;j < orderProductDetailsList.size() ;j++) {
				GetProductStockRequest getProductStockRequest = new GetProductStockRequest();
				GetProductRequestModel getProductRequestModel = new GetProductRequestModel();
				
				getProductRequestModel.setDistributorId(OrderDetailsList.get(0).getDistributorId());
				getProductRequestModel.setProductId(orderProductDetailsList.get(j).getOtsProductId());
				getProductStockRequest.setRequestData(getProductRequestModel);
				OrderProductDetails orderProductDetails = new OrderProductDetails();
				orderProductDetails.setType(orderProductDetailsList.get(j).getType());
				orderProductDetails = orderProductDetailsList.get(j);
				orderProductDetails.setStock(productStockDao.getProductStockByUidAndPid(getProductStockRequest).getStockQuantity()==null?null:productStockDao.getProductStockByUidAndPid(getProductStockRequest).getStockQuantity());
				orderProductDetailsList2.add(j,orderProductDetails);
			}
			
			GetOrderDetailsAndProductDetails.add(i,GetProductAndOrderDetails(OrderDetailsList.get(i),orderProductDetailsList2,CustomerAmount));
			CustomerAmount = null;
		}
		orderProductBOResponse.setOrderList(GetOrderDetailsAndProductDetails);
		return orderProductBOResponse;}
	catch(Exception e){
			throw new BusinessException(e,ErrorEnumeration.FAILURE_ORDER_GET);}
	catch (Throwable e) {
			throw new BusinessException(e,ErrorEnumeration.FAILURE_ORDER_GET);
		}
	}

}
