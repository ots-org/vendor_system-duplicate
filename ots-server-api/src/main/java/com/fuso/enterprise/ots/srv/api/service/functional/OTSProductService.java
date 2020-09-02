package com.fuso.enterprise.ots.srv.api.service.functional;

import java.util.List;

import com.fuso.enterprise.ots.srv.api.model.domain.AirTableModel;
import com.fuso.enterprise.ots.srv.api.service.request.AddProductCategoryAndProductRequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddProductStockBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddorUpdateProductBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.AirTableRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetProductDetailsForBillRequst;
import com.fuso.enterprise.ots.srv.api.service.request.GetProductStockListRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetProductStockRequest;
import com.fuso.enterprise.ots.srv.api.service.request.ProductBulkUploadRequest;
import com.fuso.enterprise.ots.srv.api.service.request.ProductDetailsBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateProductStatusRequest;
import com.fuso.enterprise.ots.srv.api.service.response.BillProductDetailsResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetProductBOStockResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetProductStockListBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.ProductDetailsBOResponse;

public interface OTSProductService {

	ProductDetailsBOResponse getProductList(ProductDetailsBORequest productDetailsBORequest);

	String addOrUpdateProduct(AddorUpdateProductBORequest addorUpdateProductBORequest);

	String addOrUpdateProductStock(AddProductStockBORequest addStockProductBORequest);
	
	GetProductStockListBOResponse getProductStockList(GetProductStockListRequest getProductStockListRequest);
	
	GetProductBOStockResponse getProductStockByUidAndPid(GetProductStockRequest getProductStockRequest );

	BillProductDetailsResponse getProductDetailsForBill(GetProductDetailsForBillRequst getProductDetailsForBillRequst);

	String UpdateProductStatus(UpdateProductStatusRequest updateProductStatusRequestModel);
	
	//ProductDetailsBOResponse productBulkUpload(String base64Excel);
	
	String addProductAndCategory(AddProductCategoryAndProductRequest addProductAndCategoryRequest);
	
	ProductDetailsBOResponse searchProduct();

	String productBulkUpload(ProductBulkUploadRequest base64Excel);
	
	String addAirTabelData(AirTableRequest airTableRequest);

	List<AirTableModel> airTabelCaluclation(GetProductStockListRequest todaysDate);
}
