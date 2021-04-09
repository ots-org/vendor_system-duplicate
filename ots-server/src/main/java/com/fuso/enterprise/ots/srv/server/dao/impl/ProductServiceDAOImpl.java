package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Types;
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
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.fuso.enterprise.ots.srv.api.model.domain.AirTableModel;
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
	            					queryParameter.put("status",productDetailsBORequest.getRequestData().getStatus());
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
			
			if(!(addorUpdateProductBORequest.getRequestData().getProductId().isEmpty()||addorUpdateProductBORequest.getRequestData().getProductId().equalsIgnoreCase("string"))) { 
				otsProduct.setOtsProductId(Integer.parseInt(addorUpdateProductBORequest.getRequestData().getProductId()));
			}
			
			otsProduct.setOtsProductBasePrice(addorUpdateProductBORequest.getRequestData().getProductBasePrice());
			otsProduct.setOtsProductGst(addorUpdateProductBORequest.getRequestData().getGst());
			otsProduct.setOtsProductName(addorUpdateProductBORequest.getRequestData().getProductName());
			otsProduct.setOtsProductDescription(addorUpdateProductBORequest.getRequestData().getProductDescription());
			otsProduct.setOtsProductStatus(addorUpdateProductBORequest.getRequestData().getProductStatus());
			otsProduct.setOtsProductImage(addorUpdateProductBORequest.getRequestData().getProductImage());
			otsProduct.setOtsProductType(addorUpdateProductBORequest.getRequestData().getProductType());
			otsProduct.setOtsProductThresholdDay(addorUpdateProductBORequest.getRequestData().getThreshHold());
		
			otsProduct.setOtsProductImage(addorUpdateProductBORequest.getRequestData().getProductImage());
			otsProduct.setOtsMultiProductImage1(addorUpdateProductBORequest.getRequestData().getMultiProductImage1());
			otsProduct.setOtsMultiProductImage2(addorUpdateProductBORequest.getRequestData().getMultiProductImage2());
			otsProduct.setOtsMultiProductImage3(addorUpdateProductBORequest.getRequestData().getMultiProductImage3());
			otsProduct.setOtsMultiProductImage4(addorUpdateProductBORequest.getRequestData().getMultiProductImage4());
			otsProduct.setOtsMultiProductImage5(addorUpdateProductBORequest.getRequestData().getMultiProductImage5());
			otsProduct.setOtsMultiProductImage6(addorUpdateProductBORequest.getRequestData().getMultiProductImage6());
			otsProduct.setOtsMultiProductImage7(addorUpdateProductBORequest.getRequestData().getMultiProductImage7());
			otsProduct.setOtsMultiProductImage8(addorUpdateProductBORequest.getRequestData().getMultiProductImage8());
			otsProduct.setOtsMultiProductImage9(addorUpdateProductBORequest.getRequestData().getMultiProductImage9());
			otsProduct.setOtsMultiProductImage10(addorUpdateProductBORequest.getRequestData().getMultiProductImage10());
			
			OtsProductLevel productLevel = new OtsProductLevel();
			productLevel.setOtsProductLevelId(Integer.parseInt(addorUpdateProductBORequest.getRequestData().getProductLevel()));
			otsProduct.setOtsProductLevelId(productLevel);
			
			try {
				otsProduct = super.getEntityManager().merge(otsProduct);
//				if(addorUpdateProductBORequest.getRequestData().getProductImage() !=null) {
//					String image = Base64UtilImage.convertBase64toImage(addorUpdateProductBORequest.getRequestData().getProductImage(),otsProduct.getOtsProductId());
//					otsProduct.setOtsProductImage(image);
//					super.getEntityManager().merge(otsProduct);
//				}
				
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
	
	private ProductDetails convertProductDetailsFromEntityToDomainWithRating(OtsProduct otsProduct,List<Map<String, Object>> productReviewAndRating) {
		ProductDetails productDetails = new ProductDetails();
		productDetails.setProductId(otsProduct.getOtsProductId()==null?null:otsProduct.getOtsProductId().toString());
		productDetails.setProductName(otsProduct.getOtsProductName()==null?null:otsProduct.getOtsProductName());
		productDetails.setProductDescription(otsProduct.getOtsProductDescription()==null?null:otsProduct.getOtsProductDescription());
		productDetails.setProductPrice(otsProduct.getOtsProductPrice()==null?null:otsProduct.getOtsProductPrice().toString());
		productDetails.setProductStatus(otsProduct.getOtsProductStatus()==null?null:otsProduct.getOtsProductStatus());
		productDetails.setProductLevel("3");
		productDetails.setProductImage(otsProduct.getOtsProductImage()==null?null:otsProduct.getOtsProductImage());
		productDetails.setProductType(otsProduct.getOtsProductType()==null?null:otsProduct.getOtsProductType());
		productDetails.setProductLevel(otsProduct.getOtsProductLevelId().getOtsProductName()==null?null:otsProduct.getOtsProductLevelId().getOtsProductName());
		productDetails.setGst(otsProduct.getOtsProductGst()==null?"0":otsProduct.getOtsProductGst());
		productDetails.setThreshHold(otsProduct.getOtsProductThresholdDay()==null?null:otsProduct.getOtsProductThresholdDay());
	//	productDetails.setDistributorId(otsProduct.getOtsDistributorId()==null?null:otsProduct.getOtsDistributorId().getOtsUsersId().toString());
		productDetails.setProductBasePrice(otsProduct.getOtsProductBasePrice());
		productDetails.setMultiProductImage1(otsProduct.getOtsMultiProductImage1()==null?null:otsProduct.getOtsMultiProductImage1());
		productDetails.setMultiProductImage2(otsProduct.getOtsMultiProductImage2()==null?null:otsProduct.getOtsMultiProductImage2());
		productDetails.setMultiProductImage3(otsProduct.getOtsMultiProductImage3()==null?null:otsProduct.getOtsMultiProductImage3());
		productDetails.setMultiProductImage4(otsProduct.getOtsMultiProductImage4()==null?null:otsProduct.getOtsMultiProductImage4());
		productDetails.setMultiProductImage5(otsProduct.getOtsMultiProductImage5()==null?null:otsProduct.getOtsMultiProductImage5());
		productDetails.setMultiProductImage6(otsProduct.getOtsMultiProductImage6()==null?null:otsProduct.getOtsMultiProductImage6());
		productDetails.setMultiProductImage7(otsProduct.getOtsMultiProductImage7()==null?null:otsProduct.getOtsMultiProductImage7());
		productDetails.setMultiProductImage8(otsProduct.getOtsMultiProductImage8()==null?null:otsProduct.getOtsMultiProductImage8());
		productDetails.setMultiProductImage9(otsProduct.getOtsMultiProductImage9()==null?null:otsProduct.getOtsMultiProductImage9());
		productDetails.setMultiProductImage10(otsProduct.getOtsMultiProductImage10()==null?null:otsProduct.getOtsMultiProductImage10());
		productDetails.setProductRating(otsProduct.getOtsProductRating()==null?null:otsProduct.getOtsProductRating().toString());
		productDetails.setProductRatingCount(otsProduct.getOtsProductRatingCount()==null?null:otsProduct.getOtsProductRatingCount().toString());
		productDetails.setRatingAndReview(productReviewAndRating);
		
		return productDetails;
	}

	
	private ProductDetails convertProductDetailsFromEntityToDomain(OtsProduct otsProduct) {
		ProductDetails productDetails = new ProductDetails();
		productDetails.setProductId(otsProduct.getOtsProductId()==null?null:otsProduct.getOtsProductId().toString());
		productDetails.setProductName(otsProduct.getOtsProductName()==null?null:otsProduct.getOtsProductName());
		productDetails.setProductDescription(otsProduct.getOtsProductDescription()==null?null:otsProduct.getOtsProductDescription());
		productDetails.setProductPrice(otsProduct.getOtsProductPrice()==null?null:otsProduct.getOtsProductPrice().toString());
		productDetails.setProductStatus(otsProduct.getOtsProductStatus()==null?null:otsProduct.getOtsProductStatus());
		productDetails.setProductLevel("3");
		productDetails.setProductImage(otsProduct.getOtsProductImage()==null?null:otsProduct.getOtsProductImage());
		productDetails.setProductType(otsProduct.getOtsProductType()==null?null:otsProduct.getOtsProductType());
		productDetails.setProductLevel(otsProduct.getOtsProductLevelId().getOtsProductName()==null?null:otsProduct.getOtsProductLevelId().getOtsProductName());
		productDetails.setGst(otsProduct.getOtsProductGst()==null?"0":otsProduct.getOtsProductGst());
		productDetails.setThreshHold(otsProduct.getOtsProductThresholdDay()==null?null:otsProduct.getOtsProductThresholdDay());
	//	productDetails.setDistributorId(otsProduct.getOtsDistributorId()==null?null:otsProduct.getOtsDistributorId().getOtsUsersId().toString());
		productDetails.setProductBasePrice(otsProduct.getOtsProductBasePrice());
		productDetails.setMultiProductImage1(otsProduct.getOtsMultiProductImage1()==null?null:otsProduct.getOtsMultiProductImage1());
		productDetails.setMultiProductImage2(otsProduct.getOtsMultiProductImage2()==null?null:otsProduct.getOtsMultiProductImage2());
		productDetails.setMultiProductImage3(otsProduct.getOtsMultiProductImage3()==null?null:otsProduct.getOtsMultiProductImage3());
		productDetails.setMultiProductImage4(otsProduct.getOtsMultiProductImage4()==null?null:otsProduct.getOtsMultiProductImage4());
		productDetails.setMultiProductImage5(otsProduct.getOtsMultiProductImage5()==null?null:otsProduct.getOtsMultiProductImage5());
		productDetails.setMultiProductImage6(otsProduct.getOtsMultiProductImage6()==null?null:otsProduct.getOtsMultiProductImage6());
		productDetails.setMultiProductImage7(otsProduct.getOtsMultiProductImage7()==null?null:otsProduct.getOtsMultiProductImage7());
		productDetails.setMultiProductImage8(otsProduct.getOtsMultiProductImage8()==null?null:otsProduct.getOtsMultiProductImage8());
		productDetails.setMultiProductImage9(otsProduct.getOtsMultiProductImage9()==null?null:otsProduct.getOtsMultiProductImage9());
		productDetails.setMultiProductImage10(otsProduct.getOtsMultiProductImage10()==null?null:otsProduct.getOtsMultiProductImage10());
		productDetails.setProductRating(otsProduct.getOtsProductRating()==null?null:otsProduct.getOtsProductRating().toString());
		productDetails.setProductRatingCount(otsProduct.getOtsProductRatingCount()==null?null:otsProduct.getOtsProductRatingCount().toString());
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
					OtsProduct.setOtsProductGst(addProductAndCategoryRequest.getRequestData().getProductDetails().get(i).getGst());
					OtsProduct.setOtsProductBasePrice(addProductAndCategoryRequest.getRequestData().getProductDetails().get(i).getProductBasePrice());
				}	
				OtsProduct.setOtsProductDescription(addProductAndCategoryRequest.getRequestData().getProductDetails().get(i).getProductDescription());
				OtsProduct.setOtsProductStatus(addProductAndCategoryRequest.getRequestData().getProductDetails().get(i).getProductStatus());
				OtsProduct.setOtsProductType(addProductAndCategoryRequest.getRequestData().getProductDetails().get(i).getProductType());
				
				OtsProduct.setOtsProductImage("https://i.ibb.co/y8kyL72/1-03-1.png");
				OtsProduct.setOtsMultiProductImage1(addProductAndCategoryRequest.getRequestData().getProductDetails().get(i).getMultiProductImage1());
				OtsProduct.setOtsMultiProductImage2(addProductAndCategoryRequest.getRequestData().getProductDetails().get(i).getMultiProductImage2());
				OtsProduct.setOtsMultiProductImage3(addProductAndCategoryRequest.getRequestData().getProductDetails().get(i).getMultiProductImage3());
				OtsProduct.setOtsMultiProductImage4(addProductAndCategoryRequest.getRequestData().getProductDetails().get(i).getMultiProductImage4());
				OtsProduct.setOtsMultiProductImage5(addProductAndCategoryRequest.getRequestData().getProductDetails().get(i).getMultiProductImage5());
				OtsProduct.setOtsMultiProductImage6(addProductAndCategoryRequest.getRequestData().getProductDetails().get(i).getMultiProductImage6());
				OtsProduct.setOtsMultiProductImage7(addProductAndCategoryRequest.getRequestData().getProductDetails().get(i).getMultiProductImage7());
				OtsProduct.setOtsMultiProductImage8(addProductAndCategoryRequest.getRequestData().getProductDetails().get(i).getMultiProductImage8());
				OtsProduct.setOtsMultiProductImage9(addProductAndCategoryRequest.getRequestData().getProductDetails().get(i).getMultiProductImage9());
				OtsProduct.setOtsMultiProductImage10(addProductAndCategoryRequest.getRequestData().getProductDetails().get(i).getMultiProductImage10());
				
				save(OtsProduct);
				super.getEntityManager().flush();
				System.out.print(addProductAndCategoryRequest.getRequestData().getKey());
//				if(addProductAndCategoryRequest.getRequestData().getKey().equalsIgnoreCase("subAndProd")&&addProductAndCategoryRequest.getRequestData().getProductDetails().get(i).getProductImage()!=null) {
//					String image = Base64UtilImage.convertBase64toImage(addProductAndCategoryRequest.getRequestData().getProductDetails().get(i).getProductImage(),OtsProduct.getOtsProductId());
//					OtsProduct.setOtsProductImage(image);
//					save(OtsProduct);
//				}
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

	@Override
	public AirTableModel addProductAirTable(AirTableModel airTableModel) {
		OtsProduct productCat = new OtsProduct();
		OtsProduct productSubCat = new OtsProduct();
		//------------------ to add category 
		try {
			Map<String, Object> queryParameter = new HashMap<>();
			queryParameter.put("otsProductAirtableId",airTableModel.getProductCategoryId());
			productCat  = super.getResultByNamedQuery("OtsProduct.findByOtsProductAirtableId", queryParameter);
			airTableModel.setNewCategoryId(productCat.getOtsProductId().toString());
			airTableModel.setProductCategoryname("yes");
		}catch(Exception e) {
	//		System.out.println("Erorr");
			airTableModel.setTempLevelId("1");
			airTableModel.setTransactionId(airTableModel.getProductCategoryId());
			productCat = convertAirTableToCategorySubCategoryAndProduct(airTableModel);
			productCat = saveOrUpdate(productCat);
			airTableModel.setNewCategoryId(productCat.getOtsProductId().toString());
		}
		//------------------ to add sub category 
		try {
			Map<String, Object> queryParameter = new HashMap<>();
			queryParameter.put("otsProductAirtableId",airTableModel.getProductSubCategoryId());
			productSubCat = super.getResultByNamedQuery("OtsProduct.findByOtsProductAirtableId", queryParameter);
			airTableModel.setProductSubCategoryName("yes");
			airTableModel.setProductSubCategoryId(productSubCat.getOtsProductId().toString());
		}catch(Exception e) {
	//		System.out.println("Erorr");
			airTableModel.setTempLevelId("2");
			airTableModel.setTransactionId(airTableModel.getProductSubCategoryId());
			productSubCat = convertAirTableToCategorySubCategoryAndProduct(airTableModel);
			productSubCat.setOtsProductName(airTableModel.getProductSubCategoryName());
			productSubCat = saveOrUpdate(productSubCat);
			airTableModel.setProductSubCategoryId(productSubCat.getOtsProductId().toString());
		}
		//------------------ to add product ------------------------------------------
		
		try {
			List<OtsProduct> productList = new ArrayList<OtsProduct>();
			Map<String, Object> queryParameter = new HashMap<>();
			queryParameter.put("otsProductAirtableId",airTableModel.getProductId());
			productList = super.getResultListByNamedQuery("OtsProduct.findByOtsProductAirtableId", queryParameter);
			airTableModel.setProductId(productList.get(0).getOtsProductId().toString());
			airTableModel.setPreviousDayFlag("yes");
		}catch(Exception e) {
			OtsProduct product = new OtsProduct();
		//	System.out.print(e);
			airTableModel.setTempLevelId("3");
		//	System.out.println("Erorr");
			product.setOtsProductName(airTableModel.getProductName());
			product = convertAirTableToCategorySubCategoryAndProduct(airTableModel);
			product.setOtsProductAirtableId(airTableModel.getProductId());
			product = saveOrUpdate(product);
			airTableModel.setProductId(product.getOtsProductId().toString());
			airTableModel.setPreviousDayFlag("no");
		}
		return airTableModel;
	}
	
	OtsProduct convertAirTableToCategorySubCategoryAndProduct(AirTableModel airTableModel){
			OtsProduct product = new OtsProduct();
			product.setOtsProductName(airTableModel.getProductName());
		//	product.setOtsProductDescription();
			product.setOtsProductStatus("active");
			Float totalprice = Float.parseFloat(airTableModel.getProductGst()) * Float.parseFloat(airTableModel.getProductPrice()) +  Float.parseFloat(airTableModel.getProductPrice());
			BigDecimal price= new BigDecimal(totalprice); 
			product.setOtsProductPrice(price);
			product.setOtsProductImage(airTableModel.getProductImage());
			OtsProductLevel otsProductLevelId = new OtsProductLevel();
			otsProductLevelId.setOtsProductLevelId(Integer.parseInt(airTableModel.getTempLevelId()));
			product.setOtsProductLevelId(otsProductLevelId);
			//product.setOtsProductThresholdDay(otsProductLevelId);
			product.setOtsProductBasePrice(airTableModel.getProductPrice());
			product.setOtsProductGst(airTableModel.getProductGst());
			product.setOtsProductProducerName(airTableModel.getProductProducerName());
			product.setOtsProductAirtableId(airTableModel.getTransactionId());
			if(airTableModel.getTempLevelId().equalsIgnoreCase("2")) {
				product.setOtsProductName(airTableModel.getProductSubCategoryName());
			}else if (airTableModel.getTempLevelId().equalsIgnoreCase("1")) {
				product.setOtsProductName(airTableModel.getProductCategoryname());
			}
			return product;
		}

	@Override
	public List<ProductDetails> getProductDetilsByName(String productName) {
		try {
			List<ProductDetails> productDetails = new ArrayList<ProductDetails>();
			List<OtsProduct> productList = new ArrayList<OtsProduct>();
			Map<String, Object> queryParameter = new HashMap<>();
			queryParameter.put("otsProductName",productName);
			productList = super.getResultListByNamedQuery("OtsProduct.findByOtsProductName", queryParameter);
			productDetails =  productList.stream().map(OtsProduct -> convertProductDetailsFromEntityToDomain(OtsProduct)).collect(Collectors.toList());	
			System.out.println(productDetails.get(0).getProductId());
			return productDetails;
		}catch(Exception e) {
			System.out.print(e);
		}
		return null;
		
	}
	
	@Override
	public List<ProductDetails> getProductDetilsByTransactionId(String transactionId) {
		try {
			List<ProductDetails> productDetails = new ArrayList<ProductDetails>();
			List<OtsProduct> productList = new ArrayList<OtsProduct>();
			Map<String, Object> queryParameter = new HashMap<>();
			queryParameter.put("otsProductAirtableId",transactionId);
			productList = super.getResultListByNamedQuery("OtsProduct.findByOtsProductAirtableId", queryParameter);
			productDetails =  productList.stream().map(OtsProduct -> convertProductDetailsFromEntityToDomain(OtsProduct)).collect(Collectors.toList());
			System.out.println(productDetails.get(0).getProductId());
			return productDetails;
		}catch(Exception e) {
		//	System.out.print(e);
		}
		return null;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductDetails> getPaginatedProduct(ProductDetailsBORequest productDetailsBORequest) {
		List<ProductDetails> productList = new ArrayList<ProductDetails>();
		try {
			Map<String, Object> inParamMap = new HashMap<String, Object>();				
			
			inParamMap.put("starton", productDetailsBORequest.getRequestData().getStartOn());
			inParamMap.put("bsize", productDetailsBORequest.getRequestData().getSize());
			inParamMap.put("productLevel", productDetailsBORequest.getRequestData().getProductLevel());
			inParamMap.put("productcategoryid", productDetailsBORequest.getRequestData().getSearchvalue());
			inParamMap.put("productstatus", productDetailsBORequest.getRequestData().getProcedureKey());
			
			SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
					.withProcedureName("getproductpagination")
					.withoutProcedureColumnMetaDataAccess();
			
			simpleJdbcCall.addDeclaredParameter(new SqlParameter("starton", Types.INTEGER));
			simpleJdbcCall.addDeclaredParameter(new SqlParameter("bsize", Types.INTEGER));
			simpleJdbcCall.addDeclaredParameter(new SqlParameter("productLevel", Types.INTEGER));
			simpleJdbcCall.addDeclaredParameter(new SqlParameter("productcategoryid", Types.INTEGER));
			simpleJdbcCall.addDeclaredParameter(new SqlParameter("productstatus", Types.VARCHAR));
			
			Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(inParamMap);
			List<Map<String, Object>> out = (List<Map<String, Object>>) simpleJdbcCallResult.get("#result-set-1");
			System.out.println(out);
			for(int i=0;i<out.size();i++) {
				OtsProduct otsProduct = new OtsProduct();
				otsProduct.setOtsProductId(Integer.parseInt(out.get(i).get("ots_product_id").toString()));
				otsProduct.setOtsProductName(out.get(i).get("ots_product_name")==null?"":out.get(i).get("ots_product_name").toString());
				otsProduct.setOtsProductDescription(out.get(0).get("ots_product_description")==null?"":out.get(i).get("ots_product_description").toString());
				if(out.get(i).get("ots_product_price")!=null) {
					BigDecimal productPrice = new BigDecimal(out.get(i).get("ots_product_price").toString());
					otsProduct.setOtsProductPrice(productPrice);
				}else {
					BigDecimal productPrice = new BigDecimal(0);
					otsProduct.setOtsProductPrice(productPrice);
				}
				OtsProductLevel otsProductLevel = new OtsProductLevel();
				otsProductLevel.setOtsProductName("");
				otsProduct.setOtsProductLevelId(otsProductLevel);
				otsProduct.setOtsProductStatus(out.get(i).get("ots_product_status")==null?"":out.get(i).get("ots_product_status").toString());
				otsProduct.setOtsProductImage(out.get(i).get("ots_product_image")==null?"":out.get(i).get("ots_product_image").toString());
				otsProduct.setOtsProductType(out.get(i).get("ots_product_type")==null?"":out.get(i).get("ots_product_type").toString());
				otsProduct.setOtsProductGst(out.get(i).get("ots_product_gst")==null?"":out.get(i).get("ots_product_gst").toString());
				otsProduct.setOtsProductThresholdDay(out.get(i).get("ots_product_threshold_day")==null?"":out.get(i).get("ots_product_threshold_day").toString());
				otsProduct.setOtsProductBasePrice(out.get(i).get("ots_product_base_price")==null?"":out.get(i).get("ots_product_base_price").toString());
				otsProduct.setOtsProductRatingCount(out.get(i).get("ots_product_rating_count")==null?0:Integer.parseInt(out.get(i).get("ots_product_rating_count").toString()));
				otsProduct.setOtsProductRating(out.get(i).get("ots_product_rating")==null?0:Float.parseFloat(out.get(i).get("ots_product_rating").toString()));
				//------------------------GET DYNAMIC ATTRIBUTE FOR A PRODUCT-------------------------------------------------
				
				Map<String, Object> inParamMapForDynamicProductAttribute = new HashMap<String, Object>();
				inParamMapForDynamicProductAttribute.put("productId", Integer.parseInt(out.get(i).get("ots_product_id").toString()));
				
				SimpleJdbcCall simpleJdbcCallForProductAttribute = new SimpleJdbcCall(jdbcTemplate)
						.withProcedureName("getEachProductReviewAndRating")
						.withoutProcedureColumnMetaDataAccess();
				
				simpleJdbcCallForProductAttribute.addDeclaredParameter(new SqlParameter("productId", Types.INTEGER));
				
				Map<String, Object> simpleJdbcCallResultForProductAttribute = simpleJdbcCallForProductAttribute.execute(inParamMapForDynamicProductAttribute);
				List<Map<String, Object>> productRating = (List<Map<String, Object>>) simpleJdbcCallResultForProductAttribute.get("#result-set-1");
				
				//------------------------END OF GET DYNAMIC ATTRIBUTE FOR A PRODUCT------------------------------------------
				
				productList.add(convertProductDetailsFromEntityToDomainWithRating(otsProduct,productRating));
			}
		}catch(Exception e) {
				System.out.print(e);
		}
		return productList;
	}

	//shreekant
		@Override
		public List<ProductDetails> getAllProductDetils() {
			List<ProductDetails> productDetails = new ArrayList<ProductDetails>();
			try {
				List<OtsProduct> productList = new ArrayList<OtsProduct>();
				Map<String, Object> queryParameter = new HashMap<>();
				OtsProductLevel productLevelId = new OtsProductLevel();
				productLevelId.setOtsProductLevelId(3);
				queryParameter.put("status","active");
				queryParameter.put("otsProductLevelId", productLevelId);
               // productList  = super.getResultListByNamedQuery("OtsProduct.findAllProduct",queryParameter);
				productList = super.getResultListByNamedQuery("OtsProduct.findAllProduct", queryParameter);
			//	productDetails = convertProductDetailsFromEntityToDomain(otsProduct);
				productDetails =  productList.stream().map(otsProduct -> convertProductDetailsFromEntityToDomain(otsProduct)).collect(Collectors.toList());
				System.out.println(productDetails);
			}catch(Exception e) {
				System.out.print(e);
				return null;
			}
		return productDetails;
		}
		/***************************/
		
		
}
