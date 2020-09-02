package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TemporalType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fuso.enterprise.ots.srv.api.service.request.GetProductStockListRequest;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.server.dao.OrderDAO;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsOrder;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsProduct;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;

@Service
@Transactional
public class OrderDAOImpl extends AbstractIptDao<OtsOrder, String> implements OrderDAO {

	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public OrderDAOImpl() {
		super(OtsOrder.class);
	}

	@Override
	public List<OtsOrder> getOrderList(Integer otsProductId,GetProductStockListRequest getProductStockListRequest) {
		List<OtsOrder> orderList = null;
		try {
			logger.info("Inside Event=1015,Class:OrderDAOImpl, Method:getOrderList, getProductStockListRequest:"
					+ getProductStockListRequest+"otsProductId:"+otsProductId);
			OtsUsers OtsUsers= new OtsUsers();
			OtsProduct otsProduct= new OtsProduct();
			OtsUsers.setOtsUsersId(Integer.parseInt("1"));
			otsProduct.setOtsProductId(otsProductId);
			orderList = super.getEntityManager()
					.createQuery("from OtsOrder where  otsDistributorId = ?1 and otsOrderDeliveredDt = ?2  ", OtsOrder.class)
					.setParameter(1,OtsUsers)
					.setParameter(2,getProductStockListRequest.getRequestData().getTodaysDate(), TemporalType.DATE)
					.getResultList();
			
   		} catch (NoResultException e) {
	    	System.out.println("no order");
	    	return null;
	    }
		return orderList;
	}
}
