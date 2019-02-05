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

import com.fuso.enterprise.ots.srv.api.model.domain.ProductDetails;
import com.fuso.enterprise.ots.srv.api.service.request.AddorUpdateProductBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.ProductDetailsBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.ProductDetailsBOResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.server.dao.ProductServiceDAO;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsProduct;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUserMapping;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;
@Repository
public class ProductServiceDAOImpl extends AbstractIptDao<OtsProduct, String> implements ProductServiceDAO {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
    private JdbcTemplate jdbcTemplate;
    public ProductServiceDAOImpl() {
		super(OtsProduct.class);
    }

	@Override
	public ProductDetailsBOResponse getProductList(ProductDetailsBORequest productDetailsBORequest) {
		List<ProductDetails> productDetails = new ArrayList<ProductDetails>();
		String searchKey=productDetailsBORequest.getRequestData().getSearchKey();
		String seachValue=productDetailsBORequest.getRequestData().getSearchvalue();
		ProductDetailsBOResponse productDetailsBOResponse = new ProductDetailsBOResponse();
		Map<String, Object> queryParameter = new HashMap<>();
		List<OtsProduct> productList = null;
		try{
            switch(searchKey){
	            case "ProductId":
	            					queryParameter.put("otsProductId", Integer.parseInt(seachValue));
	            					productList  = super.getResultListByNamedQuery("OtsProduct.findByOtsProductId", queryParameter);
	            				    break;
	            case "ProductName":
									queryParameter.put("otsProductName", seachValue);
									productList  = super.getResultListByNamedQuery("OtsProduct.findByOtsProductName", queryParameter);
								    break;
				
	            case "All":
					                productList  = super.getResultListByNamedQuery("OtsProduct.findAll",queryParameter);
				                    break;
				                    
	            case "FirstLetter":
	            	                queryParameter.put("FirstLetter", "%"+seachValue+"%");
	            	                productList  = super.getResultListByNamedQuery("OtsProduct.findByPattrenMatching",queryParameter);
	                                break;                    
	            
	            default:
	            					return null;

            }
            logger.info("Inside Event=1012,Class:ProductServiceDAOImpl,Method:getProductList, "
					+ "UserList Size:" +productList.size());
            productDetails =  productList.stream().map(otsProduct -> convertProductDetailsFromEntityToDomain(otsProduct)).collect(Collectors.toList());
            productDetailsBOResponse.setProductDetails(productDetails);
            return productDetailsBOResponse;
		}catch (NoResultException e) {
	    	logger.error("Exception while fetching data from DB :"+e.getMessage());
			e.printStackTrace();
	    	throw new BusinessException(e.getMessage(), e);
	    }
	}

	
	@Override
	public String addOrUpdateProduct(AddorUpdateProductBORequest addorUpdateProductBORequest) {
		String responseData;
		try{OtsProduct otsProduct = new OtsProduct();
			otsProduct.setOtsProductId(Integer.parseInt(addorUpdateProductBORequest.getRequestData().getProductId()));
			otsProduct.setOtsProductName(addorUpdateProductBORequest.getRequestData().getProductName());
			otsProduct.setOtsProductDescription(addorUpdateProductBORequest.getRequestData().getProductDescription());
			otsProduct.setOtsProductPrice(Long.parseLong(addorUpdateProductBORequest.getRequestData().getProductPrice()));
			otsProduct.setOtsProductStatus(addorUpdateProductBORequest.getRequestData().getProductStatus());
			try {
				super.getEntityManager().merge(otsProduct);
			}catch (NoResultException e) {
				logger.error("Exception while Inserting data to DB :"+e.getMessage());
	    		e.printStackTrace();
	        	throw new BusinessException(e.getMessage(), e);
			}
			responseData="Product added/updated Successfully";
			logger.info("Inside Event=1011,Class:ProductServiceDAOImpl,Method:addOrUpdateProduct:"+responseData);
		}catch (NoResultException e) {
        	logger.error("Exception while Inserting data to DB  :"+e.getMessage());
    		e.printStackTrace();
        	throw new BusinessException(e.getMessage(), e);
        }
		return  responseData;
	}
	
	private ProductDetails convertProductDetailsFromEntityToDomain(OtsProduct otsProduct) {
		ProductDetails productDetails = new ProductDetails();
		productDetails.setProductId(otsProduct.getOtsProductId().toString());
		productDetails.setProductName(otsProduct.getOtsProductName());
		productDetails.setProductDescription(otsProduct.getOtsProductDescription());
		productDetails.setProductPrice(otsProduct.getOtsProductPrice().toString());
		productDetails.setProductStatus(otsProduct.getOtsProductStatus());
		return productDetails;
	}

}
