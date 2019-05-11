package com.fuso.enterprise.ots.srv.functional.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fuso.enterprise.ots.srv.api.model.domain.BillOrderModelDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.GetProductDetailsForBillModel;
import com.fuso.enterprise.ots.srv.api.model.domain.ListOfOrderId;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.ProductDetailsList;
import com.fuso.enterprise.ots.srv.api.model.domain.UserDetails;
import com.fuso.enterprise.ots.srv.api.service.functional.OTSBillService;
import com.fuso.enterprise.ots.srv.api.service.functional.OTSProductService;
import com.fuso.enterprise.ots.srv.api.service.request.BillDetailsBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.BillReportBasedOnDateBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.CustomerOutstandingBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetBillByOrderIdBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetCustomerOutstandingAmtBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetProductDetailsForBillRequst;
import com.fuso.enterprise.ots.srv.api.service.response.BillDataBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.BillDetailsBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.BillProductDetailsResponse;
import com.fuso.enterprise.ots.srv.api.service.response.BillReportByDateBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetCustomerOutstandingAmtBOResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.server.dao.BillServiceDAO;
import com.fuso.enterprise.ots.srv.server.dao.CustomerOutstandingAmtDAO;
import com.fuso.enterprise.ots.srv.server.dao.OrderDAO;
import com.fuso.enterprise.ots.srv.server.dao.OrderProductDAO;
import com.fuso.enterprise.ots.srv.server.dao.OrderServiceDAO;
import com.fuso.enterprise.ots.srv.server.dao.UserServiceDAO;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsBill;
import com.fuso.enterprise.ots.srv.server.util.EmailUtil;
import com.fuso.enterprise.ots.srv.server.util.OTSUtil;
import com.itextpdf.text.Document;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
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

