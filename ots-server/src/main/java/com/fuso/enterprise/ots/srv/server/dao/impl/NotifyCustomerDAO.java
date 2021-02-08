package com.fuso.enterprise.ots.srv.server.dao.impl;

import com.fuso.enterprise.ots.srv.api.service.request.GetProductStockListRequest;
import com.fuso.enterprise.ots.srv.api.service.request.NotifyProductForCustomerRequest;

public interface NotifyCustomerDAO {
	
	String notifyProductForCustomer(NotifyProductForCustomerRequest notifyProductForCustomerRequest);
}
