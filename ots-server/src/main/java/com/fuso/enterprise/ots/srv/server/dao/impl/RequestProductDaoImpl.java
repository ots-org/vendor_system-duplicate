package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.fuso.enterprise.ots.srv.api.model.domain.DonationBoResponse;
import com.fuso.enterprise.ots.srv.api.model.domain.DonationResponseByStatus;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderDetailsAndProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.ProductDetails;
import com.fuso.enterprise.ots.srv.api.service.request.AddDonationtoRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetDonationByStatusRequest;
import com.fuso.enterprise.ots.srv.server.dao.RequestProductDao;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsProduct;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsRequestProduct;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;

@Repository
public class RequestProductDaoImpl extends AbstractIptDao<OtsRequestProduct, String> implements RequestProductDao {

	public RequestProductDaoImpl() {
		super(OtsRequestProduct.class);
	}

	@Override
	public OrderDetailsAndProductDetails addOrUpdateRequest(OrderDetailsAndProductDetails orderDetailsAndProductDetails) {
			for(int i=0;i < orderDetailsAndProductDetails.getOrderdProducts().size() ;i++) {
				
				OtsRequestProduct requestProduct = new OtsRequestProduct();
				Map<String, Object> queryParameter = new HashMap<>();
				
				OtsProduct productId = new OtsProduct();
				productId.setOtsProductId(Integer.parseInt(orderDetailsAndProductDetails.getOrderdProducts().get(i).getOtsProductId()));
				System.out.println(orderDetailsAndProductDetails.getOrderdProducts().get(i).getOtsProductId());
				queryParameter.put("productId",productId );
				queryParameter.put("status","newRequest");
				try {
					requestProduct = super.getResultByNamedQuery("OtsRequestProduct.getNewRequestProduct", queryParameter);
					Integer totalProductQty = Integer.parseInt(requestProduct.getOtsRequestProductStock()) + Integer.parseInt(orderDetailsAndProductDetails.getOrderdProducts().get(i).getOtsOrderedQty());
					requestProduct.setOtsRequestProductStock(totalProductQty.toString());
					save(requestProduct);
					System.out.println(requestProduct.getOtsProductId());
					super.getEntityManager().flush();
				}catch(Exception e) {
					System.out.print(e);
					requestProduct.setOtsRequestProductAddedStock("0");
					requestProduct.setOtsProductId(productId);
					requestProduct.setOtsRequestProductStock(orderDetailsAndProductDetails.getOrderdProducts().get(i).getOtsOrderedQty());
					requestProduct.setOtsRequestProductStatus(orderDetailsAndProductDetails.getOrderStatus());
					try {
						save(requestProduct);
						super.getEntityManager().flush();
					}catch(Exception newException) {
						System.out.print(newException);
					}
					
				}
				orderDetailsAndProductDetails.getOrderdProducts().get(i).setRequestedId(requestProduct.getOtsRequestProductId().toString());
			}
		
		return orderDetailsAndProductDetails;
	}

	@Override
	public DonationResponseByStatus getDonationListBystatus(GetDonationByStatusRequest donationByStatusRequest) {
		DonationResponseByStatus donationResponseByStatus = new DonationResponseByStatus();
		List<ProductDetails> productList = new ArrayList<ProductDetails>();
		List<OtsRequestProduct> requestProductList = new ArrayList<OtsRequestProduct>();
		Map<String, Object> queryParameter = new HashMap<>();
		queryParameter.put("status",donationByStatusRequest.getRequest().getStatus());
		if(donationByStatusRequest.getRequest().getProductId()==null) {
			try {
				requestProductList = super.getResultListByNamedQuery("OtsRequestProduct.getDonationListBystatus", queryParameter);
			}catch(Exception e) {
				System.out.print(e);
			}
		}else {
			try {
				OtsProduct productId = new OtsProduct();
				productId.setOtsProductId(Integer.parseInt(donationByStatusRequest.getRequest().getProductId()));
				queryParameter.put("productId",productId);
				requestProductList = super.getResultListByNamedQuery("OtsRequestProduct.getDonationListBystatusAndProduct", queryParameter);
			}catch(Exception e) {
				System.out.print(e);
			}
		}
	//	otsOrderDetails =  OrderList.stream().map(OtsOrderProduct -> convertOrderDetailsFromEntityToDomain(OtsOrderProduct)).collect(Collectors.toList());;
		productList = requestProductList.stream().map(OtsRequestProduct -> convertEntityToModel(OtsRequestProduct)).collect(Collectors.toList());
		donationResponseByStatus.setProductList(productList);
		return donationResponseByStatus;
	}
	
	public ProductDetails convertEntityToModel(OtsRequestProduct requestProduct ) {
		ProductDetails productDetails = new ProductDetails();
		productDetails.setStock(requestProduct.getOtsRequestProductStock());
		productDetails.setProductId(requestProduct.getOtsProductId().getOtsProductId().toString());
		productDetails.setProductName(requestProduct.getOtsProductId().getOtsProductName());
		productDetails.setProductImage(requestProduct.getOtsProductId().getOtsProductImage());
		productDetails.setProductPrice(requestProduct.getOtsProductId().getOtsProductPrice().toString());
		productDetails.setAddedStock(requestProduct.getOtsRequestProductAddedStock()==null?null:requestProduct.getOtsRequestProductAddedStock());
		productDetails.setDonationRequestId(requestProduct.getOtsRequestProductId().toString());
		return productDetails;
	}

	@Override
	public DonationBoResponse addNewDonation(AddDonationtoRequest addDonationtoRequest) {
		DonationBoResponse donationBoResponse = new DonationBoResponse();
		OtsRequestProduct requestProduct = new OtsRequestProduct();
		try {
			for(int i=0 ; i<addDonationtoRequest.getRequest().size() ; i++) {
				
				requestProduct.setOtsRequestProductStock(addDonationtoRequest.getRequest().get(i).getPresentStock());
				requestProduct.setOtsRequestProductId(Integer.parseInt(addDonationtoRequest.getRequest().get(i).getDonationRequestId()));
				requestProduct.setOtsRequestProductAddedStock(addDonationtoRequest.getRequest().get(i).getDonatedQty());
				requestProduct.setOtsRequestProductStatus(addDonationtoRequest.getRequest().get(i).getRequestStatus());
			
				OtsProduct productId = new OtsProduct();
				productId.setOtsProductId(Integer.parseInt(addDonationtoRequest.getRequest().get(i).getProductId()));
				requestProduct.setOtsProductId(productId);
				
				requestProduct = super.getEntityManager().merge(requestProduct);
				donationBoResponse.setPaymentRequestId(requestProduct.getOtsRequestProductId().toString());
				
			}
		}catch(Exception e) {
			System.out.println(e);
		}
		return donationBoResponse;
	}

}
