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

import com.fuso.enterprise.ots.srv.api.model.domain.EmpLatLong;
import com.fuso.enterprise.ots.srv.api.service.request.EmpLatLongBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetEmpLatLongBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.GetEmpLatLongBOResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.server.dao.EmpLatLonServiceDAO;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsLatLon;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;
@Repository
public class EmpLatLonServiceDAOImpl extends AbstractIptDao<OtsLatLon, String> implements EmpLatLonServiceDAO {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
    private JdbcTemplate jdbcTemplate;
	public EmpLatLonServiceDAOImpl() {
		super(OtsLatLon.class);
	}
	@Override
	public String updateEmpLatLong(EmpLatLongBORequest empLatLongBORequest) {
		String responseData;
		try {
			OtsLatLon otsLatLon = new OtsLatLon();
			otsLatLon.setOtsLatitude(empLatLongBORequest.getRequestData().getLatitude());
			otsLatLon.setOtsLongitude(empLatLongBORequest.getRequestData().getLongitude());
			OtsUsers otsUsers = new OtsUsers();
			otsUsers.setOtsUsersId(Integer.parseInt(empLatLongBORequest.getRequestData().getUserId()));
			otsLatLon.setOtsUsersusersId(otsUsers);
			super.getEntityManager().merge(otsLatLon);
			super.getEntityManager().flush();
			responseData = "Updated Latutude and Longitude values Successfully";
			logger.info("Inside Event=1022,Class:EmpLatLonServiceDAOImpl,Method:addEmpLatLong");
		} catch (NoResultException e) {
			logger.error("Exception while Inserting data to DB  :" + e.getMessage());
			e.printStackTrace();
			throw new BusinessException(e.getMessage(), e);
		}
		return responseData;
	}

	@Override
	public GetEmpLatLongBOResponse getEmpLatLong(GetEmpLatLongBORequest getEmpLatLongBORequest) {
		GetEmpLatLongBOResponse getEmpLatLongBOResponse = new GetEmpLatLongBOResponse();
		List<EmpLatLong> empLatLong = new ArrayList<EmpLatLong>();
		try {
			OtsLatLon otsLatLon = null;

			Map<String, Object> queryParameter = new HashMap<>();
			OtsUsers otsuserid = new OtsUsers();
			otsuserid.setOtsUsersId(Integer.valueOf(getEmpLatLongBORequest.getRequestData().getUserId()));
			queryParameter.put("otsUserId", otsuserid);
			otsLatLon = super.getResultByNamedQuery("OtsLatLon.getLatLongByUserId", queryParameter);
			logger.info("Inside Event=1022,Class:EmpLatLonServiceDAOImpl,Method:getEmpLatLong");
			empLatLong.add(convertUserDetailsFromEntityToDomain(otsLatLon));
			getEmpLatLongBOResponse.setEmpLatLong(empLatLong);
		} catch (NoResultException e) {
			logger.error("Exception while Fetching data from DB  :" + e.getMessage());
			e.printStackTrace();
			throw new BusinessException(e.getMessage(), e);
		}
		return getEmpLatLongBOResponse;
	}

	private EmpLatLong convertUserDetailsFromEntityToDomain(OtsLatLon otsLatLonRes) {
			EmpLatLong empLatLong=new EmpLatLong();
			empLatLong.setLatLongId(otsLatLonRes.getOtsLatLonId().toString());
			empLatLong.setUserId(otsLatLonRes.getOtsUsersusersId().getOtsUsersId().toString());
			empLatLong.setLatitude(otsLatLonRes.getOtsLatitude());
			empLatLong.setLongitude(otsLatLonRes.getOtsLongitude());
			return empLatLong;
	}

}
