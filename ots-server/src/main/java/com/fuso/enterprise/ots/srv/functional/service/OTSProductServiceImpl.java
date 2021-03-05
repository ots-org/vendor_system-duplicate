package com.fuso.enterprise.ots.srv.functional.service;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.codec.binary.Base64;
import com.fuso.enterprise.ots.srv.server.dao.ProductCategoryMappingDAO;
import com.fuso.enterprise.ots.srv.api.model.domain.AddProductCategoryAndProductModelRequest;
import com.fuso.enterprise.ots.srv.api.model.domain.AddProductStock;
import com.fuso.enterprise.ots.srv.api.model.domain.AirTableModel;
import com.fuso.enterprise.ots.srv.api.model.domain.CustomerProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.GetProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.GetProductRequestModel;
import com.fuso.enterprise.ots.srv.api.model.domain.GetProductStockListDomain;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.OrderProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.ProductCategoryProductMappingModel;
import com.fuso.enterprise.ots.srv.api.model.domain.ProductDetails;
import com.fuso.enterprise.ots.srv.api.model.domain.ProductDetailsList;
import com.fuso.enterprise.ots.srv.api.model.domain.ProductStockDetail;
import com.fuso.enterprise.ots.srv.api.service.functional.OTSProductService;
import com.fuso.enterprise.ots.srv.api.service.request.AddNewBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddProductCategoryAndProductRequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddProductStockBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.AddorUpdateProductBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.AirTableRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetProductDetailsForBillRequst;
import com.fuso.enterprise.ots.srv.api.service.request.GetProductStockListRequest;
import com.fuso.enterprise.ots.srv.api.service.request.GetProductStockRequest;
import com.fuso.enterprise.ots.srv.api.service.request.NotifyProductForCustomerRequest;
import com.fuso.enterprise.ots.srv.api.service.request.ProductBulkUploadRequest;
import com.fuso.enterprise.ots.srv.api.service.request.ProductDetailsBORequest;
import com.fuso.enterprise.ots.srv.api.service.request.UpdateProductStatusRequest;
import com.fuso.enterprise.ots.srv.api.service.response.BillProductDetailsResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetProductBOStockResponse;
import com.fuso.enterprise.ots.srv.api.service.response.GetProductStockListBOResponse;
import com.fuso.enterprise.ots.srv.api.service.response.ProductDetailsBOResponse;
import com.fuso.enterprise.ots.srv.common.exception.BusinessException;
import com.fuso.enterprise.ots.srv.common.exception.ErrorEnumeration;
import com.fuso.enterprise.ots.srv.server.dao.AirTableDao;
import com.fuso.enterprise.ots.srv.server.dao.MapUserProductDAO;
import com.fuso.enterprise.ots.srv.server.dao.OrderDAO;
import com.fuso.enterprise.ots.srv.server.dao.OrderProductDAO;
import com.fuso.enterprise.ots.srv.server.dao.OrderServiceDAO;
import com.fuso.enterprise.ots.srv.server.dao.ProductServiceDAO;
import com.fuso.enterprise.ots.srv.server.dao.ProductStockDao;
import com.fuso.enterprise.ots.srv.server.dao.ProductStockHistoryDao;
import com.fuso.enterprise.ots.srv.server.dao.StockDistObDAO;
import com.fuso.enterprise.ots.srv.server.dao.UserServiceDAO;
import com.fuso.enterprise.ots.srv.server.dao.impl.NotifyCustomerDAO;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsOrder;
import com.fuso.enterprise.ots.srv.server.model.entity.OtsStockDistOb;
import com.fuso.enterprise.ots.srv.server.util.Base64UtilImage;
import com.fuso.enterprise.ots.srv.server.util.EmailUtil;
import com.fuso.enterprise.ots.srv.server.util.OTSUtil;

@Service
@Transactional
public class OTSProductServiceImpl implements OTSProductService {
	
	@Value("${ots.excelpath}")
	public String excelPath;
	
	@Value("${ots.imagepath}")
	public String imagePath;
	
