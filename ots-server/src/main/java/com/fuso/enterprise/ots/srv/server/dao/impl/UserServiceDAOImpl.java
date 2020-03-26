
package com.fuso.enterprise.ots.srv.server.dao.impl;

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

import com.fuso.enterprise.ots.srv.api.model.domain.CustomerProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;
import com.fuso.enterprise.ots.srv.api.service.request.AddNewBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddUserDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.ChangePasswordRequest;
import com.fuso.enterprise.ots.srv.api.service.request.LoginAuthenticationBOrequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdatePassword;
import com.fuso.enterprise.ots.srv.api.service.request.UpdatePasswordRequest;
import com.fuso.enterprise.ots.srv.api.service.response.LoginUserResponse;
import com.fuso.enterprise.ots.srv.api.service.response.UserDataBOResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.common.exception.ErrorEnumeration;
import com.fuso.enterprise.ots.srv.server.dao.UserServiceDAO;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsCustomerProduct;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsRegistration;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUserRole;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;

@Repository
public class UserServiceDAOImpl extends AbstractIptDao<OtsUsers, String> implements UserServiceDAO {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
    private JdbcTemplate jdbcTemplate;

    public UserServiceDAOImpl() {
        super(OtsUsers.class);
    }

    @Override
	public List<UserDetails> getUserIdUsers(String userId) {
    	List<UserDetails> userDetails = new ArrayList<UserDetails>();
    	try {
            List<OtsUsers> userList = null;
            try {
            	Map<String, Object> queryParameter = new HashMap<>();
    			queryParameter.put("otsUsersId", Integer.parseInt(userId));
    			userList  = super.getResultListByNamedQuery("OtsUsers.findByOtsUsersId", queryParameter);
            } catch (NoResultException e) {
            	logger.error("Exception while fetching data from DB :"+e.getMessage());
        		e.printStackTrace();
            	throw new BusinessException(e.getMessage(), e);
            }
            logger.info("Inside Event=1,Class:UserServiceDAOImpl,Method:getUserIDUsers, "
					+ "UserList Size:" +userList.size());
            userDetails =  userList.stream().map(otsUsers -> convertUserDetailsFromEntityToDomainwithCustomerproduct(otsUsers)).collect(Collectors.toList());
    	}catch(Exception e) {
    		logger.error("Exception while fetching data from DB :"+e.getMessage());
    		e.printStackTrace();
    		throw new BusinessException(e.getMessage(), e);
    	}
    	return userDetails;

	}


