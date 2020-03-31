package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Repository;

import com.fuso.enterprise.ots.srv.api.model.domain.SubscriptionHistory;
import com.fuso.enterprise.ots.srv.server.dao.SubscriptionOrderDao;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsSubscriptionOrder;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUserRole;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;

@Repository
public class SubscriptionOrderDaoImpl  extends AbstractIptDao<OtsSubscriptionOrder, String> implements SubscriptionOrderDao {
	
	public SubscriptionOrderDaoImpl() {
		super(OtsSubscriptionOrder.class);
	}

	@Override
	public SubscriptionHistory subScriptionOrder(SubscriptionHistory subscriptionHistory) {
		try {
			for(int i=0;i<subscriptionHistory.getAddSubscriptionBORequest().getSubscriptionRoleDetails().size();i++) {
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
				
				OtsSubscriptionOrder otsSubscriptionOrder = new OtsSubscriptionOrder();
				otsSubscriptionOrder.setOtsSubscriptionOrderStatus(subscriptionHistory.getSubscriptionHistoryStatus());
				
				OtsUsers otsUsersId = new OtsUsers();
				otsUsersId.setOtsUsersId(Integer.parseInt(subscriptionHistory.getUserId()));
				otsSubscriptionOrder.setOtsUsersId(otsUsersId);
				
				OtsUserRole otsUserRoleId = new OtsUserRole();
				otsUserRoleId.setOtsUserRoleId(Integer.parseInt(subscriptionHistory.getAddSubscriptionBORequest().getSubscriptionRoleDetails().get(i).getRoleId()));
				otsSubscriptionOrder.setOtsUserRoleId(otsUserRoleId);
				
				otsSubscriptionOrder.setOtsSubscriptionTotalusers(Integer.parseInt(subscriptionHistory.getAddSubscriptionBORequest().getSubscriptionRoleDetails().get(i).getCountToadd()));
				
				if(subscriptionHistory.getAddSubscriptionBORequest().getMode().equalsIgnoreCase("month")) {
					cal.add(Calendar.MONTH, 1);
					Date date = cal.getTime(); 
					Date expDate=new SimpleDateFormat("yyyy/MM/dd").parse(format.format(date));
					otsSubscriptionOrder.date(expDate);	
				}else {
					cal.add(Calendar.YEAR, 1);
					Date date = cal.getTime(); 
					Date expDate=new SimpleDateFormat("yyyy/MM/dd").parse(format.format(date));
					otsSubscriptionOrder.date(expDate);
				}
				super.getEntityManager().merge(otsSubscriptionOrder);
			}
		}catch(Exception e) {
			
		}
			return subscriptionHistory;
		}
		
	
}
