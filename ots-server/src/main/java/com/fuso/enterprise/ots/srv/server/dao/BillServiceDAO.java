package com.fuso.enterprise.ots.srv.server.dao;

import com.fuso.enterprise.ots.srv.api.service.request.BillDetailsBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.BillDetailsBOResponse;

public interface BillServiceDAO {

	BillDetailsBOResponse addOrUpdateBill(BillDetailsBORequest billDetailsBORequest);

}