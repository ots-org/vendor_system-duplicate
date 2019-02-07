package com.fuso.enterprise.ots.srv.functional.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fuso.enterprise.ots.srv.api.service.functional.OTSProductService;
import com.fuso.enterprise.ots.srv.api.service.request.AddProductStockBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddorUpdateProductBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.ProductDetailsBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.ProductDetailsBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.UserDataBOResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.server.dao.MapUserProductDAO;
import com.fuso.enterprise.ots.srv.server.dao.ProductServiceDAO;
import com.fuso.enterprise.ots.srv.server.dao.ProductStockDao;
import com.fuso.enterprise.ots.srv.server.dao.ProductStockHistoryDao;
import com.fuso.enterprise.ots.srv.server.dao.UserMapDAO;
import com.fuso.enterprise.ots.srv.server.dao.UserRegistrationDao;
import com.fuso.enterprise.ots.srv.server.dao.UserServiceDAO;
import com.fuso.enterprise.ots.srv.server.dao.UserServiceUtilityDAO;

@Service
@Transactional
public class OTSProductServiceImpl implements OTSProductService {
	private ProductStockDao productStockDao;  
	private ProductStockHistoryDao productStockHistoryDao;
	private ProductServiceDAO productServiceDAO;
	
	@Inject
	public OTSProductServiceImpl(ProductServiceDAO productServiceDAO,ProductStockDao productStockDao,ProductStockHistoryDao productStockHistoryDao) {
		this.productServiceDAO=productServiceDAO;
		this.productStockDao = productStockDao;
		this.productStockHistoryDao=productStockHistoryDao;
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
		
		try {
			productStockHistoryDao.addProductStockHistory(addProductBORequest);	
		}catch(Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}	return productStockDao.addProductStock(addProductBORequest);
	}

}
