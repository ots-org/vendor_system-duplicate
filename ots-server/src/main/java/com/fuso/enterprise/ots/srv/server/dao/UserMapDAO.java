package com.fuso.enterprise.ots.srv.server.dao;

import com.fuso.enterprise.ots.srv.api.service.request.MapUsersDataBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.MapUsersDataBOResponse;

public interface UserMapDAO {

	String mappUser(MapUsersDataBORequest mapUsersDataBORequest);

}
