package com.fuso.enterprise.ots.srv.functional.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fuso.enterprise.ots.srv.server.dao.UserMapDAO;
import com.fuso.enterprise.ots.srv.api.model.domain.BillOrderModelDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.GetCustomerOutstandingAmt;
import com.fuso.enterprise.ots.srv.api.model.domain.GetProductDetailsForBillModel;
import com.fuso.enterprise.ots.srv.api.model.domain.ListOfBillId;
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
    private UserMapDAO userMapDAO;

    @Inject
    public OTSBillServiceImpl(UserMapDAO userMapDAO, BillServiceDAO billServiceDAO, OrderServiceDAO orderServiceDAO, CustomerOutstandingAmtDAO customerOutstandingAmtDAO, OrderProductDAO orderProductDAO, UserServiceDAO userServiceDAO, OrderDAO orderDAO) {
        this.billServiceDAO = billServiceDAO;
        this.orderServiceDAO = orderServiceDAO;
        this.customerOutstandingAmtDAO = customerOutstandingAmtDAO;
        this.orderProductDAO = orderProductDAO;
        this.userServiceDAO = userServiceDAO;
        this.orderDAO = orderDAO;
        this.userMapDAO = userMapDAO;
    }

    @Inject
    private OTSProductService otsProductService;
    long millis = System.currentTimeMillis();
    java.sql.Date date = new java.sql.Date(millis);
    @Override
    public BillDetailsBOResponse addOrUpdateBill(BillDetailsBORequest billDetailsBORequest) {
        BillDetailsBOResponse billDetailsBOResponse = new BillDetailsBOResponse();
        OtsBill otsBill = new OtsBill();
        try {
            GetCustomerOutstandingAmtBORequest getCustomerOutstandingAmtBORequest = new GetCustomerOutstandingAmtBORequest();
            GetCustomerOutstandingAmt getCustomerOutstandingAmt = new GetCustomerOutstandingAmt();
            getCustomerOutstandingAmt.setCustomerId(billDetailsBORequest.getRequestData().getCustomerId().toString());
            getCustomerOutstandingAmtBORequest.setRequestData(getCustomerOutstandingAmt);

            billDetailsBORequest.getRequestData().setOutstandingAmount(customerOutstandingAmtDAO.getCustomerOutstandingAmt(getCustomerOutstandingAmtBORequest).getCustomerOutstandingAmount().get(0).getCustomerOutstandingAmt() == null ? null : customerOutstandingAmtDAO.getCustomerOutstandingAmt(getCustomerOutstandingAmtBORequest).getCustomerOutstandingAmount().get(0).getCustomerOutstandingAmt());

            billDetailsBOResponse = billServiceDAO.addOrUpdateBill(billDetailsBORequest);
            List < ListOfOrderId > listOfOrderId = billDetailsBORequest.getRequestData().getOrderId();
            otsBill.setOtsBillId(billDetailsBOResponse.getBillDetails().get(0).getBillId());
            /*
             * calling orderServiceDAo for updating billId in order table
             */
            orderServiceDAO.updateOrderwithBillID(otsBill, listOfOrderId);
            /*
             * Getting customer and product detail of bill 
             */
            GetProductDetailsForBillRequst getProductDetailsForBillRequst = new GetProductDetailsForBillRequst();
            GetProductDetailsForBillModel getProductDetailsForBillModel = new GetProductDetailsForBillModel();
            BillProductDetailsResponse billProductDetailsResponse = new BillProductDetailsResponse();
            List < String > listOrdStr = new ArrayList < String > ();
            for (ListOfOrderId orderIdList: listOfOrderId) {
                listOrdStr.add(orderIdList.getOrderId());
            }
            getProductDetailsForBillModel.setOrderId(listOrdStr);
            getProductDetailsForBillRequst.setRequest(getProductDetailsForBillModel);
            billProductDetailsResponse = otsProductService.getProductDetailsForBill(getProductDetailsForBillRequst);
            List < ProductDetailsList > lisOfproduct = billProductDetailsResponse.getProductDeatils();
            String productString = "";
            int slno = 0;
            int totalPrice = 0;
            for (ProductDetailsList productDetails: lisOfproduct) {
                slno++;
                totalPrice = totalPrice + Integer.parseInt(productDetails.getTotalProductPrice());
                productString = productString + "<tr>\r\n" +
                    "<td style=\"width: 45.6px; text-align: center;\">" + slno + "</td>\r\n" +
                    "<td style=\"width: 137.6px; text-align: center;\">" + productDetails.getProductName() + "</td>\r\n" +
                    "<td style=\"width: 96.8px; text-align: center;\">" + productDetails.getProductqty() + "</td>\r\n" +
                    "<td style=\"width: 96.8px; text-align: center;\">" + productDetails.getProductPrice() + "</td>\r\n" +
                    "<td style=\"width: 100px; text-align: center;\">" + productDetails.getTotalProductPrice() + "</td>\r\n" +
                    "</tr>\r\n";
            }
            /*
             * Create html for Bill document
             */
            double totalAmount = Double.parseDouble(billDetailsBORequest.getRequestData().getBillAmount().toString());
            long longValue = (long)totalAmount; 
            String numberInWords = numberToWord(String.valueOf(longValue));
            System.out.print("----------------------" + numberInWords);
            String htmlString = "<html>  \r\n" +
                "						<body>  \r\n" +
                "							<table style=\"width: 126.4px;\">   \r\n" +
                "								<tbody>  \r\n" +
                "									<tr style=\"height: 43.2px;\">   \r\n" +
                "									<td style=\"width: 244px;\"><h1 align=\"left\"> Tax Invoice</h1></td>   \r\n" +
                "								</tr>   \r\n" +
                "									</tbody>   \r\n" +
                "							</table>   \r\n" +
                "							<table border=\"1\" style=\"width: 508px; height: 90px;\" >  \r\n" +
                "								<tbody>   \r\n" +
                "									<tr>  \r\n" +
                "										<td style=\"width: 244px;\">Distributor Details</td>   \r\n" +
                "										<td style=\"width: 259px;\">Customer Details</td>   \r\n" +
                "									</tr> \r\n" +
                "									<tr>   \r\n" +
                "										<td style=\"width: 244px;\">" + billProductDetailsResponse.getDistributorDetails().getFirstName() + " " + billProductDetailsResponse.getDistributorDetails().getLastName() + "<br>" + billProductDetailsResponse.getDistributorDetails().getAddress1() + ",<br>" + billProductDetailsResponse.getDistributorDetails().getAddress2() + "</td> \r\n" +
                "										<td style=\"width: 259px;\">" + billProductDetailsResponse.getCustomerDetails().getFirstName() + " " + billProductDetailsResponse.getCustomerDetails().getLastName() + "<br>" + billProductDetailsResponse.getCustomerDetails().getAddress1() + ",<br>" + billProductDetailsResponse.getCustomerDetails().getAddress2() + "</td>  \r\n" +
                "									</tr>   \r\n" +
                "								</tbody>   \r\n" +
                "							</table>  \r\n" +
                "							  	\r\n" +
                "							<p>&nbsp;</p>  \r\n" +
                "							<table style=\"width: 126.4px;\">   \r\n" +
                "								<tbody>  \r\n" +
                "								<tr>\r\n" +
                "								<td style=\"width: 244px;\">Bill No:OtsBill:- " + billDetailsBOResponse.getBillDetails().get(0).getBillId() + "</td>\r\n" +
                "								<td style=\"width: 244px;\" align=\"right\">Date:- " + date + "</td>\r\n" +
                "								</tr>\r\n" +
                "									</tbody>   \r\n" +
                "							</table>   \r\n" +
                "					<table style=\"width: 508px; height: 38px;\" border=\"1\">  \r\n" +
                "					<tbody>  \r\n" +
                "					<tr style=\"background-color: #808080;\">  \r\n" +
                "					<td style=\"width: 45.6px; text-align: center;\"><strong>Slno</strong></td>  \r\n" +
                "					<td style=\"width: 137.6px; text-align: center;\"><strong>Description</strong></td>  \r\n" +
                "					<td style=\"width: 96.8px; text-align: center;\"><strong>Unit/Quantity</strong></td>  \r\n" +
                "					<td style=\"width: 96.8px; text-align: center;\"><strong>Rate&nbsp;</strong></td>  \r\n" +
                "					<td style=\"width: 100px; text-align: center;\"><strong>&nbsp;Amount</strong></td>  \r\n" +
                "					</tr>  \r\n" +
                "					<tr>\r\n" + "" + productString + "" +
                "<tr>\r\n" +
                "<td style=\"width: 96.8px; text-align: right;\" colspan=\"4\"><strong>Total</strong></td>\r\n" +
                "<td style=\"width: 100px; text-align: center;\">" + totalPrice + "</td>\r\n" +
                "</tr>\r\n" +
                "<tr>\r\n" +
                "<td style=\"width: 96.8px; text-align: right;\" colspan=\"4\"><strong>SGST</strong></td>\r\n" +
                "<td style=\"width: 100px; text-align: center;\">" + billDetailsBORequest.getRequestData().getIGST() + "</td>\r\n" +
                "</tr>\r\n" +
                "<tr>\r\n" +
                "<td style=\"width: 96.8px; text-align: right;\" colspan=\"4\"><strong>CGST</strong></td>\r\n" +
                "<td style=\"width: 100px; text-align: center;\">" + billDetailsBORequest.getRequestData().getCGST() + "</td>\r\n" +
                "</tr>\r\n" +
                "<tr>\r\n" +
                "<td style=\"width: 96.8px; text-align: right;\" colspan=\"4\"><strong>Total</strong></td>\r\n" +
                "<td style=\"width: 100px; text-align: center;\">" + billDetailsBORequest.getRequestData().getBillAmount() + "</td>\r\n" +
                "</tr>\r\n" +
                "</tbody>\r\n" +
                "</table>\r\n" +
                "<p>&nbsp;Rupees in words :" + numberInWords + " only</p>\r\n" +
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
            String billNO = billDetailsBOResponse.getBillDetails().get(0).getBillId() + "";
            OTSUtil.generatePDFFromHTML(htmlString, billNO);
            EmailUtil.sendEmailBill(billProductDetailsResponse.getCustomerDetails().getEmailId(), billProductDetailsResponse.getDistributorDetails().getEmailId(), "Your Bill for Number: OtsBill-" + billNO + ", Bill Date: 12-Apr-2019\r\n" +
                "", "Please find the attachment for your water can bill generated", "OtsBill-" + billNO + ".pdf", "C:\\template\\OtsBill-" + billNO + ".pdf");
            byte[] fileContent = FileUtils.readFileToByteArray(new File("C:\\template\\OtsBill-" + billNO + ".pdf"));
            String encodedString = Base64.getEncoder().encodeToString(fileContent);
            billDetailsBORequest.getRequestData().setBillId(billDetailsBOResponse.getBillDetails().get(0).getBillId());
            billDetailsBORequest.getRequestData().setBillPdf(encodedString);
            billDetailsBOResponse = billServiceDAO.addOrUpdateBill(billDetailsBORequest);

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
            List < OrderDetails > OrderDetails = orderServiceDAO.getListOrderForBill(billDataBOResponse.getGetBillDetails());

            List < BillOrderModelDetails > billOrderModelDetailsList = new ArrayList < BillOrderModelDetails > ();

            for (int i = 0; i < OrderDetails.size(); i++) {
                BillOrderModelDetails billOrderModelDetails = new BillOrderModelDetails();

                billOrderModelDetails.setDelivaryDate(OrderDetails.get(i).getOrderDeliveryDate());
                billOrderModelDetails.setDeliverdDate(OrderDetails.get(i).getOrderDeliverdDate());
                billOrderModelDetails.setOrderNumber(OrderDetails.get(i).getOrderNumber());
                billOrderModelDetails.setOrderStatus(OrderDetails.get(i).getStatus());
                billOrderModelDetails.setOrderCost(OrderDetails.get(i).getOrderCost());

                UserDetails DistriutorDetails, CustomerDetails, EmployeeDetails;
                DistriutorDetails = userServiceDAO.getUserDetails(Integer.parseInt(OrderDetails.get(i).getDistributorId()));
                CustomerDetails = userServiceDAO.getUserDetails(Integer.parseInt(OrderDetails.get(i).getCustomerId()));
                EmployeeDetails = userServiceDAO.getUserDetails(Integer.parseInt(OrderDetails.get(i).getAssignedId()));
                billOrderModelDetails.setOrderId(OrderDetails.get(i).getOrderId());
                billOrderModelDetails.setDistriutorDetails(DistriutorDetails);
                billOrderModelDetails.setCustomerDetails(CustomerDetails);
                billOrderModelDetails.setEmployeeDetails(EmployeeDetails);

                billOrderModelDetails.setOrderProductDetails(orderProductDAO.getProductListByOrderId(OrderDetails.get(i).getOrderId()));

                billOrderModelDetailsList.add(i, billOrderModelDetails);

            }
            billDataBOResponse.getGetBillDetails().setBillOrderModelDetailsList(billOrderModelDetailsList);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage(), e);
        }
        return billDataBOResponse;
    }


    @Override
    public BillReportByDateBOResponse getBillReportByDate(BillReportBasedOnDateBORequest billReportBasedOnDateBORequest) {
        BillReportByDateBOResponse billIdResponse = new BillReportByDateBOResponse();
        List < ListOfBillId > billNumber = new ArrayList < > ();
        try {
            if (billReportBasedOnDateBORequest.getRequestData().getRoleId().equals("4")) {

                billIdResponse = billServiceDAO.getBillReportByDate(billReportBasedOnDateBORequest);
            } else {
                BillReportByDateBOResponse billIdResponse1 = new BillReportByDateBOResponse();
                billIdResponse1 = billServiceDAO.getBillReportForDistributorByDate(billReportBasedOnDateBORequest);
                for (int i = 0; i < billIdResponse1.getBillNumber().size(); i++) {
                    String distributorId = userMapDAO.getMappedDistributor(billIdResponse1.getBillNumber().get(i).getCustomerId());
                    if (billReportBasedOnDateBORequest.getRequestData().getUserId().equals(distributorId)) {
                        billIdResponse1.getBillNumber().get(i).setCustomerDetails(userServiceDAO.getUserDetails(Integer.valueOf(billIdResponse1.getBillNumber().get(i).getCustomerId())));
                        billNumber.add(billIdResponse1.getBillNumber().get(i));
                    }
                }
                if (billReportBasedOnDateBORequest.getRequestData().getPdf().equalsIgnoreCase("yes")) {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    LocalDateTime now = LocalDateTime.now();
                    String pdf = genratePdfForbillReportBydate(billNumber, userServiceDAO.getUserIdUsers(billReportBasedOnDateBORequest.getRequestData().getUserId()).get(0).getFirstName(), now.toString().substring(0, 10));
                    billIdResponse.setPdf(pdf);
                }
                billIdResponse.setBillNumber(billNumber);
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage(), e);
        }
        return billIdResponse;
    }


    public String genratePdfForbillReportBydate(List < ListOfBillId > billNumber, String distributorName, String date) {
        String tableValueString = "";
        String reportDetails = "<head ><h3 style='text-align:center;'>Bill Report</h3></head>";
        reportDetails += "<head><h3>Distributer Name :" + distributorName + "</h3></head>";
        reportDetails += "<head ><h3>date:" + date + "</h3></head>  </br>";

        int slno = 0;
        tableValueString = "<table border=\"1\"><tr>\r\n" +
            "	<th>Sl no</th>\r\n" +
            "	<th>Bill Report</th>\r\n" +
            "	<th>Customer Name</th>\r\n" +
            "	<th>Bill Cost</th>\r\n" +
            "	<th>Recieved Amount</th>\r\n" +
            "	<th>Bill Date</th>\r\n" +
            "</tr>";

        for (ListOfBillId billDetails: billNumber) {
            slno++;
            tableValueString = tableValueString + "<tr>\r\n" +
                "	<td>" + slno + "</td>\r\n" +
                "   <td>" + billDetails.getBillNumber() + "</td>\r\n" +
                "   <td>" + billDetails.getCustomerDetails().getFirstName() + "</td>\r\n" +
                "	<td>" + billDetails.getBillAmount() + "</td>\r\n" +
                "   <td>" + billDetails.getBillAmountReceived() + "</td>\r\n" +
                "   <td>" + billDetails.getBilldate() + "</td>\r\n" +
                "</tr>";


        }
        tableValueString = tableValueString + "</table>";
        String htmlString = "<html>" + reportDetails + tableValueString + "</html>";
        String path = OTSUtil.generateReportPDFFromHTML(htmlString, "BillReportByDate.pdf");
        byte[] fileContent;
        String encodedString = null;
        try {
            fileContent = FileUtils.readFileToByteArray(new File(path));
            encodedString = Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return encodedString;

    }

    private static String numberToWord(String number) {
        String twodigitword = "";
        String word = "";
        String[] HTLC = {
            "",
            "Hundred",
            "Thousand",
            "Lakh",
            "Crore"
        }; //H-hundread , T-Thousand, ..
        int split[] = {
            0,
            2,
            3,
            5,
            7,
            9
        };
        String[] temp = new String[split.length];
        boolean addzero = true;
        int len1 = number.length();
        if (len1 > split[split.length - 1]) {
            System.out.println("Error. Maximum Allowed digits " + split[split.length - 1]);
            System.exit(0);
        }
        for (int l = 1; l < split.length; l++)
            if (number.length() == split[l]) addzero = false;
        if (addzero == true) number = "0" + number;
        int len = number.length();
        int j = 0;
        //spliting & putting numbers in temp array.
        while (split[j] < len) {
            int beg = len - split[j + 1];
            int end = beg + split[j + 1] - split[j];
            temp[j] = number.substring(beg, end);
            j = j + 1;
        }

        for (int k = 0; k < j; k++) {
            twodigitword = ConvertOnesTwos(temp[k]);
            if (k >= 1) {
                if (twodigitword.trim().length() != 0) word = twodigitword + " " + HTLC[k] + " " + word;
            } else word = twodigitword;
        }
        return (word);
    }

    private static String ConvertOnesTwos(String t) {
        final String[] ones = {
            "",
            "One",
            "Two",
            "Three",
            "Four",
            "Five",
            "Six",
            "Seven",
            "Eight",
            "Nine",
            "Ten",
            "Eleven",
            "Twelve",
            "Thirteen",
            "Fourteen",
            "Fifteen",
            "Sixteen",
            "Seventeen",
            "Eighteen",
            "Nineteen"
        };
        final String[] tens = {
            "",
            "Ten",
            "Twenty",
            "Thirty",
            "Forty",
            "Fifty",
            "Sixty",
            "Seventy",
            "Eighty",
            "Ninety"
        };

        String word = "";
        int num = Integer.parseInt(t);
        if (num % 10 == 0) word = tens[num / 10] + " " + word;
        else if (num < 20) word = ones[num] + " " + word;
        else {
            word = tens[(num - (num % 10)) / 10] + word;
            word = word + " " + ones[num % 10];
        }
        return word;
    }
}