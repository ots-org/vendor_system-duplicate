package com.fuso.enterprise.ots.srv.server.dao.impl;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fuso.enterprise.ots.srv.api.model.domain.ProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.SchedulerResponceOrderModel;
import com.fuso.enterprise.ots.srv.api.service.request.GetSchedulerRequest;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.common.exception.ErrorEnumeration;
import com.fuso.enterprise.ots.srv.server.dao.RequestOrderServiceDao;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsProduct;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsRequestOrder;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsScheduler;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsUsers;
import com.fuso.enterprise.ots.srv.server.util.AbstractIptDao;
@Repository
public class RequestOrderServiceDaoImpl extends AbstractIptDao<OtsRequestOrder, String> implements RequestOrderServiceDao{
	private Logger logger = LoggerFactory.getLogger(getClass());
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Date date = new Date();
	@Autowired
    private JdbcTemplate jdbcTemplate;

    public RequestOrderServiceDaoImpl() {
        super(OtsRequestOrder.class);
    }

	@Override
	public String insertingOrderForScheduling(List<OtsScheduler> schedulerList) {
		OtsRequestOrder RequestOrder;
		Calendar c = Calendar.getInstance();
		SimpleDateFormat simpleDateformat = new SimpleDateFormat("E");
        Date today = new Date();
        simpleDateformat = new SimpleDateFormat("EEEE"); 
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		c.setTime(date);	
		try {
			for(int i=0;i<schedulerList.size();i++) {
				RequestOrder = new OtsRequestOrder();
				RequestOrder.setOtsCustomerId(schedulerList.get(i).getOtsCustomerId());
				RequestOrder.setOtsDistributorId(schedulerList.get(i).getOtsDistributorId());
				RequestOrder.setOtsProductId(schedulerList.get(i).getOtsProductId());
				RequestOrder.setOtsRequestQty(schedulerList.get(i).getOtsOrderQty());
				OtsScheduler shedulerId = new OtsScheduler();
				shedulerId.setOtsSchedulerId(schedulerList.get(i).getOtsSchedulerId());
				RequestOrder.setOtsSchedulerId(shedulerId);
				RequestOrder.setOtsRequestStatus("Active");
				if(schedulerList.get(i).getOtsSchedulerType().equals("Daily")) {
					
					c.add(Calendar.DAY_OF_MONTH, 1);
					RequestOrder.setOtsScheduleDt(c.getTime());
					
					c.add(Calendar.DAY_OF_MONTH, 1);
					RequestOrder.setOtsNxtScheduleDt(c.getTime());
					
					RequestOrder.setOtsRequestQty(schedulerList.get(i).getOtsOrderQty());
				}else if(schedulerList.get(i).getOtsSchedulerType().equals("weekly")){
					System.out.println("dayOfWeek "+simpleDateformat.format(today));
					if(schedulerList.get(i).getOtsSchedulerWkdy().equals(simpleDateformat.format(today))) {
						System.out.println("dayOfWeek "+Integer.valueOf(dayOfWeek));
						c.add(Calendar.DAY_OF_MONTH, 7);
						RequestOrder.setOtsScheduleDt(c.getTime());
						c.add(Calendar.DAY_OF_MONTH, 7);
						RequestOrder.setOtsNxtScheduleDt(c.getTime());
					}else {
						String scheduledDate = nextDateOfDay(schedulerList.get(i).getOtsSchedulerWkdy());
						System.out.println("scheduledDate "+scheduledDate);
						SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
						Date date;
						try {
							date = formatter.parse(scheduledDate);
							RequestOrder.setOtsScheduleDt(date);
							c.setTime(date);
							c.add(Calendar.DAY_OF_MONTH, 7);
							RequestOrder.setOtsNxtScheduleDt(c.getTime());
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				}
				save(RequestOrder);
				super.getEntityManager().flush();
				RequestOrder.setOtsRequestNumber("OtsSch"+RequestOrder.getOtsRequestOrderId());
				save(RequestOrder);
			}
		}catch(Exception e){
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_SCHEDULERREQUEST);
		} catch (Throwable e) {
			throw new BusinessException(e, ErrorEnumeration.ERROR_IN_SCHEDULERREQUEST);
		}
		return "Inserted";
	}
    
    public String nextDateOfDay(String day) {
    	LocalDate today = LocalDate.now();;
    	switch(day){
    	case "Sunday":
    		return today.with(TemporalAdjusters.next(DayOfWeek.SUNDAY)).toString();
    	case "Monday":
    		return today.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).toString();
    	case "Tuesday":
    		return today.with(TemporalAdjusters.next(DayOfWeek.TUESDAY)).toString();
    	case "Wednesday ":
    		return today.with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY)).toString();
    	case "Thursday":
    		return today.with(TemporalAdjusters.next(DayOfWeek.THURSDAY)).toString();
    	case "Friday":
    		return today.with(TemporalAdjusters.next(DayOfWeek.FRIDAY)).toString();
    	case "Saturday":
    		return today.with(TemporalAdjusters.next(DayOfWeek.SATURDAY)).toString();
    	}
		return day;
    	
    }

	@Override
	public List<SchedulerResponceOrderModel> getScheduler(GetSchedulerRequest getSchedulerRequest) {
		List<SchedulerResponceOrderModel> schedulerRequestOrderModel = new ArrayList<SchedulerResponceOrderModel>();
		Map<String, Object> queryParameter = new HashMap<>();
		OtsUsers distributorId = new  OtsUsers();
		distributorId.setOtsUsersId(Integer.valueOf(getSchedulerRequest.getRequest().getDistributorId()));
		Calendar c = Calendar.getInstance();
		List<OtsRequestOrder> RequestOrderList ;
		
		queryParameter.put("DistributorId",distributorId);
		queryParameter.put("status",getSchedulerRequest.getRequest().getStatus());
		c.setTime(getSchedulerRequest.getRequest().getDate());
		c.add(Calendar.DAY_OF_MONTH, -1);
		queryParameter.put("todayDate",c.getTime());
		c.add(Calendar.DAY_OF_MONTH, 2);
		Date date = c.getTime();
		queryParameter.put("tomorrow",date);
		RequestOrderList  = super.getResultListByNamedQuery("OtsRequestOrder.getSchedulerDetails", queryParameter);
		schedulerRequestOrderModel = RequestOrderList.stream().map(OtsRequestOrder -> convertEntityToDomine(OtsRequestOrder)).collect(Collectors.toList());
		return schedulerRequestOrderModel;
	}
	
	public SchedulerResponceOrderModel convertEntityToDomine(OtsRequestOrder requestOrder) {
		SchedulerResponceOrderModel SchedulerRequestOrderModel = new SchedulerResponceOrderModel();
		SchedulerRequestOrderModel.setCustomerId(requestOrder.getOtsCustomerId().getOtsUsersId().toString());
		SchedulerRequestOrderModel.setNxtScheduledDate(requestOrder.getOtsNxtScheduleDt().toString());
		SchedulerRequestOrderModel.setProductId(requestOrder.getOtsProductId().getOtsProductId().toString());
		SchedulerRequestOrderModel.setReqStatus(requestOrder.getOtsRequestStatus());
		SchedulerRequestOrderModel.setRequestedQty(requestOrder.getOtsRequestQty().toString());
		SchedulerRequestOrderModel.setRequestOrderId(requestOrder.getOtsRequestOrderId().toString());
		SchedulerRequestOrderModel.setScheduledDate(requestOrder.getOtsScheduleDt().toString());
		return SchedulerRequestOrderModel;		
	}

	@Override
	public List<OtsRequestOrder> runSchedulerEveryDay12AMTo1AM(List<OtsScheduler> schedulerList) {
		Map<String, Object> queryParameter = new HashMap<>();
		List<OtsRequestOrder> requestOrderList = new ArrayList<OtsRequestOrder>() ;
		for(int i=0;i<schedulerList.size();i++) {
			OtsScheduler otsScheduler = new OtsScheduler();
			Calendar c = Calendar.getInstance();
			Calendar c1 = Calendar.getInstance();
			otsScheduler.setOtsSchedulerId(schedulerList.get(i).getOtsSchedulerId());
			if(schedulerList.get(i).getOtsSchedulerType().equals("Daily")) {
				
				c.add(Calendar.DAY_OF_MONTH, -1);
				queryParameter.put("yesterday",c.getTime());
				
				c1.add(Calendar.DAY_OF_MONTH, 1);
				queryParameter.put("tomorrow",c1.getTime());
				
				queryParameter.put("otsSchedulerId",otsScheduler);
				requestOrderList  = super.getResultListByNamedQuery("OtsRequestOrder.getSchedulerDetailsForCronJob", queryParameter);
				for(int j=0;j<requestOrderList.size();j++) {
					OtsRequestOrder requestOrder = new OtsRequestOrder();	
					requestOrder = requestOrderList.get(j);
					requestOrder.setOtsScheduleDt(requestOrder.getOtsNxtScheduleDt());
					requestOrder.setOtsNxtScheduleDt(c1.getTime());
 					super.getEntityManager().merge(requestOrder);
				}
			}else if(schedulerList.get(i).getOtsSchedulerType().equals("weekly")) {
				c.add(Calendar.DAY_OF_MONTH, -1);
				queryParameter.put("yesterday",c.getTime());
				c1.add(Calendar.DAY_OF_MONTH, 1);
				queryParameter.put("tomorrow",c1.getTime());
				queryParameter.put("otsSchedulerId",otsScheduler);
				requestOrderList  = super.getResultListByNamedQuery("OtsRequestOrder.getSchedulerDetailsForCronJob", queryParameter);
				for(int j=0;j<requestOrderList.size();j++) {
					OtsRequestOrder requestOrder = new OtsRequestOrder();	
					requestOrder = requestOrderList.get(j);
					requestOrder.setOtsScheduleDt(requestOrder.getOtsNxtScheduleDt());
					c1.add(Calendar.DAY_OF_MONTH, 6);
					requestOrder.setOtsNxtScheduleDt(c1.getTime());
					super.getEntityManager().merge(requestOrder);
				}
			}
			
		}
		return requestOrderList;
		
	}
}
