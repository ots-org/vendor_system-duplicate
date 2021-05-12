package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.util.List;
import java.util.Map;

import com.fuso.enterprise.ots.srv.api.service.request.GetProductStockListRequest;
import com.fuso.enterprise.ots.srv.api.service.request.NotifyProductForCustomerRequest;

public interface NotifyCustomerDAO {
	
	String notifyProductForCustomer(NotifyProductForCustomerRequest notifyProductForCustomerRequest);
	
	List<Map<String, Object>> getNotifyProductForCustomer(Integer productId);
	
}
