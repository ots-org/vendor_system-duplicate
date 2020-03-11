package com.fuso.enterprise.ots.srv.functional.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.inject.Inject;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.codec.binary.Base64;
import com.fuso.enterprise.ots.srv.api.model.domain.CustomerProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.GetProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.GetProductRequestModel;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.ProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.ProductDetailsList;
import com.fuso.enterprise.ots.srv.api.model.domain.ProductStockDetail;
import com.fuso.enterprise.ots.srv.api.service.functional.OTSProductService;
import com.fuso.enterprise.ots.srv.api.service.request.AddProductStockBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddorUpdateProductBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetProductDetailsForBillRequst;
import com.fuso.enterprise.ots.srv.api.service.request.GetProductStockListRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetProductStockRequest;
import com.fuso.enterprise.ots.srv.api.service.request.ProductDetailsBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateProductStatusRequest;
import com.fuso.enterprise.ots.srv.api.service.response.BillProductDetailsResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetProductBOStockResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetProductStockListBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.ProductDetailsBOResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.common.exception.ErrorEnumeration;
import com.fuso.enterprise.ots.srv.server.dao.MapUserProductDAO;
import com.fuso.enterprise.ots.srv.server.dao.OrderDAO;
import com.fuso.enterprise.ots.srv.server.dao.OrderProductDAO;
import com.fuso.enterprise.ots.srv.server.dao.OrderServiceDAO;
import com.fuso.enterprise.ots.srv.server.dao.ProductServiceDAO;
import com.fuso.enterprise.ots.srv.server.dao.ProductStockDao;
import com.fuso.enterprise.ots.srv.server.dao.ProductStockHistoryDao;
import com.fuso.enterprise.ots.srv.server.dao.StockDistObDAO;
import com.fuso.enterprise.ots.srv.server.dao.UserServiceDAO;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsOrder;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsStockDistOb;
import com.fuso.enterprise.ots.srv.server.util.OTSUtil;

@Service
@Transactional
public class OTSProductServiceImpl implements OTSProductService {
	private ProductStockDao productStockDao;
	private ProductStockHistoryDao productStockHistoryDao;
	private ProductServiceDAO productServiceDAO;
	private StockDistObDAO stockDistObDAO;
	private OrderDAO orderDAO;
	private OrderProductDAO orderProductDAO;
	private UserServiceDAO userServiceDAO;
	private OrderServiceDAO orderServiceDAO;
	private MapUserProductDAO mapUserProductDAO;
	@Inject
	public OTSProductServiceImpl(ProductServiceDAO productServiceDAO,ProductStockDao productStockDao,ProductStockHistoryDao productStockHistoryDao,StockDistObDAO stockDistObDAO,OrderDAO orderDAO,OrderProductDAO orderProductDAO,UserServiceDAO userServiceDAO,OrderServiceDAO orderServiceDAO,MapUserProductDAO mapUserProductDAO) {
		this.productServiceDAO=productServiceDAO;
		this.productStockDao = productStockDao;
		this.productStockHistoryDao=productStockHistoryDao;
		this.stockDistObDAO=stockDistObDAO;
		this.orderDAO=orderDAO;
		this.orderProductDAO=orderProductDAO;
		this.userServiceDAO = userServiceDAO;
		this.orderServiceDAO = orderServiceDAO;
		this.mapUserProductDAO = mapUserProductDAO;
	}
	@Override
	public ProductDetailsBOResponse getProductList(ProductDetailsBORequest productDetailsBORequest) {
		int loop=0;
		ProductDetailsBOResponse productDetailsBOResponse = new ProductDetailsBOResponse();
		List<CustomerProductDetails> customerProductDetails = new ArrayList<CustomerProductDetails>();
		List<ProductDetails> productDetails = new ArrayList<ProductDetails>();
		List<GetProductBOStockResponse> productStockvalue = new ArrayList<GetProductBOStockResponse>();
		
		if(productDetailsBORequest.getRequestData().getSearchKey().equals("All") && productDetailsBORequest.getRequestData().getDistributorId().equals("1")) {
			productDetailsBOResponse = productServiceDAO.getProductList(productDetailsBORequest);
		}else if(productDetailsBORequest.getRequestData().getCustomerId()!=null){
			try {
				customerProductDetails = mapUserProductDAO.getCustomerProductDetailsByCustomerId(productDetailsBORequest.getRequestData().getCustomerId());
				productStockvalue = productStockDao.getProductStockByUid(productDetailsBORequest.getRequestData().getDistributorId());

				if(productStockvalue!= null) {
					for(int i = 0;i<productStockvalue.size(); i++) {
						 productDetails.add(loop,productServiceDAO.getProductDetils(productStockvalue.get(i).getProductId()));
						 loop++;
					}
				}else {
					return null;
				}
			if(customerProductDetails!=null) {
				for(int i = 0 ;i<customerProductDetails.size();i++) {
					for(int j=0;j<productDetails.size();j++) {
						if(customerProductDetails.get(i).getProductId().equals(productDetails.get(j).getProductId())) {
							productDetails.get(j).setProductPrice(customerProductDetails.get(i).getProductPrice());
							loop++;
						}
					}
				}
			}
			productDetailsBOResponse.setProductDetails(productDetails);
			} catch (Exception e) {
				throw new BusinessException(e.getMessage(), e);
			}
		}else {
			System.out.print("3");
			productStockvalue = productStockDao.getProductStockByUid(productDetailsBORequest.getRequestData().getDistributorId());
			for(int i=0;i<productStockvalue.size();i++) {
				productDetails.add(i,productServiceDAO.getProductDetils(productStockvalue.get(i).getProductId()));
				
				productDetailsBOResponse.setProductDetails(productDetails);
			}
		}
		return productDetailsBOResponse;
	}

