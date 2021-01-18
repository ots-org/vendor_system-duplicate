package com.fuso.enterprise.ots.srv.server.dao;

import java.util.List;

import com.fuso.enterprise.ots.srv.api.service.request.AddToCartRequest;

import com.fuso.enterprise.ots.srv.api.service.response.GetcartListResponse;


public interface CartDAO {
	
	String addToCart(AddToCartRequest addToCartRequest);

	List<GetcartListResponse> getcartList(AddToCartRequest addToCartRequest);
	
	String removeFromCart(AddToCartRequest addToCartRequest);

	String emptyCart(AddToCartRequest addToCartRequest);
	
}
