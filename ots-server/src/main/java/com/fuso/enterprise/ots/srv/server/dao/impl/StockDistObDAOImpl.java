package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.fuso.enterprise.ots.srv.server.dao.StockDistObDAO;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsProductStock;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsStockDistOb;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;

@Service
@Transactional
public class StockDistObDAOImpl extends AbstractIptDao<OtsStockDistOb, String> implements StockDistObDAO {

	@Autowired
    private JdbcTemplate jdbcTemplate;
	
   private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public StockDistObDAOImpl() {
		super(OtsStockDistOb.class);
	}

	@Override
	public List<OtsStockDistOb> fetchOpeningBalance(List<OtsProductStock> productList,
			GetProductStockListRequest getProductStockListRequest) {
		List<OtsStockDistOb> otsStockDistOb = new ArrayList<>();	
		OtsUsers OtsUsers= new OtsUsers();
		OtsUsers.setOtsUsersId(Integer.parseInt(getProductStockListRequest.getRequestData().getUserId()));
		try {
			java.util.Iterator<OtsProductStock> OtsProductStocklistIterator = productList.iterator();
			while (OtsProductStocklistIterator.hasNext()) {
				OtsProductStock productStock = OtsProductStocklistIterator.next();
				Map<String, Object> queryParameter = new HashMap<>();
	   			queryParameter.put("otsUsersId", OtsUsers);
	   			queryParameter.put("otsProductId", productStock.getOtsProductId());
	   			queryParameter.put("otsStockDistObStockdt", getProductStockListRequest.getRequestData().getTodaysDate());
	   			List<OtsStockDistOb> otsStocDistObLocal = super.getResultListByNamedQuery("OtsStockDistOb.fetchOpenBalance", queryParameter);
	   			System.out.println("otsStocDistObLocal"+otsStocDistObLocal.size());
	   		    otsStockDistOb.addAll(otsStocDistObLocal);
			}
		} catch (NoResultException e) {
	    	logger.error("Exception while fetching data from DB :"+e.getMessage());
			e.printStackTrace();
	    	throw new BusinessException(e.getMessage(), e);
		}
		return otsStockDistOb;
	}

	@Override
	public List<OtsStockDistOb> fetchOpeningBalance(GetProductStockListRequest getProductStockListRequest) {
		// TODO Auto-generated method stub
		List<OtsStockDistOb> otsStocDist = new ArrayList<OtsStockDistOb>();
		try {
			logger.info("Inside Event=1015,Class:StockDistObDAOImpl, Method:fetchOpeningBalance, getProductStockListRequest:"
					+ getProductStockListRequest.getRequestData().getUserId()+"date"+getProductStockListRequest.getRequestData().getTodaysDate());
			OtsUsers OtsUsers= new OtsUsers();
			OtsUsers.setOtsUsersId(Integer.parseInt(getProductStockListRequest.getRequestData().getUserId()));
			otsStocDist = super.getEntityManager()
					.createQuery("from OtsStockDistOb where  otsUsersId = ?1 and otsStockDistObStockdt = ?2  ", OtsStockDistOb.class)
					.setParameter(1,OtsUsers)
					.setParameter(2,getProductStockListRequest.getRequestData().getTodaysDate(), TemporalType.DATE)
					.getResultList();
			
			
   		} catch (NoResultException e) {
	    	logger.error("Exception while fetching data from DB :"+e.getMessage());
			e.printStackTrace();
			throw new BusinessException(e.getMessage(), e);
	    }
		return otsStocDist;
	}
}
