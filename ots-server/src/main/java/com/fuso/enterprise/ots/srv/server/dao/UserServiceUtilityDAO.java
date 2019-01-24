package com.fuso.enterprise.ots.srv.server.dao;

import java.util.List;

import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;
import com.fuso.enterprise.ots.srv.api.service.request.RequestBOUserBySearch;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;

public interface UserServiceUtilityDAO  {

	List<UserDetails> getUserDetails(RequestBOUserBySearch requestBOUserBySearch);

}
