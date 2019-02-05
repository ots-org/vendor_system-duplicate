package com.fuso.enterprise.ots.srv.api.service.functional;

import com.fuso.enterprise.ots.srv.api.service.request.AddorUpdateProductBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.ProductDetailsBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.ProductDetailsBOResponse;

public interface OTSProductService {

	ProductDetailsBOResponse getProductList(ProductDetailsBORequest productDetailsBORequest);

	String addOrUpdateProduct(AddorUpdateProductBORequest addorUpdateProductBORequest);

}
