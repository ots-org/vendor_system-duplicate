package com.fuso.enterprise.ots.srv.server.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fuso.enterprise.ots.srv.api.model.domain.OrderDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderedProductDetails;
import com.fuso.enterprise.ots.srv.api.service.request.GetProductStockListRequest;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.common.exception.ErrorEnumeration;
import com.fuso.enterprise.ots.srv.server.dao.OrderProductDAO;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsOrder;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsOrderProduct;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsProduct;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;

@Service
@Transactional
public class OrderProductDAOImpl extends AbstractIptDao<OtsOrderProduct, String> implements OrderProductDAO {

	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public OrderProductDAOImpl() {
		super(OtsOrderProduct.class);
	}

		@Override
	public long getListOfDeliverdQuantityOfDay(List<OtsOrder> orderList, Integer otsProductId) {
		int orderProductList = 0;
		try {
			Iterator<OtsOrder> iterator = orderList.iterator();
			OtsProduct otsProduct= new OtsProduct();
			otsProduct.setOtsProductId(otsProductId);
			while (iterator.hasNext()) {	
				OtsOrder otsOrder = iterator.next();
				Map<String, Object> queryParameter = new HashMap<>();
				queryParameter.put("otsOrderId", otsOrder);
				queryParameter.put("otsProductId", otsProduct);
				OtsOrderProduct otsOrderProduct = super.getResultByNamedQuery("OtsOrder.fetchOtsSoldProducts", queryParameter);
				orderProductList += otsOrderProduct.getOtsDeliveredQty();
			}
		} catch (NoResultException e) {
			logger.error("Exception while fetching data from DB :" + e.getMessage());
			e.printStackTrace();
		}
		return orderProductList;
	}

		@Override
		public List<OrderProductDetails> getUserByStatuesAndDistributorId(OrderDetails orderDetails) {
			List<OrderProductDetails> otsOrderDetails = new ArrayList<>();
			List<OtsOrderProduct> OrderList = new ArrayList<>();
			try {  
				Map<String, Object> queryParameter = new HashMap<>();
				OtsOrder otsOrder = new OtsOrder();
				System.out.println("+orderDetails.getOrderId()+"+orderDetails.getOrderId());
				otsOrder.setOtsOrderId(Integer.parseInt(orderDetails.getOrderId()));
				queryParameter.put("otsOrderId",otsOrder);
				
				OrderList = super.getResultListByNamedQuery("OtsOrderProduct.GetOrderByDistrubutorIdAndStatus", queryParameter);
			}catch(Exception e){
				e.printStackTrace();
				logger.error("ERROR IN INSERTING PRODUCT TO ORDER-PRODUCT TABLE"+e.getMessage());
				throw new BusinessException(e, ErrorEnumeration.ERROR_IN_ORDER_PRODUCT_INSERTION);}
			catch (Throwable e) {
				e.printStackTrace();
				logger.error("ERROR IN INSERTING PRODUCT TO ORDER-PRODUCT TABLE"+e.getMessage());
				throw new BusinessException(e, ErrorEnumeration.ERROR_IN_ORDER_PRODUCT_INSERTION);
				}

			otsOrderDetails =  OrderList.stream().map(OtsOrderProduct -> convertOrderDetailsFromEntityToDomain(OtsOrderProduct)).collect(Collectors.toList());; 
			return otsOrderDetails;
		}

		private OrderProductDetails convertOrderDetailsFromEntityToDomain(OtsOrderProduct otsOrderProduct) {
			OrderProductDetails orderDetails =  new OrderProductDetails() ;
			orderDetails.setOtsOrderId((otsOrderProduct.getOtsOrderId().getOtsOrderId()==null?null:otsOrderProduct.getOtsOrderId().getOtsOrderId().toString()));
			orderDetails.setOtsDeliveredQty(otsOrderProduct.getOtsDeliveredQty()==null?null:otsOrderProduct.getOtsDeliveredQty().toString());
			orderDetails.setOtsOrderProductCost(otsOrderProduct.getOtsOrderProductCost()==null?null:otsOrderProduct.getOtsOrderProductCost().toString());
			orderDetails.setOtsOrderedQty(otsOrderProduct.getOtsOrderedQty()==null?null:otsOrderProduct.getOtsOrderedQty().toString()); 
			orderDetails.setOtsOrderProductStatus(otsOrderProduct.getOtsOrderProductStatus()==null?null:otsOrderProduct.getOtsOrderProductStatus().toString());
			orderDetails.setOtsOrderProductId(otsOrderProduct.getOtsOrderProductId()==null?null:otsOrderProduct.getOtsOrderProductId().toString());
			orderDetails.setOtsProductId(otsOrderProduct.getOtsProductId().getOtsProductId()==null?null:otsOrderProduct.getOtsProductId().getOtsProductId().toString());
			return orderDetails;		
		}

