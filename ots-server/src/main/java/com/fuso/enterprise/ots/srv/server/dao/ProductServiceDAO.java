package com.fuso.enterprise.ots.srv.server.dao;

import java.util.List;

import com.fuso.enterprise.ots.srv.api.model.domain.ProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.UpdateProductStatusRequestModel;
import com.fuso.enterprise.ots.srv.api.service.request.AddorUpdateProductBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.ProductDetailsBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateOrderStatusRequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateProductStatusRequest;
import com.fuso.enterprise.ots.srv.api.service.response.ProductDetailsBOResponse;

public interface ProductServiceDAO {

	ProductDetailsBOResponse getProductList(ProductDetailsBORequest productDetailsBORequest);

	String addOrUpdateProduct(AddorUpdateProductBORequest addorUpdateProductBORequest);

	ProductDetails getProductDetils(String productId);

	List<ProductDetails> getProductDetilswithStock(String ditributorId);

	String UpdateProductStatus(UpdateProductStatusRequest updateProductStatusRequestModel);
}
