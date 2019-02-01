package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fuso.enterprise.ots.srv.api.service.request.CustomerProductDataBORequest;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.server.dao.MapUserProductDAO;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsCustomerProduct;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsProduct;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;
@Repository
public class MapUserProductDAOImpl extends AbstractIptDao<OtsCustomerProduct, String> implements MapUserProductDAO {
private Logger logger = LoggerFactory.getLogger(getClass());

     @Autowired
     private JdbcTemplate jdbcTemplate;
	public MapUserProductDAOImpl() {
		super(OtsCustomerProduct.class);
		
	}

	@Override
	public String mapUserProduct(CustomerProductDataBORequest customerProductDataBORequest) {
		String responseData;
		try{
			/*
			 * setting users object for user mapping
			 */
			OtsUsers otsUsers = new OtsUsers();
			otsUsers.setOtsUsersId(Integer.parseInt(customerProductDataBORequest.getRequestData().getUserId()));
			/*
			 * setting Product object for product mapping
			 */
			OtsProduct otsProduct = new OtsProduct();
			otsProduct.setOtsProductId(Integer.parseInt(customerProductDataBORequest.getRequestData().getProductId()));
			
			OtsCustomerProduct userProductEntity=new OtsCustomerProduct();
			userProductEntity.setOtsUsersId(otsUsers);
			userProductEntity.setOtsProductId(otsProduct);
			userProductEntity.setOtsCustomerProductPrice(customerProductDataBORequest.getRequestData().getProductPrice());
			try {
				super.getEntityManager().merge(userProductEntity);
			}catch (NoResultException e) {
				logger.error("Exception while Inserting data to DB :"+e.getMessage());
	    		e.printStackTrace();
	        	throw new BusinessException(e.getMessage(), e);
			}
			responseData="User Product Mapped Successfully";
			logger.info("Inside Event=1006,Class:MapUserProductDAOImpl,Method:mapUserProduct"+"Successfull");
		}catch (Exception e) {
        	logger.error("Exception while Inserting data to DB  :"+e.getMessage());
    		e.printStackTrace();
        	throw new BusinessException(e.getMessage(), e);
        }
		return  responseData;
	}

}
