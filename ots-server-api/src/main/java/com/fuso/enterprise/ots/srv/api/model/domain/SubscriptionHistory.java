package com.fuso.enterprise.ots.srv.api.model.domain;

import com.fuso.enterprise.ots.srv.api.service.request.AddSubscriptionBORequest;

public class SubscriptionHistory {
	
	private Integer  subscriptionHistoryId;
	
	private String subscriptionHistoryStatus;
	
	private String subscriptionHistoryMode;
	
	private String userId;
	
	private AddSubscriptionBORequest addSubscriptionBORequest;
	
	private SubscriptionUserOrderModel subscriptionUserOrderModel;
	
	public Integer getSubscriptionHistoryId() {
		return subscriptionHistoryId;
	}

	public void setSubscriptionHistoryId(Integer subscriptionHistoryId) {
		this.subscriptionHistoryId = subscriptionHistoryId;
	}

	public String getSubscriptionHistoryStatus() {
		return subscriptionHistoryStatus;
	}

	public void setSubscriptionHistoryStatus(String subscriptionHistoryStatus) {
		this.subscriptionHistoryStatus = subscriptionHistoryStatus;
	}

	public String getSubscriptionHistoryMode() {
		return subscriptionHistoryMode;
	}

	public void setSubscriptionHistoryMode(String subscriptionHistoryMode) {
		this.subscriptionHistoryMode = subscriptionHistoryMode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public AddSubscriptionBORequest getAddSubscriptionBORequest() {
		return addSubscriptionBORequest;
	}

	public void setAddSubscriptionBORequest(AddSubscriptionBORequest addSubscriptionBORequest) {
		this.addSubscriptionBORequest = addSubscriptionBORequest;
	}

	public SubscriptionUserOrderModel getSubscriptionUserOrderModel() {
		return subscriptionUserOrderModel;
	}

	public void setSubscriptionUserOrderModel(SubscriptionUserOrderModel subscriptionUserOrderModel) {
		this.subscriptionUserOrderModel = subscriptionUserOrderModel;
	}

	
}
