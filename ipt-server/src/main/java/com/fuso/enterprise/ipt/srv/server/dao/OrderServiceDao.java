package com.fuso.enterprise.ipt.srv.server.dao;

import java.util.List;

import com.fuso.enterprise.ipt.srv.api.model.domain.BuyerDetails;
import com.fuso.enterprise.ipt.srv.api.model.domain.InputCustomerDetails;
import com.fuso.enterprise.ipt.srv.api.model.domain.InputForEmployeeOrders;
import com.fuso.enterprise.ipt.srv.server.model.entity.CustomerDetails;

public interface OrderServiceDao {

	InputCustomerDetails getEmployeeOrders(InputForEmployeeOrders inputForEmployeeOrders);
	
	

}