    public List<UserDetails> getEmailIdUsers(String emailId) {
    	List<UserDetails> userDetails = new ArrayList<UserDetails>();
    	try {
            List<OtsUsers> userList = null;
            try {
            	Map<String, Object> queryParameter = new HashMap<>();
    			queryParameter.put("otsUsersEmailid", emailId);
    			userList  = super.getResultListByNamedQuery("OtsUsers.findByOtsUsersEmailid", queryParameter);
            } catch (NoResultException e) {
            	logger.error("Exception while fetching data from DB :"+e.getMessage());
        		e.printStackTrace();
            	throw new BusinessException(e.getMessage(), e);
            }

            userDetails =  userList.stream().map(otsUsers -> convertUserDetailsFromEntityToDomainwithCustomerproduct(otsUsers)).collect(Collectors.toList());
    	}catch(Exception e) {
    		logger.error("Exception while fetching data from DB :"+e.getMessage());
    		e.printStackTrace();
    		throw new BusinessException(e.getMessage(), e);
    	}
    	return userDetails;

	}
    @Override
	public UserDataBOResponse updateUser(AddUserDataBORequest addUserDataBORequest) {
    	
    	List<UserDetails> userDetails = new ArrayList<UserDetails>();
    	UserDataBOResponse userDataBOResponse = new UserDataBOResponse();
    	try{
	    	OtsUsers userEntity= super.getEntityManager().find(OtsUsers.class, Integer.parseInt(addUserDataBORequest.getRequestData().getUserId()));
	    	
	    	OtsUserRole otsUserRole = new OtsUserRole();
			otsUserRole.setOtsUserRoleId(Integer.parseInt(addUserDataBORequest.getRequestData().getUserRoleId()));
			
			userEntity.setOtsUsersFirstname(addUserDataBORequest.getRequestData().getFirstName());
			userEntity.setOtsUsersLastname(addUserDataBORequest.getRequestData().getLastName());
			userEntity.setOtsUsersAddr1(addUserDataBORequest.getRequestData().getAddress1());
			userEntity.setOtsUsersAddr2(addUserDataBORequest.getRequestData().getAddress2());
			userEntity.setOtsUsersPincode(addUserDataBORequest.getRequestData().getPincode());
			userEntity.setOtsUserRoleId(otsUserRole);
			userEntity.setOtsUsersProfilePic(addUserDataBORequest.getRequestData().getProfilePic());
			userEntity.setOtsUsersStatus(addUserDataBORequest.getRequestData().getUsrStatus());
			userEntity.setOtsUsersEmailid(addUserDataBORequest.getRequestData().getEmailId());
			userEntity.setOtsUsersContactNo(addUserDataBORequest.getRequestData().getContactNo());
	//		userEntity.setOtsUsersLat(addUserDataBORequest.getRequestData().getUserLat()); 
	//		userEntity.setOtsUsersLong(addUserDataBORequest.getRequestData().getUserLong());
			
	    	super.getEntityManager().merge(userEntity);
			super.getEntityManager().flush();
			
			userDetails.add(convertUserDetailsFromEntityToDomain(userEntity));
			userDataBOResponse.setUserDetails(userDetails);
			logger.info("Inside Event=1004,Class:UserServiceDAOImpl,Method:updateUser, "
						+ "userDetails Size:" +userDetails.size());
    	}catch (NoResultException e) {
           	logger.error("Exception while updating data to DB :"+e.getMessage());
       		e.printStackTrace();
           	throw new BusinessException(e.getMessage(), e);
		}
    	return  userDataBOResponse;
	}

    @Override
	public UserDataBOResponse addNewUser(AddUserDataBORequest addUserDataBORequest) {
		 List<UserDetails> userDetails = new ArrayList<UserDetails>();
		 UserDataBOResponse userDataBOResponse = new UserDataBOResponse();
	 		try{
		 		OtsUsers userEntity=new OtsUsers();
		 		String Fname = addUserDataBORequest.getRequestData().getFirstName().substring(0, 1).toUpperCase() + addUserDataBORequest.getRequestData().getFirstName().substring(1);
				userEntity.setOtsUsersFirstname(Fname);
				userEntity.setOtsUsersLastname(addUserDataBORequest.getRequestData().getLastName());
				userEntity.setOtsUsersAddr1(addUserDataBORequest.getRequestData().getAddress1());
				userEntity.setOtsUsersAddr2(addUserDataBORequest.getRequestData().getAddress2());
				userEntity.setOtsUsersPincode(addUserDataBORequest.getRequestData().getPincode());
				userEntity.setOtsUsersContactNo(addUserDataBORequest.getRequestData().getContactNo());
				userEntity.setOtsUsersPassword(addUserDataBORequest.getRequestData().getUsrPassword()==null?"Password":addUserDataBORequest.getRequestData().getUsrPassword());
				userEntity.setOtsUsersEmailid(addUserDataBORequest.getRequestData().getEmailId());
				userEntity.setOtsUsersContactNo(addUserDataBORequest.getRequestData().getContactNo());
				OtsUserRole otsUserRole = new OtsUserRole();
				otsUserRole.setOtsUserRoleId(Integer.parseInt(addUserDataBORequest.getRequestData().getUserRoleId()));
				userEntity.setOtsUserRoleId(otsUserRole);
			    userEntity.setOtsUsersStatus("Active");
				userEntity.setOtsUsersProfilePic(addUserDataBORequest.getRequestData().getProfilePic());
				userEntity.setOtsDeviceToken(addUserDataBORequest.getRequestData().getDeviceId());
//				userEntity.setOtsUsersLat(addUserDataBORequest.getRequestData().getUserLat());
//				userEntity.setOtsUsersLong(addUserDataBORequest.getRequestData().getUserLong());
				
				OtsRegistration otsRegistration = new OtsRegistration();

				if(addUserDataBORequest.getRequestData().getRegistrationId()!=null ) {
					try {
						int registrationID = Integer.parseInt(addUserDataBORequest.getRequestData().getRegistrationId());
						otsRegistration.setOtsRegistrationId(registrationID);
						userEntity.setOtsRegistrationId(otsRegistration);
					}catch(Exception e){
						userEntity.setOtsRegistrationId(null);
					}
	 			}

				try{
					if(addUserDataBORequest.getRequestData().getUserId()!=null || addUserDataBORequest.getRequestData().getUserId()!="" || addUserDataBORequest.getRequestData().getUserId()!="0" ) {
						try{
							userEntity.setOtsUsersId(Integer.parseInt(addUserDataBORequest.getRequestData().getUserId()));
							super.getEntityManager().merge(userEntity);
							super.getEntityManager().flush();
						}catch(Exception e) {
							userEntity.setOtsUsersId(null);
							super.getEntityManager().persist(userEntity);
							super.getEntityManager().flush();
						}
					}
				}catch (NoResultException e) {
					logger.error("Exception while Inserting data to DB :"+e.getMessage());
		    		e.printStackTrace();
		    		throw new BusinessException(e,ErrorEnumeration.VALUE_ALREDY_EXISTS);
				}
				userEntity.getOtsUsersId();
				//userDetails = getEmailIdUsers(addUserDataBORequest.getRequestData().getEmailId());
				userDetails.add(convertUserDetailsFromEntityToDomain(userEntity));
				userDataBOResponse.setUserDetails(userDetails);
				logger.info("Inside Event=1004,Class:UserServiceDAOImpl,Method:addNewUser, "
  						+ "userDetails Size:" +userDetails.size());
			}catch (NoResultException e) {
	           	logger.error("Exception while Inserting data to DB :"+e.getMessage());
	       		e.printStackTrace();
	           	throw new BusinessException(e.getMessage(), e);
			}
			return  userDataBOResponse;
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
        return userDetails;
    }


