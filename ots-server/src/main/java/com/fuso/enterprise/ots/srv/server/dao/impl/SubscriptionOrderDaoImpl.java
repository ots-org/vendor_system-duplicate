package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;
import com.fuso.enterprise.ots.srv.api.model.domain.SubscriptionHistory;
import com.fuso.enterprise.ots.srv.api.model.domain.SubscriptionUserOrderModel;
import com.fuso.enterprise.ots.srv.api.service.request.GetSubscriptionDetailsRequest;
import com.fuso.enterprise.ots.srv.api.service.response.CurrentSubscriptionBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.SubscriptionDetailsResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.common.exception.ErrorEnumeration;
import com.fuso.enterprise.ots.srv.server.dao.SubscriptionOrderDao;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsSubscriptionOrder;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUserRole;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;

@Repository
public class SubscriptionOrderDaoImpl  extends AbstractIptDao<OtsSubscriptionOrder, String> implements SubscriptionOrderDao {
	
	public SubscriptionOrderDaoImpl() {
		super(OtsSubscriptionOrder.class);
	}

	@Override
	public SubscriptionHistory subScriptionOrder(SubscriptionHistory subscriptionHistory) {
		Date expDate = null;
		Date date = null;
		SubscriptionUserOrderModel subscriptionUserOrderModel = new SubscriptionUserOrderModel();
		try {
			Calendar cal = Calendar.getInstance();
			OtsSubscriptionOrder otsSubscriptionOrder = new OtsSubscriptionOrder();
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			
			if(subscriptionHistory.getAddSubscriptionBORequest().getOrderId()!=null) {
				
				//---------------------------This is for addOn or Update------------------------------------------
				otsSubscriptionOrder= super.getEntityManager().find(OtsSubscriptionOrder.class, Integer.parseInt(subscriptionHistory.getAddSubscriptionBORequest().getOrderId()));
					if(subscriptionHistory.getAddSubscriptionBORequest().getKey().equalsIgnoreCase("paymentdue")) {
						cal.setTime(otsSubscriptionOrder.getOtsUsersExpirationDate());
						if(subscriptionHistory.getAddSubscriptionBORequest().getMode().equalsIgnoreCase("month")) {
							cal.add(Calendar.MONTH, 1);
							date = cal.getTime(); 
							expDate = new SimpleDateFormat("yyyy/MM/dd").parse(format.format(date));
						}else { 
							cal.add(Calendar.YEAR, 1);
							date = cal.getTime(); 
							expDate = new SimpleDateFormat("yyyy/MM/dd").parse(format.format(date));
					}
					//---------------------------This is for addOn of Update------------------------------------------
					otsSubscriptionOrder.setOtsUsersExpirationDate(expDate);
					otsSubscriptionOrder.setOtsSubscriptionOrdercost(subscriptionHistory.getAddSubscriptionBORequest().getOrderCost());
				}else if(subscriptionHistory.getAddSubscriptionBORequest().getKey().equalsIgnoreCase("addOn")) {
					//---------------------------This is for adding new Cost for Old order cost-----------------------
					Integer orderCost = Integer.parseInt(otsSubscriptionOrder.getOtsSubscriptionOrdercost()) + Integer.parseInt(subscriptionHistory.getAddSubscriptionBORequest().getOrderCost());
					otsSubscriptionOrder.setOtsSubscriptionOrdercost(String.valueOf(orderCost));
				}
			}else {
				if(subscriptionHistory.getAddSubscriptionBORequest().getMode().equalsIgnoreCase("month")) {
					cal.add(Calendar.MONTH, 1);
					date = cal.getTime(); 
					expDate = new SimpleDateFormat("yyyy/MM/dd").parse(format.format(date));
				}else {
					cal.add(Calendar.YEAR, 1);
					date = cal.getTime(); 
					expDate=new SimpleDateFormat("yyyy/MM/dd").parse(format.format(date));
				}	
				otsSubscriptionOrder.setOtsSubscriptionOrdercost(subscriptionHistory.getAddSubscriptionBORequest().getOrderCost());
				otsSubscriptionOrder.setOtsUsersExpirationDate(expDate);
			}
			System.out.print(expDate);
			otsSubscriptionOrder.setOtsSubscriptionOrderStatus(subscriptionHistory.getSubscriptionHistoryStatus());
			
			OtsUsers otsUsersId = new OtsUsers();
			otsUsersId.setOtsUsersId(Integer.parseInt(subscriptionHistory.getUserId()));
			otsSubscriptionOrder.setOtsUsersId(otsUsersId);
			
			otsSubscriptionOrder.setOtsSubscriptionName(subscriptionHistory.getAddSubscriptionBORequest().getSubscriptionName());
			otsSubscriptionOrder.setOtsSubscriptionType(subscriptionHistory.getSubscriptionHistoryMode());
		
			save(otsSubscriptionOrder);
			super.getEntityManager().flush();
			
			subscriptionUserOrderModel = convertEntityToModel(otsSubscriptionOrder);
			subscriptionHistory.setSubscriptionUserOrderModel(subscriptionUserOrderModel);
		}catch(Exception e){
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_SUBSCRIPTION);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_SUBSCRIPTION);
		}
			return subscriptionHistory;
		}
		
	private SubscriptionUserOrderModel convertEntityToModel(OtsSubscriptionOrder otsSubscriptionOrder) {
		SubscriptionUserOrderModel subscriptionUserOrderModel = new SubscriptionUserOrderModel();
		subscriptionUserOrderModel.setOrderId((otsSubscriptionOrder.getOtsSubscriptionOrderId()==null)?null:otsSubscriptionOrder.getOtsSubscriptionOrderId().toString());
		subscriptionUserOrderModel.setSubscriptionName((otsSubscriptionOrder.getOtsSubscriptionName()==null)?null:otsSubscriptionOrder.getOtsSubscriptionName());
		subscriptionUserOrderModel.setUserId((otsSubscriptionOrder.getOtsUsersId().getOtsUsersId()==null)?null:otsSubscriptionOrder.getOtsUsersId().getOtsUsersId().toString());
		subscriptionUserOrderModel.setDateOfBilling((otsSubscriptionOrder.getOtsUsersExpirationDate()==null)?null:otsSubscriptionOrder.getOtsUsersExpirationDate().toString());
		subscriptionUserOrderModel.setStatus((otsSubscriptionOrder.getOtsSubscriptionOrderStatus()==null)?null:otsSubscriptionOrder.getOtsSubscriptionOrderStatus());
		subscriptionUserOrderModel.setMode((otsSubscriptionOrder.getOtsSubscriptionType()==null)?null:otsSubscriptionOrder.getOtsSubscriptionType());
		subscriptionUserOrderModel.setOrderCost((otsSubscriptionOrder.getOtsSubscriptionOrdercost()==null)?null:otsSubscriptionOrder.getOtsSubscriptionOrdercost());
		return subscriptionUserOrderModel;
	}

	@Override
	public CurrentSubscriptionBOResponse getCurrentSubscriptionDetails(
			GetSubscriptionDetailsRequest subscriptionDetailsRequest) {
		CurrentSubscriptionBOResponse currentSubscriptionBOResponse = new CurrentSubscriptionBOResponse();
		SubscriptionUserOrderModel subscriptionUserOrderModel = new SubscriptionUserOrderModel();
		OtsSubscriptionOrder subscriptionOrder = new OtsSubscriptionOrder();
		OtsUsers otsUsers = new OtsUsers();
		otsUsers.setOtsUsersId(Integer.parseInt(subscriptionDetailsRequest.getSubscriptionDetails().getUserId()));
		Map<String, Object> queryParameter = new HashMap<>();
		queryParameter.put("otsUsersId", otsUsers);
		subscriptionOrder = super.getResultByNamedQuery("OtsSubscriptionOrder.getCurrentSubscription", queryParameter);
		subscriptionUserOrderModel = convertEntityToModel(subscriptionOrder);
		currentSubscriptionBOResponse.setCurrentSubscriptionModel(subscriptionUserOrderModel);
		return currentSubscriptionBOResponse;
	}
}
