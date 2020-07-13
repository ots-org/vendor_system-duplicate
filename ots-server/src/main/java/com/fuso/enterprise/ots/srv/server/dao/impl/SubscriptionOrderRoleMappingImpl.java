package com.fuso.enterprise.ots.srv.server.dao.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.fuso.enterprise.ots.srv.api.model.domain.SubscriptionHistory;
import com.fuso.enterprise.ots.srv.api.model.domain.SubscriptionRoleMappingModel;
import com.fuso.enterprise.ots.srv.api.service.request.GetSubscriptionDetailsRequest;
import com.fuso.enterprise.ots.srv.api.service.response.CurrentSubscriptionBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.SubscriptionDetailsResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.common.exception.ErrorEnumeration;
import com.fuso.enterprise.ots.srv.server.dao.SubscriptionOrderRoleMappingDao;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsSubscriptionOrder;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsSubscriptionRoleMapping;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUserRole;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;

@Repository
public class SubscriptionOrderRoleMappingImpl extends AbstractIptDao<OtsSubscriptionRoleMapping , String> implements SubscriptionOrderRoleMappingDao{ 

	public SubscriptionOrderRoleMappingImpl() {
		super(OtsSubscriptionRoleMapping.class);
	}

	@Override
	public SubscriptionHistory getSubscriptionDetails(SubscriptionHistory subscriptionHistory) {
		OtsSubscriptionRoleMapping subscriptionRoleMapping = new OtsSubscriptionRoleMapping();
		try {
			for(int i=0;i<subscriptionHistory.getAddSubscriptionBORequest().getSubscriptionRoleDetails().size();i++) {
				if(subscriptionHistory.getAddSubscriptionBORequest().getKey().equalsIgnoreCase("addOn")) {
					//--------------------------------getting value for add the users for admin--------------------------------------------------
					subscriptionRoleMapping = super.getEntityManager().find(OtsSubscriptionRoleMapping.class, Integer.parseInt(subscriptionHistory.getAddSubscriptionBORequest().getSubscriptionRoleDetails().get(i).getUserRoleOrderId()));
					int userCount = subscriptionRoleMapping.getOtsSubscriptionUsercount() + Integer.parseInt(subscriptionHistory.getAddSubscriptionBORequest().getSubscriptionRoleDetails().get(i).getCountToadd());
					subscriptionRoleMapping.setOtsSubscriptionUsercount(userCount);
				}else {
					subscriptionRoleMapping.setOtsSubscriptionUsercount(Integer.parseInt(subscriptionHistory.getAddSubscriptionBORequest().getSubscriptionRoleDetails().get(i).getCountToadd()));
					
					OtsSubscriptionOrder subscriptionOrderId = new OtsSubscriptionOrder();
					subscriptionOrderId.setOtsSubscriptionOrderId(Integer.parseInt(subscriptionHistory.getSubscriptionUserOrderModel().getOrderId()));
					subscriptionRoleMapping.setOtsSubscriptionOrderId(subscriptionOrderId);
					
					OtsUserRole otsUserRoleId = new OtsUserRole();
					otsUserRoleId.setOtsUserRoleId(Integer.parseInt(subscriptionHistory.getAddSubscriptionBORequest().getSubscriptionRoleDetails().get(i).getRoleId()));
					subscriptionRoleMapping.setOtsUserRoleId(otsUserRoleId);
				}
				super.getEntityManager().merge(subscriptionRoleMapping);	
			}
		}catch(Exception e){
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_SUBSCRIPTION);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_SUBSCRIPTION);
		}
	
		return null;
	}


	@Override
	public CurrentSubscriptionBOResponse getCurrentSubscriptionDetails(
			CurrentSubscriptionBOResponse currentSubscriptionBOResponse) {
		OtsSubscriptionOrder subscriptionOrderId = new OtsSubscriptionOrder();
		List<OtsSubscriptionRoleMapping> subscriptionRoleMappingList = new ArrayList<OtsSubscriptionRoleMapping>();
		List<SubscriptionRoleMappingModel> subscriptionRoleMappingModelList = new ArrayList<SubscriptionRoleMappingModel>(); 
		subscriptionOrderId.setOtsSubscriptionOrderId(Integer.parseInt(currentSubscriptionBOResponse.getCurrentSubscriptionModel().getOrderId()));
		
		Map<String, Object> queryParameter = new HashMap<>(); 
		queryParameter.put("otsSubscriptionOrderId", subscriptionOrderId);
		subscriptionRoleMappingList = super.getResultListByNamedQuery("OtsSubscriptionRoleMapping.getCurrentTotalUser", queryParameter);
		subscriptionRoleMappingModelList = subscriptionRoleMappingList.stream().map(OtsSubscriptionRoleMapping -> convertEntityToModel(OtsSubscriptionRoleMapping)).collect(Collectors.toList());
		currentSubscriptionBOResponse.getCurrentSubscriptionModel().setSubscriptionRoleMappingModelList(subscriptionRoleMappingModelList);
		return currentSubscriptionBOResponse;
	}


	private SubscriptionRoleMappingModel convertEntityToModel(OtsSubscriptionRoleMapping subscriptionRoleMapping) {
		SubscriptionRoleMappingModel subscriptionRoleMappingModel = new SubscriptionRoleMappingModel();
		subscriptionRoleMappingModel.setRoleId(subscriptionRoleMapping.getOtsUserRoleId().getOtsUserRoleId().toString());
		subscriptionRoleMappingModel.setSubscriptionMappingId(subscriptionRoleMapping.getOtsSubscriptionRoleMappingId().toString());
		subscriptionRoleMappingModel.setUserCount(subscriptionRoleMapping.getOtsSubscriptionUsercount().toString());
		subscriptionRoleMappingModel.setSubscriptionOrderId(subscriptionRoleMapping.getOtsSubscriptionOrderId().getOtsSubscriptionOrderId().toString());
		return subscriptionRoleMappingModel;
		
	}
}