	@Override
	public String addOrUpdateProduct(AddorUpdateProductBORequest addorUpdateProductBORequest) {
		String path;
		try {
			productServiceDAO.addOrUpdateProduct(addorUpdateProductBORequest);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return "Added / updated";
	}


	@Override
	public String addOrUpdateProductStock(AddProductStockBORequest addProductBORequest) {
		String strResponse = "";
		try {
			strResponse = productStockDao.addProductStock(addProductBORequest);
			productStockHistoryDao.addProductStockHistory(addProductBORequest);
		}catch(Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return strResponse;
	}

	@Override
	public GetProductStockListBOResponse getProductStockList(GetProductStockListRequest getProductStockListRequest) {
		GetProductStockListBOResponse getProductStockListBOResponse =new GetProductStockListBOResponse();
		List<ProductStockDetail>  ProductStockDetailList = new ArrayList<ProductStockDetail>();
		try {
			/**
			 * looping based on product list items
			 */
			ProductDetailsBORequest productDetailsBORequest = new ProductDetailsBORequest();
			GetProductDetails getProductDetails = new GetProductDetails();
			getProductDetails.setSearchKey("All");
			getProductDetails.setSearchvalue("");
			getProductDetails.setDistributorId(getProductStockListRequest.getRequestData().getUserId());
			productDetailsBORequest.setRequestData(getProductDetails);
			List<ProductDetails> otsProductList = getProductList(productDetailsBORequest).getProductDetails();

			for (ProductDetails productDetailItem: otsProductList) {
				ProductStockDetail ProductStockDetail = new ProductStockDetail();
				ProductStockDetail.setProductName(productDetailItem.getProductName());
				ProductStockDetail.setProductId(Long.parseLong(productDetailItem.getProductId()));

				/*
				 * fetching opening balance
				 */
				getProductStockListRequest.getRequestData().setProductId(productDetailItem.getProductId());
				List<OtsStockDistOb> otsStockDistObList  = stockDistObDAO.fetchOpeningBalance(getProductStockListRequest);
				if(otsStockDistObList.size()>0)
					ProductStockDetail.setOtsProductOpenBalance(Long.parseLong(otsStockDistObList.get(0).getOtsStockDistOpeningBalance()));
				else
					ProductStockDetail.setOtsProductOpenBalance(0);

				/*
				 * fetching stock added
				 */
				ProductStockDetail.setOtsProductStockAddition(productStockHistoryDao.getOtsProductStockAddition(Integer.parseInt(productDetailItem.getProductId()),getProductStockListRequest));

				/*
				 * fetch sold quantity
				 */
				List<OtsOrder> orderList = orderDAO.getOrderList(Integer.parseInt(productDetailItem.getProductId()),getProductStockListRequest);
				if(orderList.size()>0)
					ProductStockDetail.setOtsProductOrderDelivered(orderProductDAO.getListOfDeliverdQuantityOfDay(orderList,Integer.parseInt(productDetailItem.getProductId())));
				else
					ProductStockDetail.setOtsProductOrderDelivered(0);

				ProductStockDetailList.add(ProductStockDetail);
			}

		}catch(Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		getProductStockListBOResponse.setProductStockDetail(ProductStockDetailList);
		if(getProductStockListRequest.getRequestData().getPdf().equals("yes")) {
			String pdf = productTransactionReportPdf(ProductStockDetailList,userServiceDAO.getUserDetails(Integer.valueOf(getProductStockListRequest.getRequestData().getUserId())).getFirstName(),getProductStockListRequest.getRequestData().getTodaysDate().toString());
			getProductStockListBOResponse.setPdf(pdf);
		}
		return getProductStockListBOResponse;
	}

	@Override
	public GetProductBOStockResponse getProductStockByUidAndPid(GetProductStockRequest getProductStockRequest) {
		try {
		GetProductBOStockResponse getProductBOStockResponse = new GetProductBOStockResponse();
		getProductBOStockResponse = productStockDao.getProductStockByUidAndPid(getProductStockRequest);
		return getProductBOStockResponse;
		}catch(Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
	}
	@Override
	public BillProductDetailsResponse getProductDetailsForBill(
			GetProductDetailsForBillRequst getProductDetailsForBillRequst) {
		try {
			BillProductDetailsResponse billProductDetailsResponse = new BillProductDetailsResponse();
			List<ProductDetailsList> ProductResponse = new ArrayList<ProductDetailsList>();
			List<OrderProductDetails> totalProductDetails = new ArrayList<OrderProductDetails>();
			int lastIndex = 0;
			int k = 0;
			int productListSize = 0;
			for(int i=0 ; i< getProductDetailsForBillRequst.getRequest().getOrderId().size() ; i++) {

				List<OrderProductDetails> ProductDetails = orderProductDAO.getProductListByOrderId(getProductDetailsForBillRequst.getRequest().getOrderId().get(i));
				productListSize = lastIndex + ProductDetails.size();
				for(int j=lastIndex ; j < productListSize; j++) {
					OrderProductDetails orderProductDetails = ProductDetails.get(j) ;
					totalProductDetails.add(j,orderProductDetails);
					lastIndex = lastIndex++;
					System.out.println("+++++"+totalProductDetails.get(j));
				}
			}
			ProductDetailsList productDetailsList;

			for(int i=0;i<totalProductDetails.size();i++) {
				if(totalProductDetails.get(i).getStatus()!="YES") {
				OrderDetails orderDetails = new OrderDetails();
				int totalProductPrice = 0;
				Integer TotalproductQty = 0;

				productDetailsList = new ProductDetailsList();

				productDetailsList.setProductId(totalProductDetails.get(i).getOtsProductId().toString());

				productDetailsList.setProductName(totalProductDetails.get(i).getProductName());

					for(int j = 0; j<totalProductDetails.size();j++) {

						if(totalProductDetails.get(i).getOtsProductId().equals(totalProductDetails.get(j).getOtsProductId())) {

							if(totalProductDetails.get(j).getStatus()!="YES") {

								totalProductDetails.get(j).setStatus("YES");

								productDetailsList.setProductPrice(totalProductDetails.get(i).getOtsOrderProductCost());

								TotalproductQty = TotalproductQty + (Integer.valueOf(totalProductDetails.get(j).getOtsDeliveredQty())) ;

								System.out.print("i"+i+"j"+j+"TotalproductQty"+TotalproductQty);

								productDetailsList.setProductqty(String.valueOf(TotalproductQty));
								
								totalProductPrice =(TotalproductQty * Math.round(Float.valueOf(totalProductDetails.get(j).getOtsOrderProductCost())));

								productDetailsList.setTotalProductPrice(String.valueOf(totalProductPrice));

								ProductResponse.add(k,productDetailsList);

							}
						}
					}
				}
			}
			Set<ProductDetailsList> set = new HashSet<>(ProductResponse);
			ProductResponse.clear();
			ProductResponse.addAll(set);

			OrderDetails orderDetails = orderServiceDAO.GetOrderDetailsByOrderId(getProductDetailsForBillRequst.getRequest().getOrderId().get(0));
			billProductDetailsResponse.setCustomerDetails(userServiceDAO.getUserDetails(Integer.valueOf(orderDetails.getCustomerId())));
			billProductDetailsResponse.setDistributorDetails(userServiceDAO.getUserDetails(Integer.valueOf(orderDetails.getDistributorId())));
			billProductDetailsResponse.setProductDeatils(ProductResponse);
			return billProductDetailsResponse;
		}catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
	}

	@Override
	public String UpdateProductStatus(UpdateProductStatusRequest updateProductStatusRequestModel) {
		String path;
		try {
			productServiceDAO.UpdateProductStatus(updateProductStatusRequestModel);

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return "Added / updated";
	}
	public String productTransactionReportPdf(List<ProductStockDetail> ProductStockDetailList,String distributorName,String date) {
		String tableValueString ="";
		String reportDetails = "<head ><h2 style='text-align:center;'>Product Transaction Report</h2></head>";
		reportDetails += "<head><h3>Distributor Name :"+distributorName+"</h3></head>";
		reportDetails += "<head ><h3>Date:"+date+"</h3></head>  </br>";
		
		int slno=0;
		tableValueString = "<table border=\"1\"><tr>\r\n" + 
				"	<th>Sl no</th>\r\n" + 
				"	<th>Product Name</th>\r\n" + 
				"    <th>Opening Balance</th>\r\n" + 
				"	<th>Stock Addition</th>\r\n" + 
				"	<th>Order deliverd</th>\r\n" + 
				"	<th>Closing Balance</th>\r\n" + 
				"</tr>";
		for(ProductStockDetail  productDetails: ProductStockDetailList) {
			slno++;
			Long closingBalance = (long) 0;
			closingBalance = productDetails.getOtsProductOpenBalance() +productDetails.getOtsProductStockAddition() -productDetails.getOtsProductOrderDelivered();  
			tableValueString=tableValueString+"<tr>\r\n" + 
					"	<td>"+slno+"</td>\r\n" + 
					"   <td>"+productDetails.getProductName()+"</td>\r\n" + 
					"	<td>"+productDetails.getOtsProductOpenBalance()+"</td>\r\n" + 
					"	<td>"+productDetails.getOtsProductStockAddition()+"</td>\r\n" + 
					"	<td>"+productDetails.getOtsProductOrderDelivered()+"</td>\r\n" + 
					"	<td>"+closingBalance+"</td>\r\n" + 
					"</tr>";
		}
		tableValueString =tableValueString+ "</table>";
		String htmlString = "<html>"+reportDetails+tableValueString+"</html>";
		String path = OTSUtil.generateReportPDFFromHTML(htmlString,"OrderRepo.pdf");
		byte[] fileContent;
		String encodedString = null;
		try {
			fileContent = FileUtils.readFileToByteArray(new File(path));
		//	encodedString = Base64.getEncoder().encodeToString(fileContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return encodedString;	
	}
	

	private void createFolder() {
		File theDir = new File("C:\\product\\data\\");
		if (!theDir.exists()) {
			try {
				theDir.mkdirs();
			} catch (SecurityException se) {
				se.printStackTrace();
			//	throw new BusinessException(se, ErrorEnumeration.UNCLASSIFIED_EXCEPTION);
			} catch (Exception e) {
			//	throw new BusinessException(e, ErrorEnumeration.UNCLASSIFIED_EXCEPTION);
			}
		}

	}

	@Override
	public String productBulkUpload(String base64Excel) {
		AddorUpdateProductBORequest addorUpdateProductBORequest = new AddorUpdateProductBORequest();
		byte[] decodedString = Base64.decodeBase64(base64Excel.getBytes(StandardCharsets.UTF_8));
		String excelPartFileName = "Product" + "1" + ".xlsx";
		String uploadpath = "C:\\product\\data\\" + excelPartFileName;
		File dwldsPath = new File(uploadpath);
		FileOutputStream os;
		createFolder();
		try {
			os = new FileOutputStream(dwldsPath, false);
			os.write(decodedString);
			os.flush();
			os.close();
			
			Workbook workbook;
			workbook = WorkbookFactory.create(new File(uploadpath));
			Sheet product = workbook.getSheetAt(0); 
//			------------------------------------READ ALL IMAGES---------------------------------------------
			List lst = workbook.getAllPictures();
			
			for(int i=0;i<product.getLastRowNum();i++)
			{
				
				PictureData pict = (PictureData) lst.get(i);
				byte[] data = pict.getData();
				FileOutputStream out = new FileOutputStream("C:\\product\\data\\img.jpg");
				out.write(data);
				out.close();
				
//				------------------------------------Fetching and getting image---------------------------------------------				
				Row ro = product.getRow(i);
				ProductDetails productDetails = new ProductDetails();
				productDetails.setProductName(ro.getCell(0).toString());
				productDetails.setProductDescription(ro.getCell(1).toString());
				productDetails.setProductPrice(ro.getCell(2).toString());
				productDetails.setProductType(ro.getCell(3).toString());
				
				productDetails.setProductImage(ImageToBase64("C:\\product\\data\\img.jpg"));
				productDetails.setProductStatus("active");
				productDetails.setProductId("string");
				addorUpdateProductBORequest.setRequestData(productDetails);
				productServiceDAO.addOrUpdateProduct(addorUpdateProductBORequest);
				System.out.print(productDetails.getProductName()+productDetails.getProductImage());
			}
				return "Data Uploaded";
			}catch(Exception e) {
				System.out.print(e);
				return "Error in excel";
			}
	}
	
	public String ImageToBase64(String filePath) throws IOException {
		byte[] fileContent = FileUtils.readFileToByteArray(new File(filePath));
		byte[] bytesEncoded = Base64.encodeBase64(fileContent);
		compressJPEGFile();
		return new String(bytesEncoded);		
	}
	
	public String compressJPEGFile() throws IOException {
		File input = new File("C:\\product\\data\\img.jpg");
		BufferedImage image = ImageIO.read(input);
		
		File output = new File("C:\\product\\data\\img.jpg");
        OutputStream out = new FileOutputStream(output);
        
        ImageWriter writer =  ImageIO.getImageWritersByFormatName("jpg").next();
        ImageOutputStream ios = ImageIO.createImageOutputStream(out);
        writer.setOutput(ios);
        
        ImageWriteParam param = writer.getDefaultWriteParam();
        if (param.canWriteCompressed()){
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.10f);
        }
        
        writer.write(null, new IIOImage(image, null, null), param);

        out.close();
        ios.close();
        writer.dispose();
        
        
		return null;
	}
	
	public void extractImageAndPlaceInFolder() {
		//PictureData pict = (PictureData) 
	}
}



