package com.fuso.enterprise.ots.srv.functional.service;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fuso.enterprise.ots.srv.api.model.domain.CustomerProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.RegistorToUserDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.UserMapping;
import com.fuso.enterprise.ots.srv.api.service.functional.OTSUserService;
import com.fuso.enterprise.ots.srv.api.service.request.AddUserDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.ApproveRegistrationBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.CustomerProductDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.LoginAuthenticationBOrequest;
import com.fuso.enterprise.ots.srv.api.service.request.MapUsersDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.LoginUserResponse;
import com.fuso.enterprise.ots.srv.api.service.response.MapUsersDataBOResponse;
import com.fuso.enterprise.ots.srv.api.service.request.RequestBOUserBySearch;
import com.fuso.enterprise.ots.srv.api.service.response.UserDataBOResponse;
import com.fuso.enterprise.ots.srv.api.service.request.AddNewBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.UserRegistrationBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.ApproveRegistrationResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetNewRegistrationResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.api.service.response.UserRegistrationResponce;
import com.fuso.enterprise.ots.srv.server.dao.MapUserProductDAO;
import com.fuso.enterprise.ots.srv.server.dao.UserMapDAO;
import com.fuso.enterprise.ots.srv.server.dao.UserRegistrationDao;
import com.fuso.enterprise.ots.srv.server.dao.UserServiceDAO;
import com.fuso.enterprise.ots.srv.server.dao.UserServiceUtilityDAO;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsRegistration;

@Service
@Transactional
public class OTSUserServiceImpl implements  OTSUserService{
	private Logger logger = LoggerFactory.getLogger(getClass());

	private UserServiceDAO userServiceDAO;
	private UserMapDAO userMapDAO;
	private UserServiceUtilityDAO userServiceUtilityDAO;
	private UserRegistrationDao userRegistrationDao;
	private MapUserProductDAO mapUserProductDAO;

	@Inject
	public OTSUserServiceImpl(UserServiceDAO userServiceDAO,UserMapDAO userMapDAO,UserServiceUtilityDAO userServiceUtilityDAO,UserRegistrationDao userRegistrationDao,
			MapUserProductDAO mapUserProductDAO) {
		this.userServiceDAO=userServiceDAO;
		this.userMapDAO=userMapDAO;
		this.userServiceUtilityDAO = userServiceUtilityDAO;
		this.userRegistrationDao =userRegistrationDao ;
		this.mapUserProductDAO=mapUserProductDAO;
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
		if(addNewBORequest != null) {
			List<OtsRegistration> UserRegistrationEmailPhonenumber = userRegistrationDao.addUserRegistrationEmailPhonenumber(addNewBORequest);
			
			if(UserRegistrationEmailPhonenumber.size() == 0) {
				userRegistrationResponce = userRegistrationDao.addUserRegistration(addNewBORequest);
			}
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
			return userServiceDAO.otsLoginAuthentication(loginAuthenticationBOrequest);
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
					/*
					* AddNewUser from registration
					*/
					addNewUser(addUserDataBORequest);
					/*
					* fetching OTSRegistration Object for approve status
					*/
					OtsRegistration otsRegistration = new OtsRegistration();
					otsRegistration = userRegistrationDao.fetOtsRegistrationBasedonRegisterID(Integer.parseInt(approveRegistrationBORequest.getRequestData().getRegistrationId()));
					/*
					* Making Registration table as active
					*/
					approveRegistrationResponse =userRegistrationDao.approveRegistration(otsRegistration);
				}
				} catch (Exception e) {
					throw new BusinessException(e.getMessage(), e);
				}
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
}
