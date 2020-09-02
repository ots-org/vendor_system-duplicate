package com.fuso.enterprise.ots.srv.rest.ws.service;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.jvnet.hk2.annotations.Service;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;

import com.fuso.enterprise.ots.srv.api.model.domain.LoginAuthenticationModel;
import com.fuso.enterprise.ots.srv.api.model.domain.RejectUserModel;
import com.fuso.enterprise.ots.srv.api.service.functional.OTSUserService;
import com.fuso.enterprise.ots.srv.api.service.request.AddUserDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.ApproveRegistrationBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.ChangePasswordRequest;
import com.fuso.enterprise.ots.srv.api.service.request.CustomerProductDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.ForgotPasswordRequest;
import com.fuso.enterprise.ots.srv.api.service.request.LoginAuthenticationBOrequest;
import com.fuso.enterprise.ots.srv.api.service.request.MapUsersDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.LoginUserResponse;
import com.fuso.enterprise.ots.srv.api.service.response.MapUsersDataBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.OutstandingCustomerResponse;
import com.fuso.enterprise.ots.srv.api.service.response.UserDataBOResponse;
import com.fuso.enterprise.ots.srv.api.service.request.AddNewBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.MappedToBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.OutstandingRequest;
import com.fuso.enterprise.ots.srv.api.service.response.ApproveRegistrationResponse;
import com.fuso.enterprise.ots.srv.api.service.response.ForgotPasswordResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetNewRegistrationResponse;

import com.fuso.enterprise.ots.srv.api.service.response.UserRegistrationResponce;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.common.exception.ErrorEnumeration;
import com.fuso.enterprise.ots.srv.server.util.ResponseWrapper;
import com.fuso.enterprise.ots.srv.api.service.request.RequestBOUserBySearch;
import com.fuso.enterprise.ots.srv.api.service.request.UpdatePassword;
import com.fuso.enterprise.ots.srv.api.service.request.UpdatePasswordRequest;

public class OTSUsersV18_1WsImpl implements OTSUsersV18_1Ws{
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	ResponseWrapper responseWrapper ;
	@Inject
	private OTSUserService otsUserService;

	@Override
	public Response getUserIDUsers(String userId) {
		Response response =null;
		logger.info("Inside Event=1,Class:OTSUsersV18_1WsImpl, Method:getUserIDUsers, UserId:"+userId);
		UserDataBOResponse UserDataBOResponse = new UserDataBOResponse();
		try {
			UserDataBOResponse = otsUserService.getUserIDUsers(userId);
			if(UserDataBOResponse!=null) {
				logger.info("Inside Event=1,Class:OTSUsersV18_1WsImpl,Method:getUserIDUsers, "
						+ "UserList Size:" +UserDataBOResponse.getUserDetails().size());
			}
			response = responseWrapper.buildResponse(UserDataBOResponse);
			
		}catch(BusinessException e) {
			throw new BusinessException(e.getMessage(), e);
		}catch(Throwable e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return response;
	}

	@Override
	public Response addNewUser(AddUserDataBORequest addUserDataBORequest) {
		Response response = null;
		logger.info("Inside Event=1004,Class:OTSUsersV18_1WsImpl, Method:addNewUser, UserDataBORequest:"
				+ addUserDataBORequest.getRequestData().getFirstName());
		UserDataBOResponse UserDataBOResponse = new UserDataBOResponse();
		try{
			addUserDataBORequest.getRequestData().setUserLat("0");
			addUserDataBORequest.getRequestData().setUserLong("0");
			UserDataBOResponse = otsUserService.checkForUserExistsOrNot(addUserDataBORequest);
			if (UserDataBOResponse != null) {
				logger.info("Inside Event=1004,Class:OTSUsersV18_1WsImpl,Method:addNewUser, " + "UserList Size:"
						+ UserDataBOResponse.getUserDetails().size());
				response = responseWrapper.buildResponse(UserDataBOResponse,"successful");
			}else
			{
				response = responseWrapper.buildResponse(UserDataBOResponse,"User Details are already present In DB");
			}
		}catch (BusinessException e){
			throw new BusinessException(e, ErrorEnumeration.USR_REGISTER_failure);
	    }catch (Throwable e) {
	    	throw new BusinessException(e, ErrorEnumeration.USR_REGISTER_failure);	    }
		return response;
	}

	@Override
	public Response mappUser(MapUsersDataBORequest mapUsersDataBORequest) {
		String responseData;
		Response response;
		logger.info("Inside Event=1005,Class:OTSUsersV18_1WsImpl, Method:mappUser, UserId:" + mapUsersDataBORequest.getRequestData().getUserId());
	  try {
		  responseData = otsUserService.mappUser(mapUsersDataBORequest);
			if (responseData != null) {
				logger.info("Inside Event=1005,Class:OTSUsersV18_1WsImpl,Method:mappUser, " + "successful");
			}
			response = buildResponse(200,responseData);
		} catch (BusinessException e) {
			throw new BusinessException(e, ErrorEnumeration.USER_MAPPINGTO_FAILURE);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.USER_MAPPINGTO_FAILURE);
		}
		return response;
	}