		@Override
		public String insertOrdrerProductByOrderId(Integer orderId,OrderedProductDetails orderedProductDetails) {
			try {
				OtsOrderProduct otsOrderProduct = new OtsOrderProduct();
				
				OtsOrder OtsorderId = new OtsOrder();
				OtsorderId.setOtsOrderId(orderId);
				otsOrderProduct.setOtsOrderId(OtsorderId);
				
				OtsProduct  ProductId= new OtsProduct();
				ProductId.setOtsProductId(Integer.parseInt(orderedProductDetails.getProductId()));
				otsOrderProduct.setOtsProductId(ProductId);
				otsOrderProduct.setOtsOrderedQty(Integer.parseInt(orderedProductDetails.getOrderedQty()));
				otsOrderProduct.setOtsOrderProductStatus(orderedProductDetails.getProductStatus());
				otsOrderProduct.setOtsOrderProductCost(Long.valueOf(orderedProductDetails.getProductCost()));
				otsOrderProduct.setOtsDeliveredQty(Integer.valueOf(orderedProductDetails.getOts_delivered_qty()));
				save(otsOrderProduct);
				return "Inserted";
			}catch(Exception e){
				e.printStackTrace();
				logger.error("ERROR IN INSERTING PRODUCT TO ORDER-PRODUCT TABLE"+e.getMessage());
				throw new BusinessException(e, ErrorEnumeration.ERROR_IN_ORDER_PRODUCT_INSERTION);}
			catch (Throwable e) {
				e.printStackTrace();
				logger.error("ERROR IN INSERTING PRODUCT TO ORDER-PRODUCT TABLE"+e.getMessage());
				throw new BusinessException(e, ErrorEnumeration.ERROR_IN_ORDER_PRODUCT_INSERTION);
				}	
		}

		@Override
		public String addOrUpdateOrderProduct(OrderedProductDetails orderedProductDetails) {
			try {
				OtsOrderProduct otsOrderProduct = new OtsOrderProduct();
				
				OtsOrder OtsorderId = new OtsOrder();
				OtsorderId.setOtsOrderId(Integer.parseInt(orderedProductDetails.getOrderdId()));
				otsOrderProduct.setOtsOrderId(OtsorderId);
			
				OtsProduct  ProductId= new OtsProduct();
				ProductId.setOtsProductId(Integer.parseInt(orderedProductDetails.getProductId()));
				otsOrderProduct.setOtsProductId(ProductId);
				otsOrderProduct.setOtsOrderedQty(Integer.parseInt(orderedProductDetails.getOrderedQty()));
				otsOrderProduct.setOtsOrderProductStatus(orderedProductDetails.getProductStatus());
				otsOrderProduct.setOtsOrderProductCost(Long.valueOf(orderedProductDetails.getProductCost()));
				otsOrderProduct.setOtsDeliveredQty(Integer.valueOf(orderedProductDetails.getOts_delivered_qty()));
				if(orderedProductDetails.getOrderProductId()==null) {
					System.out.println("Inserted");
					save(otsOrderProduct);
					return "Inserted";
				}else {
					System.out.println("Updated");
					otsOrderProduct.setOtsOrderProductId(Integer.parseInt(orderedProductDetails.getOrderProductId()));
					super.getEntityManager().merge(otsOrderProduct);
					return "Updated";
				}		
			}catch(Exception e){
				e.printStackTrace();
				logger.error("ERROR IN INSERTING PRODUCT TO ORDER-PRODUCT TABLE"+e.getMessage());
				throw new BusinessException(e, ErrorEnumeration.ERROR_IN_ORDER_PRODUCT_INSERTION);}
			catch (Throwable e) {
				e.printStackTrace();
				logger.error("ERROR IN INSERTING PRODUCT TO ORDER-PRODUCT TABLE"+e.getMessage());
				throw new BusinessException(e, ErrorEnumeration.ERROR_IN_ORDER_PRODUCT_INSERTION);
				}	
			
		}
}
