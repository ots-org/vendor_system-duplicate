package com.fuso.enterprise.ots.srv.rest.ws.service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

import com.fuso.enterprise.ots.srv.api.model.domain.RejectUserModel;
import com.fuso.enterprise.ots.srv.api.service.request.AddNewBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddReviewAndRatingRequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddToCartRequest;
import com.fuso.enterprise.ots.srv.api.service.request.MappedToBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.OutstandingRequest;
import com.fuso.enterprise.ots.srv.api.service.request.UserRegistrationBORequest;

import com.fuso.enterprise.ots.srv.api.service.request.AddUserDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddWishListRequest;
import com.fuso.enterprise.ots.srv.api.service.request.ApproveRegistrationBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.ChangePasswordRequest;
import com.fuso.enterprise.ots.srv.api.service.request.CustomerProductDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.ForgotPasswordRequest;
import com.fuso.enterprise.ots.srv.api.service.request.LoginAuthenticationBOrequest;
import com.fuso.enterprise.ots.srv.api.service.request.MapUsersDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.RequestBOUserBySearch;
import com.fuso.enterprise.ots.srv.api.service.request.UpdatePassword;
import com.fuso.enterprise.ots.srv.api.service.request.UpdatePasswordRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Validated
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "OTSUsersV18_1Ws", description = "This service provides the operations for OTS users")
@Path("users")
@CrossOrigin
public interface OTSUsersV18_1Ws {
	
