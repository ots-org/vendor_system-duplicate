package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fuso.enterprise.ots.srv.api.model.domain.AssgineEmployeeModel;
import com.fuso.enterprise.ots.srv.api.model.domain.CloseOrderModelRequest;
import com.fuso.enterprise.ots.srv.api.model.domain.CompleteOrderDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.DonationBoResponse;
import com.fuso.enterprise.ots.srv.api.model.domain.GetBillDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.ListOfOrderId;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderDetails;
import com.fuso.enterprise.ots.srv.api.service.request.AddDonationtoRequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddOrUpdateOrderProductBOrequest;
import com.fuso.enterprise.ots.srv.api.service.request.CloseOrderBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.EmployeeOrderTransferRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetAssginedOrderBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetCustomerOrderByStatusBOrequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetListOfOrderByDateBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetOrderBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetOrderByStatusRequest;
import com.fuso.enterprise.ots.srv.api.service.request.OrderDetailsBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.OrderIdBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.SaleVocherBoRequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateForAssgineBOrequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateOrderDetailsRequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateOrderStatusRequest;
import com.fuso.enterprise.ots.srv.api.service.response.GetListOfOrderByDateBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.OrderDetailsBOResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.common.exception.ErrorEnumeration;
import com.fuso.enterprise.ots.srv.server.dao.OrderServiceDAO;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsBill;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsDonation;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsOrder;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Repository
public class OrderServiceDAOImpl extends AbstractIptDao<OtsOrder, String> implements OrderServiceDAO {
	private Logger logger = LoggerFactory.getLogger(getClass());
	LocalDateTime now = LocalDateTime.now();  
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public OrderServiceDAOImpl() {
		super(OtsOrder.class);
	}

	@Override
	public void updateOrderwithBillID(OtsBill otsBill, List<ListOfOrderId> listOfOrderId) {
		try {
			OtsOrder otsOrder = new OtsOrder();
			Map<String, Object> queryParameter = new HashMap<>();
             /*
              * iteration started for getting each orderId in list
              */
			for (int i = 0; i < listOfOrderId.size(); i++) {
				int orderId = Integer.parseInt(listOfOrderId.get(i).getOrderId());
				queryParameter.put("otsOrderId", orderId);
				otsOrder = super.getResultByNamedQuery("OtsOrder.findByOtsOrderId", queryParameter);
				otsOrder.setOtsOrderId(orderId);
				otsOrder.setOtsOrderStatus("Generated");
				otsOrder.setOtsBillId(otsBill);
				super.getEntityManager().merge(otsOrder);
			}
			/*
			 * iteration end for getting each orderId
			 */
		} catch (NoResultException e) {
			logger.error("Exception while updating Order Entity  :" + e.getMessage());
			e.printStackTrace();
			throw new BusinessException(e.getMessage(), e);
		}

	}

	@Override
	public List<OrderDetails> getOrderBydate(GetOrderBORequest getOrderBORequest) {
		List<OrderDetails> otsOrderDetails = new ArrayList<OrderDetails>();
    	try {
    		List<OtsOrder> OrderList = new ArrayList<OtsOrder>() ;
            try {
            	Map<String, Object> queryParameter = new HashMap<>();
    			queryParameter.put("FromDate", getOrderBORequest.getRequest().getFromTime());
    			queryParameter.put("ToDate", getOrderBORequest.getRequest().getToTime());
            	
    			if(getOrderBORequest.getRequest().getStatus()!=null) {
            		queryParameter.put("status", getOrderBORequest.getRequest().getStatus());
            		if(getOrderBORequest.getRequest().getCustomerId()==null) {
            			OtsUsers DistrubutorId = new OtsUsers();
                		DistrubutorId.setOtsUsersId(Integer.parseInt(getOrderBORequest.getRequest().getDistributorsId()));
                		queryParameter.put("DistributorsId", DistrubutorId);
                		OrderList  = super.getResultListByNamedQuery("OtsOrder.GetOrderListByTimeAndStatusForDistributor", queryParameter);
            		}else if(getOrderBORequest.getRequest().getDistributorsId()==null) {
            			OtsUsers CustomerId = new OtsUsers();
            			CustomerId.setOtsUsersId(Integer.parseInt(getOrderBORequest.getRequest().getCustomerId()));
                		queryParameter.put("CustomerId", CustomerId);
                		OrderList  = super.getResultListByNamedQuery("OtsOrder.GetOrderListByTimeAndStatusForCustomerId", queryParameter);
            		}
            	}else {
            		if(getOrderBORequest.getRequest().getCustomerId()!=null) {
            			OtsUsers CustomerId = new OtsUsers();
            			CustomerId.setOtsUsersId(Integer.parseInt(getOrderBORequest.getRequest().getCustomerId()));
                		queryParameter.put("otsCustomerId", CustomerId);
                		OrderList  = super.getResultListByNamedQuery("OtsOrder.GetListOfOrderByDateforCustomer", queryParameter);          
            		}else {
            			OtsUsers DistrubutorId = new OtsUsers();
	                	DistrubutorId.setOtsUsersId(Integer.parseInt(getOrderBORequest.getRequest().getDistributorsId()));
	            		queryParameter.put("DistributorsId", DistrubutorId);
	            		OrderList  = super.getResultListByNamedQuery("OtsOrder.GetOrderListByTime", queryParameter);
            		}
	            		
            	}
            } catch (NoResultException e) {
            	logger.error("Exception while fetching data from DB :"+e.getMessage());
        		e.printStackTrace();
            	throw new BusinessException(e.getMessage(), e);
            }
            otsOrderDetails =  OrderList.stream().map(OtsOrder -> convertOrderDetailsFromEntityToDomain(OtsOrder)).collect(Collectors.toList());
    	}catch(Exception e) {
			logger.error("Error in  order table"+e.getMessage());
    		e.printStackTrace();
    		throw new BusinessException(e.getMessage(), e);
    	}
    	return otsOrderDetails;
	}


