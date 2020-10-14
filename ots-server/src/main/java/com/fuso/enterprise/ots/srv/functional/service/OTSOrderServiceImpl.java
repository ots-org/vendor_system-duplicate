package com.fuso.enterprise.ots.srv.functional.service;
import java.io.File;
import org.json.JSONObject;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.inject.Inject;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fuso.enterprise.ots.srv.api.model.domain.AddProductStock;
import com.fuso.enterprise.ots.srv.api.model.domain.CompleteOrderDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.CustomerOutstandingDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.CustomerProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.DonationBoResponse;
import com.fuso.enterprise.ots.srv.api.model.domain.DonationModel;
import com.fuso.enterprise.ots.srv.api.model.domain.DonationResponseByStatus;
import com.fuso.enterprise.ots.srv.api.model.domain.GetCustomerOutstandingAmt;
import com.fuso.enterprise.ots.srv.api.model.domain.GetProductRequestModel;
import com.fuso.enterprise.ots.srv.api.model.domain.GetUserDetailsBORequest;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderDetailsAndProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderDetailsRequest;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderProductDetailsSaleVocher;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderedProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.ProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.SaleVocherModelRequest;
import com.fuso.enterprise.ots.srv.api.model.domain.SchedulerResponceOrderModel;
import com.fuso.enterprise.ots.srv.api.model.domain.UpdateOrderDetailsModelRequest;
import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;
import com.fuso.enterprise.ots.srv.api.service.functional.OTSOrderService;
import com.fuso.enterprise.ots.srv.api.service.request.AddDonationtoRequest;
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
import com.fuso.enterprise.ots.srv.api.service.request.GetDonationByStatusRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetDonationReportByDateRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetListOfOrderByDateBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetOrderBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetOrderByStatusRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetProductStockRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetSchedulerRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetUserDetailsForRequest;
import com.fuso.enterprise.ots.srv.api.service.request.OrderIdBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.RequestBOUserBySearch;
import com.fuso.enterprise.ots.srv.api.service.request.SaleVocherBoRequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateDonationRequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateForAssgineBOrequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateOrderDetailsRequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateOrderStatusRequest;
import com.fuso.enterprise.ots.srv.api.service.response.GetDonationReportByDateResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetListOfOrderByDateBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetSchedulerResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetUserDetailsForResponse;
import com.fuso.enterprise.ots.srv.api.service.response.OrderDetailsBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.OrderProductBOResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.common.exception.ErrorEnumeration;
import com.fuso.enterprise.ots.srv.server.dao.CustomerOutstandingAmtDAO;
import com.fuso.enterprise.ots.srv.server.dao.DonationRequestMappingDAO;
import com.fuso.enterprise.ots.srv.server.dao.DonationServiceDAO;
import com.fuso.enterprise.ots.srv.server.dao.MapUserProductDAO;
import com.fuso.enterprise.ots.srv.server.dao.OrderProductDAO;
import com.fuso.enterprise.ots.srv.server.dao.OrderRequestMappingDAO;
import com.fuso.enterprise.ots.srv.server.dao.OrderServiceDAO;
import com.fuso.enterprise.ots.srv.server.dao.ProductServiceDAO;
import com.fuso.enterprise.ots.srv.server.dao.ProductStockDao;
import com.fuso.enterprise.ots.srv.server.dao.ProductStockHistoryDao;
import com.fuso.enterprise.ots.srv.server.dao.RequestOrderServiceDao;
import com.fuso.enterprise.ots.srv.server.dao.RequestProductDao;
import com.fuso.enterprise.ots.srv.server.dao.SchedulerDao;
import com.fuso.enterprise.ots.srv.server.dao.UserMapDAO;
import com.fuso.enterprise.ots.srv.server.dao.UserServiceDAO;
import com.fuso.enterprise.ots.srv.server.dao.UserServiceUtilityDAO;
import com.fuso.enterprise.ots.srv.server.dao.impl.UserServiceDAOImpl;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsDonationRequestMapping;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsRequestOrder;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsScheduler;
import com.fuso.enterprise.ots.srv.server.util.EmailUtil;
import com.fuso.enterprise.ots.srv.server.util.FcmPushNotification;
import com.fuso.enterprise.ots.srv.server.util.OTSUtil;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
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
	private UserServiceUtilityDAO userServiceUtilityDAO;
	private RequestProductDao requestProductDao;
	private OrderRequestMappingDAO orderRequestMappingDAO;
	private DonationServiceDAO donationServiceDAO;
	private DonationRequestMappingDAO donationRequestMappingDAO;
	@Inject
	public OTSOrderServiceImpl(DonationRequestMappingDAO donationRequestMappingDAO,DonationServiceDAO donationServiceDAO,OrderRequestMappingDAO orderRequestMappingDAO,RequestProductDao requestProductDao,UserServiceUtilityDAO userServiceUtilityDAO,UserMapDAO userMapDAO,OrderServiceDAO orderServiceDAO , OrderProductDAO orderProductDao,ProductStockHistoryDao productStockHistoryDao,ProductStockDao productStockDao,UserServiceDAOImpl userServiceDAO,CustomerOutstandingAmtDAO customerOutstandingAmtDAO,MapUserProductDAO mapUserProductDAO,SchedulerDao schedulerDao,RequestOrderServiceDao requestOrderServiceDao,ProductServiceDAO productServiceDAO)
	{
		this.donationRequestMappingDAO = donationRequestMappingDAO;
		this.donationServiceDAO = donationServiceDAO;
		this.orderRequestMappingDAO = orderRequestMappingDAO;
		this.requestProductDao = requestProductDao;
		this.userServiceUtilityDAO = userServiceUtilityDAO;
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

	@Value("${ots.donation.razorpaykey}")
	public String donationRazorpayKey;
	
	@Value("${ots.donation.razorpaysignature}")
	public String donationRazorpaySignature;
	
	@Value("${ots.donation.razorpaykey}")
	public String giftRazorpayKey;
	
	@Value("${ots.donation.razorpaykey}")
	public String giftRazorpaySignature;
	
	

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
		orderDetailsAndProductDetails.setOrderNumber(orderDetails.getOrderNumber());
		orderDetailsAndProductDetails.setAssignedId(orderDetails.getAssignedId());
		orderDetailsAndProductDetails.setOrderCost(orderDetails.getOrderCost());
		orderDetailsAndProductDetails.setOrderStatus(orderDetails.getStatus());
		orderDetailsAndProductDetails.setOrderdProducts(OrderProductDetails);
		orderDetailsAndProductDetails.setDelivaryDate(orderDetails.getOrderDeliveryDate());
		orderDetailsAndProductDetails.setAmountRecived(orderDetails.getAmountRecived());
		orderDetailsAndProductDetails.setOutStandingAmount(orderDetails.getOutstandingAmount());
		orderDetailsAndProductDetails.setOrderDate(orderDetails.getOrderDate());
		orderDetailsAndProductDetails.setDelivaredDate(orderDetails.getOrderDeliverdDate());
		orderDetailsAndProductDetails.setCustomerDetails(userServiceDAO.getUserDetails(Integer.parseInt(orderDetails.getCustomerId()))); 
		if(orderDetails.getDonatorId() !=null) {
			orderDetailsAndProductDetails.setDonarDetails(userServiceDAO.getUserDetails(Integer.parseInt(orderDetails.getDonatorId())));
			orderDetailsAndProductDetails.setDonationStatus(orderDetails.getDonationStatus());
		}
		
		
		orderDetailsAndProductDetails.setAddressToBePlaced(orderDetails.getAddress());
		if(!orderDetails.getPaymentStatus().equalsIgnoreCase("cash")) {
			orderDetailsAndProductDetails.setPaymentId(orderDetails.getPaymentId());
		}
		orderDetailsAndProductDetails.setPayementStatus(orderDetails.getPaymentStatus());
		return orderDetailsAndProductDetails;
	}

	private OrderDetailsAndProductDetails GetProductAndOrderDetails(OrderDetails orderDetails,List<OrderProductDetails> OrderProductDetails,String CustomerAmount)
	{
		OrderDetailsAndProductDetails orderDetailsAndProductDetails = new OrderDetailsAndProductDetails();
		orderDetailsAndProductDetails.setOrderId(orderDetails.getOrderId());
		orderDetailsAndProductDetails.setDistributorId(orderDetails.getDistributorId());
		orderDetailsAndProductDetails.setDistributorDetails(userServiceDAO.getUserIdUsers(orderDetails.getDistributorId()).get(0));
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
		orderDetailsAndProductDetails.setPayementStatus(orderDetails.getPaymentStatus());
		if(!orderDetails.getPaymentStatus().equalsIgnoreCase("cash")) {
			orderDetailsAndProductDetails.setPaymentId(orderDetails.getPaymentId());
		}
		orderDetailsAndProductDetails.setDistributorDetails(userServiceDAO.getUserDetails(Integer.parseInt(orderDetails.getDistributorId())));
		orderDetailsAndProductDetails.setCustomerDetails(userServiceDAO.getUserDetails(Integer.parseInt(orderDetails.getCustomerId())));
		orderDetailsAndProductDetails.setAddressToBePlaced(orderDetails.getAddress());
		if(orderDetails.getAssignedId()!= null) {
			orderDetailsAndProductDetails.setEmployeeDetails(userServiceDAO.getUserDetails(Integer.parseInt(orderDetails.getAssignedId())));
		}	
		
		
		return orderDetailsAndProductDetails;
	}

	@Override
	public OrderProductBOResponse insertOrderAndProduct(AddOrUpdateOrderProductBOrequest addOrUpdateOrderProductBOrequest) {
		try {
	//		capturePaymentForOrder(addOrUpdateOrderProductBOrequest);
		}catch(Exception e) {
			throw new BusinessException(e,ErrorEnumeration.RazorPay_Error);
		}
		
		OrderDetails otsOrderDetails = new OrderDetails();
		OrderProductBOResponse Response = new OrderProductBOResponse();
		List<UserDetails> userDetails = new ArrayList<UserDetails>();
		RequestBOUserBySearch requestBOUserBySearch = new RequestBOUserBySearch();	
		GetUserDetailsBORequest userDetailsBORequest = new GetUserDetailsBORequest();
		
		userDetailsBORequest.setUserLat(addOrUpdateOrderProductBOrequest.getRequest().getUserLat());
		userDetailsBORequest.setUserLong(addOrUpdateOrderProductBOrequest.getRequest().getUserLong());
		requestBOUserBySearch.setRequestData(userDetailsBORequest);
		if(addOrUpdateOrderProductBOrequest.getRequest().getOrderStatus().equalsIgnoreCase("new")) {
			userDetails = userServiceUtilityDAO.findNearestDistributor(requestBOUserBySearch);
		}else {
			userDetails = userServiceDAO.getUserIdUsers("1");
		}
		
		addOrUpdateOrderProductBOrequest.getRequest().setDistributorId(userDetails.get(0).getUserId());
		
		UserDetails user = new UserDetails();
		user = userServiceDAO.getUserDetails(Integer.parseInt(addOrUpdateOrderProductBOrequest.getRequest().getDistributorId()));
		UserDetails Customer = new UserDetails();
		Customer = userServiceDAO.getUserDetails(Integer.parseInt(addOrUpdateOrderProductBOrequest.getRequest().getCustomerId()));
		
		if(!addOrUpdateOrderProductBOrequest.getRequest().getOrderStatus().equalsIgnoreCase("newRequest")) {
			try {
				otsOrderDetails = orderServiceDAO.insertOrderAndGetOrderId(addOrUpdateOrderProductBOrequest);
				try {
					for(int i=0 ; i <addOrUpdateOrderProductBOrequest.getRequest().getProductList().size() ; i++)
					{
						orderProductDao.insertOrdrerProductByOrderId(Integer.parseInt(otsOrderDetails.getOrderId()), addOrUpdateOrderProductBOrequest.getRequest().getProductList().get(i));
					}
					Response = getOrderDiruectSalesVoucher(otsOrderDetails.getOrderId());
					try {
						String notification = otsOrderDetails.getOrderNumber() + " had been placed by " + Customer.getFirstName()+" "+Customer.getLastName()+" and requested delivery date is "+addOrUpdateOrderProductBOrequest.getRequest().getDelivaryDate()+" please click here to assign the Employee for order";
						fcmPushNotification.sendPushNotification(user.getDeviceId(),"etaarana Apps" ,notification);
						notification = "Order Placed : Your order "+otsOrderDetails.getOrderNumber()+" had been placed";
						fcmPushNotification.sendPushNotification(Customer.getDeviceId(),"etaarana Apps" ,notification);
					}catch(Exception e) {
						return Response;
					}
					
					return Response;
				}catch(Exception e){
					throw new BusinessException(e,ErrorEnumeration.ERROR_IN_STOCK);
				} catch (Throwable e) {
					throw new BusinessException(e, ErrorEnumeration.ERROR_IN_STOCK);
				}
			}catch(Exception e){
				throw new BusinessException(e, ErrorEnumeration.ERROR_IN_STOCK);
			} catch (Throwable e) {
				throw new BusinessException(e, ErrorEnumeration.ERROR_IN_STOCK);
			}
		}else {
			AddOrUpdateOrderProductBOrequest requestProduct = new AddOrUpdateOrderProductBOrequest();
			requestProduct.setRequest(addOrUpdateOrderProductBOrequest.getRequest());
			System.out.println("size before"+requestProduct.getRequest().getProductList().size());
			for(int j=0;j<requestProduct.getRequest().getProductList().size();j++) {
				OrderDetailsRequest orderDetailsRequest = new OrderDetailsRequest();
				orderDetailsRequest = requestProduct.getRequest();
				List<OrderedProductDetails> ProductList = new ArrayList<OrderedProductDetails>();
				orderDetailsRequest.setProductList(requestProduct.getRequest().getProductList());
				System.out.println("size after - 0 "+requestProduct.getRequest().getProductList().size());
				addOrUpdateOrderProductBOrequest.setRequest(orderDetailsRequest);
				System.out.println("size after - 1 "+requestProduct.getRequest().getProductList().size());
				try {
					Float totalProductCost = Float.parseFloat(addOrUpdateOrderProductBOrequest.getRequest().getProductList().get(j).getProductCost()) *  Float.parseFloat(addOrUpdateOrderProductBOrequest.getRequest().getProductList().get(j).getOrderedQty());
					addOrUpdateOrderProductBOrequest.getRequest().setOrderCost(totalProductCost.toString());
					otsOrderDetails = orderServiceDAO.insertOrderAndGetOrderId(addOrUpdateOrderProductBOrequest);
					System.out.println("size after +0"+requestProduct.getRequest().getProductList().size());
					try {
							orderProductDao.insertOrdrerProductByOrderId(Integer.parseInt(otsOrderDetails.getOrderId()), addOrUpdateOrderProductBOrequest.getRequest().getProductList().get(j));
						Response = getOrderDiruectSalesVoucher(otsOrderDetails.getOrderId());	
						try {
							String notification = "Donation request is in progress";
							fcmPushNotification.sendPushNotification(user.getDeviceId(),"etaarana App" ,notification);
							notification = "New Donation request";
							fcmPushNotification.sendPushNotification(Customer.getDeviceId(),"etaarana App" ,notification);
						}catch(Exception e) {
							
						}
					}catch(Exception e){
						throw new BusinessException(e,ErrorEnumeration.ERROR_IN_STOCK);
					} catch (Throwable e) {
						throw new BusinessException(e, ErrorEnumeration.ERROR_IN_STOCK);
					}
				}catch(Exception e){
					throw new BusinessException(e, ErrorEnumeration.ERROR_IN_STOCK);
				} catch (Throwable e) {
					throw new BusinessException(e, ErrorEnumeration.ERROR_IN_STOCK);
				}
			}
		}
		return Response;
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
			
			Date date = new Date(0);
			long d = System.currentTimeMillis();
			date.setTime(d);
			updateOrderDetailsRequest.getRequest().setDeliverdDate(date.toString());
			orderServiceDAO.UpdateOrder(updateOrderDetailsRequest);
			Response = "Updated For OrderId"+updateOrderDetailsRequest.getRequest().getOrderId();
			try {
				UserDetails User;
				User = userServiceDAO.getUserDetails(Integer.parseInt(updateOrderDetailsRequest.getRequest().getAssignedId()));
				fcmPushNotification.sendPushNotification(User.getDeviceId(),"etaarana Apps" , "order is updated");
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
				fcmPushNotification.sendPushNotification(Employee.getDeviceId(),"etaarana app" , Notification);

				UserDetails customer = userServiceDAO.getUserDetails(Integer.getInteger(otsOrderDetails.getCustomerId()));
				Notification =" Order Placed : Your order "+ otsOrderDetails.getOrderNumber()+" has been confirmed and will be delivered on or before "+otsOrderDetails.getOrderDeliveryDate();
				fcmPushNotification.sendPushNotification(customer.getDeviceId(),"etaarana app" , Notification);
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
				orderProductDetails.setStock("0");
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

			
			//---------------------------------------Check for stock-----------------------------------------
//			try {
//				for(int j=0;j<saleVocherBoRequest.getRequest().getOrderProductlist().size();j++) {
//						GetProductStockRequest getProductStockRequest = new GetProductStockRequest();
//						GetProductRequestModel productRequestModel = new GetProductRequestModel();
//						productRequestModel.setDistributorId(saleVocherBoRequest.getRequest().getDistributorId());
//						productRequestModel.setProductId(saleVocherBoRequest.getRequest().getOrderProductlist().get(j).getProdcutId());
//						getProductStockRequest.setRequestData(productRequestModel);
//						System.out.println("APP------------------>"+productStockDao.getProductStockByUidAndPid(getProductStockRequest).getStockQuantity());
//						System.out.println("APP------------------>"+Integer.parseInt(saleVocherBoRequest.getRequest().getOrderProductlist().get(j).getOrderQty()));
//						if(Integer.parseInt(productStockDao.getProductStockByUidAndPid(getProductStockRequest).getStockQuantity())<Integer.parseInt(saleVocherBoRequest.getRequest().getOrderProductlist().get(j).getOrderQty())) {
//							return "NO";
//						}
//				}
//			}catch(Exception e) {
//				return "NO";
//			}
//			
				
		
			//---------------------------------------Check fpr stock-----------------------------------------
		
			
			CustomerOutstandingBORequest customerOutstandingBORequest = new CustomerOutstandingBORequest();
			orderDetails = orderServiceDAO.SalesVocher(saleVocherBoRequest);


			CustomerOutstandingDetails customerOutstandingDetails = new CustomerOutstandingDetails();
			customerOutstandingDetails.setCustomerId(saleVocherBoRequest.getRequest().getCustomerId());
			customerOutstandingDetails.setCustomerOutstandingAmt(saleVocherBoRequest.getRequest().getOutstandingAmount());
			customerOutstandingBORequest.setRequestData(customerOutstandingDetails);

		//	customerOutstandingAmtDAO.updateCustomerOutstandingAmt(customerOutstandingBORequest);

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
//				mapUserProductDAO.UpdateBySaleVocher(customerProductDataBORequest);

				addProductStock.setOrderId(saleVocherBoRequest.getRequest().getOrderId());
				addProductStock.setUsersId(orderDetails.getDistributorId());
				addProductStock.setProductStockQty(saleVocherBoRequest.getRequest().getOrderProductlist().get(i).getOrderQty());
				addProductStock.setProductStockStatus("Active");
				addProductStock.setProductId(saleVocherBoRequest.getRequest().getOrderProductlist().get(i).getProdcutId());

				addProductStockBORequest.setRequestData(addProductStock);

				productStockHistoryDao.addProductStockHistory(addProductStockBORequest);
				addProductStockBORequest.getRequestData().setUsersId("1");
				productStockDao.removeProductStock(addProductStockBORequest);
				
			}
			try {
				UserDetails distributor;
				distributor = userServiceDAO.getUserDetails(Integer.parseInt(orderDetails.getDistributorId()));
				String notification ="The order "+orderDetails.getOrderNumber()+" has been successfully delivered on "+saleVocherBoRequest.getRequest().getDeliverdDate();
				fcmPushNotification.sendPushNotification(distributor.getDeviceId(),"etaarana Apps" , notification);

				UserDetails Customer;
				Customer = userServiceDAO.getUserDetails(Integer.parseInt(orderDetails.getCustomerId()));
				notification = "Your order "+orderDetails.getOrderNumber()+" has been successfully delivered on "+ saleVocherBoRequest.getRequest().getDeliverdDate();
				fcmPushNotification.sendPushNotification(Customer.getDeviceId(),"etaarana Apps" , notification);
			}catch(Exception e) {
				return "Updated";
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
				if(getOrderBORequest.getRequest().getProductId()!=null || getOrderBORequest.getRequest().getProductId() == "" ) {
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
			if(getOrderBORequest.getRequest(). getPdf().equalsIgnoreCase("YES")) {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");  
				LocalDateTime now = LocalDateTime.now(); 
				String pdf = orderLedgureReportPDF(getOrderDetailsAndProductDetails,userServiceDAO.getUserIdUsers(getOrderBORequest.getRequest().getDistributorsId()).get(0).getFirstName(),now.toString().substring(0, 10));
				orderProductBOResponse.setPdf(pdf);
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
		schedulerResponceOrderModel = schedulerDao.getSchedularData(getSchedulerRequest);
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
 				scheduleOrder.setProductId(requestOrder.get(i).getOtsProductId().getOtsProductId().toString());
 				Float cost = Float.valueOf(productServiceDAO.getProductDetils(requestOrder.get(i).getOtsProductId().getOtsProductId().toString()).getProductPrice())*Float.valueOf(requestOrder.get(i).getOtsRequestQty());
 				orderDetailsRequest.setOrderCost(cost.toString());
 				scheduleOrder.setProductCost(productServiceDAO.getProductDetils(requestOrder.get(i).getOtsProductId().getOtsProductId().toString()).getProductPrice());
 			}
 			scheduleOrder.setOrderedQty(requestOrder.get(i).getOtsRequestQty().toString());
 			scheduleOrder.setDeliveredQty(requestOrder.get(i).getOtsRequestQty().toString());
 			productList.add(scheduleOrder);
 			orderDetailsRequest.setProductList(productList);
 			addOrUpdateOrderProductBOrequest.setRequest(orderDetailsRequest);
  		//	int ordercost = Math.round(Integer.parseInt(addOrUpdateOrderProductBOrequest.getRequest().getOrderCost()));
 		//	addOrUpdateOrderProductBOrequest.getRequest().setOrderCost(String.valueOf(ordercost));
 			schedulerOrder(addOrUpdateOrderProductBOrequest); 			 		
 			System.out.print("------------------------Scheduler worked----------------------------------------");
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
			AddProductStock addProductStock = new AddProductStock();
			AddProductStockBORequest addProductBORequest = new AddProductStockBORequest();
			
			if(updateOrderStatusRequest.getRequest().getStatus().equalsIgnoreCase("DoneDonation")) {
				List<OrderProductDetails> orderproduct = orderProductDao.getProductListByOrderId(updateOrderStatusRequest.getRequest().getOrderId());
				
				addProductStock.setUsersId("1");
				addProductStock.setProductStockStatus("active");
				addProductStock.setProductId(orderproduct.get(0).getOtsProductId());
				addProductStock.setProductStockQty(orderproduct.get(0).getOtsOrderedQty());
				addProductStock.setOrderId(updateOrderStatusRequest.getRequest().getOrderId());
				addProductBORequest.setRequestData(addProductStock);
				productStockDao.removeProductStock(addProductBORequest);
				productStockHistoryDao.addProductStockHistory(addProductBORequest);
				
			}
			
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
					fcmPushNotification.sendPushNotification(user.getDeviceId(),"etaarana App" ,notification);
					notification = "Order Placed : Your order "+otsOrderDetails.getOrderNumber()+" had been placed";
					fcmPushNotification.sendPushNotification(Customer.getDeviceId(),"etaarana App" ,notification);
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
			//	orderProductDetails.setStock(productStockDao.getProductStockByUidAndPid(getProductStockRequest).getStockQuantity()==null?null:productStockDao.getProductStockByUidAndPid(getProductStockRequest).getStockQuantity());
				orderProductDetailsList2.add(j,orderProductDetails);
			}
			
			GetOrderDetailsAndProductDetails.add(i,GetProductAndOrderDetails(OrderDetailsList.get(i),orderProductDetailsList2,CustomerAmount));
			CustomerAmount = null;
		}
		
		orderProductBOResponse.setOrderList(GetOrderDetailsAndProductDetails);
		if(GetOrderDetailsAndProductDetails.get(0).getOrderStatus().equalsIgnoreCase("newRequest")) {
			System.out.println("----------"+GetOrderDetailsAndProductDetails.get(0).getOrderStatus()+"-----------");
			OrderDetailsAndProductDetails requestProductDetails =  requestProductDao.addOrUpdateRequest(orderProductBOResponse.getOrderList().get(0));

			orderRequestMappingDAO.MapOrderAndRequest(requestProductDetails);
			
		}
		return orderProductBOResponse;
	}
	catch(Exception e){
		
	}
	catch (Throwable e) {
		
		}
	return null;
	}

	public String  orderLedgureReportPDF(List<OrderDetailsAndProductDetails> getOrderDetailsAndProductDetails ,String distributorName, String date) {
		String tableValueString ="";
		String reportDetails = "<head ><h3 style='text-align:center;'>Customer Ledger Report</h3></head>";
		reportDetails += "<head>Distributer Name :"+distributorName+"</head>";
		reportDetails += "<head ><h3>Date:"+date+"</h3></head>  </br>";
		
		int slno=0;
		tableValueString = "<table border=\"1\"><tr>\r\n" + 
				"	<th>Sl no</th>\r\n" + 
				"	<th>Customer Name</th>\r\n" + 
				"    <th>Order No</th>\r\n" + 
				"	<th>Delivered date</th>\r\n" + 
				"	<th>Product Name</th>\r\n" +
				"	<th>Order Qty</th>\r\n" +
				"	<th>Delivered Qty</th>\r\n" +
				"	<th>Empty Can Recieved</th>\r\n" +
				"	<th>Balance Can</th>\r\n" +
				"	<th>Order Cost</th>\r\n" +
				"	<th>Cash Recieved</th>\r\n" +
				"	<th>Amount OutStanding</th>\r\n" +
				"</tr>";
		
		String productList= " ";
		String orderqty = " ";
		String deliverdQty = " ";
		String emptyCanRecived = " ";
		String balanceCan = " ";
		for(OrderDetailsAndProductDetails orderDetailsAndProductDetailed:getOrderDetailsAndProductDetails) {
			for(int i=0; i<orderDetailsAndProductDetailed.getOrderdProducts().size(); i++) {
				productList +=  "<table border=\"0\"><tr>\r\n" + 
						"	<td>"+orderDetailsAndProductDetailed.getOrderdProducts().get(i).getProductName()+"</td>\r\n" +
						"</tr>";
				productList=productList+ "</table>";
				/*---------------------------------------------------------------------------------------------------------*/
				orderqty +=  "<table border=\"0\"><tr>\r\n" + 
						"	<td>"+orderDetailsAndProductDetailed.getOrderdProducts().get(i).getOtsOrderedQty()+"</td>\r\n" +
						"</tr>";
				orderqty=orderqty+ "</table>";
				/*---------------------------------------------------------------------------------------------------------*/
				deliverdQty +=  "<table border=\"0\"><tr>\r\n" + 
						"	<td>"+orderDetailsAndProductDetailed.getOrderdProducts().get(i).getOtsDeliveredQty()+"</td>\r\n" +
						"</tr>";
				deliverdQty=deliverdQty+ "</table>";
				/*---------------------------------------------------------------------------------------------------------*/
				emptyCanRecived +=  "<table border=\"0\"><tr>\r\n" + 
						"	<td>"+orderDetailsAndProductDetailed.getOrderdProducts().get(i).getEmptyCanRecived()+"</td>\r\n" +
						"</tr>";
				emptyCanRecived=emptyCanRecived+ "</table>";
				/*---------------------------------------------------------------------------------------------------------*/
				balanceCan +=  "<table border=\"0\"><tr>\r\n" + 
						"	<td>"+orderDetailsAndProductDetailed.getOrderdProducts().get(i).getBalanceCan()+"</td>\r\n" +
						"</tr>";
				balanceCan=balanceCan+ "</table>";
				/*---------------------------------------------------------------------------------------------------------*/

			}
			
			slno++;
			tableValueString=tableValueString+"<tr>\r\n" + 
					"	<td>"+slno+"</td>\r\n" + 
					"   <td>"+orderDetailsAndProductDetailed.getCustomerDetails().getFirstName()+"</td>\r\n" +
					"	<td>"+orderDetailsAndProductDetailed.getOrderNumber()+"</td>\r\n" + 
					"	<td>"+orderDetailsAndProductDetailed.getDelivaredDate()+"</td>\r\n" + 
					"	<td>"+productList+"</td>\r\n" + 
					"   <td>"+orderqty+"</td>\r\n" +
					"   <td>"+deliverdQty+"</td>\r\n" +
					"   <td>"+emptyCanRecived+"</td>\r\n" +
					"   <td>"+balanceCan+"</td>\r\n" +
					"   <td>"+orderDetailsAndProductDetailed.getOrderCost()+"</td>\r\n" +
					"   <td>"+orderDetailsAndProductDetailed.getAmountRecived()+"</td>\r\n" +
					"   <td>"+orderDetailsAndProductDetailed.getOrderOutStanding()+"</td>\r\n" +
					"</tr>";
			productList="";
			orderqty="";
			deliverdQty="";
			emptyCanRecived="";
			balanceCan="";
			
			}
		tableValueString =tableValueString+ "</table>";
		String htmlString = "<html>"+reportDetails+tableValueString+"</html>";
		String path = OTSUtil.generateReportPDFFromHTML(htmlString,"CustomerLedgureRepo.pdf");
		byte[] fileContent;
		String encodedString = null;
		try {
			fileContent = FileUtils.readFileToByteArray(new File(path));
			encodedString = Base64.getEncoder().encodeToString(fileContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return encodedString;	
			
	}


	@Override
	public DonationResponseByStatus getDonationListBystatus(GetDonationByStatusRequest donationByStatusRequest) {
		return requestProductDao.getDonationListBystatus(donationByStatusRequest);
	}
	
	@Override
	public String addNewDonation(AddDonationtoRequest addDonationtoRequest) {
//		captureRazorPayment(addDonationtoRequest) ;
		//--------------------------- to reduce bulk request---------------------------------------------
		try {
			if(addDonationtoRequest.getRequest().get(0).getDonationStatus().equalsIgnoreCase("directDonation")) {
				donationServiceDAO.addNewDonation(addDonationtoRequest);
			}else {
				DonationBoResponse donationBoResponse = requestProductDao.addNewDonation(addDonationtoRequest);
				donationBoResponse.setDonationId(donationServiceDAO.addNewDonation(addDonationtoRequest));
				donationRequestMappingDAO.addNewDonation(donationBoResponse);
				donationBoResponse.setOrderId(addDonationtoRequest.getRequest().get(0).getOrderId());
				//--------------------------- end of reduce bulk request---------------------------------------------
				
				if(addDonationtoRequest.getRequest().get(0).getDonationMethod().equalsIgnoreCase("cash")) {	
					if(!addDonationtoRequest.getRequest().get(0).getOrderId().equalsIgnoreCase("any")) {
					//----------------------------donor selected the beneficiary------------------------------------------------
						orderServiceDAO.UpdateOrderForRequest(donationBoResponse);
					}
					//----------------------------to add stock to rotary------------------------------------------------
					AddProductStock addProductStock = new AddProductStock();
					addProductStock.setProductId(addDonationtoRequest.getRequest().get(0).getProductId());
					addProductStock.setProductStockQty(addDonationtoRequest.getRequest().get(0).getDonatedQty());
					addProductStock.setUsersId("1");
//					AddProductStockBORequest addProductStockBORequest = new AddProductStockBORequest();
//					addProductStockBORequest.setRequestData(addProductStock);
//					productStockDao.addProductStock(addProductStockBORequest);
					
				}else if(addDonationtoRequest.getRequest().get(0).getDonationMethod().equalsIgnoreCase("kind")){
					if(addDonationtoRequest.getRequest().get(0).getOrderId().equalsIgnoreCase("any")) {
						
//						AddProductStock addProductStock = new AddProductStock();
//						addProductStock.setProductId(addDonationtoRequest.getRequest().get(0).getProductId());
//						addProductStock.setProductStockQty(addDonationtoRequest.getRequest().get(0).getDonatedQty());
//						addProductStock.setUsersId("1");
//						AddProductStockBORequest addProductStockBORequest = new AddProductStockBORequest();
//						addProductStockBORequest.setRequestData(addProductStock);
//						productStockDao.addProductStock(addProductStockBORequest);
						
					}else {
						orderServiceDAO.UpdateOrderForRequest(donationBoResponse);
					}
				}
				
				return "success";
			}
		}catch(Exception e) {
			System.out.print(e);
		}
		
		return "success";
		
	}


	@Override
	public GetDonationReportByDateResponse getDonationReportByDate(
			GetDonationReportByDateRequest donationReportByDateRequest) {
		try {
			return donationRequestMappingDAO.getDonationReportByDate(donationReportByDateRequest);
		}catch(Exception e){
			throw new BusinessException(e, ErrorEnumeration.No_Donation);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.No_Donation);
		}
	}


	@Override
	public GetUserDetailsForResponse getListOfOrderDetailsForRequest(
			GetUserDetailsForRequest getUserDetailsForRequest) {
		try {
			GetUserDetailsForResponse userDetailsForResponse = new GetUserDetailsForResponse();
			userDetailsForResponse = orderRequestMappingDAO.getListOfOrderDetailsForRequest(getUserDetailsForRequest);
			
			for(int i=0 ; i<userDetailsForResponse.getRequestProductDetails().size();i++) {
				List<OrderProductDetails> orderProductDetailsList = new ArrayList<OrderProductDetails>();
				orderProductDetailsList = orderProductDao.getProductListByOrderId(userDetailsForResponse.getRequestProductDetails().get(i).getOrderDetails().getOrderId());
				userDetailsForResponse.getRequestProductDetails().get(i).getOrderDetails().setOrderdProducts(orderProductDetailsList);
			}
			
			return userDetailsForResponse;
		}catch(Exception e) {
			throw new BusinessException(e, ErrorEnumeration.No_Donation);
		}
	}


	@Override
	public GetDonationReportByDateResponse getDonationForUpdateStatus(GetDonationByStatusRequest donationByStatusRequest) {
		GetDonationReportByDateResponse getDonationReportByDateResponse = new GetDonationReportByDateResponse();
		System.out.print("2");
		getDonationReportByDateResponse = donationServiceDAO.getDonationForUpdateStatus(donationByStatusRequest);
		if(!donationByStatusRequest.getRequest().getStatus().equalsIgnoreCase("directDonation")) {
			getDonationReportByDateResponse = donationRequestMappingDAO.getRequestByDonationId(getDonationReportByDateResponse);	
		}
		
		return getDonationReportByDateResponse;
	}


	@Override
	public String updateDonation(UpdateDonationRequest updateDonationRequest) {
		if(updateDonationRequest.getRequest().getDonationStatus().equalsIgnoreCase("receivedonation")) {
			AddProductStock addProductStock = new AddProductStock();

			System.out.print("1");
			addProductStock.setProductId(updateDonationRequest.getRequest().getProductDetails().getProductId());
			addProductStock.setProductStockQty(updateDonationRequest.getRequest().getDonatedQty());
			
			addProductStock.setUsersId("1");
			AddProductStockBORequest addProductStockBORequest = new AddProductStockBORequest();
			addProductStockBORequest.setRequestData(addProductStock);
			productStockDao.addProductStock(addProductStockBORequest);
			
			AddProductStockBORequest addProductBORequest = new AddProductStockBORequest();
			addProductBORequest.setRequestData(addProductStock);
			productStockHistoryDao.addProductStockHistory(addProductBORequest);
		}
		return donationServiceDAO.updateDonation(updateDonationRequest);
	}


	@Override
	public String donateDonation(SaleVocherBoRequest saleVocherBoRequest) {
		// TODO Auto-generated method stub
		AddProductStockBORequest addProductStockBORequest = new AddProductStockBORequest();
		addProductStockBORequest.getRequestData().setProductId(saleVocherBoRequest.getRequest().getOrderProductlist().get(0).getProdcutId());
		addProductStockBORequest.getRequestData().setProductStockQty(saleVocherBoRequest.getRequest().getOrderProductlist().get(0).getOrderQty());
		addProductStockBORequest.getRequestData().setUsersId("1");
		productStockDao.removeProductStock(addProductStockBORequest);
		return orderServiceDAO.donateDonation(saleVocherBoRequest);
	}


	@Override
	public OrderDetailsBOResponse getRazorPayOrder(UpdateOrderDetailsRequest updateOrderDetailsRequest) throws JSONException {
		// TODO Auto-generated method stub
		OrderDetailsBOResponse orderDetailsBOResponse = new OrderDetailsBOResponse();
		try {
			RazorpayClient razorpay = null;
			if(updateOrderDetailsRequest.getRequest().getPaymentFlowStatus().equalsIgnoreCase("gift")) {
				razorpay = new RazorpayClient("rzp_test_S5Dx5cZQVEb1NC", "ZicA2AsX2rt55gRCWFgwix5R");
			//	orderDetails.setRazorPayKey();
			}else {
				//ots account
				razorpay = new RazorpayClient("rzp_test_efRXqD8KT3N1wL", "a0mAWrRA0T0PZ6rYvbja7BGx");
			//	orderDetails.setRazorPayKey();
			}
			
			JSONObject orderRequest = new JSONObject();
			  try {
				orderRequest.put("amount", updateOrderDetailsRequest.getRequest().getOrderCost());
				orderRequest.put("currency", "INR");
				orderRequest.put("receipt", updateOrderDetailsRequest.getRequest().getOrderId());
				orderRequest.put("payment_capture", true);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println(e);
				e.printStackTrace();
			} // amount in the smallest currency unit
			 Order order = razorpay.Orders.create(orderRequest);
			 System.out.println(order);
			 System.out.println(order.toJson().get("amount"));
			 OrderDetails orderDetails = new OrderDetails();
			 orderDetails.setOrderId(order.toJson().get("id").toString());
			 orderDetails.setReceipt(order.toJson().get("receipt").toString());
			 List<OrderDetails> orderDetailsList = new ArrayList<OrderDetails>();
			 orderDetailsList.add(orderDetails);
			 orderDetailsBOResponse.setOrderDetails(orderDetailsList);
		} catch (RazorpayException e) {
			e.printStackTrace();
			System.out.println(e);
			
		}
		return orderDetailsBOResponse;
	}

//	public Payment captureRazorPayment(AddDonationtoRequest addDonationtoRequest) {
//		Payment payment = null;
//		RazorpayClient razorpay = null;
//		try {
//			if(addDonationtoRequest.getRequest().get(0).getPaymentFlowStatus().equalsIgnoreCase("gift")) {
//				razorpay = new RazorpayClient("rzp_test_kA2hROKJeaOPQU", "zNwDTkKrdnkOSaBs43b4N915");
//			}else {
//				razorpay = new RazorpayClient("rzp_test_TQ28uTerb7d5Oj", "60gpWr3Gqqqxw7VrseSvDWST");
//			}
//			  JSONObject captureRequest = new JSONObject();
//			  Float RazorPayAmount = Float.parseFloat(addDonationtoRequest.getRequest().get(0).getDontaionAmount() ) * 100 ;
//			  captureRequest.put("amount",RazorPayAmount );
//			  captureRequest.put("currency", "INR");
//
//			  payment   = razorpay.Payments.capture(addDonationtoRequest.getRequest().get(0).getPaymentId(), captureRequest);
//			  System.out.println(payment.toString());
//			  
//			  try {
//					 List<UserDetails> userDetails = userServiceDAO.getUserIdUsers(addDonationtoRequest.getRequest().get(0).getDonorId());
//					EmailUtil.sendDonationMail(userDetails.get(0).getEmailId(),"","Etaarana Donation", 
//							"Dear "+userDetails.get(0).getFirstName() +" \n \n"
//							+ "Thank you for your generous gift to Rotary"
//							+ " you have donated " 
//							+ addDonationtoRequest.getRequest().get(0).getDontaionAmount()
//							+" INR. \n"
//							+"You truly make the difference for us, and we are extremely grateful! \n"
//							+"Thank you");
//					
//				}catch(Exception e) {
//					System.out.print(e);
//				}
//			  
//			}catch (RazorpayException e) {
//				EmailUtil.sendDonationMail("manoj.vg@ortusolis.com","","Etaarana Error in donation", 
//						addDonationtoRequest.toString()
//						+ "captureRazorPayment ---> addNewDonationAPI");
//			  System.out.println(e.getMessage());
//			}  catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		
//		return payment;
//	}
//
//	
//	public Payment capturePaymentForOrder(AddOrUpdateOrderProductBOrequest addOrUpdateOrderProductBOrequest) {
//		Payment payment = null;
//		RazorpayClient razorpay = null;
//		try {
//			if(addOrUpdateOrderProductBOrequest.getRequest().getPaymentFlowStatus().equalsIgnoreCase("gift")) {
//				razorpay = new RazorpayClient("rzp_test_kA2hROKJeaOPQU", "zNwDTkKrdnkOSaBs43b4N915");
//			}else {
//				razorpay = new RazorpayClient("rzp_test_TQ28uTerb7d5Oj", "60gpWr3Gqqqxw7VrseSvDWST");
//			}
//			  JSONObject captureRequest = new JSONObject();
//			  Float RazorPayAmount = Float.parseFloat(addOrUpdateOrderProductBOrequest.getRequest().getOrderCost()) * 100 ;
//			  captureRequest.put("amount",RazorPayAmount );
//			  captureRequest.put("currency", "INR");
//
//			  payment   = razorpay.Payments.capture(addOrUpdateOrderProductBOrequest.getRequest().getPaymentId(), captureRequest);
//			  System.out.println(payment.toString());
//			  
//			} catch (RazorpayException e) {
//				EmailUtil.sendDonationMail("manoj.vg@ortusolis.com","","Etaarana Error in donation", 
//						addOrUpdateOrderProductBOrequest.toString()
//						+ "capturePaymentForOrder ---> insertOrderAPI");
//			  System.out.println(e.getMessage());
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		
//		return payment;
//	}


	@Override
	public JSONObject fetchPaymentDetailsByPaymetId(String paymentId) {
		Payment payment = null;
		RazorpayClient razorpay;
		JSONObject paymentDetails = null;
		try {
			razorpay = new RazorpayClient("rzp_test_NYxmr2CtwORaZd", "GEOEnpzemqrQuPlVesUQvLBW");
			payment = razorpay.Payments.fetch(paymentId);
			paymentDetails = new JSONObject(payment);
		} catch (RazorpayException e1) {
			EmailUtil.sendDonationMail("manoj.vg@ortusolis.com","","Etaarana Error in donation", 
					paymentId
					+ "fetchPaymentDetailsByPaymetId");
			e1.printStackTrace();
		}
		System.out.print(payment);
		return paymentDetails;
	}


	@Override
	public OrderDetails getOrderDetailsForOrderId(OrderIdBORequest updateOrderDetailsRequest) {
		
		return orderServiceDAO.getOrderDetailsForOrderId(updateOrderDetailsRequest);
	}
	

}
