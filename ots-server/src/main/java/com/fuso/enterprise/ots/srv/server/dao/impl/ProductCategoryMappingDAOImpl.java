package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.fuso.enterprise.ots.srv.api.model.domain.ProductCategoryProductMappingModel;
import com.fuso.enterprise.ots.srv.api.model.domain.ProductDetails;
import com.fuso.enterprise.ots.srv.api.service.request.AddProductCategoryAndProductRequest;
import com.fuso.enterprise.ots.srv.api.service.request.ProductDetailsBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.ProductDetailsBOResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.server.dao.ProductCategoryMappingDAO;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsProduct;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsProductCategoryMapping;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;

import net.bytebuddy.implementation.bind.annotation.Super;

@Repository
public class ProductCategoryMappingDAOImpl extends AbstractIptDao<OtsProductCategoryMapping, String> implements ProductCategoryMappingDAO{

	public ProductCategoryMappingDAOImpl() {
		super(OtsProductCategoryMapping.class);
	}

	@Override
	public List<ProductCategoryProductMappingModel> getProductListByProductCategory(ProductDetailsBORequest productDetailsBORequest) {
		List<ProductCategoryProductMappingModel> productMappingModel = new ArrayList<ProductCategoryProductMappingModel>();
		try {
			Map<String, Object> queryParameter = new HashMap<>();
			List<OtsProductCategoryMapping> productCategoryProductMappingList;
			OtsProduct OtsProduct = new OtsProduct();
			OtsProduct.setOtsProductId(Integer.parseInt(productDetailsBORequest.getRequestData().getSearchvalue()));
			queryParameter.put("otsProductCategoryId",OtsProduct);
			productCategoryProductMappingList = super.getResultListByNamedQuery("getProductByMappingotsProductCategoryId.otsProductCategoryId", queryParameter);		
			//productDetails =  productList.stream().map(otsProduct -> convertProductDetailsFromEntityToDomain(otsProduct)).collect(Collectors.toList());
			productMappingModel = productCategoryProductMappingList.stream().map(OtsProductCategoryProductMapping -> convertEntityToModel(OtsProductCategoryProductMapping)).collect(Collectors.toList());
		}catch(Exception e) {
			System.out.print(e);
		}
		return productMappingModel;
	}
	
	public ProductCategoryProductMappingModel convertEntityToModel(OtsProductCategoryMapping productCategoryProductMapping) {
		ProductCategoryProductMappingModel productCategory = new ProductCategoryProductMappingModel();
		productCategory.setProductCategoryId(productCategoryProductMapping.getOtsProductCategoryId().getOtsProductId().toString());
		productCategory.setProductCategoryMappingId(productCategoryProductMapping.getOtsProductCategorytMappingId().toString());
		productCategory.setProductId(productCategoryProductMapping.getOtsProductId().getOtsProductId().toString());
		return productCategory;
	}

	@Override
	public AddProductCategoryAndProductRequest mapProductAndCategory(
			AddProductCategoryAndProductRequest addProductAndCategoryRequest) {
		try {
			OtsProductCategoryMapping OtsProductCategoryMapping = new OtsProductCategoryMapping();
			for(int i = 0;i<addProductAndCategoryRequest.getRequestData().getProductDetails().size();i++) {
				
				OtsProduct productId = new OtsProduct();
				productId.setOtsProductId(Integer.parseInt(addProductAndCategoryRequest.getRequestData().getProductDetails().get(i).getProductId()));	
				OtsProductCategoryMapping.setOtsProductId(productId);
			
				OtsProduct productCatId = new OtsProduct();
				productCatId.setOtsProductId(Integer.parseInt(addProductAndCategoryRequest.getRequestData().getProductCategoryId()));
				OtsProductCategoryMapping.setOtsProductCategoryId(productCatId);
				
				super.getEntityManager().merge(OtsProductCategoryMapping);
			}
		}catch(Exception e) {
			System.out.println(e);
			throw new BusinessException(e.getMessage(), e);
		}
		return addProductAndCategoryRequest;
	}
	
	
	@Override
	public ProductDetailsBOResponse getProductListBySubcategory(ProductDetailsBORequest productDetailsBORequest) {
		List<ProductCategoryProductMappingModel> productMappingModel = new ArrayList<ProductCategoryProductMappingModel>();
		ProductDetailsBOResponse productDetailsBOResponse = new ProductDetailsBOResponse();
		List<ProductDetails> productDetailsList = new ArrayList<ProductDetails>();
		try {
			Map<String, Object> queryParameter = new HashMap<>();
			List<OtsProductCategoryMapping> productCategoryProductMappingList;
			OtsProduct OtsProduct = new OtsProduct();
			OtsProduct.setOtsProductId(Integer.parseInt(productDetailsBORequest.getRequestData().getSearchvalue()));
			queryParameter.put("otsProductCategoryId",OtsProduct);
			productCategoryProductMappingList = super.getResultListByNamedQuery("getProductByMappingotsProductCategoryId.otsProductCategoryId", queryParameter);		
			//productDetails =  productList.stream().map(otsProduct -> convertProductDetailsFromEntityToDomain(otsProduct)).collect(Collectors.toList());
			
			productDetailsList = productCategoryProductMappingList.stream().map(OtsProductCategoryProductMapping -> convertCustomerProductTOProductDetails(OtsProductCategoryProductMapping)).collect(Collectors.toList());
			productDetailsBOResponse.setProductDetails(productDetailsList);
		}catch(Exception e) {
			System.out.print(e);
		}
		return productDetailsBOResponse;
	}
	
	ProductDetails convertCustomerProductTOProductDetails(OtsProductCategoryMapping customerProduct) {
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