	private OrderDetails convertOrderDetailsFromEntityToDomain(OtsOrder otsOrder) {
		OrderDetails orderDetails =  new OrderDetails() ;
		orderDetails.setOrderId((otsOrder.getOtsOrderId()==null)?null:otsOrder.getOtsOrderId().toString());
		orderDetails.setOrderDate((otsOrder.getOtsOrderDt()==null)?null:otsOrder.getOtsOrderDt().toString());
		orderDetails.setOrderDeliveryDate((otsOrder.getOtsOrderDeliveryDt()==null)?null:otsOrder.getOtsOrderDeliveryDt().toString());
		orderDetails.setDistributorId((otsOrder.getOtsDistributorId()==null)?null:otsOrder.getOtsDistributorId().getOtsUsersId().toString());
		orderDetails.setCustomerId(otsOrder.getOtsCustomerId()==null?null:otsOrder.getOtsCustomerId().getOtsUsersId().toString());
		orderDetails.setAssignedId(otsOrder.getOtsAssignedId()==null?null:otsOrder.getOtsAssignedId().getOtsUsersId().toString());
		orderDetails.setOrderCost(otsOrder.getOtsOrderCost()==null?null:otsOrder.getOtsOrderCost().toString());
		orderDetails.setOrderDeliverdDate(otsOrder.getOtsOrderDeliveredDt()==null?null:otsOrder.getOtsOrderDeliveredDt().toString());
		orderDetails.setStatus(otsOrder.getOtsOrderStatus()==null?null:otsOrder.getOtsOrderStatus());
		orderDetails.setOrderNumber(otsOrder.getOtsOrderNumber()==null?null:otsOrder.getOtsOrderNumber());
		orderDetails.setBalanceCan(otsOrder.getOtsOrderBalanceCan());
		orderDetails.setOutstandingAmount(otsOrder.getOtsOrderOutstandingAmount());
		orderDetails.setAmountRecived(otsOrder.getOtsOrderAmountReceived()==null?null:otsOrder.getOtsOrderAmountReceived().toString());
		orderDetails.setOrderNumber(otsOrder.getOtsOrderNumber());
		orderDetails.setAddress(otsOrder.getOtsOrderAddress());
		orderDetails.setPaymentStatus(otsOrder.getOtsOrderPaymentStatus()==null?"":otsOrder.getOtsOrderPaymentStatus());
		orderDetails.setPaymentId(otsOrder.getOtsOrderPayementId()==null?null:otsOrder.getOtsOrderPayementId());
	//	orderDetails.setBasePrice(otsOrder.getOtsOrderBasePrice());
		if(otsOrder.getOtsDonationId()!=null) {
			orderDetails.setDonatorId(otsOrder.getOtsDonationId().getOtsDonorsId().getOtsUsersId().toString());
			orderDetails.setDonationId(otsOrder.getOtsDonationId().getOtsDonationId()==null?null:otsOrder.getOtsDonationId().toString());
			orderDetails.setDonarContactNumber(otsOrder.getOtsDonationId().getOtsDonorsId().getOtsUsersContactNo());
			orderDetails.setDonarAddress(otsOrder.getOtsDonationId().getOtsDonorsId().getOtsUsersAddr1()==null?null:otsOrder.getOtsDonationId().getOtsDonorsId().getOtsUsersAddr1());
			orderDetails.setDonationStatus(otsOrder.getOtsDonationId().getOtsDonationStatus() == null?null:otsOrder.getOtsDonationId().getOtsDonationStatus());
		}
		return orderDetails;
	}

