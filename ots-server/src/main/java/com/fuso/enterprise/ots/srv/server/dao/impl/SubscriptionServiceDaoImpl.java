package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fuso.enterprise.ots.srv.api.service.request.AddSubscriptionBORequest;
import com.fuso.enterprise.ots.srv.server.dao.SubscriptionServiceDao;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsStockDistOb;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsSubscriptionOrder;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUserRole;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;

@Repository
public class SubscriptionServiceDaoImpl extends AbstractIptDao< OtsSubscriptionOrder, String>  implements SubscriptionServiceDao{

	@Autowired
    private JdbcTemplate jdbcTemplate;
	
   private final Logger logger = LoggerFactory.getLogger(getClass());
   
	public SubscriptionServiceDaoImpl() {
		super(OtsSubscriptionOrder.class);
	}


	@Override
	public String addUserSubscription(AddSubscriptionBORequest addSubscriptionBORequest) {
	try {
		OtsSubscriptionOrder otsSubscriptionOrder = new OtsSubscriptionOrder();

		OtsUsers otsUsersId = new OtsUsers();
		otsUsersId.setOtsUsersId(1);
		otsSubscriptionOrder.setOtsUsersId(otsUsersId);
		
		for(int i = 0; i <addSubscriptionBORequest.getSubscriptionRoleDetails().size() ; i++) {
			otsSubscriptionOrder.setOtsSubscriptionTotalusers(Integer.parseInt(addSubscriptionBORequest.getSubscriptionRoleDetails().get(i).getCountToadd()));
			
			OtsUserRole otsUserRoleId = new OtsUserRole();
			otsUserRoleId.setOtsUserRoleId(Integer.parseInt(addSubscriptionBORequest.getSubscriptionRoleDetails().get(i).getRoleId()));
			otsSubscriptionOrder.setOtsUserRoleId(otsUserRoleId);
			super.getEntityManager().merge(otsSubscriptionOrder);	
			//save(otsSubscriptionHistory);
	}			
		
		otsSubscriptionOrder.setOtsSubscriptionOrderStatus("active");
		
		super.getEntityManager().merge(otsSubscriptionOrder);	
		return "Success";
		
	}catch(Exception e) {
		System.out.print(e);
	}
	return null;
		
	}

	
}
