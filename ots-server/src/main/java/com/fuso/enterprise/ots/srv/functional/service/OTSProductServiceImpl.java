package com.fuso.enterprise.ots.srv.functional.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fuso.enterprise.ots.srv.api.model.domain.ProductStockDetail;
import com.fuso.enterprise.ots.srv.api.service.functional.OTSProductService;
import com.fuso.enterprise.ots.srv.api.service.request.AddProductStockBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddorUpdateProductBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetProductStockListRequest;
import com.fuso.enterprise.ots.srv.api.service.request.ProductDetailsBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.GetProductStockListBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.ProductDetailsBOResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.server.dao.OrderDAO;
import com.fuso.enterprise.ots.srv.server.dao.OrderProductDAO;
import com.fuso.enterprise.ots.srv.server.dao.ProductServiceDAO;
import com.fuso.enterprise.ots.srv.server.dao.ProductStockDao;
import com.fuso.enterprise.ots.srv.server.dao.ProductStockHistoryDao;
import com.fuso.enterprise.ots.srv.server.dao.StockDistObDAO;
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
	
	@Inject
	public OTSProductServiceImpl(ProductServiceDAO productServiceDAO,ProductStockDao productStockDao,ProductStockHistoryDao productStockHistoryDao,StockDistObDAO stockDistObDAO,OrderDAO orderDAO,OrderProductDAO orderProductDAO) {
		this.productServiceDAO=productServiceDAO;
		this.productStockDao = productStockDao;
		this.productStockHistoryDao=productStockHistoryDao;
		this.stockDistObDAO=stockDistObDAO;
		this.orderDAO=orderDAO;
		this.orderProductDAO=orderProductDAO;
	}
	@Override
	public ProductDetailsBOResponse getProductList(ProductDetailsBORequest productDetailsBORequest) {
		ProductDetailsBOResponse productDetailsBOResponse = new ProductDetailsBOResponse();
		try {
			productDetailsBOResponse = productServiceDAO.getProductList(productDetailsBORequest);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return productDetailsBOResponse;
	}
	
	@Override
	public String addOrUpdateProduct(AddorUpdateProductBORequest addorUpdateProductBORequest) {
		String responseData;
		try {
			responseData = productServiceDAO.addOrUpdateProduct(addorUpdateProductBORequest);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return responseData;
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
			List<OtsStockDistOb> otsStockDistObList  = stockDistObDAO.fetchOpeningBalance(getProductStockListRequest);
			for (OtsStockDistOb otsStockDistOb: otsStockDistObList) {
				ProductStockDetail ProductStockDetail = new ProductStockDetail();
				ProductStockDetail.setProductName(otsStockDistOb.getOtsProductId().getOtsProductName());
				ProductStockDetail.setProductId(otsStockDistOb.getOtsProductId().getOtsProductId());
				ProductStockDetail.setOtsProductOpenBalance(Long.parseLong(otsStockDistOb.getOtsStockDistOpeningBalance()));
				ProductStockDetail.setOtsProductStockAddition(productStockHistoryDao.getOtsProductStockAddition(otsStockDistOb.getOtsProductId().getOtsProductId(),getProductStockListRequest));
				List<OtsOrder> orderList = orderDAO.getOrderList(otsStockDistOb.getOtsProductId().getOtsProductId(),getProductStockListRequest);
				ProductStockDetail.setOtsProductOrderDelivered(orderProductDAO.getListOfDeliverdQuantityOfDay(orderList,otsStockDistOb.getOtsProductId().getOtsProductId()));
				ProductStockDetailList.add(ProductStockDetail);
			}
		}catch(Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		getProductStockListBOResponse.setProductStockDetail(ProductStockDetailList);
		return getProductStockListBOResponse;
	}

}
