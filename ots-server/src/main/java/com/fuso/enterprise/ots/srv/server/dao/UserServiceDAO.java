package com.fuso.enterprise.ots.srv.server.dao;

import java.util.List;

import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;

public interface UserServiceDAO {
	
	List<UserDetails> getUserIdUsers(String userId);
	
}
