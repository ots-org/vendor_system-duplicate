package com.fuso.enterprise.ots.srv.server.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringReader;

import com.itextpdf.text.Document;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;

public class OTSUtil {
	
	public static void generatePDFFromHTML(String html ,String billNo) {
		try {
			OutputStream file = new FileOutputStream(new File("bill.pdf"));
			Document document = new Document();
		    PdfWriter.getInstance(document, file);
		    document.open();
		    HTMLWorker htmlWorker = new HTMLWorker(document);
		    htmlWorker.parse(new StringReader(html));
		    document.close();
		    file.close();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}

	public static String generateReportPDFFromHTML(String html ,String value) {
		String pdfPath = "C:\\template\\"+value;
		try {
			OutputStream file = new FileOutputStream(new File(pdfPath));
			Document document = new Document();
		    PdfWriter.getInstance(document, file);
		    document.open();
		    HTMLWorker htmlWorker = new HTMLWorker(document);
		    htmlWorker.parse(new StringReader(html));
		    document.close();
		    file.close();
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return pdfPath;
	}
}
