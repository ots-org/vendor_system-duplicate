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

import com.fuso.enterprise.ots.srv.api.service.request.AddReviewAndRatingRequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddToCartRequest;
import com.fuso.enterprise.ots.srv.api.service.response.GetReviewAndRatingResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetcartListResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.server.dao.CartDAO;
import com.fuso.enterprise.ots.srv.server.dao.ReviewAndRatingDAO;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsCart;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsOrder;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsProduct;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsProductWishlist;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsRatingReview;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;

@Repository
public class ReviewAndRatingDAOImpl extends AbstractIptDao<OtsRatingReview, String> implements ReviewAndRatingDAO {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public ReviewAndRatingDAOImpl() {
		super(OtsRatingReview.class);
	}

	@Override
	public String addReviewAndRating(AddReviewAndRatingRequest addReviewAndRatingRequest) {
		Map<String, Object> queryParameter = new HashMap<>();
	
		OtsProduct productId = new OtsProduct();
		productId.setOtsProductId(addReviewAndRatingRequest.getRequestData().getProductId());
		queryParameter.put("otsProductId",productId);
		
		OtsUsers customerId = new OtsUsers();
		customerId.setOtsUsersId(addReviewAndRatingRequest.getRequestData().getCustomerId());
		queryParameter.put("otsCustomerId",customerId);
		
		OtsRatingReview ratingReview = new OtsRatingReview();
		ratingReview.setOtsProductId(productId);
		ratingReview.setOtsCustomerId(customerId);
		ratingReview.setOtsRatingReviewId(addReviewAndRatingRequest.getRequestData().getOtsRatingReviewId());
		ratingReview.setOtsRatingReviewTitle(addReviewAndRatingRequest.getRequestData().getOtsRatingReviewTitle());
		ratingReview.setOtsRatingReviewComment(addReviewAndRatingRequest.getRequestData().getOtsRatingReviewComment());
		ratingReview.setOtsRatingReviewRating(addReviewAndRatingRequest.getRequestData().getOtsRatingReviewRating());
		ratingReview.setOtsRatingReviewStatus(addReviewAndRatingRequest.getRequestData().getOtsRatingReviewStatus());
		ratingReview.setOtsRatingReviewAddedDate(addReviewAndRatingRequest.getRequestData().getOtsRatingReviewAddedDate());
		
		OtsOrder orderId= new OtsOrder();	
		orderId.setOtsOrderId(addReviewAndRatingRequest.getRequestData().getOrderId());
		ratingReview.setOtsOrderId(orderId);
		//	OtsCart cart;
		if(addReviewAndRatingRequest.getRequestData().getOtsRatingReviewId()!=0){
			ratingReview.setOtsRatingReviewId(addReviewAndRatingRequest.getRequestData().getOtsRatingReviewId());
		}
		try{
				super.getEntityManager().merge(ratingReview);
			
			
			}catch (Exception e) {
				System.out.println("*******"+ e.toString());
	}
		return "Thanks for your review comments";
	}

