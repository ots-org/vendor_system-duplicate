package com.fuso.enterprise.ots.srv.api.service.functional;

import com.fuso.enterprise.ots.srv.api.service.request.BillDetailsBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.BillReportBasedOnDateBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.CustomerOutstandingBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetBillByOrderIdBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetCustomerOutstandingAmtBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.BillDataBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.BillDetailsBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.BillReportByDateBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetCustomerOutstandingAmtBOResponse;

public interface OTSBillService {

	BillDetailsBOResponse addOrUpdateBill(BillDetailsBORequest billDetailsBORequest);

	String updateCustomerOutstandingAmt(CustomerOutstandingBORequest customerOutstandingBORequest);

	GetCustomerOutstandingAmtBOResponse getCustomerOutstandingAmt(GetCustomerOutstandingAmtBORequest getCustomerOutstandingAmtBORequest);

	BillDataBOResponse getBillDetailsByOrderId(GetBillByOrderIdBORequest getBillByOrderIdBORequest);

	BillReportByDateBOResponse getBillReportByDate(BillReportBasedOnDateBORequest billReportBasedOnDateBORequest);

}
