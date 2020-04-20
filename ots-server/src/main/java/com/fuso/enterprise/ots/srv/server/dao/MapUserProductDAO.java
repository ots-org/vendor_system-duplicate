package com.fuso.enterprise.ots.srv.server.dao;

import java.util.List;

import com.fuso.enterprise.ots.srv.api.model.domain.BalanceCan;
import com.fuso.enterprise.ots.srv.api.model.domain.CustomerProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.ProductDetails;
import com.fuso.enterprise.ots.srv.api.service.request.AddProductCategoryAndProductRequest;
import com.fuso.enterprise.ots.srv.api.service.request.CustomerProductDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.ProductDetailsBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.ProductDetailsBOResponse;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsCustomerProduct;

public interface MapUserProductDAO {

	String mapUserProduct(CustomerProductDataBORequest customerProductDataBORequest);

	String UpdateBySaleVocher(CustomerProductDataBORequest customerProductDataBORequest);

	CustomerProductDetails getCustomerProductDetailsByUserIdandProductId(String productId, String customerId);

	List<BalanceCan> getBalanceCanByUserId(String customerId);

	List<CustomerProductDetails> getCustomerProductDetailsByCustomerId(String customerId);

	String addProductAndCategory(AddProductCategoryAndProductRequest addProductAndCategoryRequest);
	
	ProductDetailsBOResponse getProductDetailsForDistributor(ProductDetailsBOResponse productDetailsBOResponse);
}
