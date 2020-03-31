package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.fuso.enterprise.ots.srv.api.model.domain.SubscriptionDetailsModel;
import com.fuso.enterprise.ots.srv.api.model.domain.SubscriptionHistory;
import com.fuso.enterprise.ots.srv.api.model.domain.SubscriptionRoleOrderModel;
import com.fuso.enterprise.ots.srv.api.service.request.AddSubscriptionBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.SubscriptionDetailsResponse;
import com.fuso.enterprise.ots.srv.server.dao.SubscriptionOrderDetailsDao;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsSubscriptionOrderHistory;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsSubscriptionOrderroledetails;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUserRole;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;

@Repository
public class SubscriptionOrderDetailsDaoImpl extends AbstractIptDao<OtsSubscriptionOrderroledetails, String> implements SubscriptionOrderDetailsDao{

	public SubscriptionOrderDetailsDaoImpl() {
		super(OtsSubscriptionOrderroledetails.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String addSubscription(SubscriptionHistory subscriptionHistory) {
		OtsSubscriptionOrderroledetails OtsSubscriptionOrderroledetails = new OtsSubscriptionOrderroledetails();
		
		OtsSubscriptionOrderHistory otsSubscriptionOrderHistoryId= new OtsSubscriptionOrderHistory();
		otsSubscriptionOrderHistoryId.setOtsSubscriptionOrderHistoryId(subscriptionHistory.getSubscriptionHistoryId());
		OtsSubscriptionOrderroledetails.setOtsSubscriptionOrderHistoryId(otsSubscriptionOrderHistoryId);
		
		for(int i =0;i<subscriptionHistory.getAddSubscriptionBORequest().getSubscriptionRoleDetails().size();i++) {
			OtsSubscriptionOrderroledetails.setOtssubscriptionorderaddOns(subscriptionHistory.getAddSubscriptionBORequest().getSubscriptionRoleDetails().get(i).getCountToadd());
			OtsUserRole otsUserRoleId = new OtsUserRole();
			otsUserRoleId.setOtsUserRoleId(Integer.parseInt(subscriptionHistory.getAddSubscriptionBORequest().getSubscriptionRoleDetails().get(i).getRoleId()));
			OtsSubscriptionOrderroledetails.setOtsUserRoleId(otsUserRoleId);
			save(OtsSubscriptionOrderroledetails);
			super.getEntityManager().flush();
		}
		return null;
	}

	@Override
	public SubscriptionDetailsResponse getSubscriptionDetails(SubscriptionDetailsResponse subscriptionDetailsResponse) {
		try {
			List<OtsSubscriptionOrderroledetails> subscriptionOrderList = new ArrayList<OtsSubscriptionOrderroledetails>();
			
			Map<String, Object> queryParameter = new HashMap<>(); 
			
			for(int i=0;i<subscriptionDetailsResponse.getSubscriptionDetails().size();i++) {
				List<SubscriptionRoleOrderModel> subscriptionRoleOrderModelList = new ArrayList<SubscriptionRoleOrderModel>();
				OtsSubscriptionOrderHistory subscriptionOrderHistoryId = new OtsSubscriptionOrderHistory();
				subscriptionOrderHistoryId.setOtsSubscriptionOrderHistoryId(Integer.parseInt(subscriptionDetailsResponse.getSubscriptionDetails().get(i).getHistorId()));
				queryParameter.put("historyId",subscriptionOrderHistoryId);
				
				subscriptionOrderList = super.getResultListByNamedQuery("OtsSubscriptionOrderroledetails.findSubscriptionDetails", queryParameter);
				subscriptionRoleOrderModelList = subscriptionOrderList.stream().map(OtsSubscriptionOrderroledetails -> convertEntityToModel(OtsSubscriptionOrderroledetails)).collect(Collectors.toList());
				subscriptionDetailsResponse.getSubscriptionDetails().get(i).setAddUserRole(subscriptionRoleOrderModelList);
			}
		
		}catch(Exception e) {
			System.out.print(e);
		}	
		
		return subscriptionDetailsResponse;
	}
	
	public SubscriptionRoleOrderModel convertEntityToModel(OtsSubscriptionOrderroledetails subscriptionOrderroledetails) {
		SubscriptionRoleOrderModel subscriptionRoleOrderModel = new SubscriptionRoleOrderModel();
		subscriptionRoleOrderModel.setRoleId(subscriptionOrderroledetails.getOtsUserRoleId().getOtsUserRoleId().toString());
		subscriptionRoleOrderModel.setOrderHistoryId(subscriptionOrderroledetails.getOtsSubscriptionOrderHistoryId().getOtsSubscriptionOrderHistoryId().toString());
		subscriptionRoleOrderModel.setSubscriptionOrderDetailsId(subscriptionOrderroledetails.getOtssubscriptionorderRoleDetailsid().toString());
		subscriptionRoleOrderModel.setAddOns(subscriptionOrderroledetails.getOtssubscriptionorderaddOns());
		return subscriptionRoleOrderModel;
	}

}