	@Override
	public Response getUserDetails(RequestBOUserBySearch requestBOUserBySearch) {		
	if(!requestBOUserBySearch.getRequestData().getSearchKey().isEmpty()|| !requestBOUserBySearch.getRequestData().getSearchKey().isEmpty())
		{ 
			Response response =null;
			logger.info("Inside Event=VM1-T18 and VM1-T13,Class:OTSUsersV18_1WsImpl, Method:getUserDetails, RequestBOUserBySearch:"+requestBOUserBySearch);
			UserDataBOResponse UserDataBOResponse = new UserDataBOResponse();
			try {
				UserDataBOResponse = otsUserService.getUserDetails(requestBOUserBySearch);
				if(UserDataBOResponse!=null) {
					logger.info("Inside Event=1008,Class:OTSUsersV18_1WsImpl,Method:getUserDetails, "
							+ "UserList Size:" +UserDataBOResponse.getUserDetails().size());
				}
				if(UserDataBOResponse.getUserDetails().size() == 0) {
					response = buildResponse(600,"input is not present in DB");
				}else{
					response = buildResponse(UserDataBOResponse,"successful");
				}
				
			}catch(BusinessException e) {
				throw new BusinessException(e.getMessage(), e);
			}catch(Throwable e) {
				throw new BusinessException(e.getMessage(), e);
			}
		
			return response;
		}else
			
		{
			Response response = buildResponse(600,"Check Input");
			return response;
		}
	}

	@Override
	public Response addUserRegistration( AddNewBORequest addNewBORequest) {
		Response response;
		logger.info("Inside Event=1001,Class:OTSUsersV18_1WsImpl, addUserRegistration, "
				+ "addNewBORequest:"+addNewBORequest.getRequestData().getEmailId()+",FirstName:"+addNewBORequest.getRequestData().getFirstName()+",LastName:"+addNewBORequest.getRequestData().getLastName());
		UserRegistrationResponce userRegistrationResponce = new UserRegistrationResponce();
		try {
			userRegistrationResponce = otsUserService.addUserRegistration(addNewBORequest);  
			if(!userRegistrationResponce.getEmailId().isEmpty()) {
					logger.info("Inside Event=1001,Class:OTSUsersV18_1WsImpl,Method:addUserRegistration, "
							+ "UserEmail:" +userRegistrationResponce.getEmailId());
				}
			response = responseWrapper.buildResponse(userRegistrationResponce.getEmailId(),"User successfuly Added for Registration");
			}catch(BusinessException e) {
				throw new BusinessException(e,ErrorEnumeration.USR_REGISTER_failure);
				}catch(Throwable e) {
				throw new BusinessException(e,ErrorEnumeration.USR_REGISTER_failure);
			}
		return response;
	}

	@Override
	public Response getNewRegistration(MappedToBORequest mappedToBORequest) {
		Response response = null;
		String strMessage = "There Are No Mapped Users";
		logger.info("Inside Event=1002,Class:OTSUsersV18_1WsImpl, addUserRegistration, "
				+ "addNewBORequest, getMappedUser for UserId:"+mappedToBORequest.getRequestData().getMappedTo());
		GetNewRegistrationResponse getNewRegistrationResponse = new GetNewRegistrationResponse();
		try {
			getNewRegistrationResponse = otsUserService.getNewRegistration(mappedToBORequest.getRequestData().getMappedTo());
			if(!getNewRegistrationResponse.getRegistorToUserDetails().isEmpty()) {
				logger.info("Inside Event=1002,Class:OTSUsersV18_1WsImpl,Method:getNewRegistration, "
						+ "UserListSize:" + getNewRegistrationResponse.getRegistorToUserDetails().size());
				strMessage ="User Retrieved Succesfully";
			}
			response = responseWrapper.buildResponse(getNewRegistrationResponse.getRegistorToUserDetails(),strMessage);
		}catch(Exception e) {
			throw new BusinessException(e,ErrorEnumeration.USR_REGISTER_failure);
		}
		return response;
	}

	

