package com.fuso.enterprise.ots.srv.functional.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fuso.enterprise.ots.srv.api.model.domain.ListOfOrderId;
import com.fuso.enterprise.ots.srv.api.service.functional.OTSBillService;
import com.fuso.enterprise.ots.srv.api.service.request.BillDetailsBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.BillDetailsBOResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.server.dao.BillServiceDAO;
import com.fuso.enterprise.ots.srv.server.dao.OrderServiceDAO;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsBill;
@Service
@Transactional
public class OTSBillServiceImpl implements OTSBillService {
private BillServiceDAO billServiceDAO;
private OrderServiceDAO orderServiceDAO;
	
	@Inject
	public OTSBillServiceImpl(BillServiceDAO billServiceDAO,OrderServiceDAO orderServiceDAO) {
		this.billServiceDAO=billServiceDAO;
		this.orderServiceDAO=orderServiceDAO;
	}
	@Override
	public BillDetailsBOResponse addOrUpdateBill(BillDetailsBORequest billDetailsBORequest) {
		BillDetailsBOResponse billDetailsBOResponse = new BillDetailsBOResponse();
		OtsBill otsBill = new OtsBill();
		try {
			billDetailsBOResponse = billServiceDAO.addOrUpdateBill(billDetailsBORequest);
			List<ListOfOrderId> listOfOrderId=billDetailsBORequest.getRequestData().getOrderId();
			otsBill.setOtsBillId(billDetailsBOResponse.getBillDetails().get(0).getBillId());
			/*
			 * calling orderServiceDAo for updating billId in order table
			 */
			orderServiceDAO.updateOrderwithBillID(otsBill,listOfOrderId);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return billDetailsBOResponse;
	}

}
