package com.fuso.enterprise.ots.srv.api.service.functional;

import com.fuso.enterprise.ots.srv.api.service.request.AddUserDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddWishListRequest;
import com.fuso.enterprise.ots.srv.api.service.request.ApproveRegistrationBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.ChangePasswordRequest;
import com.fuso.enterprise.ots.srv.api.service.request.CustomerDetailsForLoginRequest;
import com.fuso.enterprise.ots.srv.api.service.request.CustomerProductDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.ForgotPasswordRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetSellerByLatAndLonRequest;
import com.fuso.enterprise.ots.srv.api.service.request.LoginAuthenticationBOrequest;
import com.fuso.enterprise.ots.srv.api.service.request.MapUsersDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.OutstandingRequest;
import com.fuso.enterprise.ots.srv.api.service.response.LoginUserResponse;
import com.fuso.enterprise.ots.srv.api.service.response.MapUsersDataBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.OutstandingCustomerResponse;

import java.util.List;
import java.util.Map;

import com.fuso.enterprise.ots.srv.api.model.domain.RejectUserModel;
import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;
import com.fuso.enterprise.ots.srv.api.service.request.AddNewBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddReviewAndRatingRequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddToCartRequest;
import com.fuso.enterprise.ots.srv.api.service.request.UserRegistrationBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.ApproveRegistrationResponse;
import com.fuso.enterprise.ots.srv.api.service.response.ForgotPasswordResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetNewRegistrationResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetReviewAndRatingResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetcartListResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetwishListResponse;
import com.fuso.enterprise.ots.srv.api.service.response.UserRegistrationResponce;
import com.fuso.enterprise.ots.srv.api.service.response.UserDataBOResponse;
import com.fuso.enterprise.ots.srv.api.service.request.RequestBOUserBySearch;
import com.fuso.enterprise.ots.srv.api.service.request.UpdatePassword;
import com.fuso.enterprise.ots.srv.api.service.request.UpdatePasswordRequest;
public interface OTSUserService {
	
	UserDataBOResponse getUserIDUsers(String userId);

	UserDataBOResponse addNewUser(AddUserDataBORequest addUserDataBORequest);

	String mappUser(MapUsersDataBORequest mapUsersDataBORequest);

	UserDataBOResponse getUserDetails(RequestBOUserBySearch requestBOUserBySearch);

	UserRegistrationResponce addUserRegistration(AddNewBORequest addNewBORequest);

	GetNewRegistrationResponse getNewRegistration(String mappedTo);

	LoginUserResponse otsLoginAuthentication(LoginAuthenticationBOrequest  loginAuthenticationBOrequest);

	String mapUserProduct(CustomerProductDataBORequest customerProductDataBORequest);

	ApproveRegistrationResponse approveRegistration(ApproveRegistrationBORequest approveRegistrationBORequest);

	UserDataBOResponse getUserDetailsByMapped(String mappedTo);
	
	UserDataBOResponse checkForUserExistsOrNot(AddUserDataBORequest addUserDataBORequest);

	OutstandingCustomerResponse getOutstandingData(OutstandingRequest outstandingRequest);

	String rejectUser(RejectUserModel rejectUserModel);

	ForgotPasswordResponse sendOTP(ForgotPasswordRequest forgotPasswordRequest);

	String changePassword(ChangePasswordRequest changePasswordRequest);

	String updatePassword(UpdatePasswordRequest updatePasswordRequest);
	
	String addWishList(AddWishListRequest addWishListRequest) ;
	
	List<GetwishListResponse> getwishList(AddWishListRequest addWishListRequest) ;
	
	String removeFromWishList(AddWishListRequest addWishListRequest) ;
	
	String addToCart(AddToCartRequest addToCartRequest) ;
	
	List<GetcartListResponse> getcartList(AddToCartRequest addToCartRequest) ;
	
	String removeFromCart(AddToCartRequest addToCartRequest) ;

	String emptyCart(AddToCartRequest addToCartRequest);
	
/* Shreekant Rathod 29-1-2021 */
	String addReviewAndRating(AddReviewAndRatingRequest addReviewAndRatingRequest);

	List<GetReviewAndRatingResponse> getReviewAndRating(AddReviewAndRatingRequest addReviewAndRatingRequest);

	String updateReviewAndStatus(AddReviewAndRatingRequest addReviewAndRatingRequest);
	
	UserDetails loginWithOtp(LoginAuthenticationBOrequest loginAuthenticationBOrequest);
	
	UserDetails getCustomerDetailsForLogin(CustomerDetailsForLoginRequest customerDetailsForLoginRequest);
	
	List<Map<String, Object>> getSellerDetails(GetSellerByLatAndLonRequest getSellerByLatAndLonRequest);
}
