package com.fuso.enterprise.ots.srv.functional.service;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fuso.enterprise.ots.srv.api.model.domain.CustomerProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.GetProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.GetProductRequestModel;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.ProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.ProductDetailsList;
import com.fuso.enterprise.ots.srv.api.model.domain.ProductStockDetail;
import com.fuso.enterprise.ots.srv.api.service.functional.OTSProductService;
import com.fuso.enterprise.ots.srv.api.service.request.AddProductStockBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddorUpdateProductBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetProductDetailsForBillRequst;
import com.fuso.enterprise.ots.srv.api.service.request.GetProductStockListRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetProductStockRequest;
import com.fuso.enterprise.ots.srv.api.service.request.ProductDetailsBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateProductStatusRequest;
import com.fuso.enterprise.ots.srv.api.service.response.BillProductDetailsResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetProductBOStockResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetProductStockListBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.ProductDetailsBOResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.server.dao.MapUserProductDAO;
import com.fuso.enterprise.ots.srv.server.dao.OrderDAO;
import com.fuso.enterprise.ots.srv.server.dao.OrderProductDAO;
import com.fuso.enterprise.ots.srv.server.dao.OrderServiceDAO;
import com.fuso.enterprise.ots.srv.server.dao.ProductServiceDAO;
import com.fuso.enterprise.ots.srv.server.dao.ProductStockDao;
import com.fuso.enterprise.ots.srv.server.dao.ProductStockHistoryDao;
import com.fuso.enterprise.ots.srv.server.dao.StockDistObDAO;
import com.fuso.enterprise.ots.srv.server.dao.UserServiceDAO;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsOrder;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsStockDistOb;

@Service
@Transactional
public class OTSProductServiceImpl implements OTSProductService {
	private ProductStockDao productStockDao;
	private ProductStockHistoryDao productStockHistoryDao;
	private ProductServiceDAO productServiceDAO;
	private StockDistObDAO stockDistObDAO;
	private OrderDAO orderDAO;
	private OrderProductDAO orderProductDAO;
	private UserServiceDAO userServiceDAO;
	private OrderServiceDAO orderServiceDAO;
	private MapUserProductDAO mapUserProductDAO;
	@Inject
	public OTSProductServiceImpl(ProductServiceDAO productServiceDAO,ProductStockDao productStockDao,ProductStockHistoryDao productStockHistoryDao,StockDistObDAO stockDistObDAO,OrderDAO orderDAO,OrderProductDAO orderProductDAO,UserServiceDAO userServiceDAO,OrderServiceDAO orderServiceDAO,MapUserProductDAO mapUserProductDAO) {
		this.productServiceDAO=productServiceDAO;
		this.productStockDao = productStockDao;
		this.productStockHistoryDao=productStockHistoryDao;
		this.stockDistObDAO=stockDistObDAO;
		this.orderDAO=orderDAO;
		this.orderProductDAO=orderProductDAO;
		this.userServiceDAO = userServiceDAO;
		this.orderServiceDAO = orderServiceDAO;
		this.mapUserProductDAO = mapUserProductDAO;
	}
	@Override
	public ProductDetailsBOResponse getProductList(ProductDetailsBORequest productDetailsBORequest) {
		ProductDetailsBOResponse productDetailsBOResponse = new ProductDetailsBOResponse();
		List<CustomerProductDetails> customerProductDetails = new ArrayList<CustomerProductDetails>();
		List<ProductDetails> productDetails = new ArrayList<ProductDetails>();
		List<GetProductBOStockResponse> productStockvalue = new ArrayList<GetProductBOStockResponse>();
		if(productDetailsBORequest.getRequestData().getSearchKey().equals("All")) {
			productDetailsBOResponse = productServiceDAO.getProductList(productDetailsBORequest);
		}else {
			try {
				customerProductDetails = mapUserProductDAO.getCustomerProductDetailsByCustomerId(productDetailsBORequest.getRequestData().getDistributorId());
				productStockvalue = productStockDao.getProductStockByUid(productDetailsBORequest.getRequestData().getDistributorId());

				if(productStockvalue!= null) {
					for(int i = 0;i<productStockvalue.size(); i++) {
						 productDetails.add(productServiceDAO.getProductDetils(productStockvalue.get(i).getProductId()));
					}
				}else {
					return null;
				}
			if(customerProductDetails!=null) {
				for(int i = 0 ;i<customerProductDetails.size();i++) {
					for(int j=0;j<productDetails.size();j++) {
						if(customerProductDetails.get(i).getCustomerProductId()==productDetails.get(j).getProductId()) {
							productDetails.get(j).setProductPrice(customerProductDetails.get(i).getProductPrice());
						}
					}
				}
			}
			productDetailsBOResponse.setProductDetails(productDetails);
			} catch (Exception e) {
				throw new BusinessException(e.getMessage(), e);
			}
		}
		return productDetailsBOResponse;
	}

