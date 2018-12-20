package com.fuso.enterprise.ipt.srv.server.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fuso.enterprise.ipt.srv.api.model.domain.BuyerDetails;
import com.fuso.enterprise.ipt.srv.api.model.domain.InputCustomerDetails;
import com.fuso.enterprise.ipt.srv.api.model.domain.InputForEmployeeOrders;
import com.fuso.enterprise.ipt.srv.api.service.functional.OTSOrderService;
import com.fuso.enterprise.ipt.srv.server.dao.OrderServiceDao;
import com.fuso.enterprise.ipt.srv.server.model.entity.Buyers;
import com.fuso.enterprise.ipt.srv.server.model.entity.BuyersGroup;
import com.fuso.enterprise.ipt.srv.server.model.entity.CustomerDetails;
import com.fuso.enterprise.ipt.srv.server.util.AbstractIptDao;

@Repository
public class OrderServiceDaoImpl extends AbstractIptDao<CustomerDetails, String> implements OrderServiceDao {

	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	public OrderServiceDaoImpl() {
		super(CustomerDetails.class);
	}
	
	@Override
	public InputCustomerDetails getEmployeeOrders(InputForEmployeeOrders inputForEmployeeOrders) {
		// TODO Auto-generated method stub
		System.out.println("inside the dao impl");
		String buyerCode = inputForEmployeeOrders.getUserId();
		CustomerDetails customer = new CustomerDetails();
		
		Map<String, Object> queryParameter = new HashMap<>();
        queryParameter.put("buyer_code", "G0D");
        List<CustomerDetails> customers = super.getResultListByNamedQuery("getCustomerDetails", queryParameter);
        System.out.println("customers details are " +super.getResultListByNamedQuery("getCustomerDetails", queryParameter));
        return (InputCustomerDetails) customers.stream().map(buyer -> convertBuyerDetailssFromEntityToDomain(buyer)).collect(Collectors.toList());
		
		
		
		//return null;
	}

	private InputCustomerDetails convertBuyerDetailssFromEntityToDomain(CustomerDetails customer)
	{
		InputCustomerDetails customerDetails = new InputCustomerDetails();
		
		customerDetails.setBuyerCode(customer.getBuyerCode());
		customerDetails.setBuyerEmail(customer.getBuyerEmail());
		customerDetails.setBuyerName(customer.getBuyerName());
		
		return customerDetails;
	}
}
