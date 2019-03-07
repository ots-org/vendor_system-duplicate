package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.TemporalType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.fuso.enterprise.ots.srv.api.service.request.AddProductStockBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetProductStockListRequest;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.server.dao.ProductStockHistoryDao;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsProduct;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsProductStockHistory;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;
@Repository
public class ProductStockHistoryDaoImpl extends AbstractIptDao<OtsProductStockHistory, String> implements ProductStockHistoryDao{
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
    private JdbcTemplate jdbcTemplate;

	public ProductStockHistoryDaoImpl() {
		super(OtsProductStockHistory.class);
	}

	@Override
	public void addProductStockHistory(AddProductStockBORequest addProductStockBORequest) {
		try{
			OtsProductStockHistory otsProductStockHistory = new OtsProductStockHistory();
				
			 OtsProduct otsProduct = new OtsProduct();
			 otsProduct.setOtsProductId(Integer.parseInt(addProductStockBORequest.getRequestData().getProductId()));
			 otsProductStockHistory.setOtsProductId (otsProduct);
			 
			 OtsUsers otsUsers = new OtsUsers();
			 otsUsers.setOtsUsersId(Integer.parseInt(addProductStockBORequest.getRequestData().getUsersId()));
			 otsProductStockHistory.setOtsUsersId(otsUsers);
			 otsProductStockHistory.setOtsProductStockAddDate(addProductStockBORequest.getRequestData().getProductStockAddDate());
			 otsProductStockHistory.setOtsProductStockHistoryQty(addProductStockBORequest.getRequestData().getProductStockQty());
			 otsProductStockHistory.setOtsProductStockOrderId(addProductStockBORequest.getRequestData().getOrderId());
			 super.getEntityManager().merge(otsProductStockHistory);
			 logger.info("Inside Event=1014,Class:ProductStockHistoryDaoImpl,Method:addProductStockHistory ");
		}catch(Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
	}
	
	@Override
	public long getOtsProductStockAddition(Integer otsProductId,
			GetProductStockListRequest getProductStockListRequest) {
		int prodcutStockQuantityDated = 0;
		List<OtsProductStockHistory> otsProductStockHistory = new ArrayList<OtsProductStockHistory>();
		try {
			logger.info("Inside Event=1015,Class:ProductStockHistoryDaoImpl, Method:getOtsProductStockAddition, otsProductId:"
					+ otsProductId + "getProductStockListRequest: "+getProductStockListRequest.getRequestData().getUserId()+ "date"+ getProductStockListRequest.getRequestData().getTodaysDate());
			OtsUsers OtsUsers= new OtsUsers();
			OtsProduct otsProduct= new OtsProduct();
			OtsUsers.setOtsUsersId(Integer.parseInt(getProductStockListRequest.getRequestData().getUserId()));
			otsProduct.setOtsProductId(otsProductId);
			otsProductStockHistory = super.getEntityManager()
					.createQuery("from OtsProductStockHistory where  otsUsersId = ?1 and otsProductId = ?2 and otsProductStockAddDate = ?3  ", OtsProductStockHistory.class)
					.setParameter(1,OtsUsers)
					.setParameter(2,otsProduct)
					.setParameter(3,getProductStockListRequest.getRequestData().getTodaysDate(), TemporalType.DATE)
					.getResultList();
			for(int i=0;i<otsProductStockHistory.size();i++) {
				prodcutStockQuantityDated += Integer.parseInt(otsProductStockHistory.get(i).getOtsProductStockHistoryQty());
			}
			
   		} catch (NoResultException e) {
	    	logger.error("Exception while fetching data from DB :"+e.getMessage());
			e.printStackTrace();
			throw new BusinessException(e.getMessage(), e);
	    }
		return prodcutStockQuantityDated;
	}
	

}