	@Override
	public List<OrderDetails> getOrderIdByDistributorId(GetOrderByStatusRequest getOrderByStatusRequest){
		List<OrderDetails> otsOrderDetails = new ArrayList<OrderDetails>();
    	try {
    		List<OtsOrder> OrderList = new ArrayList<OtsOrder>() ;
            try {
            	Map<String, Object> queryParameter = new HashMap<>();
            	OtsUsers DistrubutorId = new OtsUsers();
            	DistrubutorId.setOtsUsersId(Integer.parseInt(getOrderByStatusRequest.getRequest().getDistrubitorId()));
    			queryParameter.put("otsDistributorId", DistrubutorId);
    			queryParameter.put("Status", getOrderByStatusRequest.getRequest().getStatus());
    			OrderList  = super.getResultListByNamedQuery("OtsOrder.getOrderIdByDistrubitorId", queryParameter);
            } catch (NoResultException e) {
            	logger.error("Exception while fetching data from DB :"+e.getMessage());
        		e.printStackTrace();
            	throw new BusinessException(e.getMessage(), e);}
            otsOrderDetails =  OrderList.stream().map(OtsOrder -> convertOrderDetailsFromEntityToDomain(OtsOrder)).collect(Collectors.toList());
    	    System.out.println(otsOrderDetails.get(0).getOrderDate()+"DAO");
    	}catch(Exception e){
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_ORDER_INSERTION);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_ORDER_INSERTION);
		}
    	return otsOrderDetails;
	}

	@Override
	public OrderDetails insertOrderAndGetOrderId(AddOrUpdateOrderProductBOrequest addOrUpdateOrderProductBOrequest) {
		OrderDetails orderDetails = new OrderDetails();
		try {
			OtsOrder otsOrder = convertDomainToOrderEntity(addOrUpdateOrderProductBOrequest);
			save(otsOrder);
			super.getEntityManager().flush();
			String OrderNumber = "ORD-"+otsOrder.getOtsOrderId().toString();
			otsOrder.setOtsOrderNumber(OrderNumber);
			super.getEntityManager().merge(otsOrder);
			orderDetails = convertOrderDetailsFromEntityToDomain(otsOrder);
			return orderDetails;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("Error in inserting order in order table"+e.getMessage());
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_ORDER_INSERTION);
		} catch (Throwable e) {
			e.printStackTrace();
			logger.error("Error in inserting order in order table"+e.getMessage());
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_ORDER_INSERTION);
		}
	}

	private OtsOrder convertDomainToOrderEntity(AddOrUpdateOrderProductBOrequest addOrUpdateOrderProductBOrequest)
	{
 		OtsOrder otsOrder = new OtsOrder();
 		
 		if(addOrUpdateOrderProductBOrequest.getRequest().getPaymentStatus().equalsIgnoreCase("paid")) {
 			otsOrder.setOtsOrderStatus(addOrUpdateOrderProductBOrequest.getRequest().getPaymentStatus());
 			otsOrder.setOtsOrderPayementId(addOrUpdateOrderProductBOrequest.getRequest().getPaymentId());
 		}else {
 			otsOrder.setOtsOrderPaymentStatus(addOrUpdateOrderProductBOrequest.getRequest().getPaymentStatus());
 		}

		OtsUsers DistributorId = new OtsUsers();
		DistributorId.setOtsUsersId(Integer.parseInt(addOrUpdateOrderProductBOrequest.getRequest().getDistributorId()));
		otsOrder.setOtsDistributorId(DistributorId);

		otsOrder.setOtsOrderBasePrice(addOrUpdateOrderProductBOrequest.getRequest().getBasePrice());
		
		OtsUsers CustomerId = new OtsUsers();
		CustomerId.setOtsUsersId(Integer.parseInt(addOrUpdateOrderProductBOrequest.getRequest().getCustomerId()));
		otsOrder.setOtsCustomerId(CustomerId);

		otsOrder.setOtsOrderAddress(addOrUpdateOrderProductBOrequest.getRequest().getAddress());
		
		if(addOrUpdateOrderProductBOrequest.getRequest().getAssignedId()==null)
		{
			otsOrder.setOtsAssignedId(null);
		}else {
			OtsUsers EmployeeId = new OtsUsers();
			EmployeeId.setOtsUsersId(Integer.parseInt(addOrUpdateOrderProductBOrequest.getRequest().getAssignedId()));
			otsOrder.setOtsAssignedId(EmployeeId);
		}
			BigDecimal costData=new BigDecimal(addOrUpdateOrderProductBOrequest.getRequest().getOrderCost());
			otsOrder.setOtsOrderCost(costData);
			otsOrder.setOtsOrderStatus(addOrUpdateOrderProductBOrequest.getRequest().getOrderStatus());
			otsOrder.setOtsOrderDt(Date.valueOf(addOrUpdateOrderProductBOrequest.getRequest().getOrderDate()));
			otsOrder.setOtsOrderDeliveryDt(Date.valueOf(addOrUpdateOrderProductBOrequest.getRequest().getDelivaryDate()));
		if(addOrUpdateOrderProductBOrequest.getRequest().getDeliverdDate()==null)
		{
			otsOrder.setOtsOrderDeliveredDt(null);
		}else
		{
		otsOrder.setOtsOrderDeliveryDt(Date.valueOf(addOrUpdateOrderProductBOrequest.getRequest().getDelivaryDate()));
		}


		return otsOrder;
	}

	@Override
	public String UpdateOrder(UpdateOrderDetailsRequest updateOrderDetailsRequest) {
	try {
		OtsOrder otsOrder = new OtsOrder();

		otsOrder.setOtsOrderId(Integer.parseInt(updateOrderDetailsRequest.getRequest().getOrderId()));
		OtsUsers DistributorId = new OtsUsers();
		DistributorId.setOtsUsersId(Integer.parseInt(updateOrderDetailsRequest. getRequest().getDistributorId()));
		otsOrder.setOtsDistributorId(DistributorId);

		OtsUsers CustomerId = new OtsUsers();
		CustomerId.setOtsUsersId(Integer.parseInt(updateOrderDetailsRequest.getRequest().getCustomerId()));
		otsOrder.setOtsCustomerId(CustomerId);

		OtsUsers EmployeeId = new OtsUsers();
		if(updateOrderDetailsRequest.getRequest().getAssignedId()==null)
		{
			otsOrder.setOtsAssignedId(null);
		}else {
			EmployeeId.setOtsUsersId(Integer.parseInt(updateOrderDetailsRequest.getRequest().getAssignedId()));
			otsOrder.setOtsAssignedId(EmployeeId);
		}

			otsOrder.setOtsOrderNumber(updateOrderDetailsRequest.getRequest().getOrderNumber());
			try {
				BigDecimal costData=new BigDecimal(updateOrderDetailsRequest.getRequest().getOrderCost());
				otsOrder.setOtsOrderCost(costData);
			}catch(Exception e) {
				otsOrder.setOtsOrderCost(null);
			}
			otsOrder.setOtsOrderStatus(updateOrderDetailsRequest.getRequest().getOrderStatus());
			otsOrder.setOtsOrderDeliveryDt(Date.valueOf(updateOrderDetailsRequest.getRequest().getDeliveryDate()));

		if(updateOrderDetailsRequest.getRequest().getDeliverdDate()==null)
		{
			otsOrder.setOtsOrderDeliveredDt(null);
		}
		else{
			otsOrder.setOtsOrderDeliveryDt(Date.valueOf(updateOrderDetailsRequest.getRequest().getDeliverdDate()));
		}

		super.getEntityManager().merge(otsOrder);
		return "Inseted";
	}catch(Exception e){
		e.printStackTrace();
		logger.error("Error in inserting order in order table"+e.getMessage());
		throw new BusinessException(e, ErrorEnumeration.ERROR_IN_ORDER_INSERTION);}
	catch (Throwable e) {
		e.printStackTrace();
		logger.error("Error in inserting order in order table"+e.getMessage());
		throw new BusinessException(e, ErrorEnumeration.ERROR_IN_ORDER_INSERTION);
		}
	}

	@Override
	public String updateAssginedOrder(UpdateForAssgineBOrequest  updateForAssgineBOrequest) {
		try {
			OtsOrder otsOrder = new OtsOrder();
			Map<String, Object> queryParameter = new HashMap<>();

			queryParameter.put("otsOrderId",Integer.parseInt( updateForAssgineBOrequest.getRequest().getOrderId()));
			otsOrder = super.getResultByNamedQuery("OtsOrder.findByOtsOrderId", queryParameter);

			OtsUsers AssginedId = new OtsUsers();
			AssginedId.setOtsUsersId(Integer.parseInt(updateForAssgineBOrequest.getRequest().getAssignedId()));
			otsOrder.setOtsAssignedId(AssginedId);

			otsOrder.setOtsOrderStatus(updateForAssgineBOrequest.getRequest().getOrderStatus());
			otsOrder.setOtsOrderDeliveryDt(Date.valueOf(updateForAssgineBOrequest.getRequest().getDeliveryDate()));

			String Response = "OrderId "+ updateForAssgineBOrequest.getRequest().getOrderId() +" Is updated";
			return Response;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("Error in inserting order in order table"+e.getMessage());
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_ORDER_INSERTION);
		} catch (Throwable e) {
			e.printStackTrace();
			logger.error("Error in inserting order in order table"+e.getMessage());
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_ORDER_INSERTION);
		}
	}

	@Override
	public List<OrderDetails> getAssginedOrder(GetAssginedOrderBORequest getAssginedOrderBORequest) {
	try {
		List<OtsOrder> orderList = new ArrayList<OtsOrder>() ;
		List<OrderDetails> otsOrderDetails = new ArrayList<OrderDetails>();

		Map<String, Object> queryParameter = new HashMap<>();
		OtsUsers AssginedId = new OtsUsers();
		AssginedId.setOtsUsersId(Integer.parseInt(getAssginedOrderBORequest.getRequest().getEmployeeId()));

		queryParameter.put("otsAssignedId",AssginedId);
		queryParameter.put("otsOrderStatus",getAssginedOrderBORequest.getRequest().getStatus());

		orderList = super.getResultListByNamedQuery("OtsOrder.getAssginedOrder", queryParameter);
		otsOrderDetails =  orderList.stream().map(OtsOrder -> convertOrderDetailsFromEntityToDomain(OtsOrder)).collect(Collectors.toList());
		return otsOrderDetails;
		}
	catch(Exception e){
			throw new BusinessException(e,ErrorEnumeration.FAILURE_ORDER_GET);}
	catch (Throwable e) {
			throw new BusinessException(e,ErrorEnumeration.FAILURE_ORDER_GET);}
	}

	@Override
	public List<OrderDetails> getCustomerOrderStatus(GetCustomerOrderByStatusBOrequest getCustomerOrderByStatusBOrequest) {
		logger.info("Inside Event=1030,Class:OrderServiceDAOImpl,Method:getCustomerOrderStatus,getCustomerOrderByStatusBOrequest " + getCustomerOrderByStatusBOrequest);
		List<OrderDetails> otsOrderDetails = new ArrayList<OrderDetails>();
    	try {
    		List<OtsOrder> OrderList = new ArrayList<OtsOrder>() ;
            try {
            	Map<String, Object> queryParameter = new HashMap<>();
            	OtsUsers CustomerId = new OtsUsers();
            	CustomerId.setOtsUsersId(Integer.parseInt(getCustomerOrderByStatusBOrequest.getRequest().getCustomerId()));

            	queryParameter.put("otsCustomerId", CustomerId);
    			queryParameter.put("Status", getCustomerOrderByStatusBOrequest.getRequest().getStatus());

    			OrderList  = super.getResultListByNamedQuery("OtsOrder.getOrderIdByCustomerId", queryParameter);
            } catch (NoResultException e) {
            	logger.error("Exception while fetching data from DB :"+e.getMessage());
        		e.printStackTrace();
            	throw new BusinessException(e.getMessage(), e);}
            otsOrderDetails =  OrderList.stream().map(OtsOrder -> convertOrderDetailsFromEntityToDomain(OtsOrder)).collect(Collectors.toList());
    	}catch(Exception e){
			throw new BusinessException(e, ErrorEnumeration.FAILURE_ORDER_GET);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.FAILURE_ORDER_GET);
		}
    	return otsOrderDetails;
	}

	@Override
	public OrderDetails closeOrder(CloseOrderBORequest closeOrderBORequest) {
		try {
		OtsOrder otsOrder = new OtsOrder();
		OrderDetails otsOrderDetails = new OrderDetails();

		Map<String, Object> queryParameter = new HashMap<>();

		queryParameter.put("otsOrderId",Integer.parseInt( closeOrderBORequest.getRequest().getOrderId()));
		otsOrder = super.getResultByNamedQuery("OtsOrder.findByOtsOrderId", queryParameter);

		otsOrder.setOtsOrderDeliveredDt(Date.valueOf(closeOrderBORequest.getRequest().getDeliveredDate()));
		otsOrder.setOtsOrderStatus(closeOrderBORequest.getRequest().getOrderStatus());
        otsOrderDetails =  convertOrderDetailsFromEntityToDomain(otsOrder);

		return otsOrderDetails;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("ERROR IN INSERTING PRODUCT TO ORDER-PRODUCT TABLE"+e.getMessage());
			throw new BusinessException(e, ErrorEnumeration.ORDER_CLOSE);}
		catch (Throwable e) {
			e.printStackTrace();
			logger.error("ERROR IN INSERTING PRODUCT TO ORDER-PRODUCT TABLE"+e.getMessage());
			throw new BusinessException(e, ErrorEnumeration.ORDER_CLOSE);}
		}


	@Override
	public List<OrderDetails> getListOrderForBill(GetBillDetails BillDetails){
		logger.info("Inside Event=1031,Class:OrderServiceDAOImpl,Method:getCustomerOrderStatus,BillDetails " + BillDetails);
		try
		{
			List<OtsOrder> OrderList = new ArrayList<OtsOrder>();
			List<OrderDetails> OrderDetails = new ArrayList<OrderDetails>();;
			Map<String, Object> queryParameter = new HashMap<>();

			OtsBill BillId = new OtsBill();
			BillId.setOtsBillId(Integer.valueOf(BillDetails.getBillId()));

			queryParameter.put("otsBillId", BillId);
			OrderList = super.getResultListByNamedQuery("OtsOrder.getOrderListForBillID", queryParameter);
			OrderDetails = OrderList.stream().map(OtsOrder -> convertOrderDetailsFromEntityToDomain(OtsOrder)).collect(Collectors.toList());
			return OrderDetails;
		}catch(Exception e) {
			e.printStackTrace();
			logger.error("ERROR IN INSERTING PRODUCT TO ORDER-PRODUCT TABLE"+e.getMessage());
			throw new BusinessException(e, ErrorEnumeration.ORDER_CLOSE);
		}
	}


	@Override
	public List<CompleteOrderDetails> getListOfOrderByDate(GetListOfOrderByDateBORequest getListOfOrderByDateBORequest) {
		try {
			List<CompleteOrderDetails> otsOrderDetails = new ArrayList<CompleteOrderDetails>();
			List<OtsOrder> orderList = new ArrayList<OtsOrder>();
			Map<String, Object> queryParameter = new HashMap<>();
	    	OtsUsers userId = new OtsUsers();
	    	userId.setOtsUsersId(Integer.parseInt(getListOfOrderByDateBORequest.getRequest().getUserId()));
	    	int realDate = getListOfOrderByDateBORequest.getRequest().getStartDate().getDate()-1;
	    	getListOfOrderByDateBORequest.getRequest().getStartDate().setDate(realDate);
	    	queryParameter.put("FromDate",getListOfOrderByDateBORequest.getRequest().getStartDate());
			queryParameter.put("ToDate",getListOfOrderByDateBORequest.getRequest().getEndDate());
			switch(getListOfOrderByDateBORequest.getRequest().getRole())
			{
				case "Customer":
					queryParameter.put("otsCustomerId", userId);
					if(getListOfOrderByDateBORequest.getRequest().getStatus()==null) {   		
		    			orderList  = super.getResultListByNamedQuery("OtsOrder.GetListOfOrderByDateforCustomer", queryParameter);
					}else {
						queryParameter.put("otsOrderStatus", getListOfOrderByDateBORequest.getRequest().getStatus());
						orderList  = super.getResultListByNamedQuery("OtsOrder.GetListOfOrderByDateforCustomerByStatus", queryParameter);
					}
					break;
				case "Distributor":
				//	queryParameter.put("otsDistributorId", userId);
					if(getListOfOrderByDateBORequest.getRequest().getStatus()==null) {
						orderList  = super.getResultListByNamedQuery("OtsOrder.GetListOfOrderByDateforDistrbutor", queryParameter);
					}else {
						queryParameter.put("otsOrderStatus", getListOfOrderByDateBORequest.getRequest().getStatus());
						orderList  = super.getResultListByNamedQuery("OtsOrder.GetListOfOrderByDateforDistrbutorByStatus", queryParameter);
					}
					break;
				case "Employee":
					queryParameter.put("otsAssignedId", userId);
					if(getListOfOrderByDateBORequest.getRequest().getStatus()==null) {
		    			orderList  = super.getResultListByNamedQuery("OtsOrder.GetListOfOrderByDateforEmployee", queryParameter);
					}else {
						queryParameter.put("otsOrderStatus", getListOfOrderByDateBORequest.getRequest().getStatus());
						orderList  = super.getResultListByNamedQuery("OtsOrder.GetListOfOrderByDateforEmployeeByStatus", queryParameter);
					}
					break;
				default:
					return null;
	        }
	        otsOrderDetails =  orderList.stream().map(OtsOrder -> convertCompleteOrderDetailsFromEntityToDomain(OtsOrder)).collect(Collectors.toList());

			return otsOrderDetails;
		}catch(Exception e){
			throw new BusinessException(e, ErrorEnumeration.FAILURE_ORDER_GET);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.FAILURE_ORDER_GET);
		}
	}

	private CompleteOrderDetails convertCompleteOrderDetailsFromEntityToDomain(OtsOrder otsOrder) {
		CompleteOrderDetails orderDetails =  new CompleteOrderDetails() ;
		orderDetails.setOrderId((otsOrder.getOtsOrderId()==null)?null:otsOrder.getOtsOrderId().toString());
		orderDetails.setOrderDate((otsOrder.getOtsOrderDt()==null)?null:otsOrder.getOtsOrderDt().toString());
		orderDetails.setOrderDeliveryDate((otsOrder.getOtsOrderDeliveryDt()==null)?null:otsOrder.getOtsOrderDeliveryDt().toString());
		orderDetails.setOrderDeliverdDate((otsOrder.getOtsOrderDeliveredDt()==null)?null:otsOrder.getOtsOrderDeliveredDt().toString());
		orderDetails.setDistributorId((otsOrder.getOtsDistributorId()==null)?null:otsOrder.getOtsDistributorId().getOtsUsersId().toString());
		orderDetails.setCustomerId(otsOrder.getOtsCustomerId()==null?null:otsOrder.getOtsCustomerId().getOtsUsersId().toString());
		orderDetails.setAssignedId(otsOrder.getOtsAssignedId()==null?null:otsOrder.getOtsAssignedId().getOtsUsersId().toString());
		orderDetails.setOrderCost(otsOrder.getOtsOrderCost()==null?null:otsOrder.getOtsOrderCost().toString());
		orderDetails.setOrderDate(otsOrder.getOtsOrderDt()==null?null:otsOrder.getOtsOrderDt().toString());
		orderDetails.setOrderDeliverdDate(otsOrder.getOtsOrderDeliveredDt()==null?null:otsOrder.getOtsOrderDeliveredDt().toString());
		orderDetails.setStatus(otsOrder.getOtsOrderStatus()==null?null:otsOrder.getOtsOrderStatus());
		orderDetails.setOrderDeliveryDate(otsOrder.getOtsOrderDeliveryDt()==null?null:otsOrder.getOtsOrderDeliveryDt().toString());
		orderDetails.setOrderNumber(otsOrder.getOtsOrderNumber()==null?null:otsOrder.getOtsOrderNumber());
		orderDetails.setOrderBalanceCan(otsOrder.getOtsOrderBalanceCan()==null?null:otsOrder.getOtsOrderBalanceCan());
		orderDetails.setOrderOutstandingAmount(otsOrder.getOtsOrderBalanceCan()==null?null:otsOrder.getOtsOrderBalanceCan());
		orderDetails.setOrderBasePrice(otsOrder.getOtsOrderBasePrice()==null?null:otsOrder.getOtsOrderBasePrice());
		return orderDetails;
	}

	@Override
	public OrderDetails SalesVocher(SaleVocherBoRequest saleVocherBoRequest) {
		OrderDetails otsOrderDetails = new OrderDetails();
		try {
			OtsOrder otsOrder = new OtsOrder();
			Map<String, Object> queryParameter = new HashMap<>();
//			try {
//				if(!saleVocherBoRequest.getRequest().getPaymentStatus().equalsIgnoreCase("cash")) {
//					Float amount = Float.parseFloat(saleVocherBoRequest.getRequest().getAmountReceived()) * 100;
//					razorPay(amount.toString(), saleVocherBoRequest.getRequest().getPaymentId());
//				}
//			}catch(Exception e){
//				throw new BusinessException(e, ErrorEnumeration.FAILURE_RazorPay);
//			}
			
			
			queryParameter.put("otsOrderId",Integer.parseInt( saleVocherBoRequest.getRequest().getOrderId()));
			otsOrder = super.getResultByNamedQuery("OtsOrder.findByOtsOrderId", queryParameter);
			otsOrder.setOtsOrderDeliveredDt(saleVocherBoRequest.getRequest().getDeliverdDate());
			BigDecimal costData=new BigDecimal(saleVocherBoRequest.getRequest().getOrderCost());
			BigDecimal canData=new BigDecimal(saleVocherBoRequest.getRequest().getAmountReceived());
			otsOrder.setOtsOrderAmountReceived(canData);
			otsOrder.setOtsOrderCost(costData);
			otsOrder.setOtsOrderOutstandingAmount(saleVocherBoRequest.getRequest().getOutstandingAmount());
			
			otsOrder.setOtsOrderStatus("close");
			super.getEntityManager().merge(otsOrder);
			otsOrderDetails = convertOrderDetailsFromEntityToDomain(otsOrder);

		}catch(Exception e){
			throw new BusinessException(e, ErrorEnumeration.FAILURE_ORDER_GET);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.FAILURE_ORDER_GET);
		}
		return otsOrderDetails;
	}

	@Override
	public OrderDetails GetOrderDetailsByOrderId(String OrderId) {
		OrderDetails otsOrderDetails = new OrderDetails();
		OtsOrder otsOrder = new OtsOrder();
		try {
			Map<String, Object> queryParameter = new HashMap<>();
			queryParameter.put("otsOrderId",Integer.parseInt(OrderId));
			otsOrder = super.getResultByNamedQuery("OtsOrder.findByOtsOrderId", queryParameter);
			otsOrderDetails = convertOrderDetailsFromEntityToDomain(otsOrder);
			return otsOrderDetails;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("Error in inserting order in order table"+e.getMessage());
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_ORDER_INSERTION);
		} catch (Throwable e) {
			e.printStackTrace();
			logger.error("Error in inserting order in order table"+e.getMessage());
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_ORDER_INSERTION);
		}

	}

	@Override
	public List<OrderDetails> getOrderReportByDate(GetOrderBORequest getOrderBORequest) {
		try {
			List<OrderDetails> orderDetails = new ArrayList<OrderDetails>();
			List<OtsOrder> orderList = new ArrayList<OtsOrder>();
			Map<String, Object> queryParameter = new HashMap<>();
	    	
	    	queryParameter.put("startDate",getOrderBORequest.getRequest().getFromTime());
	    	queryParameter.put("endDate",getOrderBORequest.getRequest().getToTime());
	    	if(getOrderBORequest.getRequest().getCustomerId()==null || getOrderBORequest.getRequest().getCustomerId()== "") {
	    		OtsUsers distributorId = new OtsUsers();
		    	distributorId.setOtsUsersId(Integer.parseInt(getOrderBORequest.getRequest().getDistributorsId()));
	    		queryParameter.put("DistributorId", distributorId);
	    		orderList  = super.getResultListByNamedQuery("OtsOrder.GetOrderListForDistributorByDateAndStatus", queryParameter);
	    	}else {
	    		OtsUsers customerId = new OtsUsers();
	    		customerId.setOtsUsersId(Integer.parseInt(getOrderBORequest.getRequest().getCustomerId()));
				queryParameter.put("customerId", customerId);
	    		orderList  = super.getResultListByNamedQuery("OtsOrder.GetOrderListForCustomerByDateAndStatus", queryParameter);
	    	}
	    	
	    	orderDetails =  orderList.stream().map(OtsOrder -> convertOrderDetailsFromEntityToDomain(OtsOrder)).collect(Collectors.toList());
			return orderDetails;
		}catch(Exception e){
			throw new BusinessException(e, ErrorEnumeration.FAILURE_ORDER_GET);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.FAILURE_ORDER_GET);
		}
	}

	@Override
	public String employeeTransferOrder(EmployeeOrderTransferRequest employeeOrderTransferRequest) {
		OtsOrder otsOrder = new OtsOrder();
		try {
			Map<String, Object> queryParameter = new HashMap<>();
			queryParameter.put("otsOrderId",Integer.parseInt(employeeOrderTransferRequest.getRequest().getOrderId()));
			otsOrder = super.getResultByNamedQuery("OtsOrder.findByOtsOrderId", queryParameter);
			
			OtsUsers userId = new OtsUsers();
			userId.setOtsUsersId(Integer.parseInt(employeeOrderTransferRequest.getRequest().getEmployeeId()));
			otsOrder.setOtsAssignedId(userId);
			return "Updated";
		}catch(Exception e){
			throw new BusinessException(e, ErrorEnumeration.FAILURE_ORDER_GET);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.FAILURE_ORDER_GET);
		}
	}

	@Override
	public String UpdateOrderStatus(UpdateOrderStatusRequest updateOrderStatusRequest) {
		OtsOrder otsOrder = new OtsOrder();
		try {
			Map<String, Object> queryParameter = new HashMap<>();
			queryParameter.put("otsOrderId",Integer.parseInt(updateOrderStatusRequest.getRequest().getOrderId()));
			otsOrder = super.getResultByNamedQuery("OtsOrder.findByOtsOrderId", queryParameter);
			if(updateOrderStatusRequest.getRequest().getStatus().equalsIgnoreCase("DoneDonation")) {
				
				otsOrder.setOtsOrderDeliveredDt(Date.valueOf(now.toLocalDate()));
				System.out.print(Date.valueOf(now.toLocalDate()));
			}
			otsOrder.setOtsOrderStatus(updateOrderStatusRequest.getRequest().getStatus());
			super.getEntityManager().merge(otsOrder);
		}catch(Exception e){
			throw new BusinessException(e, ErrorEnumeration.FAILURE_ORDER_GET);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.FAILURE_ORDER_GET);
		}
		return "updated";
	}

	@Override
	public OrderDetails directSalesVoucher(AddOrUpdateOrderProductBOrequest addOrUpdateOrderProductBOrequest) {
		OrderDetails orderDetails = new OrderDetails();
		try {
			OtsOrder otsOrder = convertDomainToOrderEntity(addOrUpdateOrderProductBOrequest);
			save(otsOrder);
			super.getEntityManager().flush();
			String OrderNumber = "ORD-"+otsOrder.getOtsOrderId().toString();
			//int OrderId = otsOrder.getOtsOrderId();
			otsOrder.setOtsOrderNumber(OrderNumber);
			super.getEntityManager().merge(otsOrder);
			orderDetails = convertOrderDetailsFromEntityToDomain(otsOrder);
			return orderDetails;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("Error in inserting order in order table"+e.getMessage());
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_ORDER_INSERTION);
		} catch (Throwable e) {
			e.printStackTrace();
			logger.error("Error in inserting order in order table"+e.getMessage());
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_ORDER_INSERTION);
		}
	}

	@Override
	public OrderDetails getLastOrder() {
		OrderDetails orderDetails = new OrderDetails();
		Map<String, Object> queryParameter = new HashMap<>();
		List<OtsOrder> otsOrder = super.getResultListByNamedQuery("OtsOrder.findAll", queryParameter);
		orderDetails = convertOrderDetailsFromEntityToDomain(otsOrder.get((otsOrder.size()-1)));
		return orderDetails;
	}
	
	@Override
	public List<OrderDetails> GetOrderForDrectSalesVoucheri(String OrderId) {
		OrderDetails otsOrderDetails = new OrderDetails();
		OtsOrder otsOrder = new OtsOrder();
		try {
			Map<String, Object> queryParameter = new HashMap<>();
			queryParameter.put("otsOrderId",Integer.parseInt(OrderId));
			otsOrder = super.getResultByNamedQuery("OtsOrder.findByOtsOrderId", queryParameter);
			otsOrderDetails = convertOrderDetailsFromEntityToDomain(otsOrder);
			List<OrderDetails> otsOrderDetailsList = new ArrayList<OrderDetails>();
			otsOrderDetailsList.add(otsOrderDetails);
			return otsOrderDetailsList;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("Error in inserting order in order table"+e.getMessage());
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_ORDER_INSERTION);
		} catch (Throwable e) {
			e.printStackTrace();
			logger.error("Error in inserting order in order table"+e.getMessage());
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_ORDER_INSERTION);
		}

	}
	
	public void razorPay(String amount , String paymentId) throws RazorpayException, JSONException {
		RazorpayClient razorpay = new RazorpayClient("rzp_test_i50l0d4Cja32Mc", "Hv0j2n4laXp9SQa3eXDv36i3");
		try {
			  JSONObject captureRequest = new JSONObject();
			  captureRequest.put("amount", amount);
			  captureRequest.put("currency", "INR");

			  Payment payment = razorpay.Payments.capture("pay_Et08nx2YlPUzAE", captureRequest);
			} catch (RazorpayException e) {
			  // Handle Exception
			  System.out.println(e.getMessage());
			}
	}

	@Override
	public String UpdateOrderForRequest(DonationBoResponse donationBoResponse) {
		try {

			OtsOrder otsOrder = new OtsOrder();
			Map<String, Object> queryParameter = new HashMap<>();
			queryParameter.put("otsOrderId",Integer.parseInt(donationBoResponse.getOrderId()));
			otsOrder = super.getResultByNamedQuery("OtsOrder.findByOtsOrderId", queryParameter);
			OtsDonation donationId = new OtsDonation();
			donationId.setOtsDonationId(Integer.parseInt(donationBoResponse.getDonationId()));
			otsOrder.setOtsDonationId(donationId);
			otsOrder.setOtsOrderStatus("closeRequest");
			otsOrder.getOtsDonationId();
			super.getEntityManager().merge(otsOrder);
		}catch(Exception e) {
			System.out.println(e);
		}
		
		return "Success";
	}

	@Override
	public String donateDonation(SaleVocherBoRequest saleVocherBoRequest) {
		try {

			OtsOrder otsOrder = new OtsOrder();
			Map<String, Object> queryParameter = new HashMap<>();
			queryParameter.put("otsOrderId",Integer.parseInt(saleVocherBoRequest.getRequest().getOrderId()));
			otsOrder = super.getResultByNamedQuery("OtsOrder.findByOtsOrderId", queryParameter);
			otsOrder.setOtsOrderStatus("donationDone");
			super.getEntityManager().merge(otsOrder);
		}catch(Exception e) {
			System.out.println(e);
		}
		
		return "Success";
	}

	@Override
	public OrderDetails getOrderDetailsForOrderId(OrderIdBORequest updateOrderDetailsRequest) {
		OrderDetails orderDetails = new OrderDetails();
		OtsOrder otsOrder = new OtsOrder();
		Map<String, Object> queryParameter = new HashMap<>();
		Integer orderId = Integer.parseInt(updateOrderDetailsRequest.getOrderId().getOrderId());
		queryParameter.put("otsOrderId", orderId);
		otsOrder = super.getResultByNamedQuery("OtsOrder.findByOtsOrderId", queryParameter);
		orderDetails = convertOrderDetailsFromEntityToDomain(otsOrder);
		return orderDetails;
	}

}