	@Override
	public Response otsLoginAuthentication(LoginAuthenticationBOrequest loginAuthenticationBOrequest) {
		if(!loginAuthenticationBOrequest.getRequestData().getPhoneNumber().isEmpty()|| !loginAuthenticationBOrequest.getRequestData().getPassword().isEmpty())
		{ 
			Response response =null;
			logger.info("Inside Event=VM1-T28,Class:OTSUsersV18_1WsImpl, Method:otsLoginAuthentication, loginAuthenticationBOrequest:"+loginAuthenticationBOrequest);
			LoginUserResponse loginUserResponse = new LoginUserResponse();
			try {
				loginUserResponse = otsUserService.otsLoginAuthentication(loginAuthenticationBOrequest);
				if(loginUserResponse!=null) {
					logger.info("Inside Event=1008,Class:OTSUsersV18_1WsImpl,Method:getUserDetails, "
							+ "UserData" +loginUserResponse.getUserDetails());
				}
				if(loginUserResponse.getUserDetails().getUsrStatus().equalsIgnoreCase("pending")){
					response = buildResponse(600,"please wait or ask for admin approval");
				}else if(loginUserResponse.getUserDetails().getUsrStatus().equalsIgnoreCase("reject")){
					response = buildResponse(600,"your approval process is rejected");
				}else if (BCrypt.checkpw(loginAuthenticationBOrequest.getRequestData().getPassword(), loginUserResponse.getUserDetails().getUsrPassword())) {
					response = buildResponse(loginUserResponse,"Successful");
				}else{
					response = buildResponse(600,"Password is Incorrect");
				}
			}catch(BusinessException e) {
				throw new BusinessException(e,ErrorEnumeration.USER_NOT_EXISTS);
			}catch(Throwable e) {
				throw new BusinessException(e,ErrorEnumeration.USER_NOT_EXISTS);
			}
		
			return response;
		}else
		{
			Response response = buildResponse(600,"Check Input");
			return response;
		}
	}


