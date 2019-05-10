package com.fuso.enterprise.ots.srv.functional.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fuso.enterprise.ots.srv.api.model.domain.BalanceCan;
import com.fuso.enterprise.ots.srv.api.model.domain.CustomerOutstanding;
import com.fuso.enterprise.ots.srv.api.model.domain.CustomerProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.GetCustomerOutstandingAmt;
import com.fuso.enterprise.ots.srv.api.model.domain.ProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.RegistorToUserDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.UserMapping;
import com.fuso.enterprise.ots.srv.api.service.functional.OTSUserService;
import com.fuso.enterprise.ots.srv.api.service.request.AddUserDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.ApproveRegistrationBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.CustomerProductDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetCustomerOutstandingAmtBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.LoginAuthenticationBOrequest;
import com.fuso.enterprise.ots.srv.api.service.request.MapUsersDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.LoginUserResponse;
import com.fuso.enterprise.ots.srv.api.service.response.MapUsersDataBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.OutstandingCustomerResponse;
import com.fuso.enterprise.ots.srv.api.service.request.RequestBOUserBySearch;
import com.fuso.enterprise.ots.srv.api.service.response.UserDataBOResponse;
import com.fuso.enterprise.ots.srv.api.service.request.AddNewBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.UserRegistrationBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.ApproveRegistrationResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetCustomerOutstandingAmtBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetNewRegistrationResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.common.exception.ErrorEnumeration;
import com.fuso.enterprise.ots.srv.api.service.response.UserRegistrationResponce;
import com.fuso.enterprise.ots.srv.server.dao.CustomerOutstandingAmtDAO;
import com.fuso.enterprise.ots.srv.server.dao.MapUserProductDAO;
import com.fuso.enterprise.ots.srv.server.dao.ProductServiceDAO;
import com.fuso.enterprise.ots.srv.server.dao.UserMapDAO;
import com.fuso.enterprise.ots.srv.server.dao.UserRegistrationDao;
import com.fuso.enterprise.ots.srv.server.dao.UserServiceDAO;
import com.fuso.enterprise.ots.srv.server.dao.UserServiceUtilityDAO;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsRegistration;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;
import com.fuso.enterprise.ots.srv.server.util.FcmPushNotification;

@Service
@Transactional
public class OTSUserServiceImpl implements  OTSUserService{
	private Logger logger = LoggerFactory.getLogger(getClass());
	private UserServiceDAO userServiceDAO;
	private UserMapDAO userMapDAO;
	private UserServiceUtilityDAO userServiceUtilityDAO;
	private UserRegistrationDao userRegistrationDao;
	private MapUserProductDAO mapUserProductDAO;
	private FcmPushNotification fcmPushNotification;
	private CustomerOutstandingAmtDAO customerOutstandingAmtDAO;
	private ProductServiceDAO productServiceDAO;
	@Inject
	public OTSUserServiceImpl(UserServiceDAO userServiceDAO,UserMapDAO userMapDAO,UserServiceUtilityDAO userServiceUtilityDAO,UserRegistrationDao userRegistrationDao,ProductServiceDAO productServiceDAO,
			MapUserProductDAO mapUserProductDAO,CustomerOutstandingAmtDAO customerOutstandingAmtDAO) {
		this.userServiceDAO=userServiceDAO;
		this.userMapDAO=userMapDAO;
		this.userServiceUtilityDAO = userServiceUtilityDAO;
		this.userRegistrationDao =userRegistrationDao ;
		this.mapUserProductDAO=mapUserProductDAO;
		this.customerOutstandingAmtDAO = customerOutstandingAmtDAO;
		this.productServiceDAO = productServiceDAO;
	}

