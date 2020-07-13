package com.fuso.enterprise.ots.srv.functional.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fuso.enterprise.ots.srv.api.model.domain.SubscriptionHistory;
import com.fuso.enterprise.ots.srv.api.service.functional.OTSSubscriptionService;
import com.fuso.enterprise.ots.srv.api.service.request.AddSubscriptionBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetSubscriptionDetailsRequest;
import com.fuso.enterprise.ots.srv.api.service.response.CurrentSubscriptionBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.SubscriptionDetailsResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.common.exception.ErrorEnumeration;
import com.fuso.enterprise.ots.srv.server.dao.SubscriptionOrderDao;
import com.fuso.enterprise.ots.srv.server.dao.SubscriptionOrderDetailsDao;
import com.fuso.enterprise.ots.srv.server.dao.SubscriptionOrderHistoryDao;
import com.fuso.enterprise.ots.srv.server.dao.SubscriptionOrderRoleMappingDao;


@Service
@Transactional
public class OTSSubscriptionServiceImpl implements OTSSubscriptionService  {
	
	private SubscriptionOrderDao subscriptionOrderDao;
	private SubscriptionOrderDetailsDao subscriptionOrderDetailsDao;
	private SubscriptionOrderHistoryDao subscriptionOrderHistoryDao;
	private SubscriptionOrderRoleMappingDao SubscriptionOrderRoleMapping;
	
	@Inject
	public OTSSubscriptionServiceImpl(SubscriptionOrderDao subscriptionOrderDao ,
			SubscriptionOrderDetailsDao subscriptionOrderDetailsDao,
			SubscriptionOrderHistoryDao subscriptionOrderHistoryDao,
			SubscriptionOrderRoleMappingDao SubscriptionOrderRoleMapping) {;
		this.subscriptionOrderDao = subscriptionOrderDao;
		this.subscriptionOrderDetailsDao = subscriptionOrderDetailsDao;
		this.subscriptionOrderHistoryDao = subscriptionOrderHistoryDao;
		this.SubscriptionOrderRoleMapping = SubscriptionOrderRoleMapping;
	}

	@Override
	public SubscriptionHistory addUserSubscription(AddSubscriptionBORequest addSubscriptionBORequest) { 
		SubscriptionHistory subscriptionHistory = new SubscriptionHistory();
		try {
			subscriptionHistory = subscriptionOrderHistoryDao.addSubscription(addSubscriptionBORequest);
			subscriptionOrderDetailsDao.addSubscription(subscriptionHistory);
			if(addSubscriptionBORequest.getKey().equalsIgnoreCase("add")) {
				
				System.out.print("add");
				//-------------------first time adding subscription------------------------------
				subscriptionHistory = subscriptionOrderDao.subScriptionOrder(subscriptionHistory);
				SubscriptionOrderRoleMapping.getSubscriptionDetails(subscriptionHistory);
			}else if(addSubscriptionBORequest.getKey().equalsIgnoreCase("paymentdue")) {
				
				System.out.print("paymentdue");
				//------------------------updating subscription------------------------------
				subscriptionHistory = subscriptionOrderDao.subScriptionOrder(subscriptionHistory);
			}else if(addSubscriptionBORequest.getKey().equalsIgnoreCase("addOn")) {
				
				System.out.print("addOn");
				//----------------------------only addOn-------------------------------------
				subscriptionHistory = subscriptionOrderDao.subScriptionOrder(subscriptionHistory);
				SubscriptionOrderRoleMapping.getSubscriptionDetails(subscriptionHistory);
			}
		}catch(Exception e){
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_SUBSCRIPTION);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_SUBSCRIPTION);
		}
		return subscriptionHistory;
	}

	@Override
	public SubscriptionDetailsResponse getSubscriptionDetails(
			GetSubscriptionDetailsRequest subscriptionDetailsRequest) {
		SubscriptionDetailsResponse subscriptionDetailsResponse = new SubscriptionDetailsResponse();
		try {
			subscriptionDetailsResponse = subscriptionOrderHistoryDao.getUserSubscriptionDetails(subscriptionDetailsRequest);
	 		subscriptionDetailsResponse = subscriptionOrderDetailsDao.getSubscriptionDetails(subscriptionDetailsResponse);
		}catch(Exception e){
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_SUBSCRIPTION);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_SUBSCRIPTION);
		}
		return subscriptionDetailsResponse;	
	}

	@Override
	public CurrentSubscriptionBOResponse getCurrentSubscriptionDetails(
			GetSubscriptionDetailsRequest subscriptionDetailsRequest) {
		CurrentSubscriptionBOResponse currentSubscriptionBOResponse = new CurrentSubscriptionBOResponse();
		currentSubscriptionBOResponse =subscriptionOrderDao.getCurrentSubscriptionDetails(subscriptionDetailsRequest);
		currentSubscriptionBOResponse = SubscriptionOrderRoleMapping.getCurrentSubscriptionDetails(currentSubscriptionBOResponse);
		return currentSubscriptionBOResponse;
	}
	
	
	
}