	@Override
	public String addOrUpdateProduct(AddorUpdateProductBORequest addorUpdateProductBORequest) {
		String path;
		try {
			productServiceDAO.addOrUpdateProduct(addorUpdateProductBORequest);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return "Added / updated";
	}


	@Override
	public String addOrUpdateProductStock(AddProductStockBORequest addProductBORequest) {
		String strResponse = "";
		try {
			strResponse = productStockDao.addProductStock(addProductBORequest);
			productStockHistoryDao.addProductStockHistory(addProductBORequest);
		}catch(Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return strResponse;
	}

	@Override
	public GetProductStockListBOResponse getProductStockList(GetProductStockListRequest getProductStockListRequest) {
		GetProductStockListBOResponse getProductStockListBOResponse =new GetProductStockListBOResponse();
		List<ProductStockDetail>  ProductStockDetailList = new ArrayList<ProductStockDetail>();
		try {
			/**
			 * looping based on product list items
			 */
			ProductDetailsBORequest productDetailsBORequest = new ProductDetailsBORequest();
			GetProductDetails getProductDetails = new GetProductDetails();
			getProductDetails.setSearchKey("All");
			getProductDetails.setSearchvalue("");
			getProductDetails.setDistributorId(getProductStockListRequest.getRequestData().getUserId());
			productDetailsBORequest.setRequestData(getProductDetails);
			List<ProductDetails> otsProductList = getProductList(productDetailsBORequest).getProductDetails();

			for (ProductDetails productDetailItem: otsProductList) {
				ProductStockDetail ProductStockDetail = new ProductStockDetail();
				ProductStockDetail.setProductName(productDetailItem.getProductName());
				ProductStockDetail.setProductId(Long.parseLong(productDetailItem.getProductId()));

				/*
				 * fetching opening balance
				 */
				getProductStockListRequest.getRequestData().setProductId(productDetailItem.getProductId());
				List<OtsStockDistOb> otsStockDistObList  = stockDistObDAO.fetchOpeningBalance(getProductStockListRequest);
				if(otsStockDistObList.size()>0)
					ProductStockDetail.setOtsProductOpenBalance(Long.parseLong(otsStockDistObList.get(0).getOtsStockDistOpeningBalance()));
				else
					ProductStockDetail.setOtsProductOpenBalance(0);

				/*
				 * fetching stock added
				 */
				ProductStockDetail.setOtsProductStockAddition(productStockHistoryDao.getOtsProductStockAddition(Integer.parseInt(productDetailItem.getProductId()),getProductStockListRequest));

				/*
				 * fetch sold quantity
				 */
				List<OtsOrder> orderList = orderDAO.getOrderList(Integer.parseInt(productDetailItem.getProductId()),getProductStockListRequest);
				if(orderList.size()>0)
					ProductStockDetail.setOtsProductOrderDelivered(orderProductDAO.getListOfDeliverdQuantityOfDay(orderList,Integer.parseInt(productDetailItem.getProductId())));
				else
					ProductStockDetail.setOtsProductOrderDelivered(0);

				ProductStockDetailList.add(ProductStockDetail);
			}

		}catch(Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		getProductStockListBOResponse.setProductStockDetail(ProductStockDetailList);
		return getProductStockListBOResponse;
	}

	@Override
	public GetProductBOStockResponse getProductStockByUidAndPid(GetProductStockRequest getProductStockRequest) {
		try {
		GetProductBOStockResponse getProductBOStockResponse = new GetProductBOStockResponse();
		getProductBOStockResponse = productStockDao.getProductStockByUidAndPid(getProductStockRequest);
		return getProductBOStockResponse;
		}catch(Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
	}
	@Override
	public BillProductDetailsResponse getProductDetailsForBill(
			GetProductDetailsForBillRequst getProductDetailsForBillRequst) {
		try {
			BillProductDetailsResponse billProductDetailsResponse = new BillProductDetailsResponse();
			List<ProductDetailsList> ProductResponse = new ArrayList<ProductDetailsList>();
			List<OrderProductDetails> totalProductDetails = new ArrayList<OrderProductDetails>();
			int lastIndex = 0;
			int k = 0;
			int productListSize = 0;
			for(int i=0 ; i< getProductDetailsForBillRequst.getRequest().getOrderId().size() ; i++) {

				List<OrderProductDetails> ProductDetails = orderProductDAO.getProductListByOrderId(getProductDetailsForBillRequst.getRequest().getOrderId().get(i));
				productListSize = lastIndex + ProductDetails.size();
				for(int j=lastIndex ; j < productListSize; j++) {
					OrderProductDetails orderProductDetails = ProductDetails.get(j) ;
					totalProductDetails.add(j,orderProductDetails);
					lastIndex = lastIndex++;
					System.out.println("+++++"+totalProductDetails.get(j));
				}
			}
			ProductDetailsList productDetailsList;

			for(int i=0;i<totalProductDetails.size();i++) {
				if(totalProductDetails.get(i).getStatus()!="YES") {
				OrderDetails orderDetails = new OrderDetails();
				int totalProductPrice = 0;
				Integer TotalproductQty = 0;

				productDetailsList = new ProductDetailsList();

				productDetailsList.setProductId(totalProductDetails.get(i).getOtsProductId().toString());

				productDetailsList.setProductName(totalProductDetails.get(i).getProductName());

					for(int j = 0; j<totalProductDetails.size();j++) {

						if(totalProductDetails.get(i).getOtsProductId().equals(totalProductDetails.get(j).getOtsProductId())) {

							if(totalProductDetails.get(j).getStatus()!="YES") {

								totalProductDetails.get(j).setStatus("YES");

								productDetailsList.setProductPrice(totalProductDetails.get(i).getOtsOrderProductCost());

								TotalproductQty = TotalproductQty + (Integer.valueOf(totalProductDetails.get(j).getOtsDeliveredQty())) ;

								System.out.print("i"+i+"j"+j+"TotalproductQty"+TotalproductQty);

								productDetailsList.setProductqty(String.valueOf(TotalproductQty));

								totalProductPrice =(TotalproductQty * Integer.valueOf(totalProductDetails.get(j).getOtsOrderProductCost()));

								productDetailsList.setTotalProductPrice(String.valueOf(totalProductPrice));

								ProductResponse.add(k,productDetailsList);

							}
						}
					}
				}
			}
			Set<ProductDetailsList> set = new HashSet<>(ProductResponse);
			ProductResponse.clear();
			ProductResponse.addAll(set);

			OrderDetails orderDetails = orderServiceDAO.GetOrderDetailsByOrderId(getProductDetailsForBillRequst.getRequest().getOrderId().get(0));
			billProductDetailsResponse.setCustomerDetails(userServiceDAO.getUserDetails(Integer.valueOf(orderDetails.getCustomerId())));
			billProductDetailsResponse.setDistributorDetails(userServiceDAO.getUserDetails(Integer.valueOf(orderDetails.getDistributorId())));
			billProductDetailsResponse.setProductDeatils(ProductResponse);
			return billProductDetailsResponse;
		}catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
	}

	@Override
	public String UpdateProductStatus(UpdateProductStatusRequest updateProductStatusRequestModel) {
		String path;
		try {
			productServiceDAO.UpdateProductStatus(updateProductStatusRequestModel);

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return "Added / updated";
	}
}
