package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.fuso.enterprise.ots.srv.api.service.request.AddWishListRequest;
import com.fuso.enterprise.ots.srv.api.service.response.GetwishListResponse;
import com.fuso.enterprise.ots.srv.server.dao.OtsProductWishlistDAO;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsProduct;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsProductWishlist;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;

@Repository
public class OtsProductWishlistDAOImpl extends AbstractIptDao<OtsProductWishlist, String> implements  OtsProductWishlistDAO{

	public OtsProductWishlistDAOImpl() {
		super(OtsProductWishlist.class);
	}

	@Override
	public String addWishList(AddWishListRequest addWishListRequest) {	
		
		Map<String, Object> queryParameter = new HashMap<>();
		OtsProduct productId = new OtsProduct();
		productId.setOtsProductId(addWishListRequest.getRequestData().getProductId());
		queryParameter.put("otsProductId",productId);
		
		OtsUsers customerId = new OtsUsers();
		customerId.setOtsUsersId(addWishListRequest.getRequestData().getCustomerId());
		queryParameter.put("otsCustomerId",customerId);
		
		OtsProductWishlist otsProductWishlist = new OtsProductWishlist();
		try{
		otsProductWishlist = super.getResultByNamedQuery("OtsProductWishlist.getWhishListByCustomerIdAndProductId", queryParameter);
		return "product already added to wishlist";
		}catch (Exception e) {
			otsProductWishlist.setOtsProductId(productId);
			otsProductWishlist.setOtsCustomerId(customerId);
			super.getEntityManager().merge(otsProductWishlist);
			return "success";
		}
		
		//System.out.println(otsProductWishlist.getOtsProductId());
		
		//return "success";
	}
	

	

	@Override
	public List<GetwishListResponse> getwishList(AddWishListRequest addWishListRequest) {
		List<OtsProductWishlist> otsProductWishlist = new ArrayList<OtsProductWishlist>();
		List<GetwishListResponse> getwishListResponseList= new ArrayList<GetwishListResponse>();
		OtsUsers customerId = new OtsUsers();
		customerId.setOtsUsersId(addWishListRequest.getRequestData().getCustomerId());
		
		Map<String, Object> queryParameter = new HashMap<>();
		queryParameter.put("otsCustomerId",customerId);
		otsProductWishlist = super.getResultListByNamedQuery("OtsProductWishlist.getWhishListByCustomerId", queryParameter);
		
		System.out.println(otsProductWishlist.get(0).getOtsCustomerId().getOtsUsersFirstname());
	
		getwishListResponseList = otsProductWishlist.stream().map(productWishlist -> convertEntityToModel(productWishlist)).collect(Collectors.toList());
		
		
		
		return getwishListResponseList;
	}
	
	
	GetwishListResponse convertEntityToModel(OtsProductWishlist productWishlist) {
		GetwishListResponse getwishListResponse = new GetwishListResponse();
		getwishListResponse.setProductId(productWishlist.getOtsProductId().getOtsProductId());
		getwishListResponse.setProductName(productWishlist.getOtsProductId().getOtsProductName());
		getwishListResponse.setProductImage(productWishlist.getOtsProductId().getOtsProductImage());
		getwishListResponse.setProductPrice(productWishlist.getOtsProductId().getOtsProductPrice());
		return getwishListResponse;
	}

	@Override
	public String removeFromWishList(AddWishListRequest addWishListRequest) {
		Map<String, Object> queryParameter = new HashMap<>();
		OtsProductWishlist otsProductWishlist = new OtsProductWishlist();

		
		OtsProduct productId = new OtsProduct();
		productId.setOtsProductId(addWishListRequest.getRequestData().getProductId());
		queryParameter.put("otsProductId",productId);
		//otsProductWishlist.setOtsProductId(productId);
		
		OtsUsers customerId = new OtsUsers();
		customerId.setOtsUsersId(addWishListRequest.getRequestData().getCustomerId());
		queryParameter.put("otsCustomerId",customerId);
		//otsProductWishlist.setOtsCustomerId(customerId);
		try {
			otsProductWishlist = super.getResultByNamedQuery("OtsProductWishlist.getWhishListByCustomerIdAndProductId", queryParameter);
			//return "product already added to wislist";
			
			super.getEntityManager().remove(otsProductWishlist);
		}catch(Exception e) {
			e.printStackTrace();
			return "No data found";
		}
		
		return "success";
	}

}
