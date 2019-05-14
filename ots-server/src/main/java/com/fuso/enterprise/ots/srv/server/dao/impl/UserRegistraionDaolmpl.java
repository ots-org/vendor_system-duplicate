package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.sql.Timestamp;
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

import com.fuso.enterprise.ots.srv.api.model.domain.RegistorToUserDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.RejectUserModel;
import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;
import com.fuso.enterprise.ots.srv.api.service.request.AddNewBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddUserDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.ApproveRegistrationResponse;
import com.fuso.enterprise.ots.srv.api.service.response.UserRegistrationResponce;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.common.exception.ErrorEnumeration;
import com.fuso.enterprise.ots.srv.server.dao.UserRegistrationDao;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsProduct;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsRegistration;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUserRole;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;
@Repository
public class UserRegistraionDaolmpl  extends AbstractIptDao<OtsRegistration, String> implements UserRegistrationDao {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	//private UserTemp userTemp;
	
    public UserRegistraionDaolmpl() {
        super(OtsRegistration.class);
    }
    
	@Override
	public  UserRegistrationResponce addUserRegistration(AddNewBORequest addNewBORequest) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		UserRegistrationResponce userRegistrationResponce = new UserRegistrationResponce();
		OtsRegistration otsRegistration = new OtsRegistration();
		OtsUsers otsUsers = new OtsUsers();
	    try {
	    	otsRegistration.setOtsRegistrationFirstname(addNewBORequest.getRequestData().getFirstName());
	    	otsRegistration.setOtsRegistrationLastname(addNewBORequest.getRequestData().getLastName());
	    	otsRegistration.setOtsRegistrationContactNo(addNewBORequest.getRequestData().getPhonenumber());
	    	otsRegistration.setOtsRegistrationAddr1(addNewBORequest.getRequestData().getAddress1());
	    	otsRegistration.setOtsRegistrationAddr2(addNewBORequest.getRequestData().getAddress2());
	    	otsRegistration.setOtsRegistrationPincode(addNewBORequest.getRequestData().getPincode());
	    	OtsUserRole otsUserRole = new OtsUserRole();
	    	otsUserRole.setOtsUserRoleId(Integer.parseInt(addNewBORequest.getRequestData().getUserRoleId()));
	    	otsRegistration.setOtsUserRoleId(otsUserRole);
	    	otsRegistration.setOtsRegistrationEmailid(addNewBORequest.getRequestData().getEmailId()); 
	    	otsRegistration.setOtsDeviceToken(addNewBORequest.getRequestData().getDeviceId());
	    	otsUsers.setOtsUsersId(Integer.parseInt(addNewBORequest.getRequestData().getMappedTo()));
	    	otsRegistration.setOtsUsersMappedTo(otsUsers);
	    	  
	    	OtsProduct otsProduct = new OtsProduct();
	    	if((addNewBORequest.getRequestData().getProductId()!= null) && addNewBORequest.getRequestData().getProductId()!=0 ) {
			try {
				otsProduct.setOtsProductId(addNewBORequest.getRequestData().getProductId());
				otsRegistration.setOtsProductId(otsProduct);
			}catch(Exception e){
				otsProduct.setOtsProductId(null);
				otsRegistration.setOtsProductId(otsProduct);
				}					
	 		}
	          
	    	  otsRegistration.setOtsRegistrationStatus("New");
	    	  /*
	    	   * checking password is empty or not, if it is empty setting default password as "Password" 
	    	   */
              if(addNewBORequest.getRequestData().getPassword().equalsIgnoreCase("")|| addNewBORequest.getRequestData().getPassword()==null) {
            	  otsRegistration.setOtsRegistrationPassword("Password");  
              }else {
	    	  otsRegistration.setOtsRegistrationPassword(addNewBORequest.getRequestData().getPassword());
              }
              /*
               *inserting registration value
               */
			try{					
					super.getEntityManager().merge(otsRegistration);						
				}catch(Exception e) {
				throw new BusinessException(e,ErrorEnumeration.VALUE_ALREDY_EXISTS);
				}	  
	      }catch(Exception e){
	    	  e.printStackTrace(); 
	    	  throw new BusinessException(e,ErrorEnumeration.VALUE_ALREDY_EXISTS);
	      }
			userRegistrationResponce.setEmailId(addNewBORequest.getRequestData().getEmailId());
	        return userRegistrationResponce;
	}
	
	@Override
	public List<RegistorToUserDetails> getNewRegistrationDao(String mappedTo) {	
		List<RegistorToUserDetails> userDetails = new ArrayList<RegistorToUserDetails>();
    	try {
    		List<OtsRegistration> userList = null;		
            try {
            	Map<String, Object> queryParameter = new HashMap<>();
    			if(!(mappedTo.equals("1")))
    			{
    				queryParameter.put("otsUsersMappedTo", Integer.parseInt(mappedTo));
    				userList  = super.getResultListByNamedQuery("OtsUsers.findByRegistrationTable", queryParameter);}
    			else
    			{
    				queryParameter.put("otsRegistrationStatus", "New");
        			userList  = super.getResultListByNamedQuery("OtsRegistration.findByOtsRegistrationStatus", queryParameter);}
    			} catch (NoResultException e) {
            	logger.error("Exception while fetching data from DB :"+e.getMessage());
        		e.printStackTrace();
            	throw new BusinessException(e.getMessage(), e);
            }
            logger.info("Inside Event=1002,Class:UserRegistraionDaolmpl,Method:getNewRegistrationDao,"
					+ "UserList Size:" +userList.size());
            userDetails =  userList.stream().map(otsUsers -> convertUserDetailsFromEntityToDomain(otsUsers)).collect(Collectors.toList());
    	}catch(Exception e) {
    		logger.error("Exception while fetching data from DB :"+e.getMessage());
    		e.printStackTrace();
    		throw new BusinessException(e.getMessage(), e);
    	}
    	return userDetails;
	}
	
	@Override
	public UserDetails fetchUserDetailsfromRegistration(String registrationId) {
		UserDetails userDetails = new UserDetails();
		List<OtsRegistration> otsRegistrationList = new ArrayList<OtsRegistration>();
		try {
        	Map<String, Object> queryParameter = new HashMap<>();
			queryParameter.put("otsRegistrationId", Integer.parseInt(registrationId));
			otsRegistrationList  = super.getResultListByNamedQuery("OtsRegistration.findByOtsRegistrationId", queryParameter);
		} catch (NoResultException e) {
        	logger.error("Exception while fetching data from DB :"+e.getMessage());
    		e.printStackTrace();
        	throw new BusinessException(e.getMessage(), e);
        }
		userDetails =  convertUserDetailsFromOtsRegistrationEntityToDomain(otsRegistrationList.get(0));
		return userDetails;
	}
	
	@Override
	public OtsRegistration fetOtsRegistrationBasedonRegisterID(Integer registrationId) {
		List<OtsRegistration> otsRegistrationList = new ArrayList<OtsRegistration>();
		try {
        	Map<String, Object> queryParameter = new HashMap<>();
			queryParameter.put("otsRegistrationId", registrationId);
			otsRegistrationList  = super.getResultListByNamedQuery("OtsRegistration.findByOtsRegistrationId", queryParameter);
		} catch (NoResultException e) {
        	logger.error("Exception while fetching data from DB :"+e.getMessage());
    		e.printStackTrace();
        	throw new BusinessException(e.getMessage(), e);
        }
		return otsRegistrationList.get(0);
	}
	
	@Override
	public ApproveRegistrationResponse approveRegistration(OtsRegistration otsRegistration) {
		ApproveRegistrationResponse approveRegistrationResponse = new ApproveRegistrationResponse();
		try {
			  otsRegistration.setOtsRegistrationStatus("Active");
	    	  super.getEntityManager().merge(otsRegistration);
	      }catch(Exception e){
	    	  e.printStackTrace(); 
	    	  throw new BusinessException(e.getMessage(), e);
	      }
		approveRegistrationResponse.setFirstName(otsRegistration.getOtsRegistrationFirstname());
		return approveRegistrationResponse;
	}
	
	private RegistorToUserDetails convertUserDetailsFromEntityToDomain(OtsRegistration otsRegistration) {
		RegistorToUserDetails registorToUserDetails = new RegistorToUserDetails();   
		registorToUserDetails.setRegistrationId(otsRegistration.getOtsRegistrationId()==null?null:otsRegistration.getOtsRegistrationId().toString());
		registorToUserDetails.setAddress1(otsRegistration.getOtsRegistrationAddr1()==null?null:otsRegistration.getOtsRegistrationAddr1());
		registorToUserDetails.setLastName(otsRegistration.getOtsRegistrationLastname()==null?null:otsRegistration.getOtsRegistrationLastname());
		registorToUserDetails.setFirstName(otsRegistration.getOtsRegistrationFirstname()==null?null:otsRegistration.getOtsRegistrationFirstname());
		registorToUserDetails.setPhonenumber(otsRegistration.getOtsRegistrationContactNo()==null?null:otsRegistration.getOtsRegistrationContactNo());
		registorToUserDetails.setPincode(otsRegistration.getOtsRegistrationPincode()==null?null:otsRegistration.getOtsRegistrationPincode());
		registorToUserDetails.setMappedTo(otsRegistration.getOtsUsersMappedTo().getOtsUsersId()==null?null:otsRegistration.getOtsUsersMappedTo().getOtsUsersId().toString());
		registorToUserDetails.setProductId(otsRegistration.getOtsProductId()==null?null:otsRegistration.getOtsProductId().getOtsProductId().toString());
		registorToUserDetails.setPassword(otsRegistration.getOtsRegistrationPassword()==null?null:otsRegistration.getOtsRegistrationPassword());
		registorToUserDetails.setStatus(otsRegistration.getOtsRegistrationStatus()==null?null:otsRegistration.getOtsRegistrationStatus());
		registorToUserDetails.setEmailId(otsRegistration.getOtsRegistrationEmailid()==null?null:otsRegistration.getOtsRegistrationEmailid());
		registorToUserDetails.setUserRoleId(otsRegistration.getOtsUserRoleId().getOtsUserRoleId().toString());
		registorToUserDetails.setDeviceId(otsRegistration.getOtsDeviceToken());
		return registorToUserDetails;
    }
	
	private UserDetails convertUserDetailsFromOtsRegistrationEntityToDomain(OtsRegistration otsRegistration) {
		UserDetails userDetails = new UserDetails();
		userDetails.setRegistrationId(otsRegistration.getOtsRegistrationId()==null?null:otsRegistration.getOtsRegistrationId().toString());
		userDetails.setFirstName(otsRegistration.getOtsRegistrationFirstname());
		userDetails.setLastName(otsRegistration.getOtsRegistrationLastname()==null?null:otsRegistration.getOtsRegistrationLastname());
		userDetails.setAddress1(otsRegistration.getOtsRegistrationAddr1()==null?null:otsRegistration.getOtsRegistrationAddr1());
		userDetails.setAddress2(otsRegistration.getOtsRegistrationAddr2()==null?null:otsRegistration.getOtsRegistrationAddr2());
		userDetails.setPincode(otsRegistration.getOtsRegistrationPincode()==null?null:otsRegistration.getOtsRegistrationPincode());
		userDetails.setContactNo(otsRegistration.getOtsRegistrationContactNo()==null?null:otsRegistration.getOtsRegistrationContactNo());
		userDetails.setEmailId(otsRegistration.getOtsRegistrationEmailid()==null?null:otsRegistration.getOtsRegistrationEmailid());
		userDetails.setUserRoleId(otsRegistration.getOtsUserRoleId().getOtsUserRoleId()==null?null:otsRegistration.getOtsUserRoleId().getOtsUserRoleId().toString());
		userDetails.setProductId(otsRegistration.getOtsProductId()==null?null:otsRegistration.getOtsProductId().toString());
		userDetails.setUsrStatus(otsRegistration.getOtsRegistrationStatus()==null?null:otsRegistration.getOtsRegistrationStatus());
		userDetails.setMappedTo(otsRegistration.getOtsUsersMappedTo().getOtsUsersId()==null?null:otsRegistration.getOtsUsersMappedTo().getOtsUsersId().toString());
		userDetails.setUsrPassword(otsRegistration.getOtsRegistrationPassword()==null?null:otsRegistration.getOtsRegistrationPassword());            
		userDetails.setDeviceId(otsRegistration.getOtsDeviceToken());
		return userDetails;
    }	
	
	@Override
	public Integer CheckForExists(AddUserDataBORequest addUserDataBORequest) {
    	UserDetails userDetails = new UserDetails();
    	Integer flag = 0;
    	OtsRegistration userList = null;
            try {
            	Map<String, Object> queryParameter = new HashMap<>();
    			queryParameter.put("otsEmailId", addUserDataBORequest.getRequestData().getEmailId());
    			queryParameter.put("otsPhonenumber",addUserDataBORequest.getRequestData().getContactNo());
    			userList  = super.getResultByNamedQuery("OtsRegistration.CheckForRegistration", queryParameter);
    			flag = 1;
            } catch (Exception e) {
            	flag = 0;
            }
    	return flag;
	}
	
	@Override
	public String UpdateStatus(RejectUserModel rejectUserModel) {
    	OtsRegistration otsRegistration = new OtsRegistration();
            try {
            	Map<String, Object> queryParameter = new HashMap<>();
    			queryParameter.put("otsRegistrationId",Integer.valueOf(rejectUserModel.getRequest().getRegistrationId()));
    			otsRegistration  = super.getResultByNamedQuery("OtsRegistration.findByOtsRegistrationId", queryParameter);
    			otsRegistration.setOtsRegistrationStatus(rejectUserModel.getRequest().getStatus());
    			super.getEntityManager().merge(otsRegistration);	
    			} catch (Exception e) {
	            	logger.error("Exception while fetching data from DB :"+e.getMessage());
	        		e.printStackTrace();
	            	throw new BusinessException(e.getMessage(), e);
            }
    	return "updated";
	}
}