	@Override
	public UserDataBOResponse getUserIDUsers(String userId) {
		UserDataBOResponse userDataBOResponse = new UserDataBOResponse();
		try {
			List<UserDetails> userDetailList= userServiceDAO.getUserIdUsers(userId);
			userDataBOResponse.setUserDetails(userDetailList);
		}catch(Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return userDataBOResponse;
	}

	@Override
	public UserDataBOResponse checkForUserExistsOrNot(AddUserDataBORequest addUserDataBORequest) {
		UserDataBOResponse userDataBOResponse = new UserDataBOResponse();
		try {
			Integer flag = userRegistrationDao.CheckForExists(addUserDataBORequest);
			if(flag == 0){
				return userDataBOResponse = addNewUser(addUserDataBORequest);
			}else {
				return null;
			}
		}catch(Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
	}
	
	@Override
	public UserDataBOResponse addNewUser(AddUserDataBORequest addUserDataBORequest) {
				
		UserDataBOResponse userDataBOResponse = new UserDataBOResponse();
		
		MapUsersDataBORequest mapUsersDataBORequest = new MapUsersDataBORequest();
		CustomerProductDataBORequest customerProductDataBORequest = new CustomerProductDataBORequest();
	
		UserMapping userMapping = new UserMapping();
		CustomerProductDetails customerProductDetails = new CustomerProductDetails();
		try {
				/*
				 * Adding new user and return back the user object
				 */
				userDataBOResponse = userServiceDAO.addNewUser(addUserDataBORequest);
				String responseGenerateduserId = userDataBOResponse.getUserDetails().get(0).getUserId();
				
				/*
				 * End of add user
				 */
				
				/*
				 * Mapping the child user to the parent user 
				 */
				userMapping.setMappedTo(addUserDataBORequest.getRequestData().getMappedTo());
				userMapping.setUserId(responseGenerateduserId);
				mapUsersDataBORequest.setRequestData(userMapping);
				String userMappingStatus = mappUser(mapUsersDataBORequest);
				logger.info("Inside Event=1004,Class:OTSUserServiceImpl,Method:addNewUser, "
						+ "User Mapping  status : " + userMappingStatus);
				/*
				 * End of mapping 
				 */
				
				/*
				 * Add customer product price 
				 */
				
				String userProductMappingStatus = "";
				if(addUserDataBORequest.getRequestData().getProductId()!=null) {
					customerProductDetails.setUserId(responseGenerateduserId);
					customerProductDetails.setProductPrice(addUserDataBORequest.getRequestData().getProductPrice());
					customerProductDetails.setProductId(addUserDataBORequest.getRequestData().getProductId());
					customerProductDataBORequest.setRequestData(customerProductDetails);
					userProductMappingStatus = mapUserProduct(customerProductDataBORequest);
				}
				
				/*
				 * End of product price mapping to user
				 */
				
				logger.info("Inside Event=1004,Class:OTSUserServiceImpl,Method:addNewUser, "
						+ "Map User Product price status : " + userProductMappingStatus);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return userDataBOResponse;
	}

	@Override
	public UserDataBOResponse getUserDetails(RequestBOUserBySearch requestBOUserBySearch) {
		UserDataBOResponse userDataBOResponse = new UserDataBOResponse();
		try {
			List<UserDetails> userDetailList= userServiceUtilityDAO.getUserDetails(requestBOUserBySearch);
			userDataBOResponse.setUserDetails(userDetailList);
		}catch(Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return userDataBOResponse;
	}

	@Override
	public String mappUser(MapUsersDataBORequest mapUsersDataBORequest) {
		String responseData;
		try {
			responseData = userMapDAO.mappUser(mapUsersDataBORequest);
            } catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return responseData;
	}

	@Override
	public UserRegistrationResponce addUserRegistration( AddNewBORequest addNewBORequest) {
		UserRegistrationResponce userRegistrationResponce = null;
		try {
			if(addNewBORequest != null) {
				Integer flag = userServiceDAO.CheckForExists(addNewBORequest);
				if(flag == 0)
				{
					userRegistrationResponce = userRegistrationDao.addUserRegistration(addNewBORequest);
					try {
						/*To send Notification to Distributor when user get registor*/
						UserDetails User;
						User = userServiceDAO.getUserDetails(Integer.parseInt(addNewBORequest.getRequestData().getMappedTo()));
						if(addNewBORequest.getRequestData().getUserRoleId().equals("4")) {
						String notification = "Registration request from "+addNewBORequest.getRequestData().getFirstName()+" "+addNewBORequest.getRequestData().getLastName()+" as customer click to approve or reject";
						fcmPushNotification.sendPushNotification(User.getDeviceId(),"Bislari APP" , notification);
						/*To send Notification to Admin when user get registor*/
						User = userServiceDAO.getUserDetails(1);
						fcmPushNotification.sendPushNotification(User.getDeviceId(),"Bislari APP" , notification);
						}else if(addNewBORequest.getRequestData().getUserRoleId().equals("3")){
							String notification = "Registration request from "+addNewBORequest.getRequestData().getFirstName()+" "+addNewBORequest.getRequestData().getLastName()+" as Employee click to approve or reject";
							fcmPushNotification.sendPushNotification(User.getDeviceId(),"Bislari APP" , notification);
							/*To send Notification to Admin when user get registor*/
							User = userServiceDAO.getUserDetails(1);
							fcmPushNotification.sendPushNotification(User.getDeviceId(),"Bislari APP" , notification);	
						}else if(addNewBORequest.getRequestData().getUserRoleId().equals("2")) {
							String notification = "Registration request from "+addNewBORequest.getRequestData().getFirstName()+" "+addNewBORequest.getRequestData().getLastName()+" as Distributor click to approve or reject";
							fcmPushNotification.sendPushNotification(User.getDeviceId(),"Bislari APP" , notification);
						}
						
					}catch(Exception e) {
						return userRegistrationResponce;
					}
					
				}else
				{
					return null;
				}
			}
		}
		catch(Exception e) {
			throw new BusinessException(e,ErrorEnumeration.USR_REGISTER_failure);
		}
		return userRegistrationResponce;
	}

	@Override
	public GetNewRegistrationResponse getNewRegistration(String mappedTo) {
		GetNewRegistrationResponse getNewRegistrationResponse = new GetNewRegistrationResponse();
		// TODO Auto-generated method stub
		try {
			List<RegistorToUserDetails> userDetailList = userRegistrationDao.getNewRegistrationDao(mappedTo);
			getNewRegistrationResponse.setRegistorToUserDetails(userDetailList);  
		}catch(Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return getNewRegistrationResponse;	
	}

	@Override
	public LoginUserResponse otsLoginAuthentication(LoginAuthenticationBOrequest loginAuthenticationBOrequest) {	
		try {
			LoginUserResponse loginUserResponse = new LoginUserResponse();
			loginUserResponse = userServiceDAO.otsLoginAuthentication(loginAuthenticationBOrequest);
			if(loginUserResponse.getUserDetails().getUserRoleId().equals("4")||loginUserResponse.getUserDetails().getUserRoleId().equals("3")) {
				String did = userMapDAO.getMappedDistributor(loginUserResponse.getUserDetails().getUserId());
				loginUserResponse.getUserDetails().setDistributorId(did);
			}
			return loginUserResponse;
            } catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
	}

	@Override
	public String mapUserProduct(CustomerProductDataBORequest customerProductDataBORequest) {
		String responseData;
		try {
			responseData = mapUserProductDAO.mapUserProduct(customerProductDataBORequest);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return responseData;
	}

	@Override
	public ApproveRegistrationResponse approveRegistration(ApproveRegistrationBORequest approveRegistrationBORequest) {
	ApproveRegistrationResponse approveRegistrationResponse = new ApproveRegistrationResponse();
	AddUserDataBORequest addUserDataBORequest = new AddUserDataBORequest();
	try {
		if(approveRegistrationBORequest.getRequestData().getRegistrationId() != null) {
		/*
		* Transform the userDetail object based on registrationID from registration
			*/
			addUserDataBORequest.setRequestData(userRegistrationDao.fetchUserDetailsfromRegistration(approveRegistrationBORequest.getRequestData().getRegistrationId()));
			addUserDataBORequest.getRequestData().setProductPrice(approveRegistrationBORequest.getRequestData().getProductPrice());
			addUserDataBORequest.getRequestData().setProductId(approveRegistrationBORequest.getRequestData().getProductId());
			/*
			* AddNewUser from registration
			*/
			addNewUser(addUserDataBORequest);
			String notification ="Your Request have been approved";
			fcmPushNotification.sendPushNotification(addUserDataBORequest.getRequestData().getDeviceId(),"Registration for Bislari" , notification);

			/*
			* fetching OTSRegistration Object for approve status
			*/
			OtsRegistration otsRegistration = new OtsRegistration();
			otsRegistration = userRegistrationDao.fetOtsRegistrationBasedonRegisterID(Integer.parseInt(approveRegistrationBORequest.getRequestData().getRegistrationId()));
			/*
			* Making Registration table as active
			*/
			approveRegistrationResponse =userRegistrationDao.approveRegistration(otsRegistration);}
		} catch (Exception e) {
				throw new BusinessException(e.getMessage(), e);}
		return approveRegistrationResponse;
	}

	@Override
	public UserDataBOResponse getUserDetailsByMapped(String MappedTo) {
		UserDataBOResponse userDataBOResponse = new UserDataBOResponse();
		try {
			List<UserDetails> userDetailList= userServiceUtilityDAO.getUserDetailsByMapped(MappedTo);
			userDataBOResponse.setUserDetails(userDetailList);
		}catch(Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return userDataBOResponse;
	}

	@Override
	public OutstandingCustomerResponse getOutstandingData(String distributorId) {
		OutstandingCustomerResponse outstandingCustomerResponse = new OutstandingCustomerResponse();
		try {
			List<CustomerOutstanding> customerOutstandingList = new ArrayList<CustomerOutstanding>();
			List<UserDetails> userDetails = getUserDetailsByMapped(distributorId).getUserDetails();
			for(int i=0 ; i<userDetails.size() ; i++) {
				if(userDetails.get(i).getUserRoleId().equals("4")) {
					CustomerOutstanding custOuts = new CustomerOutstanding();
					custOuts.setCustomerId(Integer.parseInt(userDetails.get(i).getUserId()));
					custOuts.setCustomerName(userDetails.get(i).getFirstName());
					
					GetCustomerOutstandingAmtBORequest customerOutstandingAmtBORequest = new GetCustomerOutstandingAmtBORequest();
					GetCustomerOutstandingAmt customerOutstandingAmt = new GetCustomerOutstandingAmt();
					customerOutstandingAmt.setCustomerId(userDetails.get(i).getUserId());
					customerOutstandingAmtBORequest.setRequestData(customerOutstandingAmt);
					
					UserDetails User = userServiceDAO.getUserDetails(Integer.parseInt(userDetails.get(i).getUserId()));
					GetCustomerOutstandingAmtBOResponse getCustomerOutstandingAmtBOResponse = new GetCustomerOutstandingAmtBOResponse();
					String custOutAmt = "0";
					try {
						System.out.println("custOutAmt is before " +custOutAmt);
						getCustomerOutstandingAmtBOResponse = customerOutstandingAmtDAO.getCustomerOutstandingAmt(customerOutstandingAmtBORequest);
						
						/*custOutAmt = customerOutstandingAmtDAO.getCustomerOutstandingAmt(customerOutstandingAmtBORequest).getCustomerOutstandingAmount().get(0).getCustomerOutstandingAmt();*/
						custOuts.setOutstandingAmount(getCustomerOutstandingAmtBOResponse.getCustomerOutstandingAmount().get(0).getCustomerOutstandingAmt());
						System.out.println("afte func " +custOutAmt);
					}catch(Exception e) {
						logger.error("Inside Event=1004,Class:OTSUserServiceImpl,Method:getOutstandingData, "
								+ "User Outstanding Amount  error for customer id  : " + userDetails.get(i).getFirstName());
					}
					try {
						List<BalanceCan> balanceCan = mapUserProductDAO.getBalanceCanByUserId(userDetails.get(i).getUserId().toString());
						for(int j =0 ;j<balanceCan.size() ; j++) {
							ProductDetails productDetails = productServiceDAO.getProductDetils(balanceCan.get(j).getProductId());
							balanceCan.get(j).setProductName(productDetails.getProductName());
						}
						custOuts.setBalanceCan(balanceCan);
					}catch(Exception e) {
						logger.error("Inside Event=1004,Class:OTSUserServiceImpl,Method:getOutstandingData, "
								+ "User Outstanding can  error for customer id  : " + userDetails.get(i).getFirstName());
					}
					customerOutstandingList.add(custOuts);
				}
			}
			outstandingCustomerResponse.setCustomerOutstandingList(customerOutstandingList);
		}catch(Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return outstandingCustomerResponse;
	}
}
