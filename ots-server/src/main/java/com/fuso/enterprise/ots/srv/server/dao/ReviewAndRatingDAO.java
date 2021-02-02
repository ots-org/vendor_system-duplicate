package com.fuso.enterprise.ots.srv.server.dao;

import java.util.List;

import com.fuso.enterprise.ots.srv.api.service.request.AddReviewAndRatingRequest;
import com.fuso.enterprise.ots.srv.api.service.response.GetReviewAndRatingResponse;



/*Shreekant Rathod 29-1-2021*/

public interface ReviewAndRatingDAO {
	
	String addReviewAndRating(AddReviewAndRatingRequest addReviewAndRatingRequest);

	List<GetReviewAndRatingResponse> getReviewAndRating(AddReviewAndRatingRequest addReviewAndRatingRequest);

	/*
	String removeFromCart(AddToCartRequest addToCartRequest);

	String emptyCart(AddToCartRequest addToCartRequest);
	*/
}
