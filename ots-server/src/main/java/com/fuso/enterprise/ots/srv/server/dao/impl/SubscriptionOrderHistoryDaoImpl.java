package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.fuso.enterprise.ots.srv.api.model.domain.SubscriptionDetailsModel;
import com.fuso.enterprise.ots.srv.api.model.domain.SubscriptionHistory;
import com.fuso.enterprise.ots.srv.api.service.request.AddSubscriptionBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetSubscriptionDetailsRequest;
import com.fuso.enterprise.ots.srv.api.service.response.SubscriptionDetailsResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.common.exception.ErrorEnumeration;
import com.fuso.enterprise.ots.srv.server.dao.SubscriptionOrderHistoryDao;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsSubscriptionOrderHistory;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;

@Repository
public class SubscriptionOrderHistoryDaoImpl extends AbstractIptDao<OtsSubscriptionOrderHistory, String> implements SubscriptionOrderHistoryDao{

	public SubscriptionOrderHistoryDaoImpl() {
		super(OtsSubscriptionOrderHistory.class);
	}

	@Override
	public SubscriptionHistory addSubscription(AddSubscriptionBORequest addSubscriptionBORequest) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		Date date = null;
		date = cal.getTime(); 
		SubscriptionHistory subscriptionHistory = new SubscriptionHistory();
		try {
			OtsSubscriptionOrderHistory otsSubscriptionOrderHistory = new OtsSubscriptionOrderHistory();
			otsSubscriptionOrderHistory.setOtsSubscriptionHistoryTimestamp(date);
			otsSubscriptionOrderHistory.setOtsSubscriptionOrderCost(addSubscriptionBORequest.getOrderCost());
			otsSubscriptionOrderHistory.setOtsSubscriptionHistoryStatus(addSubscriptionBORequest.getStatus());
			otsSubscriptionOrderHistory.setOtsSubscriptionHistoryMode(addSubscriptionBORequest.getMode());
			otsSubscriptionOrderHistory.setOtsSubscriptionTransactionId(addSubscriptionBORequest.getTransactionId());
			otsSubscriptionOrderHistory.setOtsSubscriptionName(addSubscriptionBORequest.getSubscriptionName());
			OtsUsers userId = new OtsUsers();
			userId.setOtsUsersId(Integer.parseInt(addSubscriptionBORequest.getUserId()));
			otsSubscriptionOrderHistory.setOtsUsersId(userId);
			save(otsSubscriptionOrderHistory);
			super.getEntityManager().flush();
			subscriptionHistory = convertEntityToModel(otsSubscriptionOrderHistory);
			subscriptionHistory.setAddSubscriptionBORequest(addSubscriptionBORequest);		
		}catch(Exception e){
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_SUBSCRIPTION);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_SUBSCRIPTION);
		}
		return subscriptionHistory;
	}
	
	public SubscriptionHistory convertEntityToModel(OtsSubscriptionOrderHistory otsSubscriptionOrderHistory) {
		SubscriptionHistory subscriptionHistory = new SubscriptionHistory();
		subscriptionHistory.setSubscriptionHistoryId(otsSubscriptionOrderHistory.getOtsSubscriptionOrderHistoryId());
		subscriptionHistory.setUserId(otsSubscriptionOrderHistory.getOtsUsersId().getOtsUsersId().toString());
		subscriptionHistory.setSubscriptionHistoryStatus(otsSubscriptionOrderHistory.getOtsSubscriptionHistoryStatus());
		subscriptionHistory.setSubscriptionHistoryMode(otsSubscriptionOrderHistory.getOtsSubscriptionHistoryMode());
		return subscriptionHistory;
	}
	
	@Override
	public SubscriptionDetailsResponse getUserSubscriptionDetails(GetSubscriptionDetailsRequest  subscriptionDetailsRequest) {
		SubscriptionDetailsResponse subscriptionDetailsResponse = new SubscriptionDetailsResponse();
		List<OtsSubscriptionOrderHistory> otsSubscriptionOrderHistoryList= new ArrayList<OtsSubscriptionOrderHistory>();
		List<SubscriptionDetailsModel> subscriptionDetailsModelList = new ArrayList<SubscriptionDetailsModel>();
		try {
			OtsUsers userId = new OtsUsers();
			userId.setOtsUsersId(Integer.parseInt(subscriptionDetailsRequest.getSubscriptionDetails().getUserId()));
			
			Map<String, Object> queryParameter = new HashMap<>(); 
			queryParameter.put("otsUsersId", userId);
			otsSubscriptionOrderHistoryList = super.getResultListByNamedQuery("OtsSubscriptionOrderHistory.findSubscriptionByOtsUsersId", queryParameter);
			subscriptionDetailsModelList = otsSubscriptionOrderHistoryList.stream().map(OtsSubscriptionOrderHistory -> convertEntityTomodelForGetRequest(OtsSubscriptionOrderHistory)).collect(Collectors.toList());
			subscriptionDetailsResponse.setSubscriptionDetails(subscriptionDetailsModelList);
		}catch(Exception e) {
			System.out.print(e);
		}
		
		return subscriptionDetailsResponse;
	}
	
	public SubscriptionDetailsModel convertEntityTomodelForGetRequest(OtsSubscriptionOrderHistory scriptionOrderHistory) {
		SubscriptionDetailsModel subscriptionDetailsModel = new SubscriptionDetailsModel();
		subscriptionDetailsModel.setHistorId(scriptionOrderHistory.getOtsSubscriptionOrderHistoryId()==null?null:scriptionOrderHistory.getOtsSubscriptionOrderHistoryId().toString());
		subscriptionDetailsModel.setTransactionId(scriptionOrderHistory.getOtsSubscriptionTransactionId()==null?null:scriptionOrderHistory.getOtsSubscriptionTransactionId());
		subscriptionDetailsModel.setOrderCost(scriptionOrderHistory.getOtsSubscriptionOrderCost()==null?null:scriptionOrderHistory.getOtsSubscriptionOrderCost());
		subscriptionDetailsModel.setPaymentDate(scriptionOrderHistory.getOtsSubscriptionHistoryTimestamp()==null?null:scriptionOrderHistory.getOtsSubscriptionHistoryTimestamp().toString());
		subscriptionDetailsModel.setUserId(scriptionOrderHistory.getOtsUsersId().getOtsUsersId()==null?null:scriptionOrderHistory.getOtsUsersId().getOtsUsersId().toString());
		subscriptionDetailsModel.setMode(scriptionOrderHistory.getOtsSubscriptionHistoryMode()==null?null:scriptionOrderHistory.getOtsSubscriptionHistoryMode());
		return subscriptionDetailsModel;
	}
	
}