@Inject
private OTSProductService otsProductService;

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
			/*
			 * Getting customer and product detail of bill 
			 */
			GetProductDetailsForBillRequst getProductDetailsForBillRequst = new GetProductDetailsForBillRequst();
			GetProductDetailsForBillModel getProductDetailsForBillModel = new GetProductDetailsForBillModel();
			BillProductDetailsResponse billProductDetailsResponse = new BillProductDetailsResponse(); 
			List<String> listOrdStr = new ArrayList<String>();
			for(ListOfOrderId orderIdList:listOfOrderId ) {
				listOrdStr.add(orderIdList.getOrderId());
			}
			getProductDetailsForBillModel.setOrderId(listOrdStr);
			getProductDetailsForBillRequst.setRequest(getProductDetailsForBillModel);
			billProductDetailsResponse=otsProductService.getProductDetailsForBill(getProductDetailsForBillRequst);
			List<ProductDetailsList> lisOfproduct = billProductDetailsResponse.getProductDeatils();
			String productString = "";
			int slno=0;
			int totalPrice = 0;
			for(ProductDetailsList  productDetails: lisOfproduct) {
				slno++;
				totalPrice = totalPrice + Integer.parseInt(productDetails.getTotalProductPrice());
				productString=productString+"<tr>\r\n" + 
						"<td style=\"width: 45.6px; text-align: center;\">"+slno+"</td>\r\n" + 
						"<td style=\"width: 137.6px; text-align: center;\">"+productDetails.getProductName()+"</td>\r\n" + 
						"<td style=\"width: 96.8px; text-align: center;\">"+productDetails.getProductqty()+"</td>\r\n" + 
						"<td style=\"width: 96.8px; text-align: center;\">"+productDetails.getProductPrice()+"</td>\r\n" + 
						"<td style=\"width: 100px; text-align: center;\">"+productDetails.getTotalProductPrice()+"</td>\r\n" + 
						"</tr>\r\n" ;
			}
			/*
			 * Create html for Bill document
			 */
			String htmlString = "<html><body> <br> <table style=\"width: 508px; height: 90px;\" border=\"1\">\r\n" + 
					"<tbody>\r\n" + 
					"<tr>\r\n" + 
					"<td style=\"width: 244px;\">From Date</td>\r\n" + 
					"<td style=\"width: 259px;\">"+billDetailsBORequest.getRequestData().getFromDate()+"</td>\r\n" + 
					"</tr>\r\n" + 
					"<tr>\r\n" + 
					"<td style=\"width: 244px;\">To Date&nbsp;</td>\r\n" + 
					"<td style=\"width: 259px;\">"+billDetailsBORequest.getRequestData().getToDate()+"</td>\r\n" + 
					"</tr>\r\n" + 
					"<tr>\r\n" + 
					"<td style=\"width: 244px;\">Distributor Name</td>\r\n" + 
					"<td style=\"width: 259px;\">"+billProductDetailsResponse.getDistributorDetails().getFirstName()+""+billProductDetailsResponse.getDistributorDetails().getLastName()+"</td>\r\n" + 
					"</tr>\r\n" + 
					"<tr>\r\n" + 
					"<td style=\"width: 244px;\">Customer Name</td>\r\n" + 
					"<td style=\"width: 259px;\">"+billProductDetailsResponse.getCustomerDetails().getFirstName()+" "+billProductDetailsResponse.getCustomerDetails().getLastName()+"</td>\r\n" + 
					"</tr>\r\n" + 
					"<tr>\r\n" + 
					"<td style=\"width: 244px;\">Customer Address</td>\r\n" + 
					"<td style=\"width: 259px;\">"+billProductDetailsResponse.getCustomerDetails().getAddress1()+",<br>"+billProductDetailsResponse.getCustomerDetails().getAddress1()+"</td>\r\n" + 
					"</tr>\r\n" + 
					"<tr>\r\n" + 
					"<td style=\"width: 244px;\">Bill No</td>\r\n" + 
					"<td style=\"width: 259px;\">OtsBill-"+billDetailsBOResponse.getBillDetails().get(0).getBillId()+"</td>\r\n" + 
					"</tr>\r\n" + 
					"</tbody>\r\n" + 
					"</table>\r\n" + 
					"<p>&nbsp;</p>\r\n" + 
					"<table style=\"width: 508px; height: 38px;\" border=\"1\">\r\n" + 
					"<tbody>\r\n" + 
					"<tr style=\"background-color: #808080;\">\r\n" + 
					"<td style=\"width: 45.6px; text-align: center;\"><strong>Slno</strong></td>\r\n" + 
					"<td style=\"width: 137.6px; text-align: center;\"><strong>Description</strong></td>\r\n" + 
					"<td style=\"width: 96.8px; text-align: center;\"><strong>Unit/Quantity</strong></td>\r\n" + 
					"<td style=\"width: 96.8px; text-align: center;\"><strong>Rate&nbsp;</strong></td>\r\n" + 
					"<td style=\"width: 100px; text-align: center;\"><strong>&nbsp;Amount</strong></td>\r\n" + 
					"</tr>\r\n" + 
					"<tr>\r\n"
					+ ""+productString+"" + 
					"<tr>\r\n" + 
					"<td style=\"width: 96.8px; text-align: right;\" colspan=\"4\"><strong>Total</strong></td>\r\n" + 
					"<td style=\"width: 100px; text-align: center;\">"+totalPrice+"</td>\r\n" + 
					"</tr>\r\n" + 
					"<tr>\r\n" + 
					"<td style=\"width: 96.8px; text-align: right;\" colspan=\"4\"><strong>SGST</strong></td>\r\n" + 
					"<td style=\"width: 100px; text-align: center;\">"+billDetailsBORequest.getRequestData().getIGST()+"</td>\r\n" + 
					"</tr>\r\n" + 
					"<tr>\r\n" + 
					"<td style=\"width: 96.8px; text-align: right;\" colspan=\"4\"><strong>CGST</strong></td>\r\n" + 
					"<td style=\"width: 100px; text-align: center;\">"+billDetailsBORequest.getRequestData().getCGST()+"</td>\r\n" + 
					"</tr>\r\n" + 
					"<tr>\r\n" + 
					"<td style=\"width: 96.8px; text-align: right;\" colspan=\"4\"><strong>Total</strong></td>\r\n" + 
					"<td style=\"width: 100px; text-align: center;\">"+billDetailsBORequest.getRequestData().getBillAmount()+"</td>\r\n" + 
					"</tr>\r\n" + 
					"</tbody>\r\n" + 
					"</table>\r\n" + 
					"<p>&nbsp;Rupees in words :</p>\r\n" + 
					"<p>&nbsp;</p>\r\n" + 
					"<p>&nbsp;</p>\r\n" + 
					"<p>&nbsp;</p>\r\n" + 
					"<table style=\"width: 126.4px;\">\r\n" + 
					"<tbody>\r\n" + 
					"<tr style=\"height: 43.2px;\">\r\n" + 
					"<td style=\"width: 139.4px; height: 43.2px;\">Signature&nbsp;</td>\r\n" + 
					"</tr>\r\n" + 
					"</tbody>\r\n" + 
					"</table> </body></html>";
			String billNO = billDetailsBOResponse.getBillDetails().get(0).getBillId()+"";
			OTSUtil.generatePDFFromHTML(htmlString,billNO);
			
			byte[] fileContent = FileUtils.readFileToByteArray(new File("C:\\template\\OtsBill-"+billNO+".pdf"));
			String encodedString = Base64.getEncoder().encodeToString(fileContent);
			billDetailsBORequest.getRequestData().setBillId(billDetailsBOResponse.getBillDetails().get(0).getBillId());
			billDetailsBORequest.getRequestData().setBillPdf(encodedString);
			billDetailsBOResponse = billServiceDAO.addOrUpdateBill(billDetailsBORequest);
			EmailUtil.sendEmailBill(billProductDetailsResponse.getCustomerDetails().getEmailId(),billProductDetailsResponse.getDistributorDetails().getEmailId(), "Your Bill for Number: OtsBill-"+billNO+", Bill Date: 12-Apr-2019\r\n" + 
					"", "Please find the attachment for your water can bill generated", "OtsBill-"+billNO+".pdf", "C:\\template\\OtsBill-"+billNO+".pdf");
	
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
