package com.fuso.enterprise.ots.srv.server.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fuso.enterprise.ots.srv.api.service.request.GetProductStockListRequest;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.server.dao.OrderProductDAO;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsOrder;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsOrderProduct;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsProduct;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;

@Service
@Transactional
public class OrderProductDAOImpl extends AbstractIptDao<OtsOrderProduct, String> implements OrderProductDAO {

	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public OrderProductDAOImpl() {
		super(OtsOrderProduct.class);
	}

		@Override
	public long getListOfDeliverdQuantityOfDay(List<OtsOrder> orderList, Integer otsProductId) {
		int orderProductList = 0;
		try {
			Iterator<OtsOrder> iterator = orderList.iterator();
			OtsProduct otsProduct= new OtsProduct();
			otsProduct.setOtsProductId(otsProductId);
			while (iterator.hasNext()) {	
				OtsOrder otsOrder = iterator.next();
				Map<String, Object> queryParameter = new HashMap<>();
				queryParameter.put("otsOrderId", otsOrder);
				queryParameter.put("otsProductId", otsProduct);
				OtsOrderProduct otsOrderProduct = super.getResultByNamedQuery("OtsOrder.fetchOtsSoldProducts", queryParameter);
				orderProductList += otsOrderProduct.getOtsDeliveredQty();
			}
		} catch (NoResultException e) {
			logger.error("Exception while fetching data from DB :" + e.getMessage());
			e.printStackTrace();
		}
		return orderProductList;
	}
}
