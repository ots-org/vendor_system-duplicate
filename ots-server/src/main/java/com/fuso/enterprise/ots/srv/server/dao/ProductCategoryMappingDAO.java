package com.fuso.enterprise.ots.srv.server.dao;

import java.util.List;

import com.fuso.enterprise.ots.srv.api.model.domain.ProductCategoryProductMappingModel;
import com.fuso.enterprise.ots.srv.api.model.domain.ProductDetails;
import com.fuso.enterprise.ots.srv.api.service.request.AddProductCategoryAndProductRequest;
import com.fuso.enterprise.ots.srv.api.service.request.ProductDetailsBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.ProductDetailsBOResponse;

public interface ProductCategoryMappingDAO {
	
	List<ProductCategoryProductMappingModel> getProductListByProductCategory(ProductDetailsBORequest productDetailsBORequest);

	AddProductCategoryAndProductRequest mapProductAndCategory(AddProductCategoryAndProductRequest addProductAndCategoryRequest);

	ProductDetailsBOResponse getProductListBySubcategory(ProductDetailsBORequest productDetailsBORequest);
}
