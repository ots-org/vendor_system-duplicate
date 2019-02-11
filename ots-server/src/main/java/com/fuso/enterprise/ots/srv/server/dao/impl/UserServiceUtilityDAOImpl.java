	package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;
import com.fuso.enterprise.ots.srv.api.service.request.RequestBOUserBySearch;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.server.dao.UserServiceUtilityDAO;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;

@Repository
public class UserServiceUtilityDAOImpl  extends AbstractIptDao<OtsUsers, String> implements UserServiceUtilityDAO{

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
    private JdbcTemplate jdbcTemplate;

	  public UserServiceUtilityDAOImpl() {
	        super(OtsUsers.class);
	    }

	@Override
	public List<UserDetails> getUserDetails(RequestBOUserBySearch requestBOUserBySearch) {
		List<UserDetails> userDetails = new ArrayList<UserDetails>();
		String searchKey 	= requestBOUserBySearch.getRequestData().getSearchKey();
		String searchvalue  = requestBOUserBySearch.getRequestData().getSearchvalue();
        Map<String, Object> queryParameter = new HashMap<>();
        List<OtsUsers> userList = null;
		try {
	            switch(searchKey)
	            {
	            case "UsersId":
	            					queryParameter.put("otsUsersId", Integer.parseInt(searchvalue));
	            					userList  = super.getResultListByNamedQuery("OtsUsers.findByOtsUsersId", queryParameter);
	            				    break;
	            case "UsersFirstname":
									queryParameter.put("otsUsersFirstname", searchvalue);
									userList  = super.getResultListByNamedQuery("OtsUsers.findByOtsUsersFirstname", queryParameter);
								    break;
	            case "UsersLastname":
									queryParameter.put("otsUsersLastname",searchvalue);
									userList  = super.getResultListByNamedQuery("OtsUsers.findByOtsUsersLastname", queryParameter);
									break;
	            case "UsersEmailid":
									queryParameter.put("otsUsersEmailid", searchvalue);
									userList  = super.getResultListByNamedQuery("OtsUsers.findByOtsUsersEmailid", queryParameter);
								    break;
	            case "UserRoleId":
									OtsUserRole otsUserRole = new OtsUserRole();
	            					otsUserRole.setOtsUserRoleId(Integer.parseInt(searchvalue));
	            					queryParameter.put("otsUserRoleId", otsUserRole);
									userList  = super.getResultListByNamedQuery("OtsUsers.findByUserOtsRoleId", queryParameter);
									break;
	            default:
	            					return null;

	            }
	            logger.info("Inside Event=1,Class:UserServiceDAOImpl,Method:getUserIDUsers, "
						+ "UserList Size:" +userList.size());
	            //@formatter:off
	            userDetails =  userList.stream().map(OtsUsers -> convertUserDetailsFromEntityToDomain(OtsUsers)).collect(Collectors.toList());
	            System.out.println("+++++++++++++++++"+userDetails);
	            return userDetails;
	            //@formatter:on
    	}catch (NoResultException e) {
        	logger.error("Exception while fetching data from DB :"+e.getMessage());
    		e.printStackTrace();
        	throw new BusinessException(e.getMessage(), e);
        }
	}





	 private UserDetails convertUserDetailsFromEntityToDomain(OtsUsers users) {

	   	UserDetails userDetails = new UserDetails();
	   	userDetails.setUserId(Integer.toString(users.getOtsUsersId()));
	   	userDetails.setFirstName(users.getOtsUsersFirstname());
	   	userDetails.setLastName(users.getOtsUsersLastname());
	   	userDetails.setAddress1(users.getOtsUsersAddr1());
	   	userDetails.setAddress2(users.getOtsUsersAddr2());
	   	userDetails.setPincode(users.getOtsUsersPincode());
	   	userDetails.setEmailId(users.getOtsUsersEmailid());
	   	userDetails.setProfilePic(users.getOtsUsersProfilePic());
	   	userDetails.setUsrStatus(users.getOtsUsersStatus());
	   	userDetails.setUsrStatus(users.getOtsUsersStatus());
	   	userDetails.setUsrPassword(users.getOtsUsersPassword()); 
	   	return userDetails;
   }

	 @Override
		public List<UserDetails> getUserDetailsByMapped(String MappedTo) {
	    	List<UserDetails> userDetails = new ArrayList<UserDetails>();
	    	try {
	            List<OtsUsers> userList = null;
	            try {
	            	Map<String, Object> queryParameter = new HashMap<>();
	    			queryParameter.put("MappedTo", Integer.parseInt(MappedTo));
	    			userList  = super.getResultListByNamedQuery("OtsUsers.findByUserOtsbyMappedTo", queryParameter);
	            } catch (NoResultException e) {
	            	logger.error("Exception while fetching data from DB :"+e.getMessage());
	        		e.printStackTrace();
	            	throw new BusinessException(e.getMessage(), e);
	            }
	            logger.info("Inside Event=1008,Class:UserServiceUtilityDAOImpl,Method:getUserDetailsByMapped, "
						+ "UserList Size:" +userList.size());
	            userDetails =  userList.stream().map(otsUsers -> convertUserDetailsFromEntityToDomain(otsUsers)).collect(Collectors.toList());
	    	}catch(Exception e) {
	    		logger.error("Exception while fetching data from DB :"+e.getMessage());
	    		e.printStackTrace();
	    		throw new BusinessException(e.getMessage(), e);
	    	}
	    	return userDetails;
			
		}

}
