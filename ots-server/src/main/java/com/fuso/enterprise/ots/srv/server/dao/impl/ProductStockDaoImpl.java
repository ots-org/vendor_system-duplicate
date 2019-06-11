package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fuso.enterprise.ots.srv.api.service.request.AddProductStockBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetProductStockRequest;
import com.fuso.enterprise.ots.srv.api.service.response.GetProductBOStockResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.common.exception.ErrorEnumeration;
import com.fuso.enterprise.ots.srv.server.dao.ProductStockDao;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsProduct;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsProductStock;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;

@Repository
public class ProductStockDaoImpl extends AbstractIptDao<OtsProductStock, String> implements ProductStockDao{

	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public ProductStockDaoImpl() {
		super(OtsProductStock.class);
	}
 
	//Inserting and Updating the ProductStock Table
	@Override
	public String addProductStock(AddProductStockBORequest addProductStockBORequest) {
		OtsProductStock otsProductStock = new OtsProductStock();		
		OtsProduct otsProduct = new OtsProduct();
		otsProduct.setOtsProductId(Integer.parseInt(addProductStockBORequest.getRequestData().getProductId()));				
		OtsUsers otsUsers = new OtsUsers();
		otsUsers.setOtsUsersId(Integer.parseInt(addProductStockBORequest.getRequestData().getUsersId()));				
	 	Map<String, Object> queryParameter = new HashMap<>();		
	 	queryParameter.put("otsUsersId",otsUsers );		
		queryParameter.put("otsProductId", otsProduct);		
		try {
			otsProductStock = super.getResultByNamedQuery("OtsProduct.findByOtsProductIdAndUserId", queryParameter) ;			
			Integer stock = Integer.valueOf(addProductStockBORequest.getRequestData().getProductStockQty());
			stock = stock + Integer.valueOf(otsProductStock.getOtsProdcutStockActQty());
			otsProductStock.setOtsProdcutStockId(otsProductStock.getOtsProdcutStockId());
			otsProductStock.setOtsProdcutStockActQty(stock.toString());
			super.getEntityManager().merge(otsProductStock);
			
		}catch (NoResultException e) {
			otsUsers.setOtsUsersId(Integer.parseInt(addProductStockBORequest.getRequestData().getUsersId()));
			otsProductStock.setOtsUsersId(otsUsers);		 
			otsProductStock.setOtsProdcutStockActQty(addProductStockBORequest.getRequestData().getProductStockQty());
			otsProductStock.setOtsProdcutStockStatus(addProductStockBORequest.getRequestData().getProductStockStatus());
			otsProduct.setOtsProductId(Integer.parseInt(addProductStockBORequest.getRequestData().getProductId()));
			otsProductStock.setOtsProductId (otsProduct);
			super.getEntityManager().merge(otsProductStock);
		}catch (Exception e) {
			e.printStackTrace();
	    	throw new BusinessException(e, ErrorEnumeration.User_Not_exists);
		}
		logger.info("Inside Event=1014,Class:OTSProduct_WsImpl,Method:ProductStockDaoImpl ");
		return "Stock Updated Scuccessfully";
	}
	
	@Override
	public String updateProductStockQuantity(AddProductStockBORequest addProductStockBORequest) {
		OtsProductStock otsProductStock = new OtsProductStock();		
		OtsProduct otsProduct = new OtsProduct();
		otsProduct.setOtsProductId(Integer.parseInt(addProductStockBORequest.getRequestData().getProductId()));				
		OtsUsers otsUsers = new OtsUsers();
		otsUsers.setOtsUsersId(Integer.parseInt(addProductStockBORequest.getRequestData().getUsersId()));				
	 	Map<String, Object> queryParameter = new HashMap<>();		
	 	queryParameter.put("otsUsersId",otsUsers );		
		queryParameter.put("otsProductId", otsProduct);		
		try {
			otsProductStock = super.getResultByNamedQuery("OtsProduct.findByOtsProductIdAndUserId", queryParameter) ;			
			Integer stock = Integer.valueOf(addProductStockBORequest.getRequestData().getProductStockQty());
			stock =Integer.valueOf(otsProductStock.getOtsProdcutStockActQty())-stock;
			otsProductStock.setOtsProdcutStockId(otsProductStock.getOtsProdcutStockId());
			otsProductStock.setOtsProdcutStockActQty(stock.toString());
			super.getEntityManager().merge(otsProductStock);
			
		}catch (Exception e) {
			e.printStackTrace();
	    	throw new BusinessException(e, ErrorEnumeration.User_Not_exists);
		}
		return "Stock Quntity Updated Scuccessfully";
		
	}