	@GET
    @Path("/get-userID-users")
	@ApiOperation(value = "get-userID-users", notes = "This operation will give the list of user based on userID", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response getUserIDUsers(@ApiParam(value = "userId", required = true) @NotNull @Valid @QueryParam("userId") String userId);
	
	@POST
	@Path("/addNewUser")
	@ApiOperation(value = "addNewUser", notes = "This operation will add New user ", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response addNewUser(@ApiParam(value = "request", required = true) @NotNull @Valid AddUserDataBORequest addUserDataBORequest);
	
	@POST
	@Path("/mappUser")
	@ApiOperation(value = "mappUser", notes = "This operation map users ", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response mappUser(@ApiParam(value = "request", required = true) @NotNull @Valid MapUsersDataBORequest mapUsersDataBORequest);

	@POST
    @Path("/getUserDetails")
	@ApiOperation(value = "getUserDetails", notes = "Getting User Request Based On Name, Email and getUserDetails", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response getUserDetails(@ApiParam(value = "request", required = true) @NotNull  @Valid RequestBOUserBySearch  requestBOUserBySearch);		

	@POST
    @Path("/addUserRegistration")
	@ApiOperation(value = "UserRegistration", notes = "This operation will help to register the user", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response addUserRegistration(@NotNull @RequestBody AddNewBORequest addNewBORequest);
	
	@POST
    @Path("/getNewRegistration")
	@ApiOperation(value = "getNewRegistration", notes = "This operation will give the list of user based on MappedTo", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response getNewRegistration(@NotNull @RequestBody MappedToBORequest mappedToBORequest);

	@POST
    @Path("/otsLoginAuthentication")
	@ApiOperation(value = "getUserDetails", notes = "Api For Login Users Using EmailId and Password", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response otsLoginAuthentication(@ApiParam(value = "request", required = true) @NotNull  @Valid LoginAuthenticationBOrequest  loginAuthenticationBOrequest);		
	
	@POST
	@Path("MapUserProduct")
	@ApiOperation(value = "MapUserProduct", notes = "This operation will add New user ", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response mapUserProduct(@ApiParam(value = "request", required = true) @NotNull @Valid CustomerProductDataBORequest customerProductDataBORequest);
	
	@POST
	@Path("/approveRegistration")
	@ApiOperation(value = "approveRegistration", notes = "This Operation will approve the registeration", response= Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response approveRegistration(@ApiParam(value = "request", required = true) @NotNull @Valid ApproveRegistrationBORequest approveRegistrationBORequest );
	
	@POST
    @Path("/getUserDetailsByMapped")
	@ApiOperation(value = "getUserDetailsByMapped", notes = "Getting All the details of mapped Users", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response getUserDetailsByMapped(@ApiParam(value = "request", required = false)  @Valid MappedToBORequest mappedToBORequest);
	
	@POST
    @Path("/getCustomerOutstandingData")
	@ApiOperation(value = "getCustomerOutstandingData", notes = "Getting customer outstanding amount and cans", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response getCustomerOutstandingData(@ApiParam(value = "request", required = true) @NotNull  @Valid OutstandingRequest outstandingRequest);
	
	@POST
	@Path("/rejectUser")
	@ApiOperation(value = "addNewUser", notes = "This operation will add New user ", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response rejectUser(@ApiParam(value = "request", required = true) @NotNull @Valid RejectUserModel rejectUserModel);

	@POST
	@Path("/forgotPassword")
	@ApiOperation(value = "mappUser", notes = "This operation will add New user ", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response forgotPassword(@ApiParam(value = "request", required = true) @NotNull @Valid ForgotPasswordRequest forgotPasswordRequest);

	
	@POST
	@Path("/changePassword")
	@ApiOperation(value = "mappUser", notes = "This operation will change user password ", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response changePassword(@ApiParam(value = "request", required = true) @NotNull @Valid ChangePasswordRequest changePasswordRequest);

	@POST
	@Path("/updatePassword")
	@ApiOperation(value = "updatePassword", notes = "This operation will update user password ", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response updatePassword(@ApiParam(value = "request", required = true) @NotNull @Valid UpdatePasswordRequest updatePasswordRequest);

	
	@POST
	@Path("/addWishList")
	@ApiOperation(value = "addWishList", notes = "This operation will add the wish list product of the customer", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response addWishList(@ApiParam(value = "request", required = true) @NotNull @Valid AddWishListRequest addWishListRequest);

	@POST
	@Path("/getWishList")
	@ApiOperation(value = "getWishList", notes = "This operation will get the wish list product of the customer", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response getWishList(@ApiParam(value = "request", required = true) @NotNull @Valid AddWishListRequest addWishListRequest);
	
	@POST
	@Path("/removeFromWishList")
	@ApiOperation(value = "removeFromWishList", notes = "This operation will get the cart list product of the customer", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response removeFromWishList(@ApiParam(value = "request", required = true) @NotNull @Valid AddWishListRequest addWishListRequest);

	@POST
	@Path("/addToCart")
	@ApiOperation(value = "addToCart", notes = "This operation will add the cart list product of the customer", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response addToCart(@ApiParam(value = "request", required = true) @NotNull @Valid AddToCartRequest addToCartRequest);

	
	@POST
	@Path("/getCartList")
	@ApiOperation(value = "getCartList", notes = "This operation will get the cart list product of the customer", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response getCartList(@ApiParam(value = "request", required = true) @NotNull @Valid AddToCartRequest addToCartRequest);


	@POST
	@Path("/removeFromCartList")
	@ApiOperation(value = "removeFromCartList", notes = "This operation will get the cart list product of the customer", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response removeFromCartList(@ApiParam(value = "request", required = true) @NotNull @Valid AddToCartRequest addToCartRequest);

	@POST
	@Path("/emptyCartList")
	@ApiOperation(value = "removeFromCartList", notes = "This operation will empty the cart list ", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response emptyCartList(@ApiParam(value = "request", required = true) @NotNull @Valid AddToCartRequest addToCartRequest);
	
	/*Shreekant Rathod 29-1-2021*/
	@POST
	@Path("/addReviewAndRating")
	@ApiOperation(value = "addReviewAndRating", notes = "This operation will add the review and rating of  product from the customer", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response addReviewAndRating(@ApiParam(value = "request", required = true) @NotNull @Valid AddReviewAndRatingRequest addReviewAndRatingRequest);

	@POST
	@Path("/getReviewAndRating")
	@ApiOperation(value = "getCartList", notes = "This operation will get the review and rating list product of the customer", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response getReviewAndRating(@ApiParam(value = "request", required = true) @NotNull @Valid AddReviewAndRatingRequest addReviewAndRatingRequest);
	
	@POST
	@Path("/updateReviewAndStatus")
	@ApiOperation(value = "updateReviewAndStatus", notes = "This operation will update updateReviewAndStatus ", response = Response.class)
	@ApiResponses(value = { @ApiResponse(code = 0, message = "SUCCESS") })
	Response updateReviewAndStatus(@ApiParam(value = "request", required = true) @NotNull @Valid AddReviewAndRatingRequest addReviewAndRatingRequest);
}
