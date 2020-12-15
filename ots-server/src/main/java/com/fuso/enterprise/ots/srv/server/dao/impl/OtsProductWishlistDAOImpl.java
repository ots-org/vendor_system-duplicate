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
		OtsProductWishlist otsProductWishlist = new OtsProductWishlist();

	
		OtsProduct productId = new OtsProduct();
		productId.setOtsProductId(addWishListRequest.getAddWishListRequestModel().getProductId());
		otsProductWishlist.setOtsProductId(productId);
		
		OtsUsers customerId = new OtsUsers();
		customerId.setOtsUsersId(addWishListRequest.getAddWishListRequestModel().getCustomerId());
		otsProductWishlist.setOtsCustomerId(customerId);
		
		try {
			super.getEntityManager().merge(otsProductWishlist);
		}catch(Exception e) {
			System.out.println(e);
		}
		
		
		return "success";
	}

	@Override
	public List<GetwishListResponse> getwishList(AddWishListRequest addWishListRequest) {
		List<OtsProductWishlist> otsProductWishlist = new ArrayList<OtsProductWishlist>();
		List<GetwishListResponse> getwishListResponseList= new ArrayList<GetwishListResponse>();
		OtsUsers customerId = new OtsUsers();
		customerId.setOtsUsersId(addWishListRequest.getAddWishListRequestModel().getCustomerId());
		
		Map<String, Object> queryParameter = new HashMap<>();
		queryParameter.put("otsCustomerId",customerId);
		otsProductWishlist = super.getResultListByNamedQuery("OtsProductWishlist.getWhishListByCustomerId", queryParameter);
		
		System.out.println(otsProductWishlist.get(0).getOtsCustomerId().getOtsUsersFirstname());
		getwishListResponseList = otsProductWishlist.stream().map(productWishlist -> convertEntityToModel(productWishlist)).collect(Collectors.toList());
		//todayairtableDataList.stream().map(OtsAirtable -> convertAirTabelTOModel(OtsAirtable)).collect(Collectors.toList());
		
		return getwishListResponseList;
	}
	
	
	GetwishListResponse convertEntityToModel(OtsProductWishlist productWishlist) {
		GetwishListResponse getwishListResponse = new GetwishListResponse();
		getwishListResponse.setProductName(productWishlist.getOtsProductId().getOtsProductName());
		return getwishListResponse;
	}

}
