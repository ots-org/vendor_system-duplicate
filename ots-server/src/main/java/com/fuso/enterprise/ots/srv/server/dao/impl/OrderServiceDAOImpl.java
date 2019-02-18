package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fuso.enterprise.ots.srv.api.model.domain.ListOfOrderId;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderDetails;
import com.fuso.enterprise.ots.srv.api.service.request.AddOrUpdateOrderProductBOrequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetOrderBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetOrderByStatusRequest;
import com.fuso.enterprise.ots.srv.api.service.request.OrderDetailsBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateOrderDetailsRequest;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.common.exception.ErrorEnumeration;
import com.fuso.enterprise.ots.srv.server.dao.OrderServiceDAO;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsBill;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsOrder;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;

@Repository
public class OrderServiceDAOImpl extends AbstractIptDao<OtsOrder, String> implements OrderServiceDAO {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public OrderServiceDAOImpl() {
		super(OtsOrder.class);
		// TODO Auto-generated constructor stub
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
            	OtsUsers DistrubutorId = new OtsUsers();
            	DistrubutorId.setOtsUsersId(Integer.parseInt(getOrderBORequest.getRequest().getDistributorsId()));
    			queryParameter.put("DistributorsId", DistrubutorId);
    			queryParameter.put("FromDate", getOrderBORequest.getRequest().getFromTime());
    			queryParameter.put("ToDate", getOrderBORequest.getRequest().getToTime());
    			OrderList  = super.getResultListByNamedQuery("OtsOrder.GetOrderListByTime", queryParameter);
            } catch (NoResultException e) {
            	logger.error("Exception while fetching data from DB :"+e.getMessage());
        		e.printStackTrace();
            	throw new BusinessException(e.getMessage(), e);
            }

            otsOrderDetails =  OrderList.stream().map(OtsOrder -> convertOrderDetailsFromEntityToDomain(OtsOrder)).collect(Collectors.toList());
    	}catch(Exception e) {
			logger.error("Error in inserting order in order table"+e.getMessage());
    		e.printStackTrace();
    		throw new BusinessException(e.getMessage(), e);
    	}
    	return otsOrderDetails;
	}


	private OrderDetails convertOrderDetailsFromEntityToDomain(OtsOrder otsOrder) {
		OrderDetails orderDetails =  new OrderDetails() ;
		orderDetails.setOrderId((otsOrder.getOtsOrderId()==null)?null:otsOrder.getOtsOrderId().toString());
		orderDetails.setDistributorId((otsOrder.getOtsDistributorId().getOtsUsersId()==null)?null:otsOrder.getOtsDistributorId().getOtsUsersId().toString());
		orderDetails.setCustomerId(otsOrder.getOtsCustomerId().getOtsUsersId()==null?null:otsOrder.getOtsCustomerId().getOtsUsersId().toString());
		orderDetails.setAssignedId(otsOrder.getOtsAssignedId().getOtsUsersId()==null?null:otsOrder.getOtsAssignedId().getOtsUsersId().toString());
		orderDetails.setOrderCost(otsOrder.getOtsOrderCost()==null?null:otsOrder.getOtsOrderCost().toString());
		orderDetails.setOrderDate(otsOrder.getOtsOrderDt()==null?null:otsOrder.getOtsOrderDt().toString());
		orderDetails.setOrderDeliverdDate(otsOrder.getOtsOrderDeliveredDt()==null?null:otsOrder.getOtsOrderDeliveredDt().toString());
		orderDetails.setCreatedBy(otsOrder.getOtsOrderCreated()==null?null:otsOrder.getOtsOrderCreated().toString());
		orderDetails.setStatus(otsOrder.getOtsOrderStatus()==null?null:otsOrder.getOtsOrderStatus());
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
            	throw new BusinessException(e.getMessage(), e);
            }otsOrderDetails =  OrderList.stream().map(OtsOrder -> convertOrderDetailsFromEntityToDomain(OtsOrder)).collect(Collectors.toList());
    	}catch(Exception e){
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_ORDER_INSERTION);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_ORDER_INSERTION);
		}
    	return otsOrderDetails;
	}

	@Override
	public Integer insertOrderAndGetOrderId(AddOrUpdateOrderProductBOrequest addOrUpdateOrderProductBOrequest) {
		try {
			OtsOrder otsOrder = convertDomainToOrderEntity(addOrUpdateOrderProductBOrequest);
			save(otsOrder);
			super.getEntityManager().flush();
			String OrderNumber = "ORD-"+otsOrder.getOtsOrderId().toString();
			//int OrderId = otsOrder.getOtsOrderId();
			otsOrder.setOtsOrderNumber(OrderNumber);
			return otsOrder.getOtsOrderId();
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
		
		OtsUsers DistributorId = new OtsUsers();
		DistributorId.setOtsUsersId(Integer.parseInt(addOrUpdateOrderProductBOrequest.getRequest().getDistributorId()));		
		otsOrder.setOtsDistributorId(DistributorId);
		
		OtsUsers CustomerId = new OtsUsers();
		CustomerId.setOtsUsersId(Integer.parseInt(addOrUpdateOrderProductBOrequest.getRequest().getCustomerId()));
		otsOrder.setOtsCustomerId(CustomerId); 
		
		OtsUsers EmployeeId = new OtsUsers();
		EmployeeId.setOtsUsersId(Integer.parseInt(addOrUpdateOrderProductBOrequest.getRequest().getAssignedId()));
		otsOrder.setOtsAssignedId(EmployeeId);
		
		otsOrder.setOtsOrderNumber(addOrUpdateOrderProductBOrequest.getRequest().getOrderNumber());	
		otsOrder.setOtsOrderCost(Long.parseLong(addOrUpdateOrderProductBOrequest.getRequest().getOrderCost()));
		otsOrder.setOtsOrderStatus(addOrUpdateOrderProductBOrequest.getRequest().getOrderStatus());
		otsOrder.setOtsOrderDeliveryDt(Date.valueOf(addOrUpdateOrderProductBOrequest.getRequest().getDelivaryDate()));
		otsOrder.setOtsOrderDeliveredDt(Date.valueOf(addOrUpdateOrderProductBOrequest.getRequest().getDeliverdDate()));
		 
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
		EmployeeId.setOtsUsersId(Integer.parseInt(updateOrderDetailsRequest.getRequest().getAssignedId()));
		otsOrder.setOtsAssignedId(EmployeeId);
		
		otsOrder.setOtsOrderNumber(updateOrderDetailsRequest.getRequest().getOrderNumber());	
		otsOrder.setOtsOrderCost(Long.parseLong(updateOrderDetailsRequest.getRequest().getOrderCost()));
		otsOrder.setOtsOrderStatus(updateOrderDetailsRequest.getRequest().getOrderStatus());
		otsOrder.setOtsOrderDeliveryDt(Date.valueOf(updateOrderDetailsRequest.getRequest().getDeliveryDate()));
		otsOrder.setOtsOrderDeliveredDt(Date.valueOf(updateOrderDetailsRequest.getRequest().getDeliverdDate()));
		 
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

	

}
