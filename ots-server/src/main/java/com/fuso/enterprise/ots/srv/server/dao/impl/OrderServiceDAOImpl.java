package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fuso.enterprise.ots.srv.api.model.domain.ListOfOrderId;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderDetails;
import com.fuso.enterprise.ots.srv.api.service.request.OrderDetailsBORequest;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.server.dao.OrderServiceDAO;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsBill;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsOrder;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;

@Repository
public class OrderServiceDAOImpl extends AbstractIptDao<OtsOrder, String> implements OrderServiceDAO {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public OrderServiceDAOImpl() {
		super(OtsOrder.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateOrderwithBillID(OtsBill otsBill, List<ListOfOrderId> listOfOrderId) {
		try {
			OtsOrder otsOrder = new OtsOrder();
			Map<String, Object> queryParameter = new HashMap<>();
             /*
              * iteration started for getting each orderId in list
              */
			for (int i = 0; i < listOfOrderId.size(); i++) {
				int orderId = Integer.parseInt(listOfOrderId.get(i).getOrderId());
				queryParameter.put("otsOrderId", orderId);
				otsOrder = super.getResultByNamedQuery("OtsOrder.findByOtsOrderId", queryParameter);
				otsOrder.setOtsOrderId(orderId);
				otsOrder.setOtsOrderStatus("Generated");
				otsOrder.setOtsBillId(otsBill);
				super.getEntityManager().merge(otsOrder);
			}
			/*
			 * iteration end for getting each orderId
			 */
		} catch (NoResultException e) {
			logger.error("Exception while updating Order Entity  :" + e.getMessage());
			e.printStackTrace();
			throw new BusinessException(e.getMessage(), e);
		}

	}

	

}
