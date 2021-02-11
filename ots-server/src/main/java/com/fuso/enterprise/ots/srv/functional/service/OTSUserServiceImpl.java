 package com.fuso.enterprise.ots.srv.functional.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import javax.inject.Inject;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fuso.enterprise.ots.srv.api.model.domain.BalanceCan;
import com.fuso.enterprise.ots.srv.api.model.domain.CustomerOutstanding;
import com.fuso.enterprise.ots.srv.api.model.domain.CustomerProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.GetCustomerOutstandingAmt;
import com.fuso.enterprise.ots.srv.api.model.domain.ProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.RegistorToUserDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.RejectUserModel;
import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.UserMapping;
import com.fuso.enterprise.ots.srv.api.service.functional.OTSUserService;
import com.fuso.enterprise.ots.srv.api.service.request.AddUserDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddWishListRequest;
import com.fuso.enterprise.ots.srv.api.service.request.ApproveRegistrationBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.ChangePasswordRequest;
import com.fuso.enterprise.ots.srv.api.service.request.CustomerProductDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.ForgotPasswordRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetCustomerOutstandingAmtBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.LoginAuthenticationBOrequest;
import com.fuso.enterprise.ots.srv.api.service.request.MapUsersDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.OutstandingRequest;
import com.fuso.enterprise.ots.srv.api.service.response.LoginUserResponse;
import com.fuso.enterprise.ots.srv.api.service.response.OutstandingCustomerResponse;
import com.fuso.enterprise.ots.srv.api.service.request.RequestBOUserBySearch;
import com.fuso.enterprise.ots.srv.api.service.request.UpdatePasswordRequest;
import com.fuso.enterprise.ots.srv.api.service.response.UserDataBOResponse;
import com.fuso.enterprise.ots.srv.api.service.request.AddNewBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddReviewAndRatingRequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddToCartRequest;
import com.fuso.enterprise.ots.srv.api.service.response.ApproveRegistrationResponse;
import com.fuso.enterprise.ots.srv.api.service.response.ForgotPasswordResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetCustomerOutstandingAmtBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetNewRegistrationResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetReviewAndRatingResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetcartListResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetwishListResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.common.exception.ErrorEnumeration;
import com.fuso.enterprise.ots.srv.api.service.response.UserRegistrationResponce;
import com.fuso.enterprise.ots.srv.server.dao.CartDAO;
import com.fuso.enterprise.ots.srv.server.dao.CustomerOutstandingAmtDAO;
import com.fuso.enterprise.ots.srv.server.dao.MapUserProductDAO;
import com.fuso.enterprise.ots.srv.server.dao.OtsProductWishlistDAO;
import com.fuso.enterprise.ots.srv.server.dao.ProductServiceDAO;
import com.fuso.enterprise.ots.srv.server.dao.ReviewAndRatingDAO;
import com.fuso.enterprise.ots.srv.server.dao.UserMapDAO;
import com.fuso.enterprise.ots.srv.server.dao.UserRegistrationDao;
import com.fuso.enterprise.ots.srv.server.dao.UserServiceDAO;
import com.fuso.enterprise.ots.srv.server.dao.UserServiceUtilityDAO;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsRegistration;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUserMapping;
import com.fuso.enterprise.ots.srv.server.util.EmailUtil;
import com.fuso.enterprise.ots.srv.server.util.FcmPushNotification;
import com.fuso.enterprise.ots.srv.server.util.OTSUtil;
import com.fuso.enterprise.ots.srv.server.util.SmsApi;

@Service
@Transactional
public class OTSUserServiceImpl implements  OTSUserService{
	
	@Value("${product.percentage.price}")
	public String productPercentage;

	private Logger logger = LoggerFactory.getLogger(getClass());
	private UserServiceDAO userServiceDAO;
	private UserMapDAO userMapDAO;
	private UserServiceUtilityDAO userServiceUtilityDAO;
	private UserRegistrationDao userRegistrationDao;
	private MapUserProductDAO mapUserProductDAO;
	private FcmPushNotification fcmPushNotification;
	private CustomerOutstandingAmtDAO customerOutstandingAmtDAO;
	private ProductServiceDAO productServiceDAO;
	private OtsProductWishlistDAO otsProductWishlistDAO;
	private CartDAO cartDAO;
	private ReviewAndRatingDAO reviewAndRatingDAO;
	