	@Override
	public Response mapUserProduct(CustomerProductDataBORequest customerProductDataBORequest) {
		String responseData;
		Response response =null;
		logger.info("Inside Event=1006,Class:OTSUsersV18_1WsImpl, Method:mapUserProduct, UserId:" + customerProductDataBORequest.getRequestData().getUserId());
		try {
		  responseData =otsUserService.mapUserProduct(customerProductDataBORequest);
			if (responseData != null) {
				logger.info("Inside Event=1006,Class:OTSUsersV18_1WsImpl,Method:mapUserProduct " + "successful");
			}
			response = buildResponse(200,responseData);
		} catch (BusinessException e) {
			throw new BusinessException(e, ErrorEnumeration.USER_MAPPING_PRODUCT_FAILURE);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.USER_MAPPING_PRODUCT_FAILURE);
		}
	  return response;
	}
	
	@Override
	public Response approveRegistration(ApproveRegistrationBORequest approveRegistrationBORequest) {
		Response response = null;
		logger.info("Inside Event=1003,Class:OTSUsersV18_1WsImpl, approveRegistration, "
			+ "ApproveRegistrationBODomineRequest, approveRegistration for UserId:"+approveRegistrationBORequest.getRequestData().getRegistrationId());
		
		ApproveRegistrationResponse approveRegistrationResponse = new ApproveRegistrationResponse();
		try {
			approveRegistrationResponse = otsUserService.approveRegistration(approveRegistrationBORequest);
			if(!approveRegistrationResponse.getFirstName().isEmpty()) {
			response = buildResponse(approveRegistrationResponse.getFirstName(),"successfuly approved. User can login with his credentials");
			}
		}catch(BusinessException e) {
			throw new BusinessException(e,ErrorEnumeration.APPROVE_REGISTRATION_FAILURE);
		}catch(Throwable e) {
			throw new BusinessException(e,ErrorEnumeration.APPROVE_REGISTRATION_FAILURE);
		}		
		return response;
	}
	
	
	public Response buildResponse(Object data,String description) {
		ResponseWrapper wrapper = new ResponseWrapper(200,description, data);
		return Response.ok(wrapper).build();
	}
	
	public Response buildResponse(int code,String description) {
		ResponseWrapper wrapper = new ResponseWrapper(code,description);
		return Response.ok(wrapper).build();
	}
	

	@Override
	public Response getUserDetailsByMapped(MappedToBORequest mappedToBORequest) {
		try {	
//			if(!mappedToBORequest.getRequestData().getMappedTo().isEmpty())
//				{
					Response response =null;
					logger.info("Inside Event=1,Class:OTSUsersV18_1WsImpl, Method:getUserDetailsByMapped, mappedToBORequest:"+mappedToBORequest);
					UserDataBOResponse UserDataBOResponse = new UserDataBOResponse();
	
					UserDataBOResponse = otsUserService.getUserDetailsByMapped(mappedToBORequest.getRequestData().getMappedTo());
					if(UserDataBOResponse!=null) {
						logger.info("Inside Event=1,Class:OTSUsersV18_1WsImpl,Method:getUserIDUsers, "
									+ "UserList Size:" +UserDataBOResponse.getUserDetails().size());
						response = responseWrapper.buildResponse(UserDataBOResponse);
						return response;
					}else {
						response = responseWrapper.buildResponse("No One is Mapped");
						return response;
					}
			
//		}else
//		{
//			Response response = buildResponse(600,"Please Check your Input");
//			return response;
//		}
	
		}catch(BusinessException e) {
			throw new BusinessException(ErrorEnumeration.Mapped_to_value_is_empty);
		}catch(Throwable e) {
			throw new BusinessException(ErrorEnumeration.Mapped_to_value_is_empty);
		}
		
	}

	@Override
	public Response getCustomerOutstandingData(OutstandingRequest outstandingRequest) {
		// TODO Auto-generated method stub
		if(!outstandingRequest.getDistributorId().isEmpty()){
			Response response =null;
			logger.info("Inside Event=12,Class:OTSUsersV18_1WsImpl, getCustomerOutstandingData, OutstandingRequest.ditributorID:"+outstandingRequest.getDistributorId());
			OutstandingCustomerResponse outstandingCustomerResponse = new OutstandingCustomerResponse();
			outstandingCustomerResponse =  otsUserService.getOutstandingData(outstandingRequest);
			if(outstandingCustomerResponse!=null) {
				logger.info("Inside Event=12,Class:OTSUsersV18_1WsImpl,getCustomerOutstandingData, "
							+ "Customer List Size:" +outstandingCustomerResponse.getCustomerOutstandingList().size());
				response = responseWrapper.buildResponse(outstandingCustomerResponse);
				return response;
			}else {
				response = responseWrapper.buildResponse("No customer mapped for the distributor to show the outstanding amount");
				return response;
			}
		}else{
			Response response = buildResponse(600,"Please Check your Input");
			return response;
		}
	}

	@Override
	public Response rejectUser(RejectUserModel rejectUserModel) {
		try {
				Response response = null;
				response =  responseWrapper.buildResponse(otsUserService.rejectUser(rejectUserModel),"user rejected successfuly");
				return response;
			}
			catch(BusinessException e) {
				throw new BusinessException(e.getMessage(), e);
			}catch(Throwable e) {
				throw new BusinessException(e.getMessage(), e);
			}
		}

	@Override
	public Response forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
		Response response = null;
		ForgotPasswordResponse forgotPasswordResponse = new ForgotPasswordResponse();
		forgotPasswordResponse = otsUserService.sendOTP(forgotPasswordRequest);
		if(forgotPasswordResponse.getUserId()!=null) {
			response =  responseWrapper.buildResponse(forgotPasswordResponse,"OTP sent to mail");
		}else {
			response = buildResponse(600,"Please check the number or contact the admin");
		}
		return response;
	}

	@Override
	public Response changePassword(ChangePasswordRequest changePasswordRequest) {
		// TODO Auto-generated method stub
		Response response = null;
		response = responseWrapper.buildResponse(otsUserService.changePassword(changePasswordRequest),"Password Updated.Please login");
		return response;
	}

	@Override
	public Response updatePassword(UpdatePasswordRequest updatePasswordRequest) {
		Response response = null;
		try {
			if(otsUserService.updatePassword(updatePasswordRequest).equalsIgnoreCase("200")) {
				response = buildResponse(200,"Password updated");
				
			}else {
				response = buildResponse(600,"please check your old password");
			}
		}catch(Exception e) {
			response = responseWrapper.buildResponse("Some thing went wrong");
		}
		return response;
	}

}
