package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.io.IOException;
import java.math.BigDecimal;
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

import com.fuso.enterprise.ots.srv.api.model.domain.ProductCategoryProductMappingModel;
import com.fuso.enterprise.ots.srv.api.model.domain.ProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.UpdateProductStatusRequestModel;
import com.fuso.enterprise.ots.srv.api.service.request.AddProductCategoryAndProductRequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddorUpdateProductBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.ProductDetailsBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateProductStatusRequest;
import com.fuso.enterprise.ots.srv.api.service.response.ProductDetailsBOResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.server.dao.ProductServiceDAO;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsProduct;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsProductLevel;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUserMapping;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;
import com.fuso.enterprise.ots.srv.server.util.Base64UtilImage;
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
	            					OtsProductLevel productLevelId = new OtsProductLevel();
	            					productLevelId.setOtsProductLevelId(3);
	            					queryParameter.put("status", productDetailsBORequest.getRequestData().getStatus());
	            					queryParameter.put("otsProductLevelId", productLevelId);
					                productList  = super.getResultListByNamedQuery("OtsProduct.findAllProduct",queryParameter);
				                    break;
				                    
	            case "FirstLetter":
	            	                queryParameter.put("FirstLetter", "%"+seachValue+"%");
	            	                OtsProductLevel OtsProductLevel = new OtsProductLevel();
	            	                OtsProductLevel.setOtsProductLevelId(Integer.parseInt(productDetailsBORequest.getRequestData().getStatus()));
	            	                queryParameter.put("FirstLetter", "%"+seachValue+"%");
	            	                queryParameter.put("otsProductLevelId", OtsProductLevel);
	            	                
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
	public String addOrUpdateProduct(AddorUpdateProductBORequest addorUpdateProductBORequest) throws IOException {
		System.out.print("1");
		String responseData;
		try{
			OtsProduct otsProduct = new OtsProduct();
			if(addorUpdateProductBORequest.getRequestData().getProductPrice() !=null) {
				BigDecimal productPrice=new BigDecimal(addorUpdateProductBORequest.getRequestData().getProductPrice());
				otsProduct.setOtsProductPrice(productPrice);
			}
			
			if(addorUpdateProductBORequest.getRequestData().getProductId().isEmpty()||addorUpdateProductBORequest.getRequestData().getProductId().equalsIgnoreCase("string")) { 
				otsProduct.setOtsProductName(addorUpdateProductBORequest.getRequestData().getProductName());
				otsProduct.setOtsProductDescription(addorUpdateProductBORequest.getRequestData().getProductDescription());
				otsProduct.setOtsProductStatus(addorUpdateProductBORequest.getRequestData().getProductStatus());
				otsProduct.setOtsProductImage(addorUpdateProductBORequest.getRequestData().getProductImage());
				otsProduct.setOtsProductType(addorUpdateProductBORequest.getRequestData().getProductType());	
			}else{
				System.out.print("_____");
				otsProduct.setOtsProductId(Integer.parseInt(addorUpdateProductBORequest.getRequestData().getProductId()));
				otsProduct.setOtsProductName(addorUpdateProductBORequest.getRequestData().getProductName());
				otsProduct.setOtsProductDescription(addorUpdateProductBORequest.getRequestData().getProductDescription());
				otsProduct.setOtsProductStatus(addorUpdateProductBORequest.getRequestData().getProductStatus());
				otsProduct.setOtsProductImage(addorUpdateProductBORequest.getRequestData().getProductImage());
				otsProduct.setOtsProductType(addorUpdateProductBORequest.getRequestData().getProductType());
			}
			OtsProductLevel productLevel = new OtsProductLevel();
			productLevel.setOtsProductLevelId(Integer.parseInt(addorUpdateProductBORequest.getRequestData().getProductLevel()));
			otsProduct.setOtsProductLevelId(productLevel);
			try {
				otsProduct = super.getEntityManager().merge(otsProduct);
				if(addorUpdateProductBORequest.getRequestData().getProductImage() !=null) {
					String image = Base64UtilImage.convertBase64toImage(addorUpdateProductBORequest.getRequestData().getProductImage(),otsProduct.getOtsProductId());
					otsProduct.setOtsProductImage(image);
					super.getEntityManager().merge(otsProduct);
				}
				
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
		
		productDetails.setProductId(otsProduct.getOtsProductId()==null?null:otsProduct.getOtsProductId().toString());
		productDetails.setProductName(otsProduct.getOtsProductName()==null?null:otsProduct.getOtsProductName());
		productDetails.setProductDescription(otsProduct.getOtsProductDescription()==null?null:otsProduct.getOtsProductDescription());
		productDetails.setProductPrice(otsProduct.getOtsProductPrice()==null?null:otsProduct.getOtsProductPrice().toString());
		productDetails.setProductStatus(otsProduct.getOtsProductStatus()==null?null:otsProduct.getOtsProductStatus());
		productDetails.setProductImage(otsProduct.getOtsProductImage()==null?null:otsProduct.getOtsProductImage());
		productDetails.setProductType(otsProduct.getOtsProductType()==null?null:otsProduct.getOtsProductType());
		productDetails.setProductLevel(otsProduct.getOtsProductLevelId().getOtsProductName()==null?null:otsProduct.getOtsProductLevelId().getOtsProductName());
		return productDetails;
	}

	@Override
	public ProductDetails getProductDetils(String  productId) {
		ProductDetails productDetails = new ProductDetails();
		try {
			OtsProduct otsProduct = new OtsProduct();
			Map<String, Object> queryParameter = new HashMap<>();
			queryParameter.put("otsProductId", Integer.parseInt(productId));
			otsProduct = super.getResultByNamedQuery("OtsProduct.findByOtsProductId", queryParameter);
			productDetails = convertProductDetailsFromEntityToDomain(otsProduct);	
		}catch(Exception e) {
			System.out.print(e);
			return null;
		}
	return productDetails;
	}
	
	@Override
	public List<ProductDetails> getProductDetilswithStock(String  distributorIdValue) {
		System.out.println("Data1"+distributorIdValue);
		List<ProductDetails> productDetails = new ArrayList<ProductDetails>();
		List<OtsProduct> productList = new ArrayList<OtsProduct>();
		try {
			OtsUsers distributorId = new OtsUsers();
			distributorId.setOtsUsersId(Integer.parseInt(distributorIdValue));
			System.out.println("Data2");
			OtsProduct otsProduct = new OtsProduct();
			Map<String, Object> queryParameter = new HashMap<>();
			queryParameter.put("distributorId", distributorId);
			productList = super.getResultListByNamedQuery("OtsProduct.findByProductDetailsStock", queryParameter);
			productDetails =  productList.stream().map(tsProduct -> convertProductDetailsFromEntityToDomain(otsProduct)).collect(Collectors.toList());	
		}catch(Exception e) {
			return null;
		}
	return productDetails;
	}

	@Override
	public String UpdateProductStatus(UpdateProductStatusRequest updateProductStatusRequestModel) {
		try {
			Map<String, Object> queryParameter = new HashMap<>();
			OtsProduct otsProduct = new OtsProduct();
			queryParameter.put("otsProductId", Integer.parseInt(updateProductStatusRequestModel.getRequest().getProductId()));
			otsProduct  = super.getResultByNamedQuery("OtsProduct.findByOtsProductId", queryParameter);
			otsProduct.setOtsProductStatus(updateProductStatusRequestModel.getRequest().getStatus());
			return "updated";
		}catch(Exception e) {
			logger.error("Exception while Inserting data to DB  :"+e.getMessage());
	    	e.printStackTrace();
	        throw new BusinessException(e.getMessage(), e);
		}
		
	}

	@Override
	public ProductDetailsBOResponse getProductCategory(ProductDetailsBORequest productDetailsBORequest) {
		ProductDetailsBOResponse productDetailsBOResponse = new ProductDetailsBOResponse();
		List<ProductCategoryProductMappingModel> productMappingModel = new ArrayList<ProductCategoryProductMappingModel>();
		List<ProductDetails> productDetails = new ArrayList<ProductDetails>();
		Map<String, Object> queryParameter = new HashMap<>();
		List<OtsProduct> productList = null;
		OtsProductLevel productLevel = new OtsProductLevel();
		try {
			productLevel.setOtsProductLevelId(Integer.parseInt(productDetailsBORequest.getRequestData().getSearchvalue()));
			queryParameter.put("otsProductLevelId",productLevel );
			productList  = super.getResultListByNamedQuery("OtsProduct.otsProductLevelId", queryParameter);
			productDetails =  productList.stream().map(otsProduct -> convertProductDetailsFromEntityToDomain(otsProduct)).collect(Collectors.toList());
			productDetailsBOResponse.setProductDetails(productDetails);
		}catch(Exception e) {
			System.out.print(e);
		}
		return productDetailsBOResponse;
	}

	@Override
	public AddProductCategoryAndProductRequest addProductAndCategory(
			AddProductCategoryAndProductRequest addProductAndCategoryRequest) {
		// To add category and sub category to productTable
		try {
			List<ProductDetails> productList = new ArrayList<ProductDetails>();
			for(int i = 0 ; i< addProductAndCategoryRequest.getRequestData().getProductDetails().size() ; i++ ) {
				ProductDetails productDetails;
				OtsProduct OtsProduct = new OtsProduct();
				OtsProduct.setOtsProductName(addProductAndCategoryRequest.getRequestData().getProductDetails().get(i).getProductName());
				
				OtsProductLevel productLevel = new OtsProductLevel();
				productLevel.setOtsProductLevelId(Integer.parseInt(addProductAndCategoryRequest.getRequestData().getProductDetails().get(i).getProductLevel()));
				OtsProduct.setOtsProductLevelId(productLevel);
				
				if(addProductAndCategoryRequest.getRequestData().getProductDetails().get(i).getProductPrice() !=null) {
					BigDecimal price= new BigDecimal(addProductAndCategoryRequest.getRequestData().getProductDetails().get(i).getProductPrice()); 
					OtsProduct.setOtsProductPrice(price);
				}	
				OtsProduct.setOtsProductDescription(addProductAndCategoryRequest.getRequestData().getProductDetails().get(i).getProductDescription());
				OtsProduct.setOtsProductStatus(addProductAndCategoryRequest.getRequestData().getProductDetails().get(i).getProductStatus());
				OtsProduct.setOtsProductType(addProductAndCategoryRequest.getRequestData().getProductDetails().get(i).getProductType());
				save(OtsProduct);
				super.getEntityManager().flush();
				System.out.print(addProductAndCategoryRequest.getRequestData().getKey());
				if(addProductAndCategoryRequest.getRequestData().getKey().equalsIgnoreCase("subAndProd")&&addProductAndCategoryRequest.getRequestData().getProductDetails().get(i).getProductImage()!=null) {
					String image = Base64UtilImage.convertBase64toImage(addProductAndCategoryRequest.getRequestData().getProductDetails().get(i).getProductImage(),OtsProduct.getOtsProductId());
					OtsProduct.setOtsProductImage(image);
					save(OtsProduct);
				}
				productDetails = convertProductDetailsFromEntityToDomain(OtsProduct); 
				productList.add(productDetails) ;
			}
			addProductAndCategoryRequest.getRequestData().setProductDetails(productList);
		}catch(Exception e) {
			System.out.println(e);
			throw new BusinessException(e.getMessage(), e);
		}
			
			
		return addProductAndCategoryRequest;
	}
}