	@Inject
	public OTSUserServiceImpl(UserServiceDAO userServiceDAO,UserMapDAO userMapDAO,UserServiceUtilityDAO userServiceUtilityDAO,UserRegistrationDao userRegistrationDao,ProductServiceDAO productServiceDAO,
			MapUserProductDAO mapUserProductDAO,CustomerOutstandingAmtDAO customerOutstandingAmtDAO,OtsProductWishlistDAO otsProductWishlistDAO,CartDAO cartDAO,ReviewAndRatingDAO reviewAndRatingDAO) {
		this.userServiceDAO=userServiceDAO;
		this.userMapDAO=userMapDAO;
		this.userServiceUtilityDAO = userServiceUtilityDAO;
		this.userRegistrationDao =userRegistrationDao ;
		this.mapUserProductDAO=mapUserProductDAO;
		this.customerOutstandingAmtDAO = customerOutstandingAmtDAO;
		this.productServiceDAO = productServiceDAO;
		this.otsProductWishlistDAO = otsProductWishlistDAO;
		this.cartDAO=cartDAO;
		this.reviewAndRatingDAO=reviewAndRatingDAO;
	}

	@Override
	public UserDataBOResponse getUserIDUsers(String userId) {
		UserDataBOResponse userDataBOResponse = new UserDataBOResponse();
		try {
			List<UserDetails> userDetailList= userServiceDAO.getUserIdUsers(userId);
			userDataBOResponse.setUserDetails(userDetailList);
			//razorPay();
		}catch(Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return userDataBOResponse;
	}
	
	@Override
	public UserDataBOResponse checkForUserExistsOrNot(AddUserDataBORequest addUserDataBORequest) {
		UserDataBOResponse userDataBOResponse = new UserDataBOResponse();
		try {
			Integer flag = 0;
			if(addUserDataBORequest.requestData.getUserId()=="" || addUserDataBORequest.requestData.getUserId()=="0") {
			// = userRegistrationDao.CheckForExists(addUserDataBORequest);
			}
			if(addUserDataBORequest.requestData.getUserRoleId().equalsIgnoreCase("4")) {
				addUserDataBORequest.requestData.setMappedTo("1");
			}
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
				if(addUserDataBORequest.requestData.getUserId() == ""|| addUserDataBORequest.requestData.getUserId() == null || addUserDataBORequest.requestData.getUserId() == "0") {
					userDataBOResponse = userServiceDAO.addNewUser(addUserDataBORequest);
				}else {
					userDataBOResponse = userServiceDAO.updateUser(addUserDataBORequest);
				}
				
				String responseGenerateduserId = userDataBOResponse.getUserDetails().get(0).getUserId();
				
				/*
				 * End of add user
				 */
				
				/*
				 * Mapping the child user to the parent user 
				 */
				if(addUserDataBORequest.getRequestData().getMappedTo()!=null)	{
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
				}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return userDataBOResponse;
	}

	@Override
	public UserDataBOResponse getUserDetails(RequestBOUserBySearch requestBOUserBySearch) {
		UserDataBOResponse userDataBOResponse = new UserDataBOResponse();
		List<OtsUserMapping> UserList = new ArrayList<OtsUserMapping>();
		List<UserDetails> userDetailsList = new ArrayList<UserDetails>();
		
		try {
			requestBOUserBySearch.getRequestData().setDistributorId("1");
			if(!(requestBOUserBySearch.getRequestData().getSearchKey().equalsIgnoreCase("UsersFirstname")
			 ||requestBOUserBySearch.getRequestData().getSearchKey().equalsIgnoreCase("UsersLastname")
			 ||requestBOUserBySearch.getRequestData().getSearchKey().equalsIgnoreCase("UsersEmailid")
			 ||requestBOUserBySearch.getRequestData().getSearchKey().equalsIgnoreCase("UserRoleId")
			 ||requestBOUserBySearch.getRequestData().getSearchKey().equalsIgnoreCase("pending"))) {
				if(requestBOUserBySearch.getRequestData().getDistributorId()!=null) {
					UserList = userMapDAO.getUserForDistributor(requestBOUserBySearch.getRequestData().getDistributorId());			
					for(int i =0 ; i<UserList.size();i++) {
						UserDetails userData = new UserDetails();					
						if(requestBOUserBySearch.getRequestData().getSearchvalue().equals("3")) {
							userData = userServiceDAO.getUserDetailsForEmployee(UserList.get(i).getOtsUsersId().getOtsUsersId());
							if(userData != null) {
								userDetailsList.add(userData);
								}
						}else if(requestBOUserBySearch.getRequestData().getSearchvalue().equals("4")) {
							userData = userServiceDAO.getUserDetailsForCustomer(UserList.get(i).getOtsUsersId().getOtsUsersId());
							if(userData != null) {
								userData.setCustomerProductDetails(mapUserProductDAO.getCustomerProductDetailsByCustomerId(userData.getUserId()));
								userDetailsList.add(userData);
							}
						}else if(requestBOUserBySearch.getRequestData().getSearchvalue().equals("2")){
							userData = userServiceDAO.getUserDetails(UserList.get(i).getOtsUsersId().getOtsUsersId());
							if(userData != null) {
								//userData.setCustomerProductDetails(mapUserProductDAO.getCustomerProductDetailsByCustomerId(userData.getUserId()));
								userDetailsList.add(userData);
							}
						}
						
					}
					
					userDataBOResponse.setUserDetails(userDetailsList);
				}
			}else{
				List<UserDetails> userDetailList= userServiceUtilityDAO.getUserDetails(requestBOUserBySearch);
				userDataBOResponse.setUserDetails(userDetailList);
			}
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
            	throw new BusinessException(e,ErrorEnumeration.USER_NOT_EXISTS);
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
	public OutstandingCustomerResponse getOutstandingData(OutstandingRequest outstandingRequest) {
		OutstandingCustomerResponse outstandingCustomerResponse = new OutstandingCustomerResponse();
		try {
			List<CustomerOutstanding> customerOutstandingList = new ArrayList<CustomerOutstanding>();
			List<UserDetails> userDetails = getUserDetailsByMapped(outstandingRequest.getDistributorId()).getUserDetails();
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
			if(outstandingRequest.getPdf().equalsIgnoreCase("YES")) {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");  
				LocalDateTime now = LocalDateTime.now(); 
				String pdf = customerOutStandingReportPDF(customerOutstandingList,userServiceDAO.getUserIdUsers(outstandingRequest.getDistributorId()).get(0).getFirstName(),now.toString().substring(0, 10));
				outstandingCustomerResponse.setPdf(pdf);
			}
			outstandingCustomerResponse.setCustomerOutstandingList(customerOutstandingList);
		}catch(Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return outstandingCustomerResponse;
	}
	@Override
	public String rejectUser(RejectUserModel rejectUserModel) {
		try {
			AddUserDataBORequest addUserDataBORequest = new AddUserDataBORequest();
			addUserDataBORequest.setRequestData(userRegistrationDao.fetchUserDetailsfromRegistration(rejectUserModel.getRequest().getRegistrationId()));
			String notification ="You are water Distribution request have been rejected";
			fcmPushNotification.sendPushNotification(addUserDataBORequest.getRequestData().getDeviceId(),"Registration for Bislari" , notification);
			return userRegistrationDao.UpdateStatus(rejectUserModel);
		}catch(Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		
	}
	
	@Override
	public ForgotPasswordResponse sendOTP(ForgotPasswordRequest forgotPasswordRequest) {
		ForgotPasswordResponse forgotPasswordResponse = new ForgotPasswordResponse();
		UserDetails userDetails = new UserDetails();
		try {
			Random rand = new Random(); 
			int otp = rand.nextInt(10000);
			if(userServiceDAO.checkForOTP(forgotPasswordRequest.getRequest().getMobileNumber())!=null) {
				EmailUtil.sendOTP(userServiceDAO.checkForOTP(forgotPasswordRequest.getRequest().getMobileNumber()).getEmailId(), "","Etaarana OTP verification", "Your one time password for Etaarana is "+ otp+" Please do not share this with anyone.");
				userDetails = userServiceDAO.checkForOTP(forgotPasswordRequest.getRequest().getMobileNumber());
				forgotPasswordResponse.setOtp(String.valueOf(otp));
				forgotPasswordResponse.setUserId(userDetails.getUserId());
				return  forgotPasswordResponse;
			}
		}catch(Exception e) {
			return	null;
		}
		return  forgotPasswordResponse;
	}
	
	@Override
	public String changePassword(ChangePasswordRequest changePasswordRequest) {
		return userServiceDAO.changePassword(changePasswordRequest);
	}

	@Override
	public String updatePassword(UpdatePasswordRequest updatePasswordRequest) {
		// TODO Auto-generated method stub
		return userServiceDAO.updatePassword(updatePasswordRequest);
	}
	public String customerOutStandingReportPDF(List<CustomerOutstanding> customerOutstandings,String distributorName, String date ) {
		String tableValueString ="";
		String reportDetails = "<head ><h3 style='text-align:center;'>Customer Outstanding Report</h3></head>";
		reportDetails += "<head><h3>Distributer Name :"+distributorName+"</h3></head>";
		reportDetails += "<head ><h3>Date:"+date+"</h3></head>  </br>";
		
		int slno=0;
		tableValueString = "<table border=\"1\"><tr>\r\n" + 
				"	<th>Sl no</th>\r\n" + 
				"	<th>Customer Name</th>\r\n" + 
				"    <th>OutStanding Amount</th>\r\n" + 
				"	<th>Product Name</th>\r\n" +
				"	<th>Balance can</th>\r\n" +
				"</tr>";
		String productList= " ";
		String balanceCan = " ";
		for(CustomerOutstanding customerOutStandingDetails:customerOutstandings) {
			for(int i=0; i<customerOutStandingDetails.getBalanceCan().size(); i++){
				productList +=  "<table border=\"0\"><tr>\r\n" + 
						"	<td>"+customerOutStandingDetails.getBalanceCan().get(i).getProductName()+"</td>\r\n" +
						"</tr>";
				balanceCan += "<table border=\"0\"><tr>\r\n" + 
						"	<td>"+customerOutStandingDetails.getBalanceCan().get(i).getBalanceCan()+"</td>\r\n" +
						"</tr>";
				productList=productList+ "</table>";
				balanceCan = balanceCan+"</table>";
			}
			slno++;
			tableValueString=tableValueString+"<tr>\r\n" + 
					"	<td>"+slno+"</td>\r\n" + 
					"   <td>"+customerOutStandingDetails.getCustomerName()+"</td>\r\n" +
					"	<td>"+customerOutStandingDetails.getOutstandingAmount()+"</td>\r\n" +
					"   <td>"+productList+"</td>\r\n" +
					"   <td>"+balanceCan+"</td>\r\n" +
					"</tr>";
			productList="";
			balanceCan ="";
	}
		
	tableValueString =tableValueString+ "</table>";
	String htmlString = "<html>"+reportDetails+tableValueString+"</html>";
	String path = OTSUtil.generateReportPDFFromHTML(htmlString,"CustomerOutStandingRepo.pdf");
	byte[] fileContent;
	String encodedString = null;
	try {
		fileContent = FileUtils.readFileToByteArray(new File(path));
		encodedString = Base64.getEncoder().encodeToString(fileContent);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return encodedString;	
		
	}

	@Override
	public String addWishList(AddWishListRequest addWishListRequest) {
		return otsProductWishlistDAO.addWishList(addWishListRequest);
	}

	@Override
	public List<GetwishListResponse> getwishList(AddWishListRequest addWishListRequest) {
		List<GetwishListResponse> wishList = otsProductWishlistDAO.getwishList(addWishListRequest);
		for(int i=0;i<wishList.size();i++) {
			Float result =	Float.parseFloat(wishList.get(i).getProductPrice().toString()) + ((Float.parseFloat(wishList.get(i).getProductPrice().toString())* Float.parseFloat(productPercentage))/100);
			BigDecimal resultBigDecimal = new BigDecimal(result);
			wishList.get(i).setProductPrice(resultBigDecimal);
		}
		return wishList;
	}

	@Override
	public String addToCart(AddToCartRequest addToCartRequest) {
		return cartDAO.addToCart(addToCartRequest);
	}

	@Override
	public List<GetcartListResponse> getcartList(AddToCartRequest addToCartRequest) {
		GetcartListResponse getcartListResponse= new GetcartListResponse();
		return cartDAO.getcartList(addToCartRequest);
	}

	@Override
	public String removeFromCart(AddToCartRequest addToCartRequest) {
		return cartDAO.removeFromCart(addToCartRequest);
	}

	@Override
	public String removeFromWishList(AddWishListRequest addWishListRequest) {
	
		return otsProductWishlistDAO.removeFromWishList(addWishListRequest);
	}

	@Override
	public String emptyCart(AddToCartRequest addToCartRequest) {
		return cartDAO.emptyCart(addToCartRequest);
	}

	/*Shreekant Rathod 29-1-2021*/
	@Override
	public String addReviewAndRating(AddReviewAndRatingRequest addReviewAndRatingRequest) {
		return reviewAndRatingDAO.addReviewAndRating(addReviewAndRatingRequest);
	}
	@Override
	public List<GetReviewAndRatingResponse> getReviewAndRating(AddReviewAndRatingRequest addReviewAndRatingRequest) {
		
		return reviewAndRatingDAO.getReviewAndRating(addReviewAndRatingRequest);
	}

	@Override
	public String updateReviewAndStatus(AddReviewAndRatingRequest addReviewAndRatingRequest) {
		return reviewAndRatingDAO.updateReviewAndStatus(addReviewAndRatingRequest);
	}

	@Override
	public UserDetails loginWithOtp(LoginAuthenticationBOrequest loginAuthenticationBOrequest) {
		UserDetails userDetails = new UserDetails();
		userDetails = userServiceDAO.checkForOTP(loginAuthenticationBOrequest.getRequestData().getPhoneNumber());
		
		Random rand = new Random(); 
		int otp = rand.nextInt(10000); 
		userDetails.setOtp(String.valueOf(otp));
		SmsApi.callSms("manoj.vg@ortusolis.com", loginAuthenticationBOrequest.getRequestData().getPhoneNumber(),String.valueOf(otp));
		
		return userDetails;
	}
	
	
}
