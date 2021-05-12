package com.fuso.enterprise.ots.srv.server.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.fuso.enterprise.ots.srv.api.model.domain.AirTableModel;
import com.fuso.enterprise.ots.srv.api.model.domain.ProductDetails;
import com.fuso.enterprise.ots.srv.api.service.request.AddProductCategoryAndProductRequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddorUpdateProductBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.ProductDetailsBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateProductStatusRequest;
import com.fuso.enterprise.ots.srv.api.service.response.ProductDetailsBOResponse;

public interface ProductServiceDAO {

	ProductDetailsBOResponse getProductList(ProductDetailsBORequest productDetailsBORequest);

	String addOrUpdateProduct(AddorUpdateProductBORequest addorUpdateProductBORequest) throws IOException;

	ProductDetails getProductDetils(String productId);

	List<ProductDetails> getProductDetilswithStock(String ditributorId);

	String UpdateProductStatus(UpdateProductStatusRequest updateProductStatusRequestModel);
	
	ProductDetailsBOResponse getProductCategory(ProductDetailsBORequest productDetailsBORequest);

	AddProductCategoryAndProductRequest addProductAndCategory(AddProductCategoryAndProductRequest addProductAndCategoryRequest);

	AirTableModel addProductAirTable(AirTableModel airTableModel);
	
	List<ProductDetails> getProductDetilsByName(String productName);

	List<ProductDetails> getProductDetilsByTransactionId(String productName);
	
	List<ProductDetails> getPaginatedProduct(ProductDetailsBORequest productDetailsBORequest);

	/*********shreekant****/
	List<ProductDetails>  getAllProductDetils();
	/*******************/
	
}
