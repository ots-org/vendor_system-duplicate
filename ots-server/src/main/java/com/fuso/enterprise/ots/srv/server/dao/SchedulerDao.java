package com.fuso.enterprise.ots.srv.server.dao;

import java.util.List;

import com.fuso.enterprise.ots.srv.api.model.domain.SchedulerResponceOrderModel;
import com.fuso.enterprise.ots.srv.api.service.request.AddSchedulerRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetSchedulerRequest;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsScheduler;

public interface SchedulerDao {

	List<OtsScheduler> InsertScheduler(AddSchedulerRequest addSchedulerRequest);

	List<OtsScheduler> runScheduler12AMTO1AM();

	List<SchedulerResponceOrderModel> getSchedularData(GetSchedulerRequest getSchedulerRequest);
}
