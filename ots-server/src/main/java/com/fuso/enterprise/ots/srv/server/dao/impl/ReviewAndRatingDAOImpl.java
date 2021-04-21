package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;
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
import com.fuso.enterprise.ots.srv.server.util.Base64UtilImage;

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
		
		try {
			if(addReviewAndRatingRequest.getRequestData().getReviewImage()!=null) {
				 Random rand = new Random();
				 int rand_int1 = rand.nextInt(1000000);
			     String path = Base64UtilImage.convertBase64toImage(addReviewAndRatingRequest.getRequestData().getReviewImage(), rand_int1);
			     ratingReview.setOtsRatingReviewImg(path);
			}
		}catch(Exception e){
			
		}
		
		
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
		
		OtsRatingReview ratingReviewStatus= new OtsRatingReview();
		
		String searchKey=addReviewAndRatingRequest.getRequestData().getSearchKey();
		Map<String, Object> queryParameter = new HashMap<>();
		OtsProduct productId = new OtsProduct();
		OtsUsers customerId = new OtsUsers();
		try{
            switch(searchKey){
	            case "product":
				            	
				        		productId.setOtsProductId(Integer.parseInt(addReviewAndRatingRequest.getRequestData().getSearchvalue()));
	            				queryParameter.put("otsProductId",productId);
	            				ratingReviews  = super.getResultListByNamedQuery("OtsRatingReview.getReviewAndRatingByProductIdAndStatus", queryParameter);
	            				break;
	            case "customer":
				            	
			    				customerId.setOtsUsersId(Integer.parseInt(addReviewAndRatingRequest.getRequestData().getSearchvalue()));
								queryParameter.put("otsCustomerId", customerId);
						    	ratingReviews = super.getResultListByNamedQuery("OtsRatingReview.getReviewAndRatingByCustomerIdAndStatus", queryParameter);
						    	break;
	            case "customer_product":
	            	
				            
				        		productId.setOtsProductId(addReviewAndRatingRequest.getRequestData().getProductId());
			    				queryParameter.put("otsProductId",productId);
			    				
			    				customerId.setOtsUsersId(addReviewAndRatingRequest.getRequestData().getCustomerId());
								queryParameter.put("otsCustomerId", customerId);
								
							 	ratingReviews = super.getResultListByNamedQuery("OtsRatingReview.getReviewAndRatingByCustomerProductIdAndStatus", queryParameter);
							 	break;
	            case "order":
	            	OtsOrder orderId = new OtsOrder();
	            	orderId.setOtsOrderId(addReviewAndRatingRequest.getRequestData().getOrderId());
	           		queryParameter.put("otsOrderId",orderId);
					
				 	ratingReviews = super.getResultListByNamedQuery("OtsRatingReview.getOrderWiseRating", queryParameter);
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
		UserDetails userDetails = new UserDetails();
		userDetails = convertUserDetailsFromEntityToDomain(ratingReview.getOtsCustomerId());
		getreviewAndRatingResponse.setCustomerName(userDetails.getFirstName());
		getreviewAndRatingResponse.setOrderId(ratingReview.getOtsOrderId().getOtsOrderId().toString());
		getreviewAndRatingResponse.setReviewImg(ratingReview.getOtsRatingReviewImg());
		
		return getreviewAndRatingResponse;
	}

	   private UserDetails convertUserDetailsFromEntityToDomain(OtsUsers otsUsers) {
	        UserDetails userDetails = new UserDetails();
	        userDetails.setUserId(otsUsers.getOtsUsersId()==null?null:otsUsers.getOtsUsersId().toString());
	        userDetails.setFirstName(otsUsers.getOtsUsersFirstname()==null?null:otsUsers.getOtsUsersFirstname());
	        userDetails.setLastName(otsUsers.getOtsUsersLastname()==null?null:otsUsers.getOtsUsersLastname());
			userDetails.setContactNo(otsUsers.getOtsUsersContactNo()==null?null:otsUsers.getOtsUsersContactNo());
	        userDetails.setAddress1(otsUsers.getOtsUsersAddr1()==null?null:otsUsers.getOtsUsersAddr1());
	        userDetails.setAddress2(otsUsers.getOtsUsersAddr2()==null?null:otsUsers.getOtsUsersAddr2());
	        userDetails.setPincode(otsUsers.getOtsUsersPincode()==null?null:otsUsers.getOtsUsersPincode());
	        userDetails.setUserRoleId(otsUsers.getOtsUserRoleId().getOtsUserRoleId()==null?null:otsUsers.getOtsUserRoleId().getOtsUserRoleId().toString());
	        userDetails.setEmailId(otsUsers.getOtsUsersEmailid()==null?null:otsUsers.getOtsUsersEmailid());
	        userDetails.setProfilePic(otsUsers.getOtsUsersProfilePic()==null?null:otsUsers.getOtsUsersProfilePic());
	        userDetails.setUsrStatus(otsUsers.getOtsUsersStatus()==null?null:otsUsers.getOtsUsersStatus());
	        userDetails.setUsrPassword(otsUsers.getOtsUsersPassword()==null?null:otsUsers.getOtsUsersPassword());
	        userDetails.setDeviceId(otsUsers.getOtsDeviceToken()==null?null:otsUsers.getOtsDeviceToken());
	        userDetails.setMappedTo(otsUsers.getOtsUserMapping()==null?null:otsUsers.getOtsUserMapping().getOtsMappedTo().toString());
	        userDetails.setUserAdminFlag(otsUsers.getOtsUsersAdminFlag()==null?null:otsUsers.getOtsUsersAdminFlag());
	        return userDetails;
	    }
	   
	@Override
	public String updateReviewAndStatus(AddReviewAndRatingRequest addReviewAndRatingRequest) {
		
		
		try {
		OtsRatingReview otsRatingReview = new OtsRatingReview();
		Map<String, Object> queryParameter = new HashMap<>();
		//queryParameter.put("otsRatingReviewId",(addReviewAndRatingRequest.getRequestData().getOtsRatingReviewId());
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