	private ProductStockDao productStockDao;
	private ProductStockHistoryDao productStockHistoryDao;
	private ProductServiceDAO productServiceDAO;
	private StockDistObDAO stockDistObDAO;
	private OrderDAO orderDAO;
	private OrderProductDAO orderProductDAO;
	private UserServiceDAO userServiceDAO;
	private OrderServiceDAO orderServiceDAO;
	private MapUserProductDAO mapUserProductDAO;
	private ProductCategoryMappingDAO productCategoryMappingDAO;
	private AirTableDao airTableDao;
	private NotifyCustomerDAO notifyCustomerDAO;
	
	
	@Inject
	public OTSProductServiceImpl(AirTableDao airTableDao,ProductServiceDAO productServiceDAO,ProductCategoryMappingDAO productCategoryMappingDAO,ProductStockDao productStockDao,ProductStockHistoryDao productStockHistoryDao,StockDistObDAO stockDistObDAO,OrderDAO orderDAO,OrderProductDAO orderProductDAO,UserServiceDAO userServiceDAO,OrderServiceDAO orderServiceDAO,MapUserProductDAO mapUserProductDAO,NotifyCustomerDAO notifyCustomerDAO) {
		this.productServiceDAO=productServiceDAO;
		this.productStockDao = productStockDao;
		this.productStockHistoryDao=productStockHistoryDao;
		this.stockDistObDAO=stockDistObDAO;
		this.orderDAO=orderDAO;
		this.orderProductDAO=orderProductDAO;
		this.userServiceDAO = userServiceDAO;
		this.orderServiceDAO = orderServiceDAO;
		this.mapUserProductDAO = mapUserProductDAO;
		this.productCategoryMappingDAO = productCategoryMappingDAO;
		this.airTableDao = airTableDao;
		this.notifyCustomerDAO = notifyCustomerDAO;
	}
	@Override
	public ProductDetailsBOResponse getProductList(ProductDetailsBORequest productDetailsBORequest) {
		List<ProductCategoryProductMappingModel> productMappingModel = new ArrayList<ProductCategoryProductMappingModel>();
		ProductDetailsBOResponse productDetailsBOResponse = new ProductDetailsBOResponse();
	try {
		if(productDetailsBORequest.getRequestData().getSearchKey().equalsIgnoreCase("category")) {
			//---------------------to get list of all category-------------------------------------
			productDetailsBOResponse = productServiceDAO.getProductCategory(productDetailsBORequest);
		}else if(productDetailsBORequest.getRequestData().getSearchKey().equalsIgnoreCase("subcategory")||productDetailsBORequest.getRequestData().getSearchKey().equalsIgnoreCase("subcategoryRange")){
			//---------------------to get list of all sub-category for main category---------------
			productMappingModel = productCategoryMappingDAO.getProductListByProductCategory(productDetailsBORequest);
			List<ProductDetails> productDetailsList = new ArrayList<ProductDetails>();
			for(ProductCategoryProductMappingModel productCategoryProductMappingModel:productMappingModel) {
				//------------------------get all sub category----------------------------------------
				ProductDetails ProductDetails = new ProductDetails();
				ProductDetails.setProductId(productCategoryProductMappingModel.getProductId());
				productDetailsList.add(ProductDetails);
			}
			//--------------------code to take mapped subcategory for particular value------------
			productDetailsBOResponse.setUserId(productDetailsBORequest.getRequestData().getDistributorId());
			List<ProductDetails> newProductDetailsList = new ArrayList<ProductDetails>();
			for(int i=0 ; i < productDetailsList.size();i++) {
				newProductDetailsList.add(i,productServiceDAO.getProductDetils(productDetailsList.get(i).getProductId()));
			}
			productDetailsBOResponse.setProductDetails(newProductDetailsList);
			//productDetailsBOResponse = mapUserProductDAO.getProductDetailsForDistributor(productDetailsBOResponse);	
		}else if(productDetailsBORequest.getRequestData().getSearchKey().equalsIgnoreCase("product")) {
			productDetailsBOResponse = productCategoryMappingDAO.getProductListBySubcategory(productDetailsBORequest);
		}else if(productDetailsBORequest.getRequestData().getSearchKey().equalsIgnoreCase("pagination")){
			productDetailsBOResponse.setProductDetails(productServiceDAO.getPaginatedProduct(productDetailsBORequest));
		}else if(productDetailsBORequest.getRequestData().getSearchKey().equalsIgnoreCase("singleProduct")) {
			List<ProductDetails> ProductDetails = new ArrayList<ProductDetails> ();
			ProductDetails.add(productServiceDAO.getProductDetils(productDetailsBORequest.getRequestData().getSearchvalue()));
			productDetailsBOResponse.setProductDetails(ProductDetails);
		}else if(productDetailsBORequest.getRequestData().getSearchKey().equalsIgnoreCase("getAllProductForCategory")||productDetailsBORequest.getRequestData().getSearchKey().equalsIgnoreCase("getAllProductForCategoryRange")) {
			// List of mapped sub categories
			List<ProductCategoryProductMappingModel> productCategoryProductMappingModel = new ArrayList<ProductCategoryProductMappingModel> ();
			productCategoryProductMappingModel = productCategoryMappingDAO.getProductListByProductCategory(productDetailsBORequest);
			//List of product for sub category
			List<ProductDetails> productDetailsList = new ArrayList<ProductDetails> ();
			for(int i=0;i<productCategoryProductMappingModel.size();i++) {
				productDetailsBORequest.getRequestData().setSearchvalue(productCategoryProductMappingModel.get(i).getProductId());
				ProductDetailsBOResponse productDetailsBOResponse2 = new ProductDetailsBOResponse();
				productDetailsBOResponse2 = productCategoryMappingDAO.getProductListBySubcategory(productDetailsBORequest);
				
				for(int j=0;j<productDetailsBOResponse2.getProductDetails().size();j++) {
					productDetailsList.add(productDetailsBOResponse2.getProductDetails().get(j));
				}
			}
			
			productDetailsBOResponse.setProductDetails(productDetailsList);
		}else{
			int loop=0;
			List<CustomerProductDetails> customerProductDetails = new ArrayList<CustomerProductDetails>();
			List<ProductDetails> productDetails = new ArrayList<ProductDetails>();
			List<GetProductBOStockResponse> productStockvalue = new ArrayList<GetProductBOStockResponse>();
			
			if(productDetailsBORequest.getRequestData().getSearchKey().equals("All")) {
				productDetailsBOResponse = productServiceDAO.getProductList(productDetailsBORequest);
			}else if(productDetailsBORequest.getRequestData().getCustomerId()!=null){
				try {
			//		customerProductDetails = mapUserProductDAO.getCustomerProductDetailsByCustomerId(productDetailsBORequest.getRequestData().getCustomerId());
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
			}else if(productDetailsBORequest.getRequestData().getSearchKey().equalsIgnoreCase("FirstLetter")){
				productDetailsBOResponse = productServiceDAO.getProductList(productDetailsBORequest);
			}
			else{
				productStockvalue = productStockDao.getProductStockByUid(productDetailsBORequest.getRequestData().getDistributorId());
				for(int i=0;i<productStockvalue.size();i++) {
					productDetails.add(i,productServiceDAO.getProductDetils(productStockvalue.get(i).getProductId()));
					
					productDetailsBOResponse.setProductDetails(productDetails);
				}
			}
		}

	}catch(Exception e) {
		System.out.print("error");
		System.out.println(e);
	}
		return productDetailsBOResponse;
	}

	@Override
	public String addOrUpdateProduct(AddorUpdateProductBORequest addorUpdateProductBORequest) {
		try {
			productServiceDAO.addOrUpdateProduct(addorUpdateProductBORequest);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return "updated";
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
			getProductDetails.setStatus("active");
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
			System.out.print(e);
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
			encodedString = Base64.encodeBase64String(fileContent);
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
	public String productBulkUpload(ProductBulkUploadRequest base64Excel) {
		AddProductCategoryAndProductRequest addorUpdateProductBORequest = new AddProductCategoryAndProductRequest();
		byte[] decodedString = Base64.decodeBase64(base64Excel.getBase64ExcelString().getBytes(StandardCharsets.UTF_8));
		Random rand = new Random(); 
		int name = rand.nextInt(10000); 
		String excelPartFileName = "Product" + name + ".xlsx";
		String uploadpath = excelPath + excelPartFileName;
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
			for(int i=1;i<=product.getLastRowNum();i++)
			{
				Row ro = product.getRow(i);
				ProductDetails productDetails = new ProductDetails();
				//-----------------------setting request object-----------------------
				
				List<ProductDetails> productDetailsListvalue = new ArrayList<ProductDetails>(); 
				productDetails.setProductId("string");
				productDetails.setDistributorId("1");
				productDetails.setProductDescription(ro.getCell(2).toString());
				productDetails.setProductPrice(ro.getCell(3).toString());
				productDetails.setThreshHold(ro.getCell(4).toString());
				productDetails.setProductBasePrice(ro.getCell(6).toString());
				productDetails.setProductName(ro.getCell(1).toString());
				productDetails.setProductStatus(ro.getCell(18).toString());
				productDetails.setProductImage(ro.getCell(7).toString());
				productDetails.setGst(ro.getCell(5).toString());
				
				productDetails.setMultiProductImage1(ro.getCell(8).toString());
				productDetails.setMultiProductImage2(ro.getCell(9).toString());
				productDetails.setMultiProductImage3(ro.getCell(10).toString());
				productDetails.setMultiProductImage4(ro.getCell(11).toString());
				productDetails.setMultiProductImage5(ro.getCell(12).toString());
				productDetails.setMultiProductImage6(ro.getCell(13).toString());
				productDetails.setMultiProductImage7(ro.getCell(14).toString());
				productDetails.setMultiProductImage8(ro.getCell(15).toString());
				productDetails.setMultiProductImage9(ro.getCell(16).toString());
				productDetails.setMultiProductImage10(ro.getCell(17).toString());
				productDetails.setProductLevel("3");
				productDetailsListvalue.add(productDetails);
				AddProductCategoryAndProductModelRequest AddProductCategoryAndProductModelRequest = new AddProductCategoryAndProductModelRequest();
				AddProductCategoryAndProductModelRequest.setProductDetails(productDetailsListvalue);
				AddProductCategoryAndProductModelRequest.setKey("subAndProd");
				AddProductCategoryAndProductModelRequest.setProductCategoryId(base64Excel.getSubcategoryId());
				AddProductCategoryAndProductModelRequest.setUserId("1");
				//--------------------------------------------------------------------
				AddProductCategoryAndProductRequest addProductAndCategoryRequest = new AddProductCategoryAndProductRequest();
				addProductAndCategoryRequest.setRequestData(AddProductCategoryAndProductModelRequest);
				
				addProductAndCategory(addProductAndCategoryRequest);
				
			}
				return "Data Uploaded";
			}catch(Exception e) {
				System.out.print(e);
				throw new BusinessException(e, ErrorEnumeration.ADD_UPDATE_PRODUCT_FAILURE);
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
	
	@Override
	public String addProductAndCategory(AddProductCategoryAndProductRequest addProductAndCategoryRequest) {
		if(!addProductAndCategoryRequest.getRequestData().getKey().equalsIgnoreCase("productUpdate")) {
			try {
				if(!addProductAndCategoryRequest.getRequestData().getKey().equalsIgnoreCase("active")) {
					//------------------------------adding list of Product category with main category----------------------------------------
					addProductAndCategoryRequest = productServiceDAO.addProductAndCategory(addProductAndCategoryRequest);
					if(addProductAndCategoryRequest.getRequestData().getKey().equalsIgnoreCase("MainAndSub")) {
						//------------------------------mapping Customer Sub Category----------------------------------------
						mapUserProductDAO.addProductAndCategory(addProductAndCategoryRequest);
					}
					//------------------------------map category and sub category----------------------------------------
					productCategoryMappingDAO.mapProductAndCategory(addProductAndCategoryRequest);
				}else if(addProductAndCategoryRequest.getRequestData().getKey().equalsIgnoreCase("active")) {
					//------------------------------update product when user request to add admin----------------------------------------
					AddorUpdateProductBORequest addorUpdateProductBORequest = new AddorUpdateProductBORequest ();
					ProductDetails productDetails = new ProductDetails();
					productDetails.setGst(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getGst());
					productDetails.setProductId(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getProductId());
					productDetails.setProductImage(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getProductImage());
					productDetails.setProductStatus(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getProductStatus());
					productDetails.setProductName(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getProductName());
					productDetails.setProductDescription(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getProductDescription());
					productDetails.setProductPrice(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getProductPrice());
					productDetails.setProductLevel(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getProductLevel());
					productDetails.setProductBasePrice(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getProductBasePrice());
					
					productDetails.setProductImage(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getProductImage());
					productDetails.setMultiProductImage1(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getMultiProductImage1());
					productDetails.setMultiProductImage2(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getMultiProductImage2());
					productDetails.setMultiProductImage3(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getMultiProductImage3());
					productDetails.setMultiProductImage4(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getMultiProductImage4());
					productDetails.setMultiProductImage5(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getMultiProductImage5());
					productDetails.setMultiProductImage6(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getMultiProductImage6());
					productDetails.setMultiProductImage7(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getMultiProductImage7());
					productDetails.setMultiProductImage8(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getMultiProductImage8());
					productDetails.setMultiProductImage9(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getMultiProductImage9());
					productDetails.setMultiProductImage10(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getMultiProductImage10());
					addorUpdateProductBORequest.setRequestData(productDetails);
					addOrUpdateProduct(addorUpdateProductBORequest);
					
					productCategoryMappingDAO.mapProductAndCategory(addProductAndCategoryRequest);
				}
				
			}catch(Exception e) {
				throw new BusinessException(e.getMessage(), e);
			}
		}else {
			AddorUpdateProductBORequest addorUpdateProductBORequest = new AddorUpdateProductBORequest ();
			ProductDetails productDetails = new ProductDetails();
			productDetails.setProductId(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getProductId());
			productDetails.setProductImage(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getProductImage());
			productDetails.setProductStatus(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getProductStatus());
			productDetails.setProductName(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getProductName());
			productDetails.setProductDescription(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getProductDescription());
			productDetails.setProductPrice(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getProductPrice());
			productDetails.setProductBasePrice(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getProductBasePrice());
			productDetails.setProductLevel(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getProductLevel());
			productDetails.setGst(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getGst());
			
			productDetails.setMultiProductImage1(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getMultiProductImage1());
			productDetails.setMultiProductImage2(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getMultiProductImage2());
			productDetails.setMultiProductImage3(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getMultiProductImage3());
			productDetails.setMultiProductImage4(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getMultiProductImage4());
			productDetails.setMultiProductImage5(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getMultiProductImage5());
			productDetails.setMultiProductImage6(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getMultiProductImage6());
			productDetails.setMultiProductImage7(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getMultiProductImage7());
			productDetails.setMultiProductImage8(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getMultiProductImage8());
			productDetails.setMultiProductImage9(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getMultiProductImage9());
			productDetails.setMultiProductImage10(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getMultiProductImage10());
			
			addorUpdateProductBORequest.setRequestData(productDetails);
			addOrUpdateProduct(addorUpdateProductBORequest);
			
			List<Map<String, Object>> customerList = notifyCustomerDAO.getNotifyProductForCustomer(Integer.parseInt(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getProductId()));
			if(customerList!=null) {
				if(addProductAndCategoryRequest.getRequestData().getProductDetails().get(0).getProductStatus().equalsIgnoreCase("active")) {
					for(int j=0;j<customerList.size();j++) {
						System.out.println("sent");
						String msg = "The product is available now in Etaarana, please order your product";
						try {
							EmailUtil.productNotification(customerList.get(j).get("ots_users_emailid").toString(), " ", "Product available", msg);
						}catch(Exception e) {
							
						}
						
					}	
				}
			}
			
		}
		return null;
	}
	@Override
	public ProductDetailsBOResponse searchProduct() {
		return null;
	}
	@Override
	public String addAirTabelData(AirTableRequest airTableRequest) {
		try {
			for(int i=0;i<airTableRequest.getAirTabel().size();i++ ) {
				URL url = new URL(airTableRequest.getAirTabel().get(i).getProductImage());
				Image image = ImageIO.read(url); 
				
				BufferedImage bufferedImage = Base64UtilImage.toBufferedImage(image);
				
				String imagePathToStore = imagePath + airTableRequest.getAirTabel().get(i).getProductId() + ".jpeg";
				System.out.println(imagePathToStore);
				ImageIO.write(bufferedImage, "jpeg", new File(imagePathToStore));
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				ImageWriter writer = (ImageWriter) ImageIO.getImageWritersByFormatName("jpeg").next();
				
				ImageWriteParam param = writer.getDefaultWriteParam();
				param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
				param.setCompressionQuality(0.9f);
				
				writer.setOutput(ImageIO.createImageOutputStream(os));
				writer.write(null, new IIOImage((RenderedImage) image, null, null), param);
				writer.dispose();
				
				String productImagePath = "http://45.130.229.77:8095/ots/etaarana/"+airTableRequest.getAirTabel().get(i).getProductId() + ".jpeg";
				airTableRequest.getAirTabel().get(i).getProductImageList().add(0, productImagePath);
			}
			return airTableDao.addAirTabelData(airTableRequest);
		} catch (IOException e) {
			System.out.println(e);
		}catch (Exception e) {
			System.out.println(e);
		}
		return "success";
	}
	
	@Override
	public List<AirTableModel> airTabelCaluclation(GetProductStockListRequest airTableRequest) {
		List<AirTableModel> airtableModelList = airTableDao.airTabelCaluclation(airTableRequest);
		
		String newProductId = new String();
		for(AirTableModel airTableModel:airtableModelList) {
			int flag = 0;
			if(airTableModel.getAirTableProductCaluclatedStock() == null) {
				airTableModel.setAirTableProductCaluclatedStock("0");
			}
			if(airTableModel.getPreviousDayFlag().equalsIgnoreCase("no")) {
				AirTableModel airTableModelMap = productServiceDAO.addProductAirTable(airTableModel);
				//------------------------ to map product------------------------------------
				if(airTableModelMap.getProductCategoryname().equalsIgnoreCase("yes") && 
				airTableModelMap.getProductSubCategoryName().equalsIgnoreCase("yes")) {
					// category and sub category is there, so needs to map sub category and product
					AddProductCategoryAndProductRequest addProductCategoryAndProductRequest = new AddProductCategoryAndProductRequest();
					AddProductCategoryAndProductModelRequest addProductCategoryAndProductModel = new AddProductCategoryAndProductModelRequest();
					List<ProductDetails> productDetails = new ArrayList<ProductDetails>();
					ProductDetails productData = new ProductDetails();
					productData.setProductId(airTableModelMap.getProductId());
					productDetails.add(productData);
					addProductCategoryAndProductModel.setProductCategoryId(airTableModelMap.getProductSubCategoryId());
					addProductCategoryAndProductModel.setProductDetails(productDetails);
					addProductCategoryAndProductRequest.setRequestData(addProductCategoryAndProductModel);
					if(airTableModel.getPreviousDayFlag().equalsIgnoreCase("no")) {
						productCategoryMappingDAO.mapProductAndCategory(addProductCategoryAndProductRequest);
					}
					flag =1;
					newProductId = airTableModelMap.getProductId();
				}else{
					
					// no category and sub category and product mapping needs to be done
					List<ProductDetails> productDetails = new ArrayList<ProductDetails>();
					ProductDetails productData = new ProductDetails();
					productData.setProductId(airTableModelMap.getProductSubCategoryId());
					AddProductCategoryAndProductModelRequest addProductCategoryAndProductModel = new AddProductCategoryAndProductModelRequest();
					AddProductCategoryAndProductRequest addProductCategoryAndProductRequest = new AddProductCategoryAndProductRequest();			
					addProductCategoryAndProductModel.setProductCategoryId(airTableModelMap.getNewCategoryId());
					productDetails.add(productData);
					addProductCategoryAndProductModel.setProductDetails(productDetails);
					addProductCategoryAndProductRequest.setRequestData(addProductCategoryAndProductModel);
					//map categoy and sub category
					productCategoryMappingDAO.mapProductAndCategory(addProductCategoryAndProductRequest);
					//-----------------------------------------------------------------------------------------------------------------
					//map sub category and product
					productData.setProductId(airTableModelMap.getProductId());
					addProductCategoryAndProductModel.setProductCategoryId(airTableModelMap.getProductSubCategoryId());
					addProductCategoryAndProductModel.setProductDetails(productDetails);
					productDetails.add(productData);
					addProductCategoryAndProductRequest.setRequestData(addProductCategoryAndProductModel);
					productCategoryMappingDAO.mapProductAndCategory(addProductCategoryAndProductRequest);
					//-----------------------------------------------------------------------------------------------------------------
					flag = 1;
					newProductId = airTableModelMap.getProductId();
				}
			}
			AddProductStockBORequest addProductBORequest = new AddProductStockBORequest();
	        AddProductStock addProductStock = new AddProductStock();
	        String productId = null ;
	        if(flag == 1) {
	        	productId = newProductId;
	        }else {
	        	productId = productServiceDAO.getProductDetilsByTransactionId(airTableModel.getTransactionId()).get(0).getProductId();
	        }
	        addProductStock.setProductId(productId);
	        //getList of deliverd qty
	        GetProductStockListDomain GetProductStockListDomain = new GetProductStockListDomain();
	        GetProductStockListDomain.setProductId(productId);
	        GetProductStockListDomain.setTodaysDate(airTableRequest.getRequestData().getTodaysDate());
	        GetProductStockListRequest getProductStockListRequest = new GetProductStockListRequest();
	        getProductStockListRequest.setRequestData(GetProductStockListDomain);
	        // minus todays deleverd qty
	        List<OtsOrder> orderList = orderDAO.getOrderList(Integer.parseInt(productId),getProductStockListRequest);
	        Long deliverdQty = null;
	        if(orderList != null) {
		        deliverdQty = orderProductDAO.getListOfDeliverdQuantityOfDay(orderList,Integer.parseInt(productId));
			    }else {
			    deliverdQty = (long) 0;
	        }
	        
	        
	        Integer finalStock = Integer.parseInt(airTableModel.getCurrentDayStock()) - Integer.parseInt(String.valueOf(deliverdQty));
	       // System.out.println("product name = "+ airTableModel.getProductName() +"product stock = " +airTableModel.getAirTableProductCaluclatedStock() +" deliverd qty = "+deliverdQty +"final stock= " + finalStock);
	        addProductStock.setProductStockStatus("active");
	        addProductStock.setUsersId("1");
	        addProductStock.setProductStockQty(finalStock.toString());
	        addProductBORequest.setRequestData(addProductStock);
	        productStockDao.addAirtableStock(addProductBORequest);
	        airTableModel.setAirTableProductCaluclatedStock(finalStock.toString());
	        System.out.println("stock " + finalStock);
		    //	        if (Integer.parseInt(airTableModel.getProductStock()) > 0){
//		         Integer finalStock = Integer.parseInt(airTableModel.getAirTableProductCaluclatedStock()) - Integer.parseInt(String.valueOf(deliverdQty));
//		         addProductStock.setProductStockQty(finalStock.toString());
//		         addProductBORequest.setRequestData(addProductStock);`
//		         productStockDao.addAirtableStock(addProductBORequest);
//		         System.out.println("positive " + finalStock);
//		    //close to add stock for each of the product 
//		      } else if(Integer.parseInt(airTableModel.getProductStock()) < 0){
//		    //if value is negative 
//		    	 Integer positiveStock = Math.abs(Integer.parseInt(airTableModel.getAirTableProductCaluclatedStock()));
//		    	 Integer finalStock = positiveStock - Integer.parseInt(String.valueOf(deliverdQty));
//		    	 addProductStock.setProductId(airTableModel.getProductId());
//		    	 addProductStock.setProductStockQty(finalStock.toString());
//		    	 addProductBORequest.setRequestData(addProductStock);-
//		    	 productStockDao.addAirtableStock(addProductBORequest);
//		    	 System.out.println("negative "+finalStock );
//		      }
		}
		return airtableModelList;
	}
	/*shreekant*/
	@Override
	public ProductDetailsBOResponse getAllProductDetails() {
		
		ProductDetailsBOResponse productDetailsBOResponse = new ProductDetailsBOResponse();
	try {
	
			
			List<ProductDetails> productDetails = new ArrayList<ProductDetails>();
			
				try {
			//		customerProductDetails = mapUserProductDAO.getCustomerProductDetailsByCustomerId(productDetailsBORequest.getRequestData().getCustomerId());
					
					productDetails=productServiceDAO.getAllProductDetils();
				productDetailsBOResponse.setProductDetails(productDetails);
				System.out.println(productDetails);
				} catch (Exception e) {
					throw new BusinessException(e.getMessage(), e);
				}
			
			
			
		
		System.out.print(productDetailsBOResponse.getProductDetails().size());

	}catch(Exception e) {
		System.out.println(e);
	}
				return productDetailsBOResponse;
	}
	@Override
	public ProductDetailsBOResponse getProductDetails(ProductDetailsBORequest productDetailsBORequest) {
		try {
			ProductDetailsBOResponse productDetailsBOResponse = new ProductDetailsBOResponse();
			List<ProductDetails> ProductDetails = new ArrayList<ProductDetails> ();
			ProductDetails.add(productServiceDAO.getProductDetils(productDetailsBORequest.getRequestData().getProductId()));
			productDetailsBOResponse.setProductDetails(ProductDetails);
			return productDetailsBOResponse;
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
	}
	
	@Override
	public String notifyProductForCustomer(NotifyProductForCustomerRequest notifyProductForCustomerRequest) {
		try {
			return notifyCustomerDAO.notifyProductForCustomer(notifyProductForCustomerRequest);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
	}
	

}