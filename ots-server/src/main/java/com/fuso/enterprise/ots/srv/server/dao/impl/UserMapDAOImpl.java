package com.fuso.enterprise.ots.srv.server.dao.impl;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fuso.enterprise.ots.srv.api.service.request.MapUsersDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.MapUsersDataBOResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.server.dao.UserMapDAO;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUserMapping;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;
@Repository
public class UserMapDAOImpl extends AbstractIptDao<OtsUserMapping, String> implements UserMapDAO {
private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	public UserMapDAOImpl() {
		super(OtsUserMapping.class);
	}
	
	@Override
	public MapUsersDataBOResponse mappUser(MapUsersDataBORequest mapUsersDataBORequest) {
		MapUsersDataBOResponse userMappingBOResponse = new MapUsersDataBOResponse();
		try{
			OtsUserMapping userMappEntity=new OtsUserMapping();
			OtsUsers otsUsers = new OtsUsers();
			otsUsers.setOtsUsersId(Integer.parseInt(mapUsersDataBORequest.getRequestData().getUserId()));
			userMappEntity.setOtsUsersId(otsUsers);
			userMappEntity.setOtsMappedTo(Integer.parseInt(mapUsersDataBORequest.getRequestData().getMappedTo()));
			
			try {
				super.getEntityManager().merge(userMappEntity);
			}catch (NoResultException e) {
				logger.error("Exception while Inserting data to DB :"+e.getMessage());
	    		e.printStackTrace();
	        	throw new BusinessException(e.getMessage(), e);
			}
			userMappingBOResponse.setMappedMessage("User Mapped Successfully");
			logger.info("Inside Event=1005,Class:UserMapDAOImpl,Method:mappUser");
		}catch (NoResultException e) {
        	logger.error("Exception while Inserting data to DB  :"+e.getMessage());
    		e.printStackTrace();
        	throw new BusinessException(e.getMessage(), e);
        }
		return  userMappingBOResponse;
	}
		
}
