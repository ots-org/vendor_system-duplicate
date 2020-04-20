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

import com.fuso.enterprise.ots.srv.api.model.domain.BalanceCan;
import com.fuso.enterprise.ots.srv.api.model.domain.CustomerProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.ProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;
import com.fuso.enterprise.ots.srv.api.service.request.AddProductCategoryAndProductRequest;
import com.fuso.enterprise.ots.srv.api.service.request.CustomerProductDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.ProductDetailsBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.ProductDetailsBOResponse;
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
			userProductEntity.setOtsCustomerProductBalCan(customerProductDataBORequest.getRequestData().getCustomerBalanceCan()==null?0:Integer.parseInt(customerProductDataBORequest.getRequestData().getCustomerBalanceCan()));
			userProductEntity.setOtsCustomerProductPrice(customerProductDataBORequest.getRequestData().getProductPrice());
			userProductEntity.setOtsCustomerProductDefault("Yes");
			
			
			
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
				otsCustomerProduct.setOtsCustomerProductBalCan(Integer.parseInt(customerProductDataBORequest.getRequestData().getCustomerBalanceCan()));
				otsCustomerProduct.setOtsCustomerProductPrice(customerProductDataBORequest.getRequestData().getProductPrice());
				super.getEntityManager().merge(otsCustomerProduct);
			}catch(Exception e) {
				otsCustomerProduct.setOtsCustomerProductBalCan(Integer.parseInt(customerProductDataBORequest.getRequestData().getCustomerBalanceCan()));
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

	@Override
	public CustomerProductDetails getCustomerProductDetailsByUserIdandProductId(String productId,String customerId) {
		CustomerProductDetails customerProductDetails = new CustomerProductDetails();
		try{
			/*
			 * setting users object for user mapping
			 */
			OtsCustomerProduct otsCustomerProduct = new OtsCustomerProduct();
			
			OtsUsers otsUsers = new OtsUsers();
			otsUsers.setOtsUsersId(Integer.parseInt(customerId));
			otsCustomerProduct.setOtsUsersId(otsUsers);
			
			OtsProduct otsProduct = new OtsProduct();
			otsProduct.setOtsProductId(Integer.parseInt(productId));
			otsCustomerProduct.setOtsProductId(otsProduct);
				
			Map<String, Object> queryParameter = new HashMap<>();
			queryParameter.put("otsUsersId",otsUsers);
			queryParameter.put("otsProductId",otsProduct);
			queryParameter.put("otsCustomerProductDefault","no");
			try {
				otsCustomerProduct = super.getResultByNamedQuery("OtsCustomerProduct.getCustomerProductDetails", queryParameter);
				customerProductDetails = convertCustomerDetailsEntityToModel(otsCustomerProduct);
			}catch (Exception e) {
	        	return null;
	        }
			logger.info("Inside Event=1006,Class:MapUserProductDAOImpl,Method:mapUserProduct"+"Successfull");
		}catch (Exception e) {
        	return null;
    		
        }
		return  customerProductDetails;
	}
	
	public CustomerProductDetails convertCustomerDetailsEntityToModel(OtsCustomerProduct otsCustomerProduct) {
		CustomerProductDetails customerProductDetails = new CustomerProductDetails();
		customerProductDetails.setCustomerBalanceCan(otsCustomerProduct.getOtsCustomerProductBalCan()+"");
		customerProductDetails.setProductPrice(otsCustomerProduct.getOtsCustomerProductPrice());
		customerProductDetails.setProductId(otsCustomerProduct.getOtsProductId().getOtsProductId().toString());
		customerProductDetails.setProductDefault(otsCustomerProduct.getOtsCustomerProductDefault());
		customerProductDetails.setUserId(otsCustomerProduct.getOtsUsersId().toString());
		return customerProductDetails;
		
	}
	
	@Override
	public List<BalanceCan> getBalanceCanByUserId(String customerId) {
		List<BalanceCan> balanceCan = new ArrayList<BalanceCan>();
		List<OtsCustomerProduct> otsCustomerProductList =  new ArrayList<OtsCustomerProduct>();
		try{
			/*
			 * setting users object for user mapping
			 */
			
			OtsCustomerProduct otsCustomerProduct = new OtsCustomerProduct();
			OtsUsers otsUsers = new OtsUsers();
			otsUsers.setOtsUsersId(Integer.parseInt(customerId));
			otsCustomerProduct.setOtsUsersId(otsUsers);
				
			Map<String, Object> queryParameter = new HashMap<>();
			queryParameter.put("otsUsersId",otsUsers);
			try {
				otsCustomerProductList = super.getResultListByNamedQuery("OtsCustomerProduct.getBalanceCan", queryParameter);
			}catch (Exception e) {
	        	return null;
	        }
			logger.info("Inside Event=1006,Class:MapUserProductDAOImpl,Method:mapUserProduct"+"Successfull");
		}catch (Exception e) {
        	logger.error("Exception while Inserting data to DB  :"+e.getMessage());
    		e.printStackTrace();
        	throw new BusinessException(e.getMessage(), e);
        }
		balanceCan =  otsCustomerProductList.stream().map(OtsCustomerProduct -> convertEntityToBalanceCan(OtsCustomerProduct)).collect(Collectors.toList());
		return  balanceCan;
	}
	
	public BalanceCan convertEntityToBalanceCan(OtsCustomerProduct otsCustomerProduct) {
		BalanceCan balanceCan = new BalanceCan();
		balanceCan.setBalanceCan(otsCustomerProduct.getOtsCustomerProductBalCan()+"");
		balanceCan.setProductId(otsCustomerProduct.getOtsProductId().getOtsProductId().toString());
		return balanceCan;		
	}
	
	@Override
	public List<CustomerProductDetails> getCustomerProductDetailsByCustomerId(String customerId) {
		List<CustomerProductDetails> customerProductDetails = new ArrayList<CustomerProductDetails>();
		try{
			/*
			 * setting users object for user mapping
			 */
			List<OtsCustomerProduct> otsCustomerProductList = new ArrayList<OtsCustomerProduct>();
			
			OtsUsers otsUsers = new OtsUsers();
			otsUsers.setOtsUsersId(Integer.parseInt(customerId));
			Map<String, Object> queryParameter = new HashMap<>();
			queryParameter.put("otsUsersId",otsUsers);
			
			try {
				otsCustomerProductList = super.getResultListByNamedQuery("OtsCustomerProduct.getCustomerProductDetailsByUserId", queryParameter);
				customerProductDetails = otsCustomerProductList.stream().map(OtsCustomerProduct -> convertCustomerDetailsEntityToModel(OtsCustomerProduct)).collect(Collectors.toList());
			}catch (Exception e) {
	        	return null;
	        }
			logger.info("Inside Event=1006,Class:MapUserProductDAOImpl,Method:mapUserProduct"+"Successfull");
		}catch (Exception e) {
        	return null;
        }
		return  customerProductDetails;
	}

	@Override
	public String addProductAndCategory(AddProductCategoryAndProductRequest addProductAndCategoryRequest) {
		try {
			for(int i=0;i<addProductAndCategoryRequest.getRequestData().getProductDetails().size();i++) {
				OtsCustomerProduct customerProduct =  new OtsCustomerProduct();
				
				OtsProduct productId = new OtsProduct();
				productId.setOtsProductId(Integer.parseInt(addProductAndCategoryRequest.getRequestData().getProductDetails().get(i).getProductId()));
				customerProduct.setOtsProductId(productId);
			
				OtsUsers userId = new OtsUsers();
				userId.setOtsUsersId(Integer.parseInt(addProductAndCategoryRequest.getRequestData().getUserId()));
				customerProduct.setOtsUsersId(userId);
				save(customerProduct);
				super.getEntityManager().flush();
			}
		}catch(Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		
		return "Success";
	}

//	@Override
//	public ProductDetailsBOResponse getProductSubcategoryByDistributor(
//			ProductDetailsBORequest productDetailsBORequest) {
//		
//		List<OtsCustomerProduct> otsCustomerProductList = new ArrayList<OtsCustomerProduct>();
//		List<ProductDetails> productDetails = new ArrayList<ProductDetails>();
//		//-------------------------------TO GET LIST OF SUBCATEGORY--------------------------
//		OtsUsers distributorId = new OtsUsers();
//		distributorId.setOtsUsersId(Integer.parseInt(productDetailsBORequest.getRequestData().getDistributorId()));
//		Map<String, Object> queryParameter = new HashMap<>();
//		queryParameter.put("distributorId",distributorId);
//		otsCustomerProductList = super.getResultListByNamedQuery("OtsCustomerProduct.getListOfCatAndSubCat", queryParameter);
//		//------------------------------------------------------------------------------------
//		productDetails =  otsCustomerProductList.stream().map(OtsCustomerProduct -> getProductDetailsFormCustomerProduct(OtsCustomerProduct)).collect(Collectors.toList());
//		return null;
//	}
	
	@Override
	public ProductDetailsBOResponse getProductDetailsForDistributor(ProductDetailsBOResponse productDetailsBOResponse) {
		List<ProductDetails> productDetailsList = new ArrayList<ProductDetails>();
		for(int i= 0;i<productDetailsBOResponse.getProductDetails().size();i++) {
			OtsCustomerProduct customerProduct = new OtsCustomerProduct();			
			Map<String, Object> queryParameter = new HashMap<>();
			
			OtsUsers distributorId = new OtsUsers();
			distributorId.setOtsUsersId(Integer.parseInt(productDetailsBOResponse.getUserId()));
			queryParameter.put("distributorId",distributorId);
			
			OtsProduct otsProduct = new OtsProduct();
			otsProduct.setOtsProductId(Integer.parseInt(productDetailsBOResponse.getProductDetails().get(i).getProductId()));
			queryParameter.put("otsProductId",otsProduct);
			
			
			try {
				customerProduct  = super.getResultByNamedQuery("OtsCustomerProduct.getListOfCatAndSubCat", queryParameter);
				ProductDetails productDetails = convertCustomerProductTOProductDetails(customerProduct);
				productDetailsList.add(productDetails);
				
			}catch(Exception e) {
				System.out.println(e);
			}
		}
		productDetailsBOResponse.setProductDetails(productDetailsList);
		return productDetailsBOResponse;
	}
	
	ProductDetails convertCustomerProductTOProductDetails(OtsCustomerProduct customerProduct) {
		ProductDetails productDetails = new ProductDetails();
		productDetails.setProductId(customerProduct.getOtsProductId().getOtsProductId()==null?null:customerProduct.getOtsProductId().getOtsProductId().toString());
		productDetails.setProductName(customerProduct.getOtsProductId().getOtsProductName()==null?null:customerProduct.getOtsProductId().getOtsProductName());
		productDetails.setProductLevel(customerProduct.getOtsProductId().getOtsProductLevelId().getOtsProductName()==null?null:customerProduct.getOtsProductId().getOtsProductLevelId().getOtsProductName());
		productDetails.setProductPrice(customerProduct.getOtsProductId().getOtsProductPrice()==null?null:customerProduct.getOtsProductId().getOtsProductPrice().toString());
		productDetails.setProductImage(customerProduct.getOtsProductId().getOtsProductImage()==null?null:customerProduct.getOtsProductId().getOtsProductImage().toString());
		productDetails.setProductDescription(customerProduct.getOtsProductId().getOtsProductDescription()==null?null:customerProduct.getOtsProductId().getOtsProductDescription());
		productDetails.setProductStatus(customerProduct.getOtsProductId().getOtsProductStatus()==null?null:customerProduct.getOtsProductId().getOtsProductStatus());
		productDetails.setProductType(customerProduct.getOtsProductId().getOtsProductType()==null?null:customerProduct.getOtsProductId().getOtsProductType());
		return productDetails;
	}
	
}