    private UserDetails convertUserDetailsFromEntityToDomainwithCustomerproduct(OtsUsers otsUsers) {
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
        userDetails.setMappedTo(otsUsers.getOtsUserMapping()==null?null:otsUsers.getOtsUserMapping().getOtsMappedTo().toString());
        List<OtsCustomerProduct> customerProductDetails = new ArrayList(otsUsers.getOtsCustomerProductCollection());

	   	for(int i=0 ; i<customerProductDetails.size() ; i++) {
	   		CustomerProductDetails tempcustomerProductDetails = new CustomerProductDetails();
	   		tempcustomerProductDetails.setProductname(customerProductDetails.get(i).getOtsProductId().getOtsProductName()==null?null:customerProductDetails.get(i).getOtsProductId().getOtsProductName());
	   		tempcustomerProductDetails.setProductPrice(customerProductDetails.get(i).getOtsCustomerProductPrice()==null?null:customerProductDetails.get(i).getOtsCustomerProductPrice().toString());
	   		tempcustomerProductDetails.setCustomerProductId(customerProductDetails.get(i).getOtsCustomerProductId()==null?null:customerProductDetails.get(i).getOtsCustomerProductId().toString());
	   		tempcustomerProductDetails.setProductDefault(customerProductDetails.get(i).getOtsCustomerProductDefault()==null?null:customerProductDetails.get(i).getOtsCustomerProductDefault().toString());
	   		
	   		userDetails.setProductPrice(customerProductDetails.get(i).getOtsCustomerProductPrice());
	   		userDetails.setProductId(customerProductDetails.get(i).getOtsProductId().getOtsProductId().toString());
	   		userDetails.getCustomerProductDetails().add(i,tempcustomerProductDetails);
	   	}
	   	
	   	
        return userDetails;
    }

