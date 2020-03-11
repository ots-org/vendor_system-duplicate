package com.fuso.enterprise.ots.srv.server.dao;

import com.fuso.enterprise.ots.srv.api.service.request.CustomerOutstandingBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetCustomerOutstandingAmtBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.GetCustomerOutstandingAmtBOResponse;

public interface CustomerOutstandingAmtDAO {

	String updateCustomerOutstandingAmt(CustomerOutstandingBORequest customerOutstandingBORequest);

	GetCustomerOutstandingAmtBOResponse getCustomerOutstandingAmt(GetCustomerOutstandingAmtBORequest getCustomerOutstandingAmtBORequest);

}
