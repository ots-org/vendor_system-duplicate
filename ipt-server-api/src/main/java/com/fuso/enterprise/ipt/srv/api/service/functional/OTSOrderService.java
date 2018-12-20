package com.fuso.enterprise.ipt.srv.api.service.functional;

import java.util.List;

import com.fuso.enterprise.ipt.srv.api.model.domain.BuyerDetails;
import com.fuso.enterprise.ipt.srv.api.model.domain.InputCustomerDetails;
import com.fuso.enterprise.ipt.srv.api.model.domain.InputForEmployeeOrders;

public interface OTSOrderService {

	InputCustomerDetails getEmployeeOrders(InputForEmployeeOrders inputForEmployeeOrders);

}
