package com.fuso.enterprise.ipt.srv.rest.ws.service;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.fuso.enterprise.ipt.srv.api.model.domain.BuyerDetails;
import com.fuso.enterprise.ipt.srv.api.model.domain.InputCustomerDetails;
import com.fuso.enterprise.ipt.srv.api.model.domain.InputForEmployeeOrders;
import com.fuso.enterprise.ipt.srv.api.service.functional.OTSOrderService;
import com.fuso.enterprise.ipt.srv.server.model.entity.CustomerDetails;

@Validated
@Service
public class OTSReportGenerationImplWS implements OTSReportGenerationWS{

	@Inject
	private OTSOrderService otsOrderService;

	@Override
	public InputCustomerDetails getEmployeeOrders(InputForEmployeeOrders inputForEmployeeOrders) {
		// TODO Auto-generated method stub
		System.out.println("inside the ws file");
		return otsOrderService.getEmployeeOrders(inputForEmployeeOrders);
	}
	

}