	@Override
	public GetProductBOStockResponse getProductStockByUidAndPid(GetProductStockRequest getProductStockRequest) {
		GetProductBOStockResponse getProductBOStockResponse = new GetProductBOStockResponse();
		OtsProductStock otsProductStock = new OtsProductStock();
		try {
		 	Map<String, Object> queryParameter = new HashMap<>();	
		 	
		 	OtsUsers UserId = new OtsUsers();
		 	UserId.setOtsUsersId(Integer.valueOf(getProductStockRequest.getRequestData().getDistributorId()));
		 	
		 	OtsProduct otsProductId = new OtsProduct();
		 	otsProductId.setOtsProductId(Integer.valueOf(getProductStockRequest.getRequestData().getProductId()));
		 	
		 	queryParameter.put("DistributorId",UserId);		
			queryParameter.put("ProductId",otsProductId );
			otsProductStock = super.getResultByNamedQuery("OtsProductStock.getQuantityById", queryParameter);
			getProductBOStockResponse = convertProductStockEntityToModel(otsProductStock);
		} catch (BusinessException e) {
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_GETPRODUCTSTOCK);
	    } catch (Throwable e) {
	    	throw new BusinessException(e, ErrorEnumeration.ERROR_IN_GETPRODUCTSTOCK);
	    }
    	return getProductBOStockResponse;
	}
	
	private GetProductBOStockResponse convertProductStockEntityToModel(OtsProductStock otsProductStock)
	{
		GetProductBOStockResponse getProductBOStockResponse = new GetProductBOStockResponse();
		getProductBOStockResponse.setProductId(otsProductStock.getOtsProductId().getOtsProductId().toString());
		getProductBOStockResponse.setProductStockId(otsProductStock.getOtsProdcutStockId().toString());
		getProductBOStockResponse.setStockQuantity(otsProductStock.getOtsProdcutStockActQty());
		getProductBOStockResponse.setProductStockStatus(otsProductStock.getOtsProdcutStockStatus());
		getProductBOStockResponse.setUserId(otsProductStock.getOtsUsersId().getOtsUsersId().toString());
		return getProductBOStockResponse;
	}
	
	@Override
	public String removeProductStock(AddProductStockBORequest addProductStockBORequest) {
		OtsProductStock otsProductStock = new OtsProductStock();		
		OtsProduct otsProduct = new OtsProduct();
		otsProduct.setOtsProductId(Integer.parseInt(addProductStockBORequest.getRequestData().getProductId()));				
		OtsUsers otsUsers = new OtsUsers();
		otsUsers.setOtsUsersId(Integer.parseInt(addProductStockBORequest.getRequestData().getUsersId()));				
	 	Map<String, Object> queryParameter = new HashMap<>();		
	 	queryParameter.put("otsUsersId",otsUsers );		
		queryParameter.put("otsProductId", otsProduct);		
		try {
			otsProductStock = super.getResultByNamedQuery("OtsProduct.findByOtsProductIdAndUserId", queryParameter) ;			
			Long stock = Long.valueOf(otsProductStock.getOtsProdcutStockActQty());
			stock = stock - Long.valueOf(addProductStockBORequest.getRequestData().getProductStockQty());
			otsProductStock.setOtsProdcutStockId(otsProductStock.getOtsProdcutStockId());
			otsProductStock.setOtsProdcutStockActQty(stock.toString());
			super.getEntityManager().merge(otsProductStock);
			
		}catch (NoResultException e) {
			otsUsers.setOtsUsersId(Integer.parseInt(addProductStockBORequest.getRequestData().getUsersId()));
			otsProductStock.setOtsUsersId(otsUsers);		 
			otsProductStock.setOtsProdcutStockActQty(addProductStockBORequest.getRequestData().getProductStockQty());
			otsProductStock.setOtsProdcutStockStatus(addProductStockBORequest.getRequestData().getProductStockStatus());
			otsProduct.setOtsProductId(Integer.parseInt(addProductStockBORequest.getRequestData().getProductId()));
			otsProductStock.setOtsProductId (otsProduct);
			super.getEntityManager().merge(otsProductStock);
		}catch (Exception e) {
			e.printStackTrace();
	    	throw new BusinessException(e, ErrorEnumeration.User_Not_exists);
		}
		logger.info("Inside Event=1014,Class:OTSProduct_WsImpl,Method:removeProductStock");
		return "Stock Updated Scuccessfully";
	}

	@Override
	public List<GetProductBOStockResponse> getProductStockByUid(String distributorId) {
		List<GetProductBOStockResponse> getProductBOStockResponse = new ArrayList<GetProductBOStockResponse>();
		List<OtsProductStock> otsProductStockList = new ArrayList<OtsProductStock>();
		try {
		 	Map<String, Object> queryParameter = new HashMap<>();	
		 	
		 	OtsUsers UserId = new OtsUsers();
		 	UserId.setOtsUsersId(Integer.valueOf(distributorId)); 	
		 	queryParameter.put("DistributorId",UserId);		
		 	
		 	otsProductStockList = super.getResultListByNamedQuery("OtsProductStock.DistributorId", queryParameter);
			getProductBOStockResponse = otsProductStockList.stream().map(OtsProductStock -> convertProductStockEntityToModel(OtsProductStock)).collect(Collectors.toList());
		} catch (BusinessException e) {
			return null;
	    } catch (Throwable e) {
	    	return null;
	    }
    	return getProductBOStockResponse;
	}
}
