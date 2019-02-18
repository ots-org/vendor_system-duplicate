package com.fuso.enterprise.ots.srv.server.dao;

import java.util.List;

import com.fuso.enterprise.ots.srv.api.model.domain.ListOfOrderId;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsBill;

public interface OrderServiceDAO {

	void updateOrderwithBillID(OtsBill otsBill, List<ListOfOrderId> listOfOrderId);

}
