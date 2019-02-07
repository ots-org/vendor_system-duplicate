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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;
import com.fuso.enterprise.ots.srv.api.service.request.AddProductStockBORequest;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.server.dao.ProductStockDao;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsProduct;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsProductStock;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class ProductStockDaoImpl extends AbstractIptDao<OtsProductStock, String> implements ProductStockDao{

	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	public ProductStockDaoImpl() {
		super(OtsProductStock.class);
	}
 
	//Inserting and Updating the ProductStock Table
	@Override
	public String addProductStock(AddProductStockBORequest addProductStockBORequest) {
		OtsProductStock otsProductStock = new OtsProductStock();		
		OtsProduct otsProduct = new OtsProduct();
		otsProduct.setOtsProductId(Integer.parseInt(addProductStockBORequest.getRequest().getProductId()));				
		OtsUsers otsUsers = new OtsUsers();
		otsUsers.setOtsUsersId(Integer.parseInt(addProductStockBORequest.getRequest().getUsersId()));				
	 	Map<String, Object> queryParameter = new HashMap<>();		
	 	queryParameter.put("otsUsersId",otsUsers );		
		queryParameter.put("otsProductId", otsProduct);		
		try {
		otsProductStock = super.getResultByNamedQuery("OtsProduct.findByOtsProductIdAndUserId", queryParameter) ;			
		try {
			if(otsProductStock == null) {
				System.out.println("null");
				otsUsers.setOtsUsersId(Integer.parseInt(addProductStockBORequest.getRequest().getUsersId()));
				otsProductStock.setOtsUsersId(otsUsers);		 
				otsProductStock.setOtsProdcutStockActQty(addProductStockBORequest.getRequest().getProductStockQty());
				otsProductStock.setOtsProdcutStockStatus(addProductStockBORequest.getRequest().getProductStockStatus());
				otsProduct.setOtsProductId(Integer.parseInt(addProductStockBORequest.getRequest().getProductId()));
				otsProductStock.setOtsProductId (otsProduct);
				super.getEntityManager().merge(otsProductStock);
			}else {
				System.out.print("update");
				Integer stock = Integer.valueOf(addProductStockBORequest.getRequest().getProductStockQty());
				stock = stock + Integer.valueOf(otsProductStock.getOtsProdcutStockActQty());
				otsProductStock.setOtsProdcutStockId(otsProductStock.getOtsProdcutStockId());
				
				otsProductStock.setOtsProdcutStockActQty(stock.toString());
				super.getEntityManager().merge(otsProductStock);
			}	
		}catch (NoResultException e) {
			e.printStackTrace();
	    	throw new BusinessException(e.getMessage(), e);
		  }	
		}
		catch (Exception e) {
			e.printStackTrace();
	    	throw new BusinessException(e.getMessage(), e);
		}
		return "Value Updated";
	}
		
}
