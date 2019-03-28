package com.fuso.enterprise.ots.srv.functional.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fuso.enterprise.ots.srv.api.model.domain.BillOrderModelDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.ListOfOrderId;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;
import com.fuso.enterprise.ots.srv.api.service.functional.OTSBillService;
import com.fuso.enterprise.ots.srv.api.service.request.BillDetailsBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.BillReportBasedOnDateBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.CustomerOutstandingBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetBillByOrderIdBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetCustomerOutstandingAmtBORequest;
import com.fuso.enterprise.ots.srv.api.service.response.BillDataBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.BillDetailsBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.BillReportByDateBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetCustomerOutstandingAmtBOResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.server.dao.BillServiceDAO;
import com.fuso.enterprise.ots.srv.server.dao.CustomerOutstandingAmtDAO;
import com.fuso.enterprise.ots.srv.server.dao.OrderDAO;
import com.fuso.enterprise.ots.srv.server.dao.OrderProductDAO;
import com.fuso.enterprise.ots.srv.server.dao.OrderServiceDAO;
import com.fuso.enterprise.ots.srv.server.dao.UserServiceDAO;
import com.fuso.enterprise.ots.srv.server.dao.impl.OrderDAOImpl;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsBill;
@Service
@Transactional
public class OTSBillServiceImpl implements OTSBillService {
private Logger logger = LoggerFactory.getLogger(getClass());
private BillServiceDAO billServiceDAO;
private OrderServiceDAO orderServiceDAO;
private CustomerOutstandingAmtDAO customerOutstandingAmtDAO;
private OrderProductDAO orderProductDAO;
private UserServiceDAO userServiceDAO;
private OrderDAO orderDAO;
	
@Inject
public OTSBillServiceImpl(BillServiceDAO billServiceDAO,OrderServiceDAO orderServiceDAO,CustomerOutstandingAmtDAO customerOutstandingAmtDAO,OrderProductDAO orderProductDAO,UserServiceDAO userServiceDAO,OrderDAO orderDAO) {
	this.billServiceDAO=billServiceDAO;
	this.orderServiceDAO=orderServiceDAO;
	this.customerOutstandingAmtDAO=customerOutstandingAmtDAO;
	this.orderProductDAO = orderProductDAO;
	this.userServiceDAO = userServiceDAO;
	this.orderDAO=orderDAO;
	
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
	
	@Override
	public String updateCustomerOutstandingAmt(CustomerOutstandingBORequest customerOutstandingBORequest) {
		String responseData;
		try {
			responseData = customerOutstandingAmtDAO.updateCustomerOutstandingAmt(customerOutstandingBORequest);
            } catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return responseData;
	}
	
	@Override
	public GetCustomerOutstandingAmtBOResponse getCustomerOutstandingAmt(GetCustomerOutstandingAmtBORequest getCustomerOutstandingAmtBORequest) {
		GetCustomerOutstandingAmtBOResponse customerOutstandingAmtResponse = new GetCustomerOutstandingAmtBOResponse();
		try {
			customerOutstandingAmtResponse = customerOutstandingAmtDAO.getCustomerOutstandingAmt(getCustomerOutstandingAmtBORequest);
            } catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return customerOutstandingAmtResponse;
	}
	
	@Override
	public BillDataBOResponse getBillDetailsByOrderId(GetBillByOrderIdBORequest getBillByOrderIdBORequest) {
		BillDataBOResponse billDataBOResponse = new BillDataBOResponse();
		try {
		billDataBOResponse = billServiceDAO.getBillDetailsByOrderId(getBillByOrderIdBORequest);
		List<OrderDetails> OrderDetails = orderServiceDAO.getListOrderForBill(billDataBOResponse.getGetBillDetails());
		
		List<BillOrderModelDetails> billOrderModelDetailsList = new ArrayList<BillOrderModelDetails>();
		
		for(int i = 0 ; i<OrderDetails.size() ; i++) {
			BillOrderModelDetails billOrderModelDetails = new BillOrderModelDetails();

			billOrderModelDetails.setDelivaryDate(OrderDetails.get(i).getOrderDeliveryDate());
			billOrderModelDetails.setDeliverdDate(OrderDetails.get(i).getOrderDeliverdDate());
			billOrderModelDetails.setOrderNumber(OrderDetails.get(i).getOrderNumber());
			billOrderModelDetails.setOrderStatus(OrderDetails.get(i).getStatus());
			billOrderModelDetails.setOrderCost(OrderDetails.get(i).getOrderCost());
			
			UserDetails DistriutorDetails,CustomerDetails,EmployeeDetails ;
			DistriutorDetails = userServiceDAO.getUserDetails(Integer.parseInt(OrderDetails.get(i).getDistributorId()));
			CustomerDetails = userServiceDAO.getUserDetails(Integer.parseInt(OrderDetails.get(i).getCustomerId()));
			EmployeeDetails = userServiceDAO.getUserDetails(Integer.parseInt(OrderDetails.get(i).getAssignedId()));
			billOrderModelDetails.setOrderId(OrderDetails.get(i).getOrderId());
			billOrderModelDetails.setDistriutorDetails(DistriutorDetails);
			billOrderModelDetails.setCustomerDetails(CustomerDetails);
			billOrderModelDetails.setEmployeeDetails(EmployeeDetails);
			
			billOrderModelDetails.setOrderProductDetails(orderProductDAO.getProductListByOrderId(OrderDetails.get(i).getOrderId()));
		
			billOrderModelDetailsList.add(i,billOrderModelDetails);
			
		}
		billDataBOResponse.getGetBillDetails().setBillOrderModelDetailsList(billOrderModelDetailsList);
		}catch(Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return billDataBOResponse;
	}
	@Override
	public BillReportByDateBOResponse getBillReportByDate(BillReportBasedOnDateBORequest billReportBasedOnDateBORequest) {
		BillReportByDateBOResponse billIdResponse=new BillReportByDateBOResponse();
		try {
			billIdResponse=billServiceDAO.getBillReportByDate(billReportBasedOnDateBORequest);
		}catch(Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return billIdResponse;
	}
}
