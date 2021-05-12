package com.fuso.enterprise.ots.srv.server.dao;

import com.fuso.enterprise.ots.srv.api.service.request.BillDetailsBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.BillReportBasedOnDateBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.CustomerOutstandingBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetBillByOrderIdBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.BillDataBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.BillDetailsBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.BillReportByDateBOResponse;

public interface BillServiceDAO {

	BillDetailsBOResponse addOrUpdateBill(BillDetailsBORequest billDetailsBORequest);

	BillDataBOResponse getBillDetailsByOrderId(GetBillByOrderIdBORequest getBillByOrderIdBORequest);

	BillReportByDateBOResponse getBillReportByDate(BillReportBasedOnDateBORequest billReportBasedOnDateBORequest);

	BillReportByDateBOResponse getBillReportForDistributorByDate(
			BillReportBasedOnDateBORequest billReportBasedOnDateBORequest);

}