	@Override
	public List<GetReviewAndRatingResponse> getReviewAndRating(AddReviewAndRatingRequest addReviewAndRatingRequest) {
		
	List<OtsRatingReview> ratingReviews = new ArrayList<OtsRatingReview>();
		
		List<GetReviewAndRatingResponse> getReviewAndRatingResponses= new ArrayList<GetReviewAndRatingResponse>();
	//	try {
		OtsUsers customerId = new OtsUsers();
		customerId.setOtsUsersId(Integer.parseInt(addReviewAndRatingRequest.getRequestData().getSearchvalue()));
		OtsProduct productId = new OtsProduct();
		productId.setOtsProductId(Integer.parseInt(addReviewAndRatingRequest.getRequestData().getSearchvalue()));
		OtsRatingReview ratingReviewStatus= new OtsRatingReview();
		//Map<String, Object> queryParameter = new HashMap<>();
		
		
		//queryParameter.put("otsCustomerId",customerId);
		String searchKey=addReviewAndRatingRequest.getRequestData().getSearchKey();
		//String seachValue=addReviewAndRatingRequest.getRequestData().getSearchvalue();
		
		Map<String, Object> queryParameter = new HashMap<>();
		
		try{
            switch(searchKey){
	            case "Product":
	            					queryParameter.put("otsProductId",productId);
	            					queryParameter.put("otsRatingReviewStatus", (addReviewAndRatingRequest.getRequestData().getOtsRatingReviewStatus()));
	            					ratingReviews  = super.getResultListByNamedQuery("OtsRatingReview.getReviewAndRatingByProductIdAndStatus", queryParameter);
	            					//ratingReviews = super.getResultListByNamedQuery("OtsRatingReview.getReviewAndRatingByProductId", queryParameter);
	            				    break;
	            case "customer":
								 	queryParameter.put("otsCustomerId", customerId);
								 	queryParameter.put("otsRatingReviewStatus", (addReviewAndRatingRequest.getRequestData().getOtsRatingReviewStatus()));
								 	ratingReviews = super.getResultListByNamedQuery("OtsRatingReview.getReviewAndRatingByCustomerIdAndStatus", queryParameter);
								    break;
	            default:
									return null;
            }
		
			getReviewAndRatingResponses = ratingReviews.stream().map(ratingReview -> convertEntityToModel(ratingReview)).collect(Collectors.toList());
		
		return getReviewAndRatingResponses;
		
		}catch (NoResultException e) {
	        	logger.error("Exception while fetching data from DB :"+e.getMessage());
	    		e.printStackTrace();
	        	throw new BusinessException("review and rating list is empty", e);
	        }
		//return getReviewAndRatingResponses;
		
	}
	
	
	GetReviewAndRatingResponse convertEntityToModel(OtsRatingReview ratingReview) {
		GetReviewAndRatingResponse getreviewAndRatingResponse = new GetReviewAndRatingResponse();
		getreviewAndRatingResponse.setProductId(ratingReview.getOtsProductId().getOtsProductId().toString());
		getreviewAndRatingResponse.setProductName(ratingReview.getOtsProductId().getOtsProductName());
		getreviewAndRatingResponse.setOtsRatingReviewId(ratingReview.getOtsRatingReviewId());
		getreviewAndRatingResponse.setOtsRatingReviewTitle(ratingReview.getOtsRatingReviewTitle());
		getreviewAndRatingResponse.setOtsRatingReviewRating(ratingReview.getOtsRatingReviewRating());
		getreviewAndRatingResponse.setOtsRatingReviewComment(ratingReview.getOtsRatingReviewComment());
		getreviewAndRatingResponse.setOtsRatingReviewStatus(ratingReview.getOtsRatingReviewStatus());
		getreviewAndRatingResponse.setOtsRatingReviewAddedDate(ratingReview.getOtsRatingReviewAddedDate().toString());
		OtsUsers customerName=new OtsUsers();
		
		getreviewAndRatingResponse.setCustomerName(customerName.getOtsUsersFirstname());
		getreviewAndRatingResponse.setOrderId(ratingReview.getOtsOrderId().getOtsOrderId().toString());
		
		
		return getreviewAndRatingResponse;
	}

	@Override
	public String updateReviewAndStatus(AddReviewAndRatingRequest addReviewAndRatingRequest) {
		
		
		try {
		OtsRatingReview otsRatingReview = new OtsRatingReview();
		Map<String, Object> queryParameter = new HashMap<>();
		queryParameter.put("otsRatingReviewId",(addReviewAndRatingRequest.getRequestData().getOtsRatingReviewId()));
		otsRatingReview = super.getResultByNamedQuery("OtsRatingReview.getReviewAndRatingByReviewAndRatingId", queryParameter);
		
	
		otsRatingReview.setOtsRatingReviewStatus(addReviewAndRatingRequest.getRequestData().getOtsRatingReviewStatus());
			super.getEntityManager().merge(otsRatingReview);
			//userDetails = convertUserDetailsFromEntityToDomain(userData);
	}catch(Exception e) {
		System.out.println(e);
		return "Not updated";
	}
		return "200";
	}
	
	
	

	
}