    private UserDetails convertUserDetailsFromEntityToDomainwithCustomerproductforLogin(OtsUsers otsUsers) {
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
        userDetails.setMappedTo(otsUsers.getOtsUserMapping()==null?null:otsUsers.getOtsUserMapping().getOtsMappedTo().toString());
       
        List<OtsCustomerProduct> customerProductDetails = new ArrayList(otsUsers.getOtsCustomerProductCollection());

	   	for(int i=0 ; i<customerProductDetails.size() ; i++) {
	   		CustomerProductDetails tempcustomerProductDetails = new CustomerProductDetails();
	   		tempcustomerProductDetails.setProductname(customerProductDetails.get(i).getOtsProductId().getOtsProductName()==null?null:customerProductDetails.get(i).getOtsProductId().getOtsProductName());
	   		tempcustomerProductDetails.setProductPrice(customerProductDetails.get(i).getOtsCustomerProductPrice()==null?null:customerProductDetails.get(i).getOtsCustomerProductPrice().toString());
	   		tempcustomerProductDetails.setCustomerProductId(customerProductDetails.get(i).getOtsCustomerProductId()==null?null:customerProductDetails.get(i).getOtsCustomerProductId().toString());
	   		tempcustomerProductDetails.setProductDefault(customerProductDetails.get(i).getOtsCustomerProductDefault()==null?null:customerProductDetails.get(i).getOtsCustomerProductDefault().toString());
	   		
	   		userDetails.setProductPrice(customerProductDetails.get(i).getOtsCustomerProductPrice());
	   		userDetails.setProductId(customerProductDetails.get(i).getOtsProductId().getOtsProductId().toString());
	   		
	   		userDetails.getCustomerProductDetails().add(i,tempcustomerProductDetails);
	   	}
	   	
        return userDetails;
    }

	@Override
	public LoginUserResponse otsLoginAuthentication(LoginAuthenticationBOrequest loginAuthenticationBOrequest) {
		String phnum = loginAuthenticationBOrequest.getRequestData().getPhoneNumber();
		String Password = loginAuthenticationBOrequest.getRequestData().getPassword();
		LoginUserResponse loginUserResponse = new LoginUserResponse();
		UserDetails userDetails = new UserDetails();
    	try{
          OtsUsers userData = null;
            try {
            	Map<String, Object> queryParameter = new HashMap<>();
    			queryParameter.put("otsUsersContactNo", phnum);
    			userData  = super.getResultByNamedQuery("OtsUsers.findByOtsUsersContactNo", queryParameter);
    			userData.setOtsDeviceToken(loginAuthenticationBOrequest.getRequestData().getDeviceId());
    			super.getEntityManager().merge(userData);
            }catch (NoResultException e) {
            	logger.error("Exception while fetching data from DB :"+e.getMessage());
        		e.printStackTrace();
        		throw new BusinessException(e,ErrorEnumeration.USER_NOT_EXISTS);
            }
            logger.info("Inside Event=1,Class:UserServiceDAOImpl,Method:getUserIDUsers, "
					+ "UserData:" +userData);
            userDetails =  convertUserDetailsFromEntityToDomainwithCustomerproductforLogin(userData);
            loginUserResponse.setUserDetails(userDetails);
    	}catch(Exception e) {
    		logger.error("Exception while fetching data from DB :"+e.getMessage());
    		e.printStackTrace();
    		throw new BusinessException(e,ErrorEnumeration.USER_NOT_EXISTS);
    	}
    	return loginUserResponse;
	}

	@Override
	public Integer CheckForExists(AddNewBORequest addNewBORequest) {
    	UserDetails userDetails = new UserDetails();
    	Integer flag = 0;
            OtsUsers userList = null;
            try {
            	Map<String, Object> queryParameter = new HashMap<>();
    			queryParameter.put("otsEmailId", addNewBORequest.getRequestData().getEmailId());
    			queryParameter.put("otsPhonenumber",addNewBORequest.getRequestData().getPhonenumber());
    			userList  = super.getResultByNamedQuery("OtsUsers.CheckForRegistration", queryParameter);
    			flag = 1;
            } catch (Exception e) {
            	flag = 0;
            }
    	return flag;
	}
	@Override
	public UserDetails getUserDetails(Integer userId) {
			UserDetails userDetails = new UserDetails();
			try {
			OtsUsers userList = null;
			Map<String, Object> queryParameter = new HashMap<>();
			queryParameter.put("otsUsersId", userId);
			userList = super.getResultByNamedQuery("OtsUsers.findByOtsUsersId", queryParameter);
			userDetails = convertUserDetailsFromEntityToDomain(userList);
		}catch(Exception e) {
			return null;
		}
			return userDetails;
	}
	
