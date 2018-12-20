package com.fuso.enterprise.ipt.srv.functional.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fuso.enterprise.ipt.srv.api.model.domain.BuyerDetails;
import com.fuso.enterprise.ipt.srv.api.model.domain.InputCustomerDetails;
import com.fuso.enterprise.ipt.srv.api.model.domain.InputForEmployeeOrders;
import com.fuso.enterprise.ipt.srv.api.service.functional.OTSOrderService;
import com.fuso.enterprise.ipt.srv.server.dao.BuyersGroupDao;
import com.fuso.enterprise.ipt.srv.server.dao.BuyersServiceDAO;
import com.fuso.enterprise.ipt.srv.server.dao.OrderServiceDao;
import com.fuso.enterprise.ipt.srv.server.model.entity.CustomerDetails;

@Service
@Transactional
public class OTSOrderServiceImpl implements OTSOrderService{

	private OrderServiceDao orderServiceDao;
	
	@Inject
	public OTSOrderServiceImpl(OrderServiceDao orderServiceDao
								) {
		
		this.orderServiceDao=orderServiceDao;
		
	}
	
	@Override
	public InputCustomerDetails getEmployeeOrders(InputForEmployeeOrders inputForEmployeeOrders) {
		// TODO Auto-generated method stub
		
		InputCustomerDetails res=orderServiceDao.getEmployeeOrders(inputForEmployeeOrders);
		System.out.println("inside the service impl" +res);
		return res;
	}
	

}
