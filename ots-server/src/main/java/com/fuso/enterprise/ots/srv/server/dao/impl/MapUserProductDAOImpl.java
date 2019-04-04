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
			OtsCustomerProduct userProductEntity=new OtsCustomerProduct();
			OtsUsers otsUsers = new OtsUsers();
			otsUsers.setOtsUsersId(Integer.parseInt(customerProductDataBORequest.getRequestData().getUserId()));
			userProductEntity.setOtsUsersId(otsUsers);
			userProductEntity.setOtsCustomerProductBalCan(customerProductDataBORequest.getRequestData().getCustomerBalanceCan());
			userProductEntity.setOtsCustomerProductPrice(customerProductDataBORequest.getRequestData().getProductPrice());
			/*
			 * setting Product object for product mapping
			 */
			OtsProduct otsProduct = new OtsProduct();
			if(customerProductDataBORequest.getRequestData().getProductId()!=null) {
				try {
					otsProduct.setOtsProductId(Integer.parseInt(customerProductDataBORequest.getRequestData().getProductId()));
					userProductEntity.setOtsProductId(otsProduct);
               }catch (Exception e) {
            	   otsProduct.setOtsProductId(null);
            	   userProductEntity.setOtsProductId(otsProduct);
				}
			}
			userProductEntity.setOtsCustomerProductPrice(customerProductDataBORequest.getRequestData().getProductPrice());
			try {
				try {
					int customerProdcutId = Integer.parseInt(customerProductDataBORequest.getRequestData().getCustomerProductId());
					userProductEntity.setOtsCustomerProductId(customerProdcutId);
				}catch(Exception e) {
					userProductEntity.setOtsCustomerProductId(null);
				}
				super.getEntityManager().merge(userProductEntity);
			}catch (NoResultException e) {
				logger.error("Exception while Inserting data to DB :"+e.getMessage());
	    		e.printStackTrace();
	        	throw new BusinessException(e.getMessage(), e);
			}catch(Exception e) {
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
	
	@Override
	public String UpdateBySaleVocher(CustomerProductDataBORequest customerProductDataBORequest) {
		String responseData;
		try{
			/*
			 * setting users object for user mapping
			 */
			OtsCustomerProduct otsCustomerProduct = new OtsCustomerProduct();
			
			OtsUsers otsUsers = new OtsUsers();
			otsUsers.setOtsUsersId(Integer.parseInt(customerProductDataBORequest.getRequestData().getUserId()));
			otsCustomerProduct.setOtsUsersId(otsUsers);
			
			OtsProduct otsProduct = new OtsProduct();
			otsProduct.setOtsProductId(Integer.parseInt(customerProductDataBORequest.getRequestData().getProductId()));
			otsCustomerProduct.setOtsProductId(otsProduct);
			
			otsCustomerProduct.setOtsCustomerProductDefault("NO");
			Map<String, Object> queryParameter = new HashMap<>();
			
			queryParameter.put("otsUsersId",otsUsers);
			queryParameter.put("otsProductId",otsProduct);
			queryParameter.put("otsCustomerProductDefault","no");
			try {
				otsCustomerProduct = super.getResultByNamedQuery("OtsCustomerProduct.getCustomerProductDetails", queryParameter);
				otsCustomerProduct.setOtsCustomerProductBalCan(customerProductDataBORequest.getRequestData().getCustomerBalanceCan());
				otsCustomerProduct.setOtsCustomerProductPrice(customerProductDataBORequest.getRequestData().getProductPrice());
				super.getEntityManager().merge(otsCustomerProduct);
			}catch(Exception e) {
					
				
				otsCustomerProduct.setOtsCustomerProductBalCan(customerProductDataBORequest.getRequestData().getCustomerBalanceCan());
				otsCustomerProduct.setOtsCustomerProductPrice(customerProductDataBORequest.getRequestData().getProductPrice());
				super.getEntityManager().persist(otsCustomerProduct);
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
