package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.fuso.enterprise.ots.srv.api.service.request.GetProductStockListRequest;
import com.fuso.enterprise.ots.srv.api.service.request.NotifyProductForCustomerRequest;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsNotifyCustomer;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsProduct;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;

@Repository
public class NotifyCustomerDAOImpl extends AbstractIptDao<OtsNotifyCustomer, String> implements NotifyCustomerDAO{

	public NotifyCustomerDAOImpl() {
		super(OtsNotifyCustomer.class);
	}

	@Override
	public String notifyProductForCustomer(NotifyProductForCustomerRequest notifyProductForCustomerRequest) {
		OtsNotifyCustomer notifyCustomer = new OtsNotifyCustomer();
		
		Map<String, Object> queryParameter = new HashMap<>();
		OtsProduct productId = new OtsProduct();
		OtsUsers customerId = new OtsUsers();
		
		productId.setOtsProductId(notifyProductForCustomerRequest.getRequestData().getProductId());
		customerId.setOtsUsersId(notifyProductForCustomerRequest.getRequestData().getCustomerId());
		
		try {
			queryParameter.put("otsProductId",productId);
			queryParameter.put("otsUsersId", customerId);
			
			notifyCustomer = super.getResultByNamedQuery("OtsNotifyCustomer.getNotifyProductDetails", queryParameter);
			System.out.println("present");
			return null;
		}catch(Exception e) {
			System.out.println("insert");
			notifyCustomer.setOtsUsersId(customerId);
			notifyCustomer.setOtsProductId(productId);
			super.getEntityManager().merge(notifyCustomer);
			return "success";
		}
		
	}

}