	@Override
	public UserDetails checkForOTP(String  mobilenumber) {
			UserDetails userDetails = new UserDetails();
			try {
			OtsUsers userList = null;
			Map<String, Object> queryParameter = new HashMap<>();
			queryParameter.put("otsUsersContactNo", mobilenumber);
			userList = super.getResultByNamedQuery("OtsUsers.findByOtsUsersContactNo", queryParameter);
			userDetails = convertUserDetailsFromEntityToDomain(userList);
		}catch(Exception e) {
			return null;
		}
			return userDetails;
	}
	
	
	@Override
	public String changePassword(ChangePasswordRequest changePasswordRequest) {
		UserDetails userDetails = new UserDetails();
		try {
		OtsUsers userData = new OtsUsers();
		Map<String, Object> queryParameter = new HashMap<>();
		queryParameter.put("otsUsersId",Integer.parseInt(changePasswordRequest.getRequest().getUserID()));
		userData = super.getResultByNamedQuery("OtsUsers.findByOtsUsersId", queryParameter);
		
		userData.setOtsUsersPassword(changePasswordRequest.getRequest().getPasword());
		super.getEntityManager().merge(userData);
		userDetails = convertUserDetailsFromEntityToDomain(userData);
	}catch(Exception e) {
		System.out.println(e);
		return null;
	}
		return "updated";
	}

	@Override
	public UserDetails getUserDetailsForEmployee(Integer userId) {
		UserDetails userDetails = new UserDetails();
		try {
			OtsUsers userList = null;
			Map<String, Object> queryParameter = new HashMap<>();
			queryParameter.put("otsUsersId", userId);
			
			OtsUserRole roleId = new OtsUserRole();
			roleId.setOtsUserRoleId(3);
			queryParameter.put("RoleId", roleId);
			queryParameter.put("otsUsersId", userId);
			userList = super.getResultByNamedQuery("OtsUsers.findByOtsUsersIdAndRoleId", queryParameter);
				userDetails = convertUserDetailsFromEntityToDomain(userList);
			}catch(Exception e) {
				System.out.print(e);
				return null;
		}
		return userDetails;
	}

	@Override
	public UserDetails getUserDetailsForCustomer(Integer userId) {
		UserDetails userDetails = new UserDetails();
		try {
			OtsUsers userList = null;
			Map<String, Object> queryParameter = new HashMap<>();
			queryParameter.put("otsUsersId", userId);
			
			OtsUserRole roleId = new OtsUserRole();
			roleId.setOtsUserRoleId(4);
			queryParameter.put("RoleId", roleId);
			
			userList = super.getResultByNamedQuery("OtsUsers.findByOtsUsersIdAndRoleId", queryParameter);
				userDetails = convertUserDetailsFromEntityToDomainwithCustomerproduct(userList);
			}catch(Exception e) {
			return null;
		}
		return userDetails;
	}

	@Override
	public String updatePassword(UpdatePasswordRequest updatePasswordRequest) {
		UserDetails userDetails = new UserDetails();
		try {
		OtsUsers userData = new OtsUsers();
		Map<String, Object> queryParameter = new HashMap<>();
		queryParameter.put("otsUsersId",Integer.parseInt(updatePasswordRequest.getUpdatePassword().getUserId()));
		userData = super.getResultByNamedQuery("OtsUsers.findByOtsUsersId", queryParameter);
		
		if(userData.getOtsUsersPassword().equals(updatePasswordRequest.getUpdatePassword().getOldPassword())) {
			userData.setOtsUsersPassword(updatePasswordRequest.getUpdatePassword().getNewPassword());
			super.getEntityManager().merge(userData);
			userDetails = convertUserDetailsFromEntityToDomain(userData);
		}else {
			return "404";
		}
		
	}catch(Exception e) {
		System.out.println(e);
		return "Not updated";
	}
		return "200";
	}

	
}
