package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fuso.enterprise.ots.srv.api.service.request.AddToCartRequest;

import com.fuso.enterprise.ots.srv.api.service.response.GetcartListResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.server.dao.CartDAO;


import com.fuso.enterprise.ots.srv.server.model.entity.OtsCart;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsProduct;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsProductWishlist;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;

@Repository
public class CartDAOImpl extends AbstractIptDao<OtsCart, String> implements CartDAO {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public CartDAOImpl() {
		super(OtsCart.class);
	}
	
	@Override
	public String addToCart(AddToCartRequest addToCartRequest) {
		Map<String, Object> queryParameter = new HashMap<>();
		
		OtsProduct productId = new OtsProduct();
		productId.setOtsProductId(addToCartRequest.getRequestData().getProductId());
		queryParameter.put("otsProductId",productId);
		
		OtsUsers customerId = new OtsUsers();
		customerId.setOtsUsersId(addToCartRequest.getRequestData().getCustomerId());
		queryParameter.put("otsCustomerId",customerId);
		
		OtsCart cart = new OtsCart();
		cart.setOtsCartQty(addToCartRequest.getRequestData().getOtsCartQty());
		//queryParameter.put("otsCartQty",cart);
		
		//	OtsCart cart;
		if(addToCartRequest.getRequestData().getOtsCartId()!=0){
			cart.setOtsCartId(addToCartRequest.getRequestData().getOtsCartId());
		}
		try{
			
			cart = super.getResultByNamedQuery("OtsCart.getCartListByCustomerIdAndProductId", queryParameter);
			if(addToCartRequest.getRequestData().getOtsCartQty()>=1){
			int oldQuantity=cart.getOtsCartQty();
			int TotalQuantity=oldQuantity+(addToCartRequest.getRequestData().getOtsCartQty());
			cart.setOtsCartQty(TotalQuantity);
				super.getEntityManager().merge(cart);
				return "success";
			}else{
				super.getEntityManager().remove(cart);
				return "success";
			}
				
			//}
			
			}catch (Exception e) {
				if(addToCartRequest.getRequestData().getOtsCartQty()>=1){
				cart.setOtsProductId(productId);
				cart.setOtsCustomerId(customerId);
				
				super.getEntityManager().merge(cart);
				}
				return "success";
			}
	       
	}

	
	
	@Override
	public List<GetcartListResponse> getcartList(AddToCartRequest addToCartRequest) {
		
		List<OtsCart> cart = new ArrayList<OtsCart>();
		
		List<GetcartListResponse> getcartListResponseList= new ArrayList<GetcartListResponse>();
		try {
		OtsUsers customerId = new OtsUsers();
		customerId.setOtsUsersId(addToCartRequest.getRequestData().getCustomerId());
		
		Map<String, Object> queryParameter = new HashMap<>();
		queryParameter.put("otsCustomerId",customerId);
		//cart = super.getResultListByNamedQuery("OtsCart.getCartListByCustomerId", queryParameter);
		
		cart = super.getResultListByNamedQuery("OtsCart.getCartListByCustomerId", queryParameter);
		//System.out.println(cart.get(0).getOtsCustomerId().getOtsUsersFirstname());
		
		getcartListResponseList = cart.stream().map(cartlist -> convertEntityToModel(cartlist)).collect(Collectors.toList());
		
		return getcartListResponseList;
		//return getcartListResponseList;
		}catch (NoResultException e) {
	        	logger.error("Exception while fetching data from DB :"+e.getMessage());
	    		e.printStackTrace();
	        	throw new BusinessException("cart list is empty", e);
	        }
		
	}
	
	
	GetcartListResponse convertEntityToModel(OtsCart cartlist) {
		GetcartListResponse getcartListResponse = new GetcartListResponse();
		getcartListResponse.setProductId(cartlist.getOtsProductId().getOtsProductId().toString());
		getcartListResponse.setProductName(cartlist.getOtsProductId().getOtsProductName());
		getcartListResponse.setProductImage(cartlist.getOtsProductId().getOtsProductImage());
		getcartListResponse.setProductPrice(cartlist.getOtsProductId().getOtsProductPrice().toString());
		getcartListResponse.setOtsCartQty(cartlist.getOtsCartQty());
		 BigDecimal totalCost = BigDecimal.ZERO;
		Integer Quantity=cartlist.getOtsCartQty();
		BigDecimal price=cartlist.getOtsProductId().getOtsProductPrice();
		BigDecimal itemCost=price.multiply(new BigDecimal(Quantity));
		 totalCost=totalCost.add(itemCost);
		 getcartListResponse.setTotalPrice(totalCost);
		return getcartListResponse;
	}

	@Override
	public String removeFromCart(AddToCartRequest addToCartRequest) {
		
		Map<String, Object> queryParameter = new HashMap<>();
		OtsCart cart = new OtsCart();

		
		OtsProduct productId = new OtsProduct();
		productId.setOtsProductId(addToCartRequest.getRequestData().getProductId());
		queryParameter.put("otsProductId",productId);
		//otsProductWishlist.setOtsProductId(productId);
		
		OtsUsers customerId = new OtsUsers();
		customerId.setOtsUsersId(addToCartRequest.getRequestData().getCustomerId());
		queryParameter.put("otsCustomerId",customerId);
		//otsProductWishlist.setOtsCustomerId(customerId);
		try {
			int TotalQuantity=0;
			cart = super.getResultByNamedQuery("OtsCart.getCartListByCustomerIdAndProductId", queryParameter);
			//return "product already added to wislist";
				int oldQuantity=cart.getOtsCartQty();
				 TotalQuantity=oldQuantity-(addToCartRequest.getRequestData().getOtsCartQty());
				cart.setOtsCartQty(TotalQuantity);
				
			 if(addToCartRequest.getRequestData().getOtsCartQty()==0){
				super.getEntityManager().remove(cart);
				return "success";
			}else if(TotalQuantity>=1)
			{
		super.getEntityManager().merge(cart);
		return "success";
		}else{
				super.getEntityManager().remove(cart);
				return "success";
			}
		}catch(Exception e) {
			e.printStackTrace();
			return "No data found";
		}
		
	//	return "success";
	
	}

	@Override
	public String emptyCart(AddToCartRequest addToCartRequest) {
		
		List<OtsCart> cart = new ArrayList<OtsCart>();
		OtsUsers customerId = new OtsUsers();
		customerId.setOtsUsersId(addToCartRequest.getRequestData().getCustomerId());
		
		Map<String, Object> queryParameter = new HashMap<>();
		queryParameter.put("otsCustomerId",customerId);
		cart = super.getResultListByNamedQuery("OtsCart.getCartListByCustomerId", queryParameter);
		try {
		 for (OtsCart otsCart:cart )
			 super.getEntityManager().remove(otsCart);
			
			return "success";
			}catch (Exception e) {
				e.printStackTrace();
			}
		return "success";
	}


	
}
