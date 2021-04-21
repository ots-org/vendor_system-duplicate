package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.fuso.enterprise.ots.srv.api.service.request.GetProductStockListRequest;
import com.fuso.enterprise.ots.srv.api.service.request.NotifyProductForCustomerRequest;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsNotifyCustomer;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsProduct;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;

@Repository
public class NotifyCustomerDAOImpl extends AbstractIptDao<OtsNotifyCustomer, String> implements NotifyCustomerDAO{

	@Autowired
    private JdbcTemplate jdbcTemplate;
	
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

	@Override
	public List<Map<String, Object>> getNotifyProductForCustomer(Integer productId) {
		
		Map<String, Object> inParamMap = new HashMap<String, Object>();	
		inParamMap.put("productId", productId);
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
				.withProcedureName("getNotifyCustomerProduct")
				.withoutProcedureColumnMetaDataAccess();
		simpleJdbcCall.addDeclaredParameter(new SqlParameter("productId", Types.INTEGER));
		Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inParamMap);
		List<Map<String, Object>> out = (List<Map<String, Object>>) simpleJdbcCallResult.get("#result-set-1");
		System.out.print(out);
		if(out.isEmpty()) {
			return null;
		}else {
			return out;
		}
		
	}

}
