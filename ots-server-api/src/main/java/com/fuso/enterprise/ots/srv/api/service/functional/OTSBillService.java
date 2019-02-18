package com.fuso.enterprise.ots.srv.api.service.functional;

import com.fuso.enterprise.ots.srv.api.service.request.BillDetailsBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.BillDetailsBOResponse;

public interface OTSBillService {

	BillDetailsBOResponse addOrUpdateBill(BillDetailsBORequest billDetailsBORequest);

}
