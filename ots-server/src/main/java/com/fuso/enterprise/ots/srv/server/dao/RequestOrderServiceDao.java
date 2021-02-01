package com.fuso.enterprise.ots.srv.server.dao;

import java.util.List;

import com.fuso.enterprise.ots.srv.api.model.domain.SchedulerResponceOrderModel;
import com.fuso.enterprise.ots.srv.api.service.request.GetSchedulerRequest;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsRequestOrder;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsScheduler;

public interface RequestOrderServiceDao {
	String insertingOrderForScheduling(List<OtsScheduler> schedulerList);
	List<SchedulerResponceOrderModel> getScheduler(GetSchedulerRequest getSchedulerRequest);
	List<OtsRequestOrder> runSchedulerEveryDay12AMTo1AM(List<OtsScheduler> schedulerList);
}