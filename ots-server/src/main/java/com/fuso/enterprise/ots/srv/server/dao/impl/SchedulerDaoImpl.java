package com.fuso.enterprise.ots.srv.server.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fuso.enterprise.ots.srv.api.model.domain.SchedulerResponceOrderModel;
import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;
import com.fuso.enterprise.ots.srv.api.service.request.AddSchedulerRequest;
import com.fuso.enterprise.ots.srv.server.dao.SchedulerDao;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsProduct;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsScheduler;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;
@Repository
public class SchedulerDaoImpl extends AbstractIptDao<OtsScheduler, String> implements SchedulerDao {

	private Logger logger = LoggerFactory.getLogger(getClass());
	Date today = Calendar.getInstance().getTime();
	@Autowired
    private JdbcTemplate jdbcTemplate;

    public SchedulerDaoImpl() {
        super(OtsScheduler.class);
    }

	@Override
	public List<OtsScheduler> InsertScheduler(AddSchedulerRequest addSchedulerRequest) {
		List<OtsScheduler> SchedulerList = new ArrayList<OtsScheduler>();
		for(int i=0 ;i<addSchedulerRequest.getAddScheduler().size();i++) {
			for(int j=0;j<addSchedulerRequest.getAddScheduler().get(i).getSchduleweekDays().size();j++) {	
				
				OtsScheduler otsScheduler = new OtsScheduler();
				Calendar c = Calendar.getInstance();
				OtsUsers did = new OtsUsers();
				OtsUsers cid = new OtsUsers();
				did.setOtsUsersId(Integer.parseInt(addSchedulerRequest.getDistributorId()));
				otsScheduler.setOtsDistributorId(did);
				cid.setOtsUsersId(Integer.parseInt(addSchedulerRequest.getCustomerId()));
				otsScheduler.setOtsCustomerId(cid);
				otsScheduler.setOtsSchedulerType(addSchedulerRequest.getTypeOfSchedule());
		
				OtsProduct productId = new  OtsProduct();
				productId.setOtsProductId(Integer.parseInt(addSchedulerRequest.getAddScheduler().get(i).getProdcutId()));
				otsScheduler.setOtsProductId(productId);
				otsScheduler.setOtsOrderQty(Integer.parseInt(addSchedulerRequest.getAddScheduler().get(i).getProdcutQty()));
				otsScheduler.setOtsSchedulerEtDt(addSchedulerRequest.getAddScheduler().get(i).getEndDate());
				otsScheduler.setOtsSchedulerStDt(addSchedulerRequest.getAddScheduler().get(i).getStartDate());
			
				otsScheduler.setOtsSchedulerWkdy(addSchedulerRequest.getAddScheduler().get(i).getSchduleweekDays().get(j));
				save(otsScheduler);
				super.getEntityManager().flush();
				SchedulerList.add(otsScheduler);
				detach(otsScheduler);
			}
			
		}return SchedulerList;
		
		
	}

	@Override
	public List<OtsScheduler> runScheduler12AMTO1AM() {
		Map<String, Object> queryParameter = new HashMap<>();
		List<OtsScheduler> SchedulerList = new ArrayList<OtsScheduler>();
		Calendar c = Calendar.getInstance();
		Date today = new Date();
		c.setTime(today);
		System.out.println("today"+today);
		queryParameter.put("today",c.getTime());
		System.out.println("today"+c.getTime());
		
		SchedulerList = super.getResultListByNamedQuery("OtsScheduler.getSchedulerDetailsForCronJob", queryParameter);
		return SchedulerList;
	}
	
}
