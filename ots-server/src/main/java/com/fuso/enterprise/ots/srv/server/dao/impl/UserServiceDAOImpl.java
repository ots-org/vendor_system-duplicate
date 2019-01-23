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
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;
import com.fuso.enterprise.ots.srv.api.service.request.AddUserDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.UserDataBOResponse;import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;import com.fuso.enterprise.ots.srv.server.dao.UserServiceDAO;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsRegistration;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUserRole;import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;
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
            userDetails =  userList.stream().map(otsUsers -> convertUserDetailsFromEntityToDomain(otsUsers)).collect(Collectors.toList());
    	}catch(Exception e) {
    		logger.error("Exception while fetching data from DB :"+e.getMessage());
    		e.printStackTrace();
    		throw new BusinessException(e.getMessage(), e);
    	}
    	return userDetails;
		
	}

    @Override
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
            
            userDetails =  userList.stream().map(otsUsers -> convertUserDetailsFromEntityToDomain(otsUsers)).collect(Collectors.toList());
    	}catch(Exception e) {
    		logger.error("Exception while fetching data from DB :"+e.getMessage());
    		e.printStackTrace();
    		throw new BusinessException(e.getMessage(), e);
    	}
    	return userDetails;
		
	}
    
    
    @Override
	public UserDataBOResponse addNewUser(AddUserDataBORequest addUserDataBORequest) {
		 List<UserDetails> userDetails = new ArrayList<UserDetails>();
		 UserDataBOResponse userDataBOResponse = new UserDataBOResponse();
	 		try{
		 		OtsUsers userEntity=new OtsUsers();
				userEntity.setOtsUsersFirstname(addUserDataBORequest.getRequestData().getFirstName());
				userEntity.setOtsUsersLastname(addUserDataBORequest.getRequestData().getLastName());
				userEntity.setOtsUsersAddr1(addUserDataBORequest.getRequestData().getAddress1());
				userEntity.setOtsUsersAddr2(addUserDataBORequest.getRequestData().getAddress2());
				userEntity.setOtsUsersPincode(addUserDataBORequest.getRequestData().getPincode());
				userEntity.setOtsUsersPassword(addUserDataBORequest.getRequestData().getUsrPassword());
				userEntity.setOtsUsersEmailid(addUserDataBORequest.getRequestData().getEmailId());
				userEntity.setOtsUsersContactNo(addUserDataBORequest.getRequestData().getContactNo());
				OtsUserRole otsUserRole = new OtsUserRole();
				otsUserRole.setOtsUserRoleId(Integer.parseInt(addUserDataBORequest.getRequestData().getUserRoleId()));
				userEntity.setOtsUserRoleId(otsUserRole);
			    userEntity.setOtsUsersStatus(addUserDataBORequest.getRequestData().getUsrStatus());
				userEntity.setOtsUsersProfilePic(addUserDataBORequest.getRequestData().getProfilePic());
				OtsRegistration otsRegistration = new OtsRegistration();
				otsRegistration.setOtsRegistrationId(Integer.parseInt(addUserDataBORequest.getRequestData().getRegistrationId()));
				userEntity.setOtsRegistrationId(otsRegistration);
				try{
					super.getEntityManager().merge(userEntity);
				}catch (NoResultException e) {
					logger.error("Exception while Inserting data to DB :"+e.getMessage());
		    		e.printStackTrace();
		        	throw new BusinessException(e.getMessage(), e);
				}
				userDetails = getEmailIdUsers(addUserDataBORequest.getRequestData().getEmailId());
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
        userDetails.setUserId(otsUsers.getOtsUsersId().toString());
        userDetails.setFirstName(otsUsers.getOtsUsersFirstname());
        userDetails.setLastName(otsUsers.getOtsUsersLastname());
        return userDetails;
    }

	@Override
	public List<UserDetails> getUserListbasedonrole(String userRoleId) {
		// TODO Auto-generated method stub
		return null;
	}
}